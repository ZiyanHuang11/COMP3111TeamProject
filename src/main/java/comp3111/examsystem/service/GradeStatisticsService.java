package comp3111.examsystem.service;

import comp3111.examsystem.entity.ExamResult;
import comp3111.examsystem.data.DataManager;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.chart.XYChart;

public class GradeStatisticsService {

    private final DataManager dataManager;

    public GradeStatisticsService() {
        this.dataManager = new DataManager();
    }

    public ObservableList<ExamResult> loadExamResults() {
        return FXCollections.observableArrayList(dataManager.getExamResults());
    }

    public ObservableList<String> getCourseList(ObservableList<ExamResult> examResults) {
        ObservableList<String> courseList = FXCollections.observableArrayList();
        for (ExamResult result : examResults) {
            if (!courseList.contains(result.getCourseID())) {
                courseList.add(result.getCourseID());
            }
        }
        return courseList;
    }

    public ObservableList<ExamResult> filterResultsByCourse(ObservableList<ExamResult> results, String courseID) {
        ObservableList<ExamResult> filteredResults = FXCollections.observableArrayList();
        for (ExamResult result : results) {
            if (result.getCourseID().equals(courseID)) {
                filteredResults.add(result);
            }
        }
        return filteredResults;
    }

    public double calculateAverageScore(ObservableList<ExamResult> results) {
        if (results.isEmpty()) return 0.0;
        int totalScore = results.stream().mapToInt(ExamResult::getScore).sum();
        return (double) totalScore / results.size();
    }

    public double calculatePassRate(ObservableList<ExamResult> results) {
        if (results.isEmpty()) return 0.0;
        long passCount = results.stream().filter(r -> "Yes".equalsIgnoreCase(r.getPassStatus())).count();
        return (double) passCount / results.size() * 100;
    }

    public XYChart.Series<String, Number> generateBarChartSeries(ObservableList<ExamResult> results) {
        XYChart.Series<String, Number> series = new XYChart.Series<>();
        for (ExamResult result : results) {
            String examName = result.getExamName();
            if (examName != null && !examName.trim().isEmpty()) { // 跳过 null 或空值
                series.getData().add(new XYChart.Data<>(examName, result.getScore()));
            }
        }

        return series;
    }
}
