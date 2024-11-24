package comp3111.examsystem.controller;

import comp3111.examsystem.entity.ExamResult;
import comp3111.examsystem.service.GradeStatisticsService;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;

public class GradeStatisticsController {

    @FXML
    private TableView<ExamResult> gradeTable;

    @FXML
    private TableColumn<ExamResult, String> courseIDColumn;
    @FXML
    private TableColumn<ExamResult, String> examNameColumn;
    @FXML
    private TableColumn<ExamResult, Integer> totalScoreColumn;
    @FXML
    private TableColumn<ExamResult, Integer> scoreColumn;
    @FXML
    private TableColumn<ExamResult, String> passColumn;

    @FXML
    private Label averageScoreLabel;

    @FXML
    private Label passRateLabel;

    @FXML
    private ComboBox<String> courseComboBox;

    @FXML
    private BarChart<String, Number> barChart;

    private final GradeStatisticsService gradeStatisticsService;

    private ObservableList<ExamResult> examResults;

    public GradeStatisticsController() {
        gradeStatisticsService = new GradeStatisticsService();
    }

    @FXML
    public void initialize() {
        // 初始化表格列
        courseIDColumn.setCellValueFactory(cell -> cell.getValue().courseIDProperty());
        examNameColumn.setCellValueFactory(cell -> cell.getValue().examNameProperty());
        totalScoreColumn.setCellValueFactory(cell -> cell.getValue().totalScoreProperty().asObject());
        scoreColumn.setCellValueFactory(cell -> cell.getValue().scoreProperty().asObject());
        passColumn.setCellValueFactory(cell -> cell.getValue().passStatusProperty());

        // 加载数据
        examResults = gradeStatisticsService.loadExamResults();
        gradeTable.setItems(examResults);

        // 初始化课程选择框
        initializeCourseComboBox();

        // 更新统计信息和柱状图
        updateStatistics(examResults);
        updateBarChart(examResults);
    }

    private void initializeCourseComboBox() {
        courseComboBox.setItems(gradeStatisticsService.getCourseList(examResults));
    }

    @FXML
    private void filterResults() {
        String selectedCourse = courseComboBox.getValue();
        if (selectedCourse == null || selectedCourse.isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "Filter Error", "Please select a course to filter.");
            return;
        }

        ObservableList<ExamResult> filteredResults = gradeStatisticsService.filterResultsByCourse(examResults, selectedCourse);
        gradeTable.setItems(filteredResults);
        updateStatistics(filteredResults);
        updateBarChart(filteredResults);
    }

    @FXML
    private void resetFilter() {
        gradeTable.setItems(examResults);
        updateStatistics(examResults);
        updateBarChart(examResults);
        courseComboBox.setValue(null);
    }

    @FXML
    private void refreshData() {
        examResults = gradeStatisticsService.loadExamResults();
        gradeTable.setItems(examResults);
        initializeCourseComboBox();
        updateStatistics(examResults);
        updateBarChart(examResults);
    }

    private void updateStatistics(ObservableList<ExamResult> results) {
        averageScoreLabel.setText(String.format("Average Score: %.2f", gradeStatisticsService.calculateAverageScore(results)));
        passRateLabel.setText(String.format("Pass Rate: %.2f%%", gradeStatisticsService.calculatePassRate(results)));
    }

    private void updateBarChart(ObservableList<ExamResult> results) {
        barChart.getData().clear();
        XYChart.Series<String, Number> series = gradeStatisticsService.generateBarChartSeries(results);
        barChart.getData().add(series);
    }

    private void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
