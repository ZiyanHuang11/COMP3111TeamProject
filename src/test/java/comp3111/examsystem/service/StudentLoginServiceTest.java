package comp3111.examsystem.service;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.FileWriter;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;

class StudentLoginServiceTest {

    private static final String TEST_FILE_PATH = "test_data/test_students.txt";
    private StudentLoginService service;

    @BeforeEach
    void setUp() throws Exception {
        // Create a temporary test file with sample student data
        try (FileWriter writer = new FileWriter(TEST_FILE_PATH)) {
            writer.write("user1,John,Male,20,CS,password1\n");
            writer.write("user2,Jane,Female,21,IT,password2\n");
            writer.write("user3,Mike,Male,22,Math,password3\n");
        }
        service = new StudentLoginService(TEST_FILE_PATH);
    }

    @AfterEach
    void tearDown() throws Exception {
        // Delete the temporary test file after each test
        Files.deleteIfExists(Path.of(TEST_FILE_PATH));
    }

    @Test
    void testValidateLogin_ValidCredentials() {
        // Act & Assert
        assertTrue(service.validateLogin("user1", "password1"), "Valid username and password should return true");
        assertTrue(service.validateLogin("user2", "password2"), "Valid username and password should return true");
        assertTrue(service.validateLogin("user3", "password3"), "Valid username and password should return true");
    }

    @Test
    void testValidateLogin_InvalidUsername() {
        // Act & Assert
        assertFalse(service.validateLogin("invalidUser", "password1"), "Invalid username should return false");
        assertFalse(service.validateLogin("user4", "password2"), "Non-existent username should return false");
    }

    @Test
    void testValidateLogin_InvalidPassword() {
        // Act & Assert
        assertFalse(service.validateLogin("user1", "wrongPassword"), "Wrong password should return false");
        assertFalse(service.validateLogin("user2", "12345"), "Wrong password should return false");
    }

    @Test
    void testValidateLogin_EmptyFile() throws Exception {
        // Clear the test file
        Files.writeString(Path.of(TEST_FILE_PATH), "");

        // Act & Assert
        assertFalse(service.validateLogin("user1", "password1"), "Empty file should always return false");
    }

    @Test
    void testValidateLogin_InvalidFileFormat() throws Exception {
        // Write invalid data to the test file
        Files.writeString(Path.of(TEST_FILE_PATH), "invalid,data");

        // Act & Assert
        assertFalse(service.validateLogin("user1", "password1"), "Invalid file format should return false");
    }

    @Test
    void testValidateLogin_FileNotFound() {
        // Setup a service with a non-existing file
        StudentLoginService serviceWithMissingFile = new StudentLoginService("test_data/non_existing_file.txt");

        // Act & Assert
        assertFalse(serviceWithMissingFile.validateLogin("user1", "password1"), "Missing file should return false");
    }
}

