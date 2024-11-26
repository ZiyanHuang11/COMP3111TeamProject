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

    private static final String TEST_FILE_PATH = "test_grades.txt";
    private GradeStatisticsService service;

    @BeforeEach
    void setUp() throws Exception {
        // Create a temporary test file with sample data
        try (FileWriter writer = new FileWriter(TEST_FILE_PATH)) {
            writer.write("COMP3111,quiz1,80,100,30\n");
            writer.write("COMP3111,quiz2,90,100,35\n");
            writer.write("COMP5111,quiz1,70,100,25\n");
        }
        service = new GradeStatisticsService(TEST_FILE_PATH);
    }

    @AfterEach
    void tearDown() throws Exception {
        // Delete the temporary test file after each test
        Files.deleteIfExists(Path.of(TEST_FILE_PATH));
    }

    @Test
    void testGetAllGradeRecords() {
        List<GradeRecord> records = service.getAllGradeRecords();
        assertEquals(3, records.size(), "All records should be loaded correctly");
        GradeRecord firstRecord = records.get(0);
        assertEquals("COMP3111", firstRecord.getCourse());
        assertEquals("quiz1", firstRecord.getExam());
        assertEquals(80, firstRecord.getScore());
        assertEquals(100, firstRecord.getFullScore());
        assertEquals(30, firstRecord.getTime());
    }

    @Test
    void testGetAllGradeRecords_EmptyFile() throws Exception {
        Files.writeString(Path.of(TEST_FILE_PATH), "");
        List<GradeRecord> records = service.getAllGradeRecords();
        assertTrue(records.isEmpty(), "Records list should be empty for an empty file");
    }

    @Test
    void testGetAllGradeRecords_PartiallyInvalidData() throws Exception {
        Files.writeString(Path.of(TEST_FILE_PATH), "COMP3111,quiz1,80,100,30\nInvalidData\nCOMP5111,quiz1,70,100,25\n");
        List<GradeRecord> records = service.getAllGradeRecords();
        assertEquals(2, records.size(), "Only valid records should be parsed");
    }

    @Test
    void testGetAllGradeRecords_InvalidData() throws Exception {
        Files.writeString(Path.of(TEST_FILE_PATH), "InvalidData\nAnotherInvalidLine");
        List<GradeRecord> records = service.getAllGradeRecords();
        assertTrue(records.isEmpty(), "Records list should be empty for a completely invalid file");
    }

    @Test
    void testGetAllCourses() {
        List<String> courses = service.getAllCourses();
        assertEquals(2, courses.size(), "Two unique courses should be returned");
        assertTrue(courses.contains("COMP3111"));
        assertTrue(courses.contains("COMP5111"));
    }

    @Test
    void testGetAllCourses_EmptyFile() throws Exception {
        Files.writeString(Path.of(TEST_FILE_PATH), "");
        List<String> courses = service.getAllCourses();
        assertTrue(courses.isEmpty(), "Courses list should be empty for an empty file");
    }

    @Test
    void testGetAllCourses_SingleCourse() throws Exception {
        Files.writeString(Path.of(TEST_FILE_PATH), "COMP3111,quiz1,80,100,30\nCOMP3111,quiz2,90,100,35\n");
        List<String> courses = service.getAllCourses();
        assertEquals(1, courses.size(), "Only one unique course should be returned");
        assertEquals("COMP3111", courses.get(0));
    }

    @Test
    void testGetGradeRecordsByCourse_ValidCourse() {
        List<GradeRecord> records = service.getGradeRecordsByCourse("COMP3111");
        assertEquals(2, records.size(), "COMP3111 should have two records");
        assertEquals("quiz1", records.get(0).getExam());
        assertEquals("quiz2", records.get(1).getExam());
    }

    @Test
    void testGetGradeRecordsByCourse_InvalidCourse() {
        List<GradeRecord> records = service.getGradeRecordsByCourse("INVALID_COURSE");
        assertTrue(records.isEmpty(), "No records should be returned for a non-existent course");
    }

    @Test
    void testGetGradeRecordsByCourse_CaseInsensitive() {
        List<GradeRecord> records = service.getGradeRecordsByCourse("comp3111");
        assertEquals(2, records.size(), "Case-insensitive matching should return the correct records");
    }

    @Test
    void testLargeFilePerformance() throws Exception {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 1000; i++) {
            sb.append("COMP3111,quiz").append(i).append(",").append(i % 100).append(",100,").append(i % 60).append("\n");
        }
        Files.writeString(Path.of(TEST_FILE_PATH), sb.toString());

        List<GradeRecord> records = service.getAllGradeRecords();
        assertEquals(1000, records.size(), "All 1000 records should be loaded");
    }
}
