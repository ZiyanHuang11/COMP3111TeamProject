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
    private TableColumn<ExamResult, Integer> scoreColumn;
    @FXML
    private TableColumn<ExamResult, Integer> fullScoreColumn;
    @FXML
    private TableColumn<ExamResult, Integer> timeColumn;

    @FXML
    private Label averageScoreLabel;

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
        // Initialize table columns
        courseIDColumn.setCellValueFactory(cell -> cell.getValue().courseIDProperty());
        examNameColumn.setCellValueFactory(cell -> cell.getValue().examNameProperty());
        scoreColumn.setCellValueFactory(cell -> cell.getValue().scoreProperty().asObject());
        fullScoreColumn.setCellValueFactory(cell -> cell.getValue().totalScoreProperty().asObject());
        timeColumn.setCellValueFactory(cell -> cell.getValue().timeProperty().asObject());

        // Load exam results and populate the table
        examResults = gradeStatisticsService.loadExamSummaryResults();
        gradeTable.setItems(examResults);

        // Populate the ComboBox with course options
        initializeCourseComboBox();

        // Update bar chart
        updateBarChart(examResults);

        // Update statistics
        updateStatistics(examResults);
    }

    private void initializeCourseComboBox() {
        ObservableList<String> courseList = gradeStatisticsService.getCourseList(examResults);
        courseComboBox.setItems(courseList);
    }

    @FXML
    private void filterResults() {
        String selectedCourse = courseComboBox.getValue();
        if (selectedCourse == null || selectedCourse.isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "Filter Error", "Please select a course to filter.");
            return;
        }

        ObservableList<ExamResult> filteredResults = gradeStatisticsService.filterResultsByCourseName(examResults, selectedCourse);
        gradeTable.setItems(filteredResults);
        updateBarChart(filteredResults);
        updateStatistics(filteredResults);
    }

    @FXML
    private void resetFilter() {
        gradeTable.setItems(examResults);
        updateBarChart(examResults);
        updateStatistics(examResults);
        courseComboBox.setValue(null);
    }

    @FXML
    private void refreshData() {
        examResults = gradeStatisticsService.loadExamSummaryResults();
        gradeTable.setItems(examResults);
        initializeCourseComboBox();
        updateBarChart(examResults);
        updateStatistics(examResults);
    }

    private void updateStatistics(ObservableList<ExamResult> results) {
        averageScoreLabel.setText(String.format("Average Score: %.2f", gradeStatisticsService.calculateAverageScore(results)));
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
