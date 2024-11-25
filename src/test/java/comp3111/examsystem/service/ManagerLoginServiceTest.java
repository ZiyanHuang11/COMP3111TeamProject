package comp3111.examsystem.service;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.*;

public class ManagerLoginServiceTest {
    private ManagerLoginService managerLoginService;
    private String testFilePath;

    @BeforeEach
    public void setUp() throws IOException {
        testFilePath = "test_managers.txt";

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(testFilePath))) {
            writer.write("admin,admin123\n");
            writer.write("manager1,password1\n");
            writer.write("manager2,password2\n");
        }
        managerLoginService = new ManagerLoginService(testFilePath);
    }

    @Test
    public void testValidateWithValidCredentials() {
        assertTrue(managerLoginService.validate("admin", "admin123"));
        assertTrue(managerLoginService.validate("manager1", "password1"));
        assertTrue(managerLoginService.validate("manager2", "password2"));
    }

    @Test
    public void testValidateWithInvalidCredentials() {
        assertFalse(managerLoginService.validate("admin", "wrongpassword"));
        assertFalse(managerLoginService.validate("unknownUser", "password1"));
        assertFalse(managerLoginService.validate("manager1", "wrongpassword"));
    }

    @Test
    public void testValidateWithEmptyCredentials() {
        assertFalse(managerLoginService.validate("", ""));
        assertFalse(managerLoginService.validate("admin", ""));
        assertFalse(managerLoginService.validate("", "admin123"));
    }

    @Test
    public void testFileNotFound() {
        ManagerLoginService invalidService = new ManagerLoginService("invalid_path/managers.txt");
        assertFalse(invalidService.validate("admin", "admin123")); // Should return false since file does not exist
    }

    @AfterEach
    public void tearDown() throws IOException {
        Files.deleteIfExists(Paths.get(testFilePath));
    }
}