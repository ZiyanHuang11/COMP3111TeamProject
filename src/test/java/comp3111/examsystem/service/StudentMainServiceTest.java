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

    private static final String TEST_FILE_PATH = "test_student_exams.txt";
    private StudentMainService service;

    @BeforeEach
    void setUp() throws Exception {
        // Create a temporary test file with sample data
        try (FileWriter writer = new FileWriter(TEST_FILE_PATH)) {
            writer.write("user1,COMP3111,quiz1,\"Software Engineering\"\n");
            writer.write("user1,COMP3111,final,\"Software Engineering\"\n");
            writer.write("user2,COMP5111,quiz1,\"Advanced Software Engineering\"\n");
            writer.write("user3,COMP6111,quiz2,\"AI for Software Systems\"\n");
            writer.write("user1,COMP5111,quiz1,\"Advanced Software Engineering\"\n");
        }
        service = new StudentMainService(TEST_FILE_PATH);
    }

    @AfterEach
    void tearDown() throws Exception {
        // Delete the temporary test file after each test
        Files.deleteIfExists(Path.of(TEST_FILE_PATH));
    }

    @Test
    void testGetExamsForStudent_ValidUser() {
        // Act
        List<String> exams = service.getExamsForStudent("user1");

        // Assert
        assertEquals(3, exams.size(), "User1 should have 3 exams");
        assertTrue(exams.contains("COMP3111 Software Engineering | quiz1"));
        assertTrue(exams.contains("COMP3111 Software Engineering | final"));
        assertTrue(exams.contains("COMP5111 Advanced Software Engineering | quiz1"));
    }

    @Test
    void testGetExamsForStudent_InvalidUser() {
        // Act
        List<String> exams = service.getExamsForStudent("nonexistent_user");

        // Assert
        assertTrue(exams.isEmpty(), "Nonexistent user should have no exams");
    }

    @Test
    void testGetExamsForStudent_EmptyFile() throws Exception {
        // Arrange
        Files.writeString(Path.of(TEST_FILE_PATH), "");

        // Act
        List<String> exams = service.getExamsForStudent("user1");

        // Assert
        assertTrue(exams.isEmpty(), "Exams list should be empty for an empty file");
    }

    @Test
    void testGetExamsForStudent_PartiallyInvalidData() throws Exception {
        // Arrange
        Files.writeString(Path.of(TEST_FILE_PATH),
                "user1,COMP3111,quiz1,\"Software Engineering\"\n" +
                        "invalid_data_line\n" +
                        "user1,COMP5111,quiz1,\"Advanced Software Engineering\"\n");

        // Act
        List<String> exams = service.getExamsForStudent("user1");

        // Assert
        assertEquals(2, exams.size(), "Only valid records should be included");
        assertTrue(exams.contains("COMP3111 Software Engineering | quiz1"));
        assertTrue(exams.contains("COMP5111 Advanced Software Engineering | quiz1"));
    }

    @Test
    void testGetExamsForStudent_IncompleteData() throws Exception {
        // Arrange
        Files.writeString(Path.of(TEST_FILE_PATH),
                "user1,COMP3111,quiz1,\"Software Engineering\"\n" +
                        "user1,,quiz2,\n"); // Missing course ID and course name

        // Act
        List<String> exams = service.getExamsForStudent("user1");

        // Assert
        assertEquals(1, exams.size(), "Only valid records should be included");
        assertTrue(exams.contains("COMP3111 Software Engineering | quiz1"));
    }

    @Test
    void testGetExamsForStudent_CaseInsensitiveUsername() {
        // Act
        List<String> exams = service.getExamsForStudent("USER1");

        // Assert
        assertEquals(3, exams.size(), "Username matching should be case-insensitive");
        assertTrue(exams.contains("COMP3111 Software Engineering | quiz1"));
        assertTrue(exams.contains("COMP3111 Software Engineering | final"));
        assertTrue(exams.contains("COMP5111 Advanced Software Engineering | quiz1"));
    }

    @Test
    void testGetExamsForStudent_FileNotFound() {
        // Arrange
        StudentMainService nonExistentService = new StudentMainService("nonexistent_file.txt");

        // Act
        List<String> exams = nonExistentService.getExamsForStudent("user1");

        // Assert
        assertTrue(exams.isEmpty(), "Exams list should be empty if the file does not exist");
    }
}

