package comp3111.examsystem.service;

import org.junit.jupiter.api.*;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import static org.junit.jupiter.api.Assertions.*;

public class TeacherLoginServiceTest {
    private TeacherLoginService loginService;
    private String testTeacherFilePath = "test_data/test_teachers.txt";

    @BeforeEach
    public void setUp() throws IOException {
        // Create test data directory if it doesn't exist
        Files.createDirectories(Paths.get("test_data"));

        // Write test teacher credentials to file
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(testTeacherFilePath))) {
            writer.write("teacher1,password1\n");
            writer.write("teacher2,password2\n");
        }

        // Initialize the service with the test file path
        loginService = new TeacherLoginService(testTeacherFilePath);
    }

    @Test
    public void testValidateValidCredentials() {
        assertTrue(loginService.validate("teacher1", "password1"));
        assertTrue(loginService.validate("teacher2", "password2"));
    }

    @Test
    public void testValidateInvalidCredentials() {
        assertFalse(loginService.validate("teacher1", "wrongpassword"));
        assertFalse(loginService.validate("invaliduser", "password1"));
    }

    @Test
    public void testValidateEmptyCredentials() {
        assertFalse(loginService.validate("", ""));
    }

    @Test
    public void testValidateNullCredentials() {
        assertFalse(loginService.validate(null, null));
    }

    @AfterEach
    public void tearDown() throws IOException {
        Files.deleteIfExists(Paths.get(testTeacherFilePath));
    }
}
