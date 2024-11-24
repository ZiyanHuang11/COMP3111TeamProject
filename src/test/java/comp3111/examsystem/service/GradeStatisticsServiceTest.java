package comp3111.examsystem.service;

import comp3111.examsystem.entity.ExamResult;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.chart.XYChart;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class GradeStatisticsServiceTest {

    private GradeStatisticsService service;
    private ObservableList<ExamResult> mockResults;

    @BeforeEach
    public void setUp() {
        service = new GradeStatisticsService();
        mockResults = FXCollections.observableArrayList(
                new ExamResult("1", "student1", "exam1", "COMP3111", "Midterm", 80, 100, "Yes"),
                new ExamResult("2", "student2", "exam1", "COMP3111", "Midterm", 60, 100, "No"),
                new ExamResult("3", "student3", "exam2", "COMP3111", "Final", 90, 100, "Yes"),
                new ExamResult("4", "student4", "exam2", "COMP5111", "Final", 70, 100, "Yes")
        );
    }

    @Test
    public void testLoadExamResults() {
        ObservableList<ExamResult> results = service.loadExamResults();
        assertNotNull(results, "The loaded exam results should not be null.");
    }

    @Test
    public void testGetCourseList() {
        ObservableList<String> courseList = service.getCourseList(mockResults);
        assertNotNull(courseList, "The course list should not be null.");
        assertTrue(courseList.contains("COMP3111"), "Course list should contain 'COMP3111'.");
        assertTrue(courseList.contains("COMP5111"), "Course list should contain 'COMP5111'.");
        assertEquals(2, courseList.size(), "There should be two unique courses.");
    }

    @Test
    public void testFilterResultsByCourse() {
        ObservableList<ExamResult> filteredResults = service.filterResultsByCourse(mockResults, "COMP3111");
        assertNotNull(filteredResults, "The filtered results should not be null.");
        assertEquals(3, filteredResults.size(), "There should be three results for 'COMP3111'.");
        for (ExamResult result : filteredResults) {
            assertEquals("COMP3111", result.getCourseID(), "The courseID should match 'COMP3111'.");
        }
    }

    @Test
    public void testCalculateAverageScore() {
        double averageScore = service.calculateAverageScore(mockResults);
        assertEquals(75.0, averageScore, 0.01, "The average score should be 75.0.");
    }

    @Test
    public void testCalculatePassRate() {
        double passRate = service.calculatePassRate(mockResults);
        assertEquals(75.0, passRate, 0.01, "The pass rate should be 75%.");
    }

    @Test
    public void testGenerateBarChartSeries() {
        XYChart.Series<String, Number> series = service.generateBarChartSeries(mockResults);
        assertNotNull(series, "The bar chart series should not be null.");
        List<XYChart.Data<String, Number>> data = series.getData();
        assertEquals(4, data.size(), "The series should contain four data points.");
        assertEquals(80, data.get(0).getYValue(), "The first data point should have a score of 80.");
        assertEquals("Midterm", data.get(0).getXValue(), "The first data point should correspond to 'Midterm'.");
    }
}
