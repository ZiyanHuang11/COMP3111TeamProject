package comp3111.examsystem.service;

import comp3111.examsystem.entity.GradeRecord;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.FileWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class GradeStatisticsServiceTest {

    private static final String TEST_GRADES_FILE_PATH = "test_data/test_grades.txt";
    private GradeStatisticsService service;

    @BeforeEach
    void setUp() throws Exception {
        // Create a temporary test file with sample data
        try (FileWriter writer = new FileWriter(TEST_GRADES_FILE_PATH)) {
            writer.write("user1,COMP3111,quiz1,80,100,30\n");
            writer.write("user1,COMP3111,quiz2,90,100,35\n");
            writer.write("user2,COMP5111,quiz1,70,100,25\n");
            writer.write("user3,COMP6111,quiz2,85,100,40\n");
        }
        service = new GradeStatisticsService(TEST_GRADES_FILE_PATH);
    }

    @AfterEach
    void tearDown() throws Exception {
        // Delete the temporary test file after each test
        Files.deleteIfExists(Path.of(TEST_GRADES_FILE_PATH));
    }

    @Test
    void testGetAllGradeRecords() {
        // Act
        List<GradeRecord> records = service.getAllGradeRecords();

        // Assert
        assertEquals(4, records.size(), "Should retrieve all 4 grade records");
        assertEquals("COMP3111", records.get(0).getCourse());
        assertEquals("quiz1", records.get(0).getExam());
        assertEquals(80, records.get(0).getScore());
    }

    @Test
    void testGetAllGradeRecords_FileNotFound() {
        // Arrange
        GradeStatisticsService serviceWithMissingFile = new GradeStatisticsService("test_data/nonexistent_file.txt");

        // Act
        List<GradeRecord> records = serviceWithMissingFile.getAllGradeRecords();

        // Assert
        assertTrue(records.isEmpty(), "Should return an empty list if the file does not exist");
    }

    @Test
    void testGetAllGradeRecords_InvalidData() throws Exception {
        // Arrange
        Files.writeString(Path.of(TEST_GRADES_FILE_PATH), "InvalidData,Here\n");

        // Act
        List<GradeRecord> records = service.getAllGradeRecords();

        // Assert
        assertTrue(records.isEmpty(), "Should return an empty list for invalid data");
    }

    @Test
    void testGetAllCourses() {
        // Act
        List<String> courses = service.getAllCourses();

        // Assert
        assertEquals(3, courses.size(), "Should retrieve 3 unique courses");
        assertTrue(courses.contains("COMP3111"));
        assertTrue(courses.contains("COMP5111"));
        assertTrue(courses.contains("COMP6111"));
    }

    @Test
    void testGetGradeRecordsByCourse_ValidCourse() {
        // Act
        List<GradeRecord> records = service.getGradeRecordsByCourse("COMP3111");

        // Assert
        assertEquals(2, records.size(), "COMP3111 should have 2 grade records");
        assertEquals("quiz1", records.get(0).getExam());
        assertEquals("quiz2", records.get(1).getExam());
    }

    @Test
    void testGetGradeRecordsByCourse_InvalidCourse() {
        // Act
        List<GradeRecord> records = service.getGradeRecordsByCourse("INVALID_COURSE");

        // Assert
        assertTrue(records.isEmpty(), "Should return an empty list for a non-existent course");
    }

    @Test
    void testLargeFilePerformance() throws Exception {
        // Arrange
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 1000; i++) {
            sb.append("user1,COMP3111,quiz").append(i).append(",").append(i % 100).append(",100,").append(i % 60).append("\n");
        }
        Files.writeString(Path.of(TEST_GRADES_FILE_PATH), sb.toString());

        // Act
        List<GradeRecord> records = service.getAllGradeRecords();

        // Assert
        assertEquals(1000, records.size(), "Should retrieve all 1000 grade records");
    }
}
