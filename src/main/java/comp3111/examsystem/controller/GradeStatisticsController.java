package comp3111.examsystem.controller;

import comp3111.examsystem.entity.GradeRecord;
import comp3111.examsystem.service.GradeStatisticsService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.util.*;

public class GradeStatisticsController {
    @FXML
    private TableView<GradeRecord> gradeTable;

    @FXML
    private TableColumn<GradeRecord, String> courseColumn;

    @FXML
    private TableColumn<GradeRecord, String> examColumn;

    @FXML
    private TableColumn<GradeRecord, Integer> scoreColumn;

    @FXML
    private TableColumn<GradeRecord, Integer> fullScoreColumn;

    @FXML
    private TableColumn<GradeRecord, Integer> timeColumn;

    @FXML
    private ComboBox<String> courseComboBox;

    @FXML
    private BarChart<String, Number> gradeBarChart;

    @FXML
    private CategoryAxis xAxis;

    private GradeStatisticsService gradeStatisticsService;
    private ObservableList<GradeRecord> masterData = FXCollections.observableArrayList();
    private String loggedInUsername;

    @FXML
    public void initialize() {
        gradeStatisticsService = new GradeStatisticsService("data/completed_quizzes.txt");

        // Initialize table columns
        courseColumn.setCellValueFactory(new PropertyValueFactory<>("course"));
        examColumn.setCellValueFactory(new PropertyValueFactory<>("exam"));
        scoreColumn.setCellValueFactory(new PropertyValueFactory<>("score"));
        fullScoreColumn.setCellValueFactory(new PropertyValueFactory<>("fullScore"));
        timeColumn.setCellValueFactory(new PropertyValueFactory<>("time"));
    }

    /**
     * Set the logged-in username.
     *
     * @param username the username of the logged-in user
     */
    public void setLoggedInUsername(String username) {
        loggedInUsername = username;
        System.out.println("Logged-in username: " + loggedInUsername);

        // Load grade data immediately after setting the username
        if (!loadGradeData()) {
            showAlert("No Data Available", "No grade statistics available for the logged-in user.", Alert.AlertType.WARNING);
        }
    }

    /**
     * Load all grade data into the table.
     *
     * @return true if data is successfully loaded, false otherwise
     */
    private boolean loadGradeData() {
        List<GradeRecord> records = gradeStatisticsService.getStudentGradeRecords(loggedInUsername);
        if (records.isEmpty()) {
            return false;
        }
        masterData.clear();
        masterData.addAll(records); // 添加新数据
        gradeTable.setItems(masterData); // 更新表格数据

        populateCourseComboBox(); // 加载课程下拉框

        return true;
    }

    /**
     * Populate course ComboBox with unique courses from data.
     */
    private void populateCourseComboBox() {
        Set<String> courses = new HashSet<>(); // 使用 HashSet 避免重复
        for (GradeRecord record : masterData) {
            courses.add(record.getCourse());
        }

        if (courses.isEmpty()) {
            showAlert("No Courses Found", "No courses are available to filter.", Alert.AlertType.INFORMATION);
            return;
        }

        List<String> courseList = new ArrayList<>(courses); // 转换为 List
        Collections.sort(courseList); // 排序
        courseList.add(0, "All Courses"); // 添加 "All Courses" 选项
        courseComboBox.setItems(FXCollections.observableArrayList(courseList));
        courseComboBox.getSelectionModel().selectFirst();
    }

    /**
     * Handle the "Filter" button action.
     * Filter data based on the selected course.
     */
    @FXML
    private void handleFilter() {
        String selectedCourse = courseComboBox.getSelectionModel().getSelectedItem();

        // 获取当前登录的学生的成绩记录
        List<GradeRecord> filteredRecords = gradeStatisticsService.getStudentGradeRecords(loggedInUsername);

        // 如果没有选中 "All Courses"，进一步按课程进行筛选
        if (selectedCourse != null && !selectedCourse.equals("All Courses")) {
            filteredRecords.removeIf(record -> !record.getCourse().equals(selectedCourse)); // 只保留选中的课程
        }

        // 如果没有符合条件的记录，显示提示信息
        if (filteredRecords.isEmpty()) {
            showAlert("No Data Found", "No grade statistics found for the selected course.", Alert.AlertType.INFORMATION);
        }

        // 更新表格和图表
        ObservableList<GradeRecord> filteredData = FXCollections.observableArrayList(filteredRecords);
        gradeTable.setItems(filteredData);
        updateBarChart(filteredData);
    }

    /**
     * Handle the "Reset" button action.
     * Clear all filters and reset to original data.
     */
    @FXML
    private void handleReset() {
        courseComboBox.getSelectionModel().selectFirst();
        gradeTable.setItems(masterData);
        updateBarChart(masterData);
    }

    /**
     * Handle the "Refresh" button action.
     * Reload data from the file and update the table and chart.
     */
    @FXML
    private void handleRefresh() {
        masterData.clear();
        if (!loadGradeData()) {
            showAlert("No Data Available", "No grade statistics available to display after refresh.", Alert.AlertType.WARNING);
        }
        populateCourseComboBox();
        updateBarChart(masterData);
    }

    /**
     * Update the bar chart with the provided data.
     *
     * @param data The data to display in the bar chart.
     */
    private void updateBarChart(ObservableList<GradeRecord> data) {
        gradeBarChart.getData().clear();

        // Clear x-axis to ensure labels are centered correctly
        xAxis.getCategories().clear();

        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.setName("Scores");

        for (GradeRecord record : data) {
            // Use "CourseID-QuizName" as the x-axis label
            String xAxisLabel = record.getCourse() + "-" + record.getExam();
            xAxis.getCategories().add(xAxisLabel); // Ensure x-axis is updated with new labels
            series.getData().add(new XYChart.Data<>(xAxisLabel, record.getScore()));
        }

        gradeBarChart.getData().add(series);
    }

    /**
     * Show alert dialog with a given title, message, and alert type.
     *
     * @param title   The title of the alert dialog.
     * @param message The message to display in the alert.
     * @param type    The type of the alert.
     */
    private void showAlert(String title, String message, Alert.AlertType type) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
