package comp3111.examsystem.service;

import comp3111.examsystem.data.DataManager;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.chart.*;

import java.util.*;
import java.util.stream.Collectors;

public class TeacherGradeStatisticService {

    private final DataManager dataManager;
    private final ObservableList<Map<String, String>> gradeList;

    public TeacherGradeStatisticService(DataManager dataManager) {
        this.dataManager = dataManager;
        this.gradeList = FXCollections.observableArrayList(loadGrades());
    }

    private List<Map<String, String>> loadGrades() {
        List<Map<String, String>> grades = new ArrayList<>();

        // 示例数据加载，您需要根据实际情况从 dataManager 获取数据
        // 以下仅为示例，请根据您的数据结构进行修改

        Map<String, String> grade1 = new HashMap<>();
        grade1.put("studentName", "Alice");
        grade1.put("courseNum", "CS101");
        grade1.put("examName", "Midterm");
        grade1.put("score", "85");
        grade1.put("fullScore", "100");
        grade1.put("passStatus", "Pass");
        grades.add(grade1);

        Map<String, String> grade2 = new HashMap<>();
        grade2.put("studentName", "Bob");
        grade2.put("courseNum", "CS101");
        grade2.put("examName", "Midterm");
        grade2.put("score", "90");
        grade2.put("fullScore", "100");
        grade2.put("passStatus", "Pass");
        grades.add(grade2);

        Map<String, String> grade3 = new HashMap<>();
        grade3.put("studentName", "Alice");
        grade3.put("courseNum", "CS102");
        grade3.put("examName", "Final");
        grade3.put("score", "95");
        grade3.put("fullScore", "100");
        grade3.put("passStatus", "Pass");
        grades.add(grade3);

        return grades;
    }

    public ObservableList<Map<String, String>> getGradeList() {
        return gradeList;
    }

    public List<String> getCourses() {
        return gradeList.stream().map(g -> g.get("courseNum")).filter(Objects::nonNull).distinct().collect(Collectors.toList());
    }

    public List<String> getExams() {
        return gradeList.stream().map(g -> g.get("examName")).filter(Objects::nonNull).distinct().collect(Collectors.toList());
    }

    public List<String> getStudents() {
        return gradeList.stream().map(g -> g.get("studentName")).filter(Objects::nonNull).distinct().collect(Collectors.toList());
    }

    public void updateBarChart(BarChart<String, Number> barChart, List<Map<String, String>> grades) {
        Map<String, Double> courseAverageScores = grades.stream()
                .filter(g -> g.get("courseNum") != null && g.get("score") != null)
                .collect(Collectors.groupingBy(
                        g -> g.get("courseNum"),
                        Collectors.averagingDouble(g -> Double.parseDouble(g.get("score")))
                ));

        XYChart.Series<String, Number> series = new XYChart.Series<>();
        courseAverageScores.forEach((course, average) ->
                series.getData().add(new XYChart.Data<>(course, average))
        );

        barChart.getData().clear();
        barChart.getData().add(series);
    }

    public void updatePieChart(PieChart pieChart, List<Map<String, String>> grades) {
        Map<String, Integer> studentScores = grades.stream()
                .filter(g -> g.get("studentName") != null && g.get("score") != null)
                .collect(Collectors.groupingBy(
                        g -> g.get("studentName"),
                        Collectors.summingInt(g -> Integer.parseInt(g.get("score")))
                ));

        pieChart.getData().clear();
        studentScores.forEach((student, score) ->
                pieChart.getData().add(new PieChart.Data(student, score))
        );
    }

    public void updateLineChart(LineChart<String, Number> lineChart, List<Map<String, String>> grades) {
        Map<String, Double> examAverageScores = grades.stream()
                .filter(g -> g.get("examName") != null && g.get("score") != null)
                .collect(Collectors.groupingBy(
                        g -> g.get("examName"),
                        Collectors.averagingDouble(g -> Double.parseDouble(g.get("score")))
                ));

        XYChart.Series<String, Number> series = new XYChart.Series<>();
        examAverageScores.forEach((exam, average) ->
                series.getData().add(new XYChart.Data<>(exam, average))
        );

        lineChart.getData().clear();
        lineChart.getData().add(series);
    }

    public List<Map<String, String>> filterGrades(String selectedCourse, String selectedExam, String selectedStudent) {
        return gradeList.stream()
                .filter(grade ->
                        (selectedCourse == null || selectedCourse.isEmpty() || selectedCourse.equals(grade.get("courseNum"))) &&
                                (selectedExam == null || selectedExam.isEmpty() || selectedExam.equals(grade.get("examName"))) &&
                                (selectedStudent == null || selectedStudent.isEmpty() || selectedStudent.equals(grade.get("studentName")))
                )
                .collect(Collectors.toList());
    }
}
