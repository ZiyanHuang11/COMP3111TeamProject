package comp3111.examsystem.service;

import comp3111.examsystem.entity.ExamResult;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.chart.XYChart;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class GradeStatisticsServiceTest {

    private GradeStatisticsService gradeStatisticsService;
    private ObservableList<ExamResult> mockResults;

    @BeforeEach
    public void setUp() {
        gradeStatisticsService = new GradeStatisticsService();

        // 模拟数据
        mockResults = FXCollections.observableArrayList(
                new ExamResult("CS101", "Midterm", 100, 80, "Yes"),
                new ExamResult("CS101", "Final", 100, 70, "Yes"),
                new ExamResult("MA101", "Midterm", 100, 60, "No"),
                new ExamResult("MA101", "Final", 100, 90, "Yes"),
                new ExamResult("PH101", "Midterm", 100, 50, "No")
        );
    }

    @Test
    public void testLoadExamResults() {
        // 在测试环境中准备测试数据文件
        ObservableList<ExamResult> results = gradeStatisticsService.loadExamResults();

        assertNotNull(results, "Results should not be null");
        assertFalse(results.isEmpty(), "Results should not be empty");

        ExamResult firstResult = results.get(0);
        assertNotNull(firstResult.getCourseID(), "Course ID should not be null");
        assertNotNull(firstResult.getExamName(), "Exam name should not be null");
    }

    @Test
    public void testGetCourseList() {
        ObservableList<String> courseList = gradeStatisticsService.getCourseList(mockResults);
        assertNotNull(courseList, "Course list should not be null");
        assertEquals(3, courseList.size(), "There should be 3 unique courses");
        assertTrue(courseList.contains("CS101"), "Course list should contain 'CS101'");
    }

    @Test
    public void testFilterResultsByCourse() {
        ObservableList<ExamResult> filteredResults = gradeStatisticsService.filterResultsByCourse(mockResults, "CS101");
        assertNotNull(filteredResults, "Filtered results should not be null");
        assertEquals(2, filteredResults.size(), "Filtered results should have 2 items");

        for (ExamResult result : filteredResults) {
            assertEquals("CS101", result.getCourseID(), "Course ID should be 'CS101'");
        }
    }

    @Test
    public void testCalculateAverageScore() {
        double averageScore = gradeStatisticsService.calculateAverageScore(mockResults);
        assertEquals(70.0, averageScore, 0.01, "Average score should be 70.0");

        ObservableList<ExamResult> emptyResults = FXCollections.observableArrayList();
        assertEquals(0.0, gradeStatisticsService.calculateAverageScore(emptyResults), "Average score of empty results should be 0.0");
    }

    @Test
    public void testCalculatePassRate() {
        double passRate = gradeStatisticsService.calculatePassRate(mockResults);
        assertEquals(60.0, passRate, 0.01, "Pass rate should be 60.0%");

        ObservableList<ExamResult> emptyResults = FXCollections.observableArrayList();
        assertEquals(0.0, gradeStatisticsService.calculatePassRate(emptyResults), "Pass rate of empty results should be 0.0");
    }

    @Test
    public void testGenerateBarChartSeries() {
        XYChart.Series<String, Number> series = gradeStatisticsService.generateBarChartSeries(mockResults);
        assertNotNull(series, "Bar chart series should not be null");
        assertEquals(mockResults.size(), series.getData().size(), "Bar chart should have the same size as mock results");

        XYChart.Data<String, Number> firstDataPoint = series.getData().get(0);
        assertEquals("Midterm", firstDataPoint.getXValue(), "First X value should be 'Midterm'");
        assertEquals(80, firstDataPoint.getYValue(), "First Y value should be 80");
    }
}


