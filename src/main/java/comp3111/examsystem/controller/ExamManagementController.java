package comp3111.examsystem.controller;

import comp3111.examsystem.entity.Exam;
import comp3111.examsystem.entity.Question;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;

public class ExamManagementController {

    // 过滤器控件
    @FXML
    private TextField examNameFilterTxt;
    @FXML
    private ComboBox<String> courseIDFilterComboBox;
    @FXML
    private ComboBox<String> publishFilterComboBox;

    @FXML
    private TextField questionFilterTxt;
    @FXML
    private ComboBox<String> typeFilterComboBox;
    @FXML
    private TextField scoreFilterTxt;

    // 考试列表
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

    // 问题列表（题库）
    @FXML
    private TableView<Question> questionTable;
    @FXML
    private TableColumn<Question, String> questionColumn;
    @FXML
    private TableColumn<Question, String> typeColumn;
    @FXML
    private TableColumn<Question, Integer> scoreColumn;

    // 已选择的问题列表（考试中的题目）
    @FXML
    private TableView<Question> selectedQuestionTable;
    @FXML
    private TableColumn<Question, String> selectedQuestionColumn;
    @FXML
    private TableColumn<Question, String> selectedTypeColumn;
    @FXML
    private TableColumn<Question, Integer> selectedScoreColumn;

    // 考试编辑字段
    @FXML
    private TextField examNameTxt;
    @FXML
    private TextField examTimeTxt;
    @FXML
    private ComboBox<String> courseIDComboBox;
    @FXML
    private ComboBox<String> publishComboBox;

    // 按钮
    @FXML
    private Button addButton;
    @FXML
    private Button updateButton;
    @FXML
    private Button deleteButton;
    @FXML
    private Button refreshButton;

    // 数据列表
    private ObservableList<Exam> examList;
    private ObservableList<Question> questionList;
    private ObservableList<Question> selectedQuestionList;

    @FXML
    public void initialize() {
        // 初始化 ComboBox 选项
        courseIDFilterComboBox.getItems().addAll("All", "COMP3111", "COMP5111");
        courseIDFilterComboBox.setValue("Course");
        publishFilterComboBox.getItems().addAll("All", "Yes", "No");
        publishFilterComboBox.setValue("Publish");

        typeFilterComboBox.getItems().addAll("All", "Single", "Multiple");
        typeFilterComboBox.setValue("Type");

        courseIDComboBox.getItems().addAll("COMP3111", "COMP5111");
        courseIDComboBox.setValue("Course");
        publishComboBox.getItems().addAll("Yes", "No");
        publishComboBox.setValue("Publish");

        // 初始化考试列表
        examList = FXCollections.observableArrayList();
        examTable.setItems(examList);

        examNameColumn.setCellValueFactory(cellData -> cellData.getValue().examNameProperty());
        courseIDColumn.setCellValueFactory(cellData -> cellData.getValue().courseIDProperty());
        examTimeColumn.setCellValueFactory(cellData -> cellData.getValue().examTimeProperty());
        publishColumn.setCellValueFactory(cellData -> cellData.getValue().publishProperty());

        // 初始化问题列表（题库）
        questionList = FXCollections.observableArrayList();
        questionTable.setItems(questionList);

        questionColumn.setCellValueFactory(cellData -> cellData.getValue().questionProperty());
        typeColumn.setCellValueFactory(cellData -> cellData.getValue().typeProperty());
        scoreColumn.setCellValueFactory(cellData -> cellData.getValue().scoreProperty().asObject());

        // 初始化已选择的问题列表（考试中的题目）
        selectedQuestionList = FXCollections.observableArrayList();
        selectedQuestionTable.setItems(selectedQuestionList);

        selectedQuestionColumn.setCellValueFactory(cellData -> cellData.getValue().questionProperty());
        selectedTypeColumn.setCellValueFactory(cellData -> cellData.getValue().typeProperty());
        selectedScoreColumn.setCellValueFactory(cellData -> cellData.getValue().scoreProperty().asObject());

        // 加载示例数据
        loadSampleData();

        // 监听考试表格的选择变化，更新已选择的问题列表和编辑字段
        examTable.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                loadQuestionsForExam(newValue);
                // 显示选中考试的详细信息
                examNameTxt.setText(newValue.getExamName());
                examTimeTxt.setText(newValue.getExamTime());
                courseIDComboBox.setValue(newValue.getCourseID());
                publishComboBox.setValue(newValue.getPublish());
            } else {
                selectedQuestionList.clear();
                // 清空编辑字段
                examNameTxt.clear();
                examTimeTxt.clear();
                courseIDComboBox.setValue(null);
                publishComboBox.setValue(null);
            }
        });
    }

    // 加载示例数据
    private void loadSampleData() {
        // 添加示例考试
        Exam midterm = new Exam("Midterm Exam", "COMP3111", "2023-11-20", "Yes");
        Exam finalExam = new Exam("Final Exam", "COMP5111", "2023-12-15", "No");
        examList.addAll(midterm, finalExam);

        // 添加示例问题到题库
        Question q1 = new Question(
                "What is Java?",
                "A programming language",
                "A coffee brand",
                "An island",
                "All of the above",
                "A programming language", // 答案
                "Single",
                5
        );

        Question q2 = new Question(
                "Explain OOP concepts.",
                "Encapsulation",
                "Inheritance",
                "Polymorphism",
                "Abstraction",
                "All of the above", // 答案
                "Multiple",
                10
        );

        questionList.addAll(q1, q2);

        // 将示例问题添加到考试中
        midterm.getQuestions().add(q1);
        finalExam.getQuestions().add(q2);
    }

    // 加载指定考试的题目
    private void loadQuestionsForExam(Exam exam) {
        selectedQuestionList.setAll(exam.getQuestions());
    }

    // 处理考试过滤器的重置
    @FXML
    private void handleReset() {
        examNameFilterTxt.clear();
        courseIDFilterComboBox.setValue("All");
        publishFilterComboBox.setValue("All");
        handleFilter(); // 重置过滤
    }

    // 处理考试过滤器
    @FXML
    private void handleFilter() {
        String examName = examNameFilterTxt.getText().toLowerCase();
        String courseID = courseIDFilterComboBox.getValue();
        String publishStatus = publishFilterComboBox.getValue();

        ObservableList<Exam> filteredList = FXCollections.observableArrayList();

        for (Exam exam : examList) {
            boolean matches = true;

            if (!examName.isEmpty() && !exam.getExamName().toLowerCase().contains(examName)) {
                matches = false;
            }

            if (!courseID.equals("All") && !exam.getCourseID().equals(courseID)) {
                matches = false;
            }

            if (!publishStatus.equals("All") && !exam.getPublish().equals(publishStatus)) {
                matches = false;
            }

            if (matches) {
                filteredList.add(exam);
            }
        }

        examTable.setItems(filteredList);
    }

    // 处理问题过滤器的重置
    @FXML
    private void handleQuestionFilterReset() {
        questionFilterTxt.clear();
        typeFilterComboBox.setValue("All");
        scoreFilterTxt.clear();
        handleQuestionFilter(); // 重置过滤
    }

    // 处理问题过滤器
    @FXML
    private void handleQuestionFilter() {
        String questionText = questionFilterTxt.getText().toLowerCase();
        String type = typeFilterComboBox.getValue();
        String scoreText = scoreFilterTxt.getText();

        ObservableList<Question> filteredList = FXCollections.observableArrayList();

        for (Question q : questionList) {
            boolean matches = true;

            if (!questionText.isEmpty() && !q.getQuestion().toLowerCase().contains(questionText)) {
                matches = false;
            }

            if (!type.equals("All") && !q.getType().equals(type)) {
                matches = false;
            }

            if (!scoreText.isEmpty()) {
                try {
                    int score = Integer.parseInt(scoreText);
                    if (q.getScore() != score) {
                        matches = false;
                    }
                } catch (NumberFormatException e) {
                    // 无效的分数输入，忽略过滤
                }
            }

            if (matches) {
                filteredList.add(q);
            }
        }

        questionTable.setItems(filteredList);
    }

    // 处理添加问题到已选择列表
    @FXML
    private void handleAddToLeft() {
        Question selectedQuestion = questionTable.getSelectionModel().getSelectedItem();
        Exam selectedExam = examTable.getSelectionModel().getSelectedItem();

        if (selectedExam == null) {
            showAlert("No Exam Selected", "Please select an exam to add questions to.", Alert.AlertType.WARNING);
            return;
        }

        if (selectedQuestion != null && !selectedQuestionList.contains(selectedQuestion)) {
            selectedQuestionList.add(selectedQuestion);
            selectedExam.getQuestions().add(selectedQuestion);
        } else {
            showAlert("No Selection", "Please select a question to add.", Alert.AlertType.WARNING);
        }
    }

    // 处理删除问题从已选择列表
    @FXML
    private void handleDeleteFromLeft() {
        Question selectedQuestion = selectedQuestionTable.getSelectionModel().getSelectedItem();
        Exam selectedExam = examTable.getSelectionModel().getSelectedItem();

        if (selectedExam == null) {
            showAlert("No Exam Selected", "Please select an exam to delete questions from.", Alert.AlertType.WARNING);
            return;
        }

        if (selectedQuestion != null) {
            selectedQuestionList.remove(selectedQuestion);
            selectedExam.getQuestions().remove(selectedQuestion);
        } else {
            showAlert("No Selection", "Please select a question to delete.", Alert.AlertType.WARNING);
        }
    }

    // 处理添加考试
    @FXML
    private void handleAdd() {
        String examName = examNameTxt.getText().trim();
        String examTime = examTimeTxt.getText().trim();
        String courseID = courseIDComboBox.getValue();
        String publish = publishComboBox.getValue();

        if (examName.isEmpty() || examTime.isEmpty() || courseID == null || publish == null) {
            showAlert("Incomplete Data", "Please fill in all fields.", Alert.AlertType.ERROR);
            return;
        }

        // 检查考试名称是否重复
        for (Exam exam : examList) {
            if (exam.getExamName().equalsIgnoreCase(examName)) {
                showAlert("Duplicate Exam", "An exam with this name already exists.", Alert.AlertType.ERROR);
                return;
            }
        }

        // 创建新考试并添加到列表
        Exam newExam = new Exam(examName, courseID, examTime, publish);
        examList.add(newExam);

        // 清空输入字段
        examNameTxt.clear();
        examTimeTxt.clear();
        courseIDComboBox.setValue(null);
        publishComboBox.setValue("No");
    }

    // 处理更新考试
    @FXML
    private void handleUpdate() {
        Exam selectedExam = examTable.getSelectionModel().getSelectedItem();
        if (selectedExam == null) {
            showAlert("No Selection", "Please select an exam to update.", Alert.AlertType.WARNING);
            return;
        }

        String examName = examNameTxt.getText().trim();
        String examTime = examTimeTxt.getText().trim();
        String courseID = courseIDComboBox.getValue();
        String publish = publishComboBox.getValue();

        if (examName.isEmpty() || examTime.isEmpty() || courseID == null || publish == null) {
            showAlert("Incomplete Data", "Please fill in all fields.", Alert.AlertType.ERROR);
            return;
        }

        // 检查考试名称是否重复（除去当前考试）
        for (Exam exam : examList) {
            if (exam != selectedExam && exam.getExamName().equalsIgnoreCase(examName)) {
                showAlert("Duplicate Exam", "An exam with this name already exists.", Alert.AlertType.ERROR);
                return;
            }
        }

        // 更新考试信息
        selectedExam.setExamName(examName);
        selectedExam.setExamTime(examTime);
        selectedExam.setCourseID(courseID);
        selectedExam.setPublish(publish);

        // 刷新表格
        examTable.refresh();

        // 清空输入字段
        examNameTxt.clear();
        examTimeTxt.clear();
        courseIDComboBox.setValue(null);
        publishComboBox.setValue("No");
        examTable.getSelectionModel().clearSelection();
    }

    // 处理删除考试
    @FXML
    private void handleDelete() {
        Exam selectedExam = examTable.getSelectionModel().getSelectedItem();
        if (selectedExam != null) {
            examList.remove(selectedExam);
            selectedQuestionList.clear();
            showAlert("Deleted", "Selected exam has been deleted.", Alert.AlertType.INFORMATION);
        } else {
            showAlert("No Selection", "Please select an exam to delete.", Alert.AlertType.WARNING);
        }
    }

    // 处理刷新按钮
    @FXML
    private void handleRefresh() {
        examTable.refresh();
        questionTable.refresh();
        selectedQuestionTable.refresh();
    }

    // 显示提示框
    private void showAlert(String title, String message, Alert.AlertType type) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null); // 去除标题
        alert.setContentText(message);
        alert.showAndWait();
    }
}
