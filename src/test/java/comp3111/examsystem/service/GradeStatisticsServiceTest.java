package comp3111.examsystem.service;

import comp3111.examsystem.data.DataManager;
import comp3111.examsystem.entity.ExamResult;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.chart.XYChart;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

class GradeStatisticsServiceTest {

    private GradeStatisticsService gradeStatisticsService;
    private ObservableList<ExamResult> mockExamResults;

    @BeforeEach
    void setUp() {
        // Mock DataManager behavior with predefined data
        DataManager mockDataManager = new DataManager() {
            @Override
            public List<Map<String, String>> loadCourseData() {
                List<Map<String, String>> courses = new ArrayList<>();
                courses.add(Map.of("courseID", "COMP3111", "courseName", "Software Engineering"));
                courses.add(Map.of("courseID", "COMP5111", "courseName", "Advanced Software Engineering"));
                return courses;
            }

            @Override
            public List<Map<String, String>> loadExamSummaryData() {
                List<Map<String, String>> examSummaryData = new ArrayList<>();
                examSummaryData.add(Map.of(
                        "id", "1",
                        "studentID", "S001",
                        "examName", "Quiz 1",
                        "courseID", "COMP3111",
                        "time", "30",
                        "score", "80",
                        "fullScore", "100"
                ));
                examSummaryData.add(Map.of(
                        "id", "2",
                        "studentID", "S002",
                        "examName", "Quiz 2",
                        "courseID", "COMP3111",
                        "time", "40",
                        "score", "60",
                        "fullScore", "100"
                ));
                return examSummaryData;
            }
        };

        // Initialize GradeStatisticsService
        gradeStatisticsService = new GradeStatisticsService();

        // Load mock data
        mockExamResults = FXCollections.observableArrayList();
        mockExamResults.add(new ExamResult("1", "S001", "Quiz 1", "COMP3111", "Software Engineering", 30, 80, 100));
        mockExamResults.add(new ExamResult("2", "S002", "Quiz 2", "COMP3111", "Software Engineering", 40, 60, 100));
    }

    @Test
    void testGetCourseList() {
        ObservableList<String> courseList = gradeStatisticsService.getCourseList(mockExamResults);
        assertNotNull(courseList);
        assertEquals(1, courseList.size());
        assertTrue(courseList.contains("COMP3111 Software Engineering"));
    }

    @Test
    void testFilterResultsByCourseName() {
        ObservableList<ExamResult> filteredResults = gradeStatisticsService.filterResultsByCourseName(mockExamResults, "COMP3111 Software Engineering");
        assertNotNull(filteredResults);
        assertEquals(2, filteredResults.size());

        ExamResult firstResult = filteredResults.get(0);
        assertEquals("Quiz 1", firstResult.getExamName());
        ExamResult secondResult = filteredResults.get(1);
        assertEquals("Quiz 2", secondResult.getExamName());
    }

    @Test
    void testCalculateAverageScore() {
        double averageScore = gradeStatisticsService.calculateAverageScore(mockExamResults);
        assertEquals(70.0, averageScore);
    }

    @Test
    void testGenerateBarChartSeries() {
        XYChart.Series<String, Number> series = gradeStatisticsService.generateBarChartSeries(mockExamResults);
        assertNotNull(series);
        assertEquals(2, series.getData().size());

        XYChart.Data<String, Number> firstData = series.getData().get(0);
        assertEquals("Quiz 1", firstData.getXValue());
        assertEquals(80, firstData.getYValue());

        XYChart.Data<String, Number> secondData = series.getData().get(1);
        assertEquals("Quiz 2", secondData.getXValue());
        assertEquals(60, secondData.getYValue());
    }
}

