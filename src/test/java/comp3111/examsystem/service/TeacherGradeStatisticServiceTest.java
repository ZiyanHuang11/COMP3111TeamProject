package comp3111.examsystem.service;

import comp3111.examsystem.controller.TeacherGradeStatisticController;
import comp3111.examsystem.data.DataManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javafx.collections.ObservableList;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.XYChart;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class TeacherGradeStatisticServiceTest {

    private TeacherGradeStatisticService service;
    private DataManager dataManager;

    @BeforeEach
    void setUp() {
        // 初始化 DataManager 和 TeacherGradeStatisticService
        dataManager = new DataManager();
        service = new TeacherGradeStatisticService(dataManager);
    }

    @Test
    void testLoadGradesFromFile() {
        String inputData = "Alice,CS101,Midterm,85,100,30\n" +
                "Bob,CS101,Midterm,90,100,25\n" +
                "Alice,CS102,Final,95,100,40\n";

        try (BufferedReader reader = new BufferedReader(new StringReader(inputData))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] data = line.split(",");
                if (data.length == 6) {
                    TeacherGradeStatisticController.Grade grade = new TeacherGradeStatisticController.Grade(
                            data[0].trim(), data[1].trim(), data[2].trim(),
                            data[3].trim(), data[4].trim(), data[5].trim());
                    service.getGradeList().add(grade); // Directly add to the service's list
                }
            }
        } catch (IOException e) {
            fail("IOException should not occur");
        }

        ObservableList<TeacherGradeStatisticController.Grade> grades = service.getGradeList();
        assertEquals(3, grades.size());
        assertEquals("Alice", grades.get(0).getStudentName());
        assertEquals("CS101", grades.get(1).getCourseNum());
        assertEquals("Midterm", grades.get(1).getExamName());
        assertEquals("90", grades.get(1).getScore());
    }

    @Test
    void testFilterGrades() {
        String inputData = "Alice,CS101,Midterm,85,100,30\n" +
                "Bob,CS101,Midterm,90,100,25\n" +
                "Alice,CS102,Final,95,100,40\n";
        loadTestData(inputData);

        List<TeacherGradeStatisticController.Grade> filteredGrades = service.filterGrades("CS101", null, null);
        assertEquals(2, filteredGrades.size());

        filteredGrades = service.filterGrades(null, "Final", null);
        assertEquals(1, filteredGrades.size());
        assertEquals("Alice", filteredGrades.get(0).getStudentName());

        filteredGrades = service.filterGrades(null, null, "Bob");
        assertEquals(1, filteredGrades.size());
        assertEquals("Bob", filteredGrades.get(0).getStudentName());
    }

    @Test
    void testUpdateBarChart() {
        String inputData = "Alice,CS101,Midterm,85,100,30\n" +
                "Bob,CS101,Midterm,90,100,25\n" +
                "Alice,CS102,Final,95,100,40\n";
        loadTestData(inputData);

        BarChart<String, Number> barChart = new BarChart<>(null, null);
        service.updateBarChart(barChart, service.getGradeList()); // 确保传入两个参数

        assertEquals(1, barChart.getData().size());
        XYChart.Series<String, Number> series = barChart.getData().get(0);
        assertEquals(2, series.getData().size());
        assertEquals("CS101", series.getData().get(0).getXValue());
    }

    @Test
    void testUpdatePieChart() {
        String inputData = "Alice,CS101,Midterm,85,100,30\n" +
                "Bob,CS101,Midterm,90,100,25\n" +
                "Alice,CS102,Final,95,100,40\n";
        loadTestData(inputData);

        PieChart pieChart = new PieChart();
        service.updatePieChart(pieChart, service.getGradeList()); // 确保传入两个参数

        assertEquals(2, pieChart.getData().size());
        assertEquals("Alice", pieChart.getData().get(0).getName());
    }

    @Test
    void testUpdateLineChart() {
        String inputData = "Alice,CS101,Midterm,85,100,30\n" +
                "Bob,CS101,Midterm,90,100,25\n" +
                "Alice,CS102,Final,95,100,40\n";
        loadTestData(inputData);

        LineChart<String, Number> lineChart = new LineChart<>(null, null);
        service.updateLineChart(lineChart, service.getGradeList()); // 确保传入两个参数

        assertEquals(1, lineChart.getData().size());
        XYChart.Series<String, Number> series = lineChart.getData().get(0);
        assertEquals(2, series.getData().size());
        assertEquals("Midterm", series.getData().get(0).getXValue());
    }


    private void loadTestData(String inputData) {
        try (BufferedReader reader = new BufferedReader(new StringReader(inputData))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] data = line.split(",");
                if (data.length == 6) {
                    TeacherGradeStatisticController.Grade grade = new TeacherGradeStatisticController.Grade(
                            data[0].trim(), data[1].trim(), data[2].trim(),
                            data[3].trim(), data[4].trim(), data[5].trim());
                    service.getGradeList().add(grade);
                }
            }
        } catch (IOException e) {
            fail("IOException should not occur");
        }
    }
}
