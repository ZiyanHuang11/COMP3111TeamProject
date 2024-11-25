package comp3111.examsystem.service;

import comp3111.examsystem.data.DataManager;
import comp3111.examsystem.entity.ExamResult;
import comp3111.examsystem.entity.Student;
import comp3111.examsystem.service.TeacherGradeStatisticService.Grade;
import javafx.collections.ObservableList;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

class TeacherGradeStatisticServiceTest {

    private TeacherGradeStatisticService service;
    private DataManager dataManager;

    @BeforeEach
    void setUp() {
        // 使用 MockDataManager
        dataManager = new MockDataManager();
        service = new TeacherGradeStatisticService(dataManager);
    }

    @Test
    void testLoadGradesFromFile() {
        String inputData = "Alice,CS101,Midterm,85,100,Pass\n" +
                "Bob,CS101,Midterm,90,100,Pass\n" +
                "Alice,CS102,Final,95,100,Pass\n";

        loadTestData(inputData);

        ObservableList<Grade> grades = service.getGradeList();
        assertEquals(3, grades.size());
        assertEquals("Alice", grades.get(0).getStudentName());
        assertEquals("CS101", grades.get(0).getCourseNum());
        assertEquals("Midterm", grades.get(0).getExamName());
        assertEquals("85", grades.get(0).getScore());
    }

    @Test
    void testFilterGrades() {
        String inputData = "Alice,CS101,Midterm,85,100,Pass\n" +
                "Bob,CS101,Midterm,90,100,Pass\n" +
                "Alice,CS102,Final,95,100,Pass\n";
        loadTestData(inputData);

        List<Grade> filteredGrades = service.filterGrades("CS101", null, null);
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
        String inputData = "Alice,CS101,Midterm,85,100,Pass\n" +
                "Bob,CS101,Midterm,90,100,Pass\n" +
                "Alice,CS102,Final,95,100,Pass\n";
        loadTestData(inputData);

        // 由于在测试中无法初始化 JavaFX 平台，我们可以模拟数据
        Map<String, Number> barChartData = new HashMap<>();
        service.updateBarChart(barChartData, service.getGradeList());

        assertEquals(2, barChartData.size());
        assertTrue(barChartData.containsKey("CS101"));
        assertEquals(87.5, barChartData.get("CS101").doubleValue(), 0.01);
    }

    @Test
    void testUpdatePieChart() {
        String inputData = "Alice,CS101,Midterm,85,100,Pass\n" +
                "Bob,CS101,Midterm,90,100,Pass\n" +
                "Alice,CS102,Final,95,100,Pass\n";
        loadTestData(inputData);

        Map<String, Number> pieChartData = new HashMap<>();
        service.updatePieChart(pieChartData, service.getGradeList());

        assertEquals(2, pieChartData.size());
        assertTrue(pieChartData.containsKey("Alice"));
        assertEquals(180, pieChartData.get("Alice").intValue());
    }

    @Test
    void testUpdateLineChart() {
        String inputData = "Alice,CS101,Midterm,85,100,Pass\n" +
                "Bob,CS101,Midterm,90,100,Pass\n" +
                "Alice,CS102,Final,95,100,Pass\n";
        loadTestData(inputData);

        Map<String, Number> lineChartData = new HashMap<>();
        service.updateLineChart(lineChartData, service.getGradeList());

        assertEquals(2, lineChartData.size());
        assertTrue(lineChartData.containsKey("Midterm"));
        assertEquals(87.5, lineChartData.get("Midterm").doubleValue(), 0.01);
    }

    private void loadTestData(String inputData) {
        ObservableList<Grade> gradeList = service.getGradeList();
        try (BufferedReader reader = new BufferedReader(new StringReader(inputData))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] data = line.split(",");
                if (data.length == 6) {
                    Grade grade = new Grade(
                            data[0].trim(), data[1].trim(), data[2].trim(),
                            data[3].trim(), data[4].trim(), data[5].trim());
                    gradeList.add(grade);
                }
            }
        } catch (IOException e) {
            fail("IOException should not occur");
        }
    }

    // MockDataManager 实现
    class MockDataManager extends DataManager {
        @Override
        public List<ExamResult> getExamResults() {
            return new ArrayList<>();
        }

        @Override
        public List<Student> getStudents() {
            return new ArrayList<>();
        }
    }
}
