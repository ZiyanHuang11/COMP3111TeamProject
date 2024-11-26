package comp3111.examsystem.service;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.FileWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class StudentMainServiceTest {

    private static final String TEST_EXAMS_FILE_PATH = "test_student_exams.txt";
    private static final String TEST_COMPLETED_QUIZZES_FILE_PATH = "test_completed_quizzes.txt";
    private StudentMainService service;

    @BeforeEach
    void setUp() throws Exception {
        // Create temporary test files with sample data
        try (FileWriter examsWriter = new FileWriter(TEST_EXAMS_FILE_PATH);
             FileWriter completedWriter = new FileWriter(TEST_COMPLETED_QUIZZES_FILE_PATH)) {

            // Write to student exams file
            examsWriter.write("user1,COMP3111,quiz1,\"Software Engineering\"\n");
            examsWriter.write("user1,COMP3111,final,\"Software Engineering\"\n");
            examsWriter.write("user2,COMP5111,quiz1,\"Advanced Software Engineering\"\n");
            examsWriter.write("user3,COMP6111,quiz2,\"AI for Software Systems\"\n");
            examsWriter.write("user1,COMP5111,quiz1,\"Advanced Software Engineering\"\n");

            // Write to completed quizzes file
            completedWriter.write("user1,COMP3111,quiz1,80,100,30\n");
            completedWriter.write("user1,COMP5111,quiz1,70,100,25\n");
            completedWriter.write("user2,COMP5111,quiz1,90,100,35\n");
        }

        service = new StudentMainService(TEST_EXAMS_FILE_PATH, TEST_COMPLETED_QUIZZES_FILE_PATH);
    }

    @AfterEach
    void tearDown() throws Exception {
        // Delete the temporary test files after each test
        Files.deleteIfExists(Path.of(TEST_EXAMS_FILE_PATH));
        Files.deleteIfExists(Path.of(TEST_COMPLETED_QUIZZES_FILE_PATH));
    }

    @Test
    void testGetExamsForStudent_ValidUser() {
        List<String> exams = service.getExamsForStudent("user1");
        assertEquals(3, exams.size(), "User1 should have 3 exams");
        assertTrue(exams.contains("COMP3111 Software Engineering | quiz1"));
        assertTrue(exams.contains("COMP3111 Software Engineering | final"));
        assertTrue(exams.contains("COMP5111 Advanced Software Engineering | quiz1"));
    }

    @Test
    void testGetCompletedExams_ValidUser() {
        List<String> completedExams = service.getCompletedExams("user1");
        assertEquals(2, completedExams.size(), "User1 should have 2 completed exams");
        assertTrue(completedExams.contains("COMP3111,quiz1"));
        assertTrue(completedExams.contains("COMP5111,quiz1"));
    }

    @Test
    void testGetExamsForStudent_InvalidUser() {
        List<String> exams = service.getExamsForStudent("nonexistent_user");
        assertTrue(exams.isEmpty(), "Nonexistent user should have no exams");
    }

    @Test
    void testGetCompletedExams_InvalidUser() {
        List<String> completedExams = service.getCompletedExams("nonexistent_user");
        assertTrue(completedExams.isEmpty(), "Nonexistent user should have no completed exams");
    }

    @Test
    void testGetExamsForStudent_EmptyFile() throws Exception {
        Files.writeString(Path.of(TEST_EXAMS_FILE_PATH), "");
        List<String> exams = service.getExamsForStudent("user1");
        assertTrue(exams.isEmpty(), "Exams list should be empty for an empty file");
    }

    @Test
    void testGetCompletedExams_EmptyFile() throws Exception {
        Files.writeString(Path.of(TEST_COMPLETED_QUIZZES_FILE_PATH), "");
        List<String> completedExams = service.getCompletedExams("user1");
        assertTrue(completedExams.isEmpty(), "Completed exams list should be empty for an empty file");
    }

    @Test
    void testGetExamsForStudent_PartiallyInvalidData() throws Exception {
        Files.writeString(Path.of(TEST_EXAMS_FILE_PATH),
                "user1,COMP3111,quiz1,\"Software Engineering\"\n" +
                        "invalid_data_line\n" +
                        "user1,COMP5111,quiz1,\"Advanced Software Engineering\"\n");
        List<String> exams = service.getExamsForStudent("user1");
        assertEquals(2, exams.size(), "Only valid records should be included");
        assertTrue(exams.contains("COMP3111 Software Engineering | quiz1"));
        assertTrue(exams.contains("COMP5111 Advanced Software Engineering | quiz1"));
    }

    @Test
    void testGetExamsForStudent_CaseInsensitiveUsername() {
        List<String> exams = service.getExamsForStudent("USER1");
        assertEquals(3, exams.size(), "Username matching should be case-insensitive");
        assertTrue(exams.contains("COMP3111 Software Engineering | quiz1"));
        assertTrue(exams.contains("COMP3111 Software Engineering | final"));
        assertTrue(exams.contains("COMP5111 Advanced Software Engineering | quiz1"));
    }

    @Test
    void testGetCompletedExams_CaseInsensitiveUsername() {
        List<String> completedExams = service.getCompletedExams("USER1");
        assertEquals(2, completedExams.size(), "Username matching for completed exams should be case-insensitive");
        assertTrue(completedExams.contains("COMP3111,quiz1"));
        assertTrue(completedExams.contains("COMP5111,quiz1"));
    }

    @Test
    void testGetExamsForStudent_FileNotFound() {
        StudentMainService nonExistentService = new StudentMainService("nonexistent_exams.txt", TEST_COMPLETED_QUIZZES_FILE_PATH);
        List<String> exams = nonExistentService.getExamsForStudent("user1");
        assertTrue(exams.isEmpty(), "Exams list should be empty if the exams file does not exist");
    }

    @Test
    void testGetCompletedExams_FileNotFound() {
        StudentMainService nonExistentService = new StudentMainService(TEST_EXAMS_FILE_PATH, "nonexistent_completed.txt");
        List<String> completedExams = nonExistentService.getCompletedExams("user1");
        assertTrue(completedExams.isEmpty(), "Completed exams list should be empty if the completed quizzes file does not exist");
    }
}



