package comp3111.examsystem.service;

import comp3111.examsystem.entity.ExamResult;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.chart.XYChart;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class GradeStatisticsService {

    public ObservableList<ExamResult> loadExamResults() {
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
            series.getData().add(new XYChart.Data<>(result.getExamName(), result.getScore()));
        }
        return series;
    }
}
