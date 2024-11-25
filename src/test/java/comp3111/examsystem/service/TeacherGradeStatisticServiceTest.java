package comp3111.examsystem.service;

import comp3111.examsystem.controller.TeacherGradeStatisticController;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javafx.collections.ObservableList;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.PieChart;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class TeacherGradeStatisticServiceTest {

    private TeacherGradeStatisticService service;

    @BeforeEach
    void setUp() {
        service = new TeacherGradeStatisticService();
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

    private void loadTestData(String inputData) {
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
    }
}