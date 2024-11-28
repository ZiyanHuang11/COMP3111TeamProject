package comp3111.examsystem.controller;

import comp3111.examsystem.entity.Exam;
import comp3111.examsystem.entity.Question;
import comp3111.examsystem.entity.Teacher;
import comp3111.examsystem.service.ExamManagementService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ExamManagementController {

    // 当前登录的教师
    private Teacher loggedInTeacher;

    // 过滤区域的 FXML 元素
    @FXML
    private TextField examNameFilterTxt;
    @FXML
    private ComboBox<String> courseIDFilterComboBox;
    @FXML
    private ComboBox<String> publishFilterComboBox;

    // 问题过滤区域的 FXML 元素
    @FXML
    private TextField questionFilterTxt;
    @FXML
    private ComboBox<String> typeFilterComboBox;
    @FXML
    private TextField scoreFilterTxt;

    // 考试 TableView 及其列
    @FXML
    private TableView<Exam> examTable;
    @FXML
    private TableColumn<Exam, String> examNameColumn;
    @FXML
    private TableColumn<Exam, String> courseIDColumn;
    @FXML
    private TableColumn<Exam, String> examTimeColumn;
    @FXML
    private TableColumn<Exam, String> publishColumn;
    @FXML
    private TableColumn<Exam, Integer> durationColumn;

    // 问题 TableView 及其列
    @FXML
    private TableView<Question> questionTable;
    @FXML
    private TableColumn<Question, String> questionColumn;
    @FXML
    private TableColumn<Question, String> typeColumn;
    @FXML
    private TableColumn<Question, Integer> scoreColumn;

    // 已选择的问题 TableView 及其列
    @FXML
    private TableView<Question> selectedQuestionTable;
    @FXML
    private TableColumn<Question, String> selectedQuestionColumn;
    @FXML
    private TableColumn<Question, String> selectedTypeColumn;
    @FXML
    private TableColumn<Question, Integer> selectedScoreColumn;

    // 添加/更新考试区域的输入字段
    @FXML
    private TextField examNameTxt;
    @FXML
    private TextField examTimeTxt;
    @FXML
    private ComboBox<String> courseIDComboBox;
    @FXML
    private ComboBox<String> publishComboBox;
    @FXML
    private TextField durationTxt;

    // 按钮
    @FXML
    private Button addButton;
    @FXML
    private Button updateButton;
    @FXML
    private Button deleteButton;
    @FXML
    private Button refreshButton;

    // 服务类和选中的问题列表
    private ExamManagementService examService;
    private ObservableList<Question> selectedQuestionList;

    /**
     * 设置当前登录的教师
     *
     * @param teacher 当前登录的教师对象
     */
    public void setLoggedInTeacher(Teacher teacher) {
        this.loggedInTeacher = teacher;
        System.out.println("Logged in teacher: " + loggedInTeacher.getUsername());
        initializeCourseIDComboBoxes(); // 初始化 ComboBox
        loadExamList(); // 在设置教师后，加载考试列表
    }

    @FXML
    public void initialize() {
        examService = new ExamManagementService("data/exam.txt", "data/questions.txt");

        // 初始化考试表
        examTable.setItems(examService.getExamList());
        examNameColumn.setCellValueFactory(cellData -> cellData.getValue().examNameProperty());
        courseIDColumn.setCellValueFactory(cellData -> cellData.getValue().courseIDProperty());
        examTimeColumn.setCellValueFactory(cellData -> cellData.getValue().examTimeProperty());
        publishColumn.setCellValueFactory(cellData -> cellData.getValue().publishProperty());
        durationColumn.setCellValueFactory(cellData -> cellData.getValue().durationProperty().asObject());

        // 初始化问题表
        questionTable.setItems(examService.getQuestionList());
        questionColumn.setCellValueFactory(cellData -> cellData.getValue().questionProperty());
        typeColumn.setCellValueFactory(cellData -> cellData.getValue().typeProperty());
        scoreColumn.setCellValueFactory(cellData -> cellData.getValue().scoreProperty().asObject());

        // 初始化已选择的问题表
        selectedQuestionList = FXCollections.observableArrayList();
        selectedQuestionTable.setItems(selectedQuestionList);
        selectedQuestionColumn.setCellValueFactory(cellData -> cellData.getValue().questionProperty());
        selectedTypeColumn.setCellValueFactory(cellData -> cellData.getValue().typeProperty());
        selectedScoreColumn.setCellValueFactory(cellData -> cellData.getValue().scoreProperty().asObject());

        // 设置 ComboBox 的选项
        publishFilterComboBox.setItems(FXCollections.observableArrayList("All", "Yes", "No"));
        typeFilterComboBox.setItems(FXCollections.observableArrayList("All", "Single", "Multiple"));
        publishComboBox.setItems(FXCollections.observableArrayList("Yes", "No"));

        // 设置 ComboBox 的默认值
        publishFilterComboBox.setValue("All");
        typeFilterComboBox.setValue("All");
        publishComboBox.setValue("No");

        // 添加 TextFormatter 以限制 examTimeTxt 和 durationTxt 的输入
        examTimeTxt.setTextFormatter(new TextFormatter<String>(change -> {
            String newText = change.getControlNewText();
            if (newText.matches("\\d{0,4}(-\\d{0,2}){0,2}")) { // 允许输入数字和-
                return change;
            }
            return null;
        }));

        durationTxt.setTextFormatter(new TextFormatter<String>(change -> {
            String newText = change.getControlNewText();
            if (newText.matches("\\d*")) { // 仅允许数字
                return change;
            }
            return null;
        }));

        // 监听考试表的选择变化
        examTable.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                selectedQuestionList.setAll(newValue.getQuestions());
                examNameTxt.setText(newValue.getExamName());
                examTimeTxt.setText(newValue.getExamTime());
                courseIDComboBox.setValue(newValue.getCourseID()); // 从 ComboBox 获取值
                publishComboBox.setValue(newValue.getPublish());
                durationTxt.setText(String.valueOf(newValue.getDuration()));
            } else {
                selectedQuestionList.clear();
                examNameTxt.clear();
                examTimeTxt.clear();
                courseIDComboBox.setValue(null); // 清除 ComboBox 的选择
                publishComboBox.setValue(null);
                durationTxt.clear();
            }
        });

        // 添加调试输出，确认控件被正确注入
        System.out.println("courseIDFilterComboBox is: " + courseIDFilterComboBox);
        System.out.println("courseIDComboBox is: " + courseIDComboBox);
        System.out.println("typeFilterComboBox is: " + typeFilterComboBox);
        System.out.println("durationTxt is: " + durationTxt);
    }

    /**
     * 初始化过滤区域和添加/更新考试区域的 Course ID ComboBox
     */
    private void initializeCourseIDComboBoxes() {
        if (loggedInTeacher == null) {
            System.err.println("Logged in teacher is not set.");
            return;
        }

        List<String> teacherCourses = new ArrayList<>();
        if (loggedInTeacher.getCourseid1() != null && !loggedInTeacher.getCourseid1().isEmpty()) {
            teacherCourses.add(loggedInTeacher.getCourseid1());
        }
        if (loggedInTeacher.getCourseid2() != null && !loggedInTeacher.getCourseid2().isEmpty()) {
            teacherCourses.add(loggedInTeacher.getCourseid2());
        }

        // 初始化过滤区域的 Course ID ComboBox
        ObservableList<String> filterCourseOptions = FXCollections.observableArrayList();
        filterCourseOptions.add("All");
        filterCourseOptions.addAll(teacherCourses);
        if (courseIDFilterComboBox != null) {
            courseIDFilterComboBox.setItems(filterCourseOptions);
            courseIDFilterComboBox.setValue("All"); // 设置默认值
            System.out.println("Set courseIDFilterComboBox items: " + filterCourseOptions);
        } else {
            System.err.println("courseIDFilterComboBox is null in initializeCourseIDComboBoxes");
        }

        // 初始化添加/更新考试区域的 Course ID ComboBox
        ObservableList<String> addUpdateCourseOptions = FXCollections.observableArrayList();
        addUpdateCourseOptions.addAll(teacherCourses);
        if (courseIDComboBox != null) {
            courseIDComboBox.setItems(addUpdateCourseOptions);
            if (!addUpdateCourseOptions.isEmpty()) {
                courseIDComboBox.setValue(addUpdateCourseOptions.get(0)); // 设置默认选择为第一个课程
            }
            System.out.println("Set courseIDComboBox items: " + addUpdateCourseOptions);
        } else {
            System.err.println("courseIDComboBox is null in initializeCourseIDComboBoxes");
        }

        // 初始化问题过滤区域的 Type ComboBox
        if (typeFilterComboBox != null) {
            typeFilterComboBox.setItems(FXCollections.observableArrayList("All", "Single", "Multiple"));
            typeFilterComboBox.setValue("All"); // 设置默认值
            System.out.println("Set typeFilterComboBox items: All, Single, Multiple");
        } else {
            System.err.println("typeFilterComboBox is null in initializeCourseIDComboBoxes");
        }
    }

    /**
     * 加载考试列表，根据教师的课程ID筛选
     */
    private void loadExamList() {
        if (loggedInTeacher == null) {
            System.err.println("Logged in teacher is null.");
            return;
        }

        // 获取教师教授的课程ID列表
        List<String> teacherCourses = new ArrayList<>();
        if (loggedInTeacher.getCourseid1() != null && !loggedInTeacher.getCourseid1().isEmpty()) {
            teacherCourses.add(loggedInTeacher.getCourseid1());
        }
        if (loggedInTeacher.getCourseid2() != null && !loggedInTeacher.getCourseid2().isEmpty()) {
            teacherCourses.add(loggedInTeacher.getCourseid2());
        }
        System.out.println("Teacher courses: " + teacherCourses);

        // 从服务类获取所有考试列表
        ObservableList<Exam> allExams = examService.getExamList();

        // 添加调试输出
        System.out.println("Total exams loaded: " + allExams.size());

        // 根据教师的课程ID筛选考试
        List<Exam> filteredExams = allExams.stream()
                .filter(exam -> teacherCourses.contains(exam.getCourseID()))
                .collect(Collectors.toList());

        System.out.println("Exams after filtering: " + filteredExams.size());

        // 将筛选后的考试列表设置到考试表中
        examTable.setItems(FXCollections.observableArrayList(filteredExams));
    }

    /**
     * 添加考试
     */
    @FXML
    private void handleAdd() {
        String examName = examNameTxt.getText().trim();
        String examTime = examTimeTxt.getText().trim();
        String courseID = courseIDComboBox.getValue();
        String publish = publishComboBox.getValue();
        String durationStr = durationTxt.getText().trim();

        // 输入验证
        String validationError = validateExamInputs(examName, examTime, courseID, publish, durationStr);
        if (validationError != null) {
            showAlert("Input Validation Error", validationError, Alert.AlertType.ERROR);
            return;
        }

        int duration = Integer.parseInt(durationStr);

        // 检查课程ID是否属于当前教师教授的课程
        if (!isCourseIDValid(courseID)) {
            showAlert("Invalid Course ID", "The selected Course ID is not taught by the logged-in teacher.", Alert.AlertType.ERROR);
            return;
        }

        Exam newExam = new Exam(examName, courseID, examTime, publish, duration, new ArrayList<>(selectedQuestionList));

        try {
            boolean success = examService.addExam(newExam);
            if (!success) {
                showAlert("Duplicate Exam", "An exam with this name already exists for the selected Course ID.", Alert.AlertType.ERROR);
            } else {
                showAlert("Success", "Exam added successfully.", Alert.AlertType.INFORMATION);
                // 清空输入字段
                clearExamInputFields();
            }
        } catch (IOException e) {
            showAlert("Error", "Failed to save exams to file.", Alert.AlertType.ERROR);
        }
    }

    /**
     * 更新考试
     */
    @FXML
    private void handleUpdate() {
        Exam selectedExam = examTable.getSelectionModel().getSelectedItem();
        if (selectedExam == null) {
            showAlert("No Selection", "Please select an exam to update.", Alert.AlertType.WARNING);
            return;
        }

        String examName = examNameTxt.getText().trim();
        String examTime = examTimeTxt.getText().trim();
        String courseID = courseIDComboBox.getValue(); // 从 ComboBox 获取值
        String publish = publishComboBox.getValue();
        String durationStr = durationTxt.getText().trim();

        // 输入验证
        String validationError = validateExamInputs(examName, examTime, courseID, publish, durationStr);
        if (validationError != null) {
            showAlert("Input Validation Error", validationError, Alert.AlertType.ERROR);
            return;
        }

        int duration = Integer.parseInt(durationStr);

        // 检查课程ID是否属于当前教师教授的课程
        if (!isCourseIDValid(courseID)) {
            showAlert("Invalid Course ID", "The selected Course ID is not taught by the logged-in teacher.", Alert.AlertType.ERROR);
            return;
        }

        Exam updatedExam = new Exam(examName, courseID, examTime, publish, duration, new ArrayList<>(selectedQuestionList));

        try {
            boolean success = examService.updateExam(updatedExam, selectedExam.getExamName(), selectedExam.getCourseID());
            if (!success) {
                showAlert("Duplicate Exam", "An exam with this name already exists for the selected Course ID.", Alert.AlertType.ERROR);
            } else {
                showAlert("Success", "Exam updated successfully.", Alert.AlertType.INFORMATION);
                examTable.refresh();
            }
        } catch (IOException e) {
            showAlert("Error", "Failed to save exams to file.", Alert.AlertType.ERROR);
        }
    }

    /**
     * 删除考试
     */
    @FXML
    private void handleDelete() {
        Exam selectedExam = examTable.getSelectionModel().getSelectedItem();
        if (selectedExam == null) {
            showAlert("No Selection", "Please select an exam to delete.", Alert.AlertType.WARNING);
            return;
        }

        try {
            boolean success = examService.deleteExam(selectedExam.getExamName(), selectedExam.getCourseID());
            if (success) {
                showAlert("Success", "Exam deleted successfully.", Alert.AlertType.INFORMATION);
                examTable.getItems().remove(selectedExam);
            } else {
                showAlert("Error", "Failed to delete the selected exam.", Alert.AlertType.ERROR);
            }
        } catch (IOException e) {
            showAlert("Error", "Failed to save exams to file.", Alert.AlertType.ERROR);
        }
    }

    /**
     * 处理添加到已选择问题的方法
     */
    @FXML
    private void handleAddToLeft() {
        Question selectedQuestion = questionTable.getSelectionModel().getSelectedItem();
        if (selectedQuestion == null) {
            showAlert("No Selection", "Please select a question to add.", Alert.AlertType.WARNING);
            return;
        }

        if (!selectedQuestionList.contains(selectedQuestion)) {
            selectedQuestionList.add(selectedQuestion);
        } else {
            showAlert("Duplicate Question", "The selected question is already in the exam.", Alert.AlertType.ERROR);
        }
    }

    /**
     * 处理从已选择问题中删除的方法
     */
    @FXML
    private void handleDeleteFromLeft() {
        Question selectedQuestion = selectedQuestionTable.getSelectionModel().getSelectedItem();
        if (selectedQuestion == null) {
            showAlert("No Selection", "Please select a question to delete.", Alert.AlertType.WARNING);
            return;
        }

        selectedQuestionList.remove(selectedQuestion);
    }

    /**
     * 过滤考试
     */
    @FXML
    private void handleFilter() {
        String examName = examNameFilterTxt.getText().trim();
        String courseID = courseIDFilterComboBox.getValue(); // 从 ComboBox 获取值
        String publishStatus = publishFilterComboBox.getValue();

        // 调用服务类的过滤方法
        List<Exam> filteredExams = examService.filterExams(examName, courseID, publishStatus);

        // 进一步根据教师的课程ID进行筛选（确保只显示教师教授的课程）
        if (loggedInTeacher != null) {
            List<String> teacherCourses = new ArrayList<>();
            if (loggedInTeacher.getCourseid1() != null && !loggedInTeacher.getCourseid1().isEmpty()) {
                teacherCourses.add(loggedInTeacher.getCourseid1());
            }
            if (loggedInTeacher.getCourseid2() != null && !loggedInTeacher.getCourseid2().isEmpty()) {
                teacherCourses.add(loggedInTeacher.getCourseid2());
            }

            // 根据选择的课程ID进行过滤，若选中 "All"，则不限制课程ID
            if (!courseID.equals("All")) {
                filteredExams = filteredExams.stream()
                        .filter(exam -> teacherCourses.contains(exam.getCourseID()) && exam.getCourseID().equals(courseID))
                        .collect(Collectors.toList());
            } else {
                // 如果选择了 "All"，则不限制课程ID
                filteredExams = filteredExams.stream()
                        .filter(exam -> teacherCourses.contains(exam.getCourseID()))
                        .collect(Collectors.toList());
            }

            // 根据发布状态进行过滤，若选中 "All"，则不限制发布状态
            if (!publishStatus.equals("All")) {
                filteredExams = filteredExams.stream()
                        .filter(exam -> exam.getPublish().equals(publishStatus))
                        .collect(Collectors.toList());
            }
        }

        // 将筛选后的考试列表设置到考试表中
        examTable.setItems(FXCollections.observableArrayList(filteredExams));
    }

    /**
     * 重置考试过滤器
     */
    @FXML
    private void handleReset() {
        examNameFilterTxt.clear();
        courseIDFilterComboBox.setValue("All"); // 设置为 "All"
        publishFilterComboBox.setValue("All");
        loadExamList(); // 重新加载考试列表
    }

    /**
     * 过滤问题
     */
    @FXML
    private void handleQuestionFilter() {
        String questionText = questionFilterTxt.getText().trim();
        String type = typeFilterComboBox.getValue();
        String scoreText = scoreFilterTxt.getText().trim();

        List<Question> filteredQuestions = examService.filterQuestions(questionText, type, scoreText);
        questionTable.setItems(FXCollections.observableArrayList(filteredQuestions));
    }

    /**
     * 重置问题过滤器
     */
    @FXML
    private void handleQuestionFilterReset() {
        questionFilterTxt.clear();
        typeFilterComboBox.setValue("All");
        scoreFilterTxt.clear();
        questionTable.setItems(examService.getQuestionList());
    }

    /**
     * 刷新考试列表和表格
     */
    @FXML
    private void handleRefresh() {
        loadExamList(); // 刷新考试列表
        questionTable.refresh();
        selectedQuestionTable.refresh();
    }

    /**
     * 验证考试输入字段
     *
     * @return 如果有错误，返回错误消息；否则，返回 null
     */
    private String validateExamInputs(String examName, String examTime, String courseID, String publish, String durationStr) {
        if (examName.isEmpty() || examTime.isEmpty() || courseID == null || courseID.isEmpty() || publish == null || durationStr.isEmpty()) {
            return "Please fill in all fields.";
        }

        // 验证考试时间格式 yyyy-mm-dd 并且是合法日期
        if (!isValidDate(examTime)) {
            return "Exam Time must be in the format yyyy-mm-dd and a valid date.";
        }

        // 验证考试时长
        int duration;
        try {
            duration = Integer.parseInt(durationStr);
            if (duration <= 0 || duration > 210) {
                return "Duration must be greater than 0 and less than or equal to 210.";
            }
        } catch (NumberFormatException e) {
            return "Duration must be a valid number.";
        }

        return null; // 无错误
    }

    /**
     * 验证日期格式是否为 yyyy-mm-dd 并且是一个合法日期
     *
     * @param dateStr 日期字符串
     * @return 如果格式正确且是合法日期，返回 true；否则，返回 false
     */
    private boolean isValidDate(String dateStr) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        try {
            LocalDate.parse(dateStr, formatter);
            return true;
        } catch (DateTimeParseException e) {
            return false;
        }
    }

    /**
     * 检查课程ID是否属于当前教师教授的课程
     *
     * @param courseID 课程ID
     * @return 如果有效，返回 true；否则，返回 false
     */
    private boolean isCourseIDValid(String courseID) {
        if (loggedInTeacher == null) {
            return false;
        }
        return loggedInTeacher.getCourseid1().equalsIgnoreCase(courseID) ||
                loggedInTeacher.getCourseid2().equalsIgnoreCase(courseID);
    }

    /**
     * 清空考试输入字段
     */
    private void clearExamInputFields() {
        examNameTxt.clear();
        examTimeTxt.clear();
        courseIDComboBox.setValue(null); // 清除 ComboBox 的选择
        publishComboBox.setValue("No");
        durationTxt.clear();
        selectedQuestionList.clear();
    }

    /**
     * 显示警报的方法
     */
    private void showAlert(String title, String message, Alert.AlertType type) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null); // 移除头部
        alert.setContentText(message);
        alert.showAndWait();
    }
}
