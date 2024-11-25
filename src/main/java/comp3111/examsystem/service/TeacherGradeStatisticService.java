package comp3111.examsystem.service;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.chart.*;
import comp3111.examsystem.controller.TeacherGradeStatisticController;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * The TeacherGradeStatisticService class provides functionalities to manage and analyze teacher grade statistics.
 * It allows loading grades from a file, updating charts with statistical data, and filtering grades based on criteria.
 */
public class TeacherGradeStatisticService {
    private final ObservableList<TeacherGradeStatisticController.Grade> gradeList = FXCollections.observableArrayList();

    /**
     * Loads grades from a specified file into the gradeList.
     *
     * @param filePath The path to the file containing grade data.
     */
    public void loadGradesFromFile(String filePath) {
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] data = line.split(",");
                if (data.length == 6) {
                    TeacherGradeStatisticController.Grade grade = new TeacherGradeStatisticController.Grade(
                            data[0].trim(), data[1].trim(), data[2].trim(),
                            data[3].trim(), data[4].trim(), data[5].trim());
                    gradeList.add(grade);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Returns the list of grades.
     *
     * @return An ObservableList of Grade objects.
     */
    public ObservableList<TeacherGradeStatisticController.Grade> getGradeList() {
        return gradeList;
    }

    /**
     * Updates the provided BarChart with average scores for each course based on the given grades.
     *
     * @param barChart The BarChart to be updated.
     * @param grades   The list of grades used to calculate average scores.
     */
    public void updateBarChart(BarChart<String, Number> barChart, List<TeacherGradeStatisticController.Grade> grades) {
        Map<String, Double> courseAverageScores = new HashMap<>();
        Map<String, Integer> courseScoreCounts = new HashMap<>();

        for (TeacherGradeStatisticController.Grade grade : grades) {
            String course = grade.getCourseNum();
            double score = Double.parseDouble(grade.getScore());

            courseAverageScores.put(course, courseAverageScores.getOrDefault(course, 0.0) + score);
            courseScoreCounts.put(course, courseScoreCounts.getOrDefault(course, 0) + 1);
        }

        XYChart.Series<String, Number> seriesBar = new XYChart.Series<>();
        for (String course : courseAverageScores.keySet()) {
            double averageScore = courseAverageScores.get(course) / courseScoreCounts.get(course);
            seriesBar.getData().add(new XYChart.Data<>(course, averageScore));
        }
        barChart.getData().clear();
        barChart.getData().add(seriesBar);
    }

    /**
     * Updates the provided PieChart with total scores for each student based on the given grades.
     *
     * @param pieChart The PieChart to be updated.
     * @param grades   The list of grades used to calculate total scores.
     */
    public void updatePieChart(PieChart pieChart, List<TeacherGradeStatisticController.Grade> grades) {
        Map<String, Integer> studentScoreCounts = new HashMap<>();

        for (TeacherGradeStatisticController.Grade grade : grades) {
            String student = grade.getStudentName();
            studentScoreCounts.put(student, studentScoreCounts.getOrDefault(student, 0) + Integer.parseInt(grade.getScore()));
        }

        pieChart.getData().clear();
        for (String student : studentScoreCounts.keySet()) {
            pieChart.getData().add(new PieChart.Data(student, studentScoreCounts.get(student)));
        }
    }

    /**
     * Updates the provided LineChart with average scores for each exam based on the given grades.
     *
     * @param lineChart The LineChart to be updated.
     * @param grades    The list of grades used to calculate average scores.
     */
    public void updateLineChart(LineChart<String, Number> lineChart, List<TeacherGradeStatisticController.Grade> grades) {
        Map<String, Double> examAverageScores = new HashMap<>();
        Map<String, Integer> examScoreCounts = new HashMap<>();

        for (TeacherGradeStatisticController.Grade grade : grades) {
            String exam = grade.getExamName();
            double score = Double.parseDouble(grade.getScore());

            examAverageScores.put(exam, examAverageScores.getOrDefault(exam, 0.0) + score);
            examScoreCounts.put(exam, examScoreCounts.getOrDefault(exam, 0) + 1);
        }

        XYChart.Series<String, Number> seriesLine = new XYChart.Series<>();
        for (String exam : examAverageScores.keySet()) {
            double averageScore = examAverageScores.get(exam) / examScoreCounts.get(exam);
            seriesLine.getData().add(new XYChart.Data<>(exam, averageScore));
        }
        lineChart.getData().clear();
        lineChart.getData().add(seriesLine);
    }

    /**
     * Filters the grades based on the selected course, exam, and student.
     *
     * @param selectedCourse The course to filter by.
     * @param selectedExam   The exam to filter by.
     * @param selectedStudent The student to filter by.
     * @return A list of grades matching the filter criteria.
     */

    public List<TeacherGradeStatisticController.Grade> filterGrades(String selectedCourse, String selectedExam, String selectedStudent) {
        return gradeList.stream()
                .filter(grade -> (selectedCourse == null || grade.getCourseNum().equals(selectedCourse)) &&
                        (selectedExam == null || grade.getExamName().equals(selectedExam)) &&
                        (selectedStudent == null || grade.getStudentName().equals(selectedStudent)))
                .collect(Collectors.toList());
    }
}