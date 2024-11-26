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

            // Write to the student exams file
            examsWriter.write("bonny,COMP3111,quiz1,\"Software Engineering\"\n");
            examsWriter.write("bonny,COMP3111,quiz2,\"Software Engineering\"\n");
            examsWriter.write("bonny,COMP5111,quiz1,\"Advanced Software Engineering\"\n");
            examsWriter.write("john,COMP6111,quiz2,\"AI for Software Systems\"\n");
            examsWriter.write("bonny,COMP5111,final,\"Advanced Software Engineering\"\n");

            // Write to the completed quizzes file
            completedWriter.write("bonny,COMP3111,quiz1,20,50,30\n");
            completedWriter.write("bonny,COMP3111,quiz2,40,50,30\n");
            completedWriter.write("bonny,COMP5111,quiz1,35,50,45\n");
            completedWriter.write("bonny,COMP5111,final,45,50,60\n");
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
        List<String> exams = service.getExamsForStudent("bonny");
        assertEquals(4, exams.size(), "bonny should have 4 exams");
        assertTrue(exams.contains("COMP3111 Software Engineering | quiz1 (Completed)"));
        assertTrue(exams.contains("COMP3111 Software Engineering | quiz2 (Completed)"));
        assertTrue(exams.contains("COMP5111 Advanced Software Engineering | quiz1 (Completed)"));
        assertTrue(exams.contains("COMP5111 Advanced Software Engineering | final (Completed)"));
    }

    @Test
    void testGetGradesForStudent_ValidUser() {
        List<String> grades = service.getGradesForStudent("bonny");
        assertEquals(4, grades.size(), "bonny should have 4 grades");
        assertTrue(grades.contains("bonny,COMP3111,quiz1,20,50,30"));
        assertTrue(grades.contains("bonny,COMP3111,quiz2,40,50,30"));
        assertTrue(grades.contains("bonny,COMP5111,quiz1,35,50,45"));
        assertTrue(grades.contains("bonny,COMP5111,final,45,50,60"));
    }

    @Test
    void testGetExamsForStudent_InvalidUser() {
        List<String> exams = service.getExamsForStudent("unknown_user");
        assertTrue(exams.isEmpty(), "unknown_user should not have any exams");
    }

    @Test
    void testGetGradesForStudent_InvalidUser() {
        List<String> grades = service.getGradesForStudent("unknown_user");
        assertTrue(grades.isEmpty(), "unknown_user should not have any grades");
    }

    @Test
    void testAddCompletedExam() {
        service.addCompletedExam("bonny", "COMP6111", "quiz2", 50, 50, 40);

        List<String> grades = service.getGradesForStudent("bonny");
        assertEquals(5, grades.size(), "After adding a new grade, bonny should have 5 grades");
        assertTrue(grades.contains("bonny,COMP6111,quiz2,50,50,40"));
    }

    @Test
    void testGetExamsForStudent_CaseInsensitiveUsername() {
        List<String> exams = service.getExamsForStudent("BONNY");
        assertEquals(4, exams.size(), "Username matching should be case-insensitive for bonny's exams");
        assertTrue(exams.contains("COMP3111 Software Engineering | quiz1 (Completed)"));
    }

    @Test
    void testGetGradesForStudent_CaseInsensitiveUsername() {
        List<String> grades = service.getGradesForStudent("BONNY");
        assertEquals(4, grades.size(), "Username matching should be case-insensitive for bonny's grades");
    }

    @Test
    void testGetExamsForStudent_EmptyFile() throws Exception {
        Files.writeString(Path.of(TEST_EXAMS_FILE_PATH), "");
        List<String> exams = service.getExamsForStudent("bonny");
        assertTrue(exams.isEmpty(), "Exams list should be empty when the file is empty");
    }

    @Test
    void testGetGradesForStudent_EmptyFile() throws Exception {
        Files.writeString(Path.of(TEST_COMPLETED_QUIZZES_FILE_PATH), "");
        List<String> grades = service.getGradesForStudent("bonny");
        assertTrue(grades.isEmpty(), "Grades list should be empty when the file is empty");
    }

    @Test
    void testGetExamsForStudent_FileNotFound() {
        StudentMainService serviceWithMissingFile = new StudentMainService("missing_exams.txt", TEST_COMPLETED_QUIZZES_FILE_PATH);
        List<String> exams = serviceWithMissingFile.getExamsForStudent("bonny");
        assertTrue(exams.isEmpty(), "Exams list should be empty when the exams file does not exist");
    }

    @Test
    void testGetGradesForStudent_FileNotFound() {
        StudentMainService serviceWithMissingFile = new StudentMainService(TEST_EXAMS_FILE_PATH, "missing_grades.txt");
        List<String> grades = serviceWithMissingFile.getGradesForStudent("bonny");
        assertTrue(grades.isEmpty(), "Grades list should be empty when the grades file does not exist");
    }

    @Test
    void testHasCompletedExams_ValidUser() {
        assertTrue(service.hasCompletedExams("bonny"), "bonny should have completed exams");
    }

    @Test
    void testHasCompletedExams_InvalidUser() {
        assertFalse(service.hasCompletedExams("unknown_user"), "unknown_user should not have completed exams");
    }
}
