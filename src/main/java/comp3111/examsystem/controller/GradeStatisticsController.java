package comp3111.examsystem.controller;

import comp3111.examsystem.entity.ExamResult;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

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

    private ObservableList<ExamResult> examResults;

    @FXML
    public void initialize() {
        // 初始化表格列
        courseIDColumn.setCellValueFactory(new PropertyValueFactory<>("courseID"));
        examNameColumn.setCellValueFactory(new PropertyValueFactory<>("examName"));
        totalScoreColumn.setCellValueFactory(new PropertyValueFactory<>("totalScore"));
        scoreColumn.setCellValueFactory(new PropertyValueFactory<>("score"));
        passColumn.setCellValueFactory(new PropertyValueFactory<>("passStatus"));

        // 加载数据
        examResults = loadExamResults();
        gradeTable.setItems(examResults);

        // 初始化课程选择框
        initializeCourseComboBox();

        // 更新统计信息和柱状图
        updateStatistics(examResults);
        updateBarChart(examResults);
    }

    private ObservableList<ExamResult> loadExamResults() {
        ObservableList<ExamResult> results = FXCollections.observableArrayList();
        try (BufferedReader br = new BufferedReader(new FileReader("data/exam_results.txt"))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] fields = line.split(",");
                if (fields.length >= 5) {
                    String courseID = fields[0].trim();
                    String examName = fields[1].trim();
                    int totalScore = Integer.parseInt(fields[2].trim());
                    int score = Integer.parseInt(fields[3].trim());
                    String passStatus = fields[4].trim();

                    results.add(new ExamResult(courseID, examName, totalScore, score, passStatus));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return results;
    }

    private void initializeCourseComboBox() {
        ObservableList<String> courseList = FXCollections.observableArrayList();
        for (ExamResult result : examResults) {
            if (!courseList.contains(result.getCourseID())) {
                courseList.add(result.getCourseID());
            }
        }
        courseComboBox.setItems(courseList);
    }

    @FXML
    private void filterResults() {
        String selectedCourse = courseComboBox.getValue();
        if (selectedCourse == null || selectedCourse.isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "Filter Error", "Please select a course to filter.");
            return;
        }

        ObservableList<ExamResult> filteredResults = FXCollections.observableArrayList();
        for (ExamResult result : examResults) {
            if (result.getCourseID().equals(selectedCourse)) {
                filteredResults.add(result);
            }
        }

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
        examResults = loadExamResults();
        gradeTable.setItems(examResults);
        updateStatistics(examResults);
        updateBarChart(examResults);
    }

    private void updateStatistics(ObservableList<ExamResult> results) {
        if (results.isEmpty()) {
            averageScoreLabel.setText("Average Score: N/A");
            passRateLabel.setText("Pass Rate: N/A");
            return;
        }

        int totalScore = 0;
        int passCount = 0;
        for (ExamResult result : results) {
            totalScore += result.getScore();
            if ("Yes".equalsIgnoreCase(result.getPassStatus())) {
                passCount++;
            }
        }

        double averageScore = (double) totalScore / results.size();
        double passRate = (double) passCount / results.size() * 100;

        averageScoreLabel.setText(String.format("Average Score: %.2f", averageScore));
        passRateLabel.setText(String.format("Pass Rate: %.2f%%", passRate));
    }

    private void updateBarChart(ObservableList<ExamResult> results) {
        barChart.getData().clear();

        XYChart.Series<String, Number> series = new XYChart.Series<>();
        for (ExamResult result : results) {
            series.getData().add(new XYChart.Data<>(result.getExamName(), result.getScore()));
        }

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
