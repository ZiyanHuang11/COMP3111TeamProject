package comp3111.examsystem.service;

import comp3111.examsystem.controller.TeacherGradeStatisticController.Grade;
import comp3111.examsystem.data.DataManager;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.chart.*;

import java.util.*;
import java.util.stream.Collectors;

public class TeacherGradeStatisticService {

    private final DataManager dataManager;
    private final ObservableList<Grade> gradeList;

    public TeacherGradeStatisticService(DataManager dataManager) {
        this.dataManager = dataManager;
        this.gradeList = FXCollections.observableArrayList(loadGrades());
    }

    private List<Grade> loadGrades() {
        List<Grade> grades = new ArrayList<>();
        dataManager.getExamResults().forEach(result -> {
            dataManager.getStudents().stream()
                    .filter(student -> student.getId().equals(result.getStudentID()))
                    .findFirst()
                    .ifPresent(student -> grades.add(new Grade(
                            student.getName(),
                            result.getCourseID(),
                            result.getExamName(),
                            String.valueOf(result.getScore()),
                            String.valueOf(result.getTotalScore()),
                            result.getPassStatus()
                    )));
        });
        return grades;
    }

    public ObservableList<Grade> getGradeList() {
        return gradeList;
    }

    public List<String> getCourses() {
        return gradeList.stream().map(Grade::getCourseNum).distinct().collect(Collectors.toList());
    }

    public List<String> getExams() {
        return gradeList.stream().map(Grade::getExamName).distinct().collect(Collectors.toList());
    }

    public List<String> getStudents() {
        return gradeList.stream().map(Grade::getStudentName).distinct().collect(Collectors.toList());
    }

    public void updateBarChart(BarChart<String, Number> barChart, List<Grade> grades) {
        Map<String, Double> courseAverageScores = grades.stream()
                .collect(Collectors.groupingBy(
                        Grade::getCourseNum,
                        Collectors.averagingDouble(g -> Double.parseDouble(g.getScore()))
                ));

        XYChart.Series<String, Number> series = new XYChart.Series<>();
        courseAverageScores.forEach((course, average) ->
                series.getData().add(new XYChart.Data<>(course, average))
        );

        barChart.getData().clear();
        barChart.getData().add(series);
    }

    public void updatePieChart(PieChart pieChart, List<Grade> grades) {
        Map<String, Integer> studentScores = grades.stream()
                .collect(Collectors.groupingBy(
                        Grade::getStudentName,
                        Collectors.summingInt(g -> Integer.parseInt(g.getScore()))
                ));

        pieChart.getData().clear();
        studentScores.forEach((student, score) ->
                pieChart.getData().add(new PieChart.Data(student, score))
        );
    }

    public void updateLineChart(LineChart<String, Number> lineChart, List<Grade> grades) {
        Map<String, Double> examAverageScores = grades.stream()
                .collect(Collectors.groupingBy(
                        Grade::getExamName,
                        Collectors.averagingDouble(g -> Double.parseDouble(g.getScore()))
                ));

        XYChart.Series<String, Number> series = new XYChart.Series<>();
        examAverageScores.forEach((exam, average) ->
                series.getData().add(new XYChart.Data<>(exam, average))
        );

        lineChart.getData().clear();
        lineChart.getData().add(series);
    }

    /**
     * 过滤成绩列表
     * @param selectedCourse 选择的课程编号
     * @param selectedExam 选择的考试名称
     * @param selectedStudent 选择的学生姓名
     * @return 过滤后的成绩列表
     */
    public List<Grade> filterGrades(String selectedCourse, String selectedExam, String selectedStudent) {
        return gradeList.stream()
                .filter(grade -> (selectedCourse == null || selectedCourse.isEmpty() || grade.getCourseNum().equals(selectedCourse)) &&
                        (selectedExam == null || selectedExam.isEmpty() || grade.getExamName().equals(selectedExam)) &&
                        (selectedStudent == null || selectedStudent.isEmpty() || grade.getStudentName().equals(selectedStudent)))
                .collect(Collectors.toList());
    }
}
