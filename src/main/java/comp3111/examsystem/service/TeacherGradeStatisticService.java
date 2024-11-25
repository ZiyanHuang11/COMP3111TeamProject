package comp3111.examsystem.service;

import comp3111.examsystem.data.DataManager;
import comp3111.examsystem.entity.ExamResult;
import comp3111.examsystem.entity.Student;
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

    // 在服务类中定义 Grade 类
    public static class Grade {
        private final String studentName;
        private final String courseNum;
        private final String examName;
        private final String score;
        private final String fullScore;
        private final String passStatus;

        public Grade(String studentName, String courseNum, String examName, String score, String fullScore, String passStatus) {
            this.studentName = studentName;
            this.courseNum = courseNum;
            this.examName = examName;
            this.score = score;
            this.fullScore = fullScore;
            this.passStatus = passStatus;
        }

        // Getter 方法
        public String getStudentName() { return studentName; }
        public String getCourseNum() { return courseNum; }
        public String getExamName() { return examName; }
        public String getScore() { return score; }
        public String getFullScore() { return fullScore; }
        public String getPassStatus() { return passStatus; }
    }

    private List<Grade> loadGrades() {
        List<Grade> grades = new ArrayList<>();
        List<ExamResult> examResults = dataManager.getExamResults();
        List<Student> students = dataManager.getStudents();

        for (ExamResult result : examResults) {
            for (Student student : students) {
                if (student.getId().equals(result.getStudentID())) {
                    grades.add(new Grade(
                            student.getName(),
                            result.getCourseID(),
                            result.getExamName(),
                            String.valueOf(result.getScore()),
                            String.valueOf(result.getTotalScore()),
                            result.getPassStatus()
                    ));
                    break;
                }
            }
        }
        return grades;
    }

    public ObservableList<Grade> getGradeList() {
        return gradeList;
    }

    public List<String> getCourses() {
        return gradeList.stream().map(Grade::getCourseNum).filter(Objects::nonNull).distinct().collect(Collectors.toList());
    }

    public List<String> getExams() {
        return gradeList.stream().map(Grade::getExamName).filter(Objects::nonNull).distinct().collect(Collectors.toList());
    }

    public List<String> getStudents() {
        return gradeList.stream().map(Grade::getStudentName).filter(Objects::nonNull).distinct().collect(Collectors.toList());
    }

    public void updateBarChart(BarChart<String, Number> barChart, List<Grade> grades) {
        Map<String, Double> courseAverageScores = grades.stream()
                .filter(g -> g.getCourseNum() != null && g.getScore() != null)
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
                .filter(g -> g.getStudentName() != null && g.getScore() != null)
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
                .filter(g -> g.getExamName() != null && g.getScore() != null)
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

    public List<Grade> filterGrades(String selectedCourse, String selectedExam, String selectedStudent) {
        return gradeList.stream()
                .filter(grade ->
                        (selectedCourse == null || selectedCourse.isEmpty() || selectedCourse.equals(grade.getCourseNum())) &&
                                (selectedExam == null || selectedExam.isEmpty() || selectedExam.equals(grade.getExamName())) &&
                                (selectedStudent == null || selectedStudent.isEmpty() || selectedStudent.equals(grade.getStudentName()))
                )
                .collect(Collectors.toList());
    }
}
