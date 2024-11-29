package comp3111.examsystem.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import static org.junit.jupiter.api.Assertions.*;

import java.io.*;


public class StudentRegisterServiceTest {

    private StudentRegisterService service;
    private String studentFilePath = "test_data/test_students.txt";  // Use a test file path

    @BeforeEach
    public void setUp() {
        service = new StudentRegisterService(studentFilePath);
    }

    // Test for username being numeric
    @Test
    public void testValidateInputs_UsernameIsNumber() {
        String result = service.validateInputs("12345", "John Doe", "Male", "25", "Computer Science", "password123", "password123");
        assertEquals("Username cannot be numeric.", result);
    }

    // Test for name being numeric
    @Test
    public void testValidateInputs_NameIsNumber() {
        String result = service.validateInputs("johndoe", "12345", "Male", "25", "Computer Science", "password123", "password123");
        assertEquals("Name cannot be numeric.", result);
    }

    // Test for username already existing in the system
    @Test
    public void testRegisterStudent_UsernameAlreadyExists() throws IOException {
        // Register the first student
        Map<String, String> studentInfo1 = new HashMap<>();
        studentInfo1.put("username", "johndoe");
        studentInfo1.put("name", "John Doe");
        studentInfo1.put("gender", "Male");
        studentInfo1.put("age", "25");
        studentInfo1.put("department", "Computer Science");
        studentInfo1.put("password", "password123");
        studentInfo1.put("confirmPassword", "password123");
        service.registerStudent(studentInfo1);

        // Try registering with the same username
        String result = service.validateInputs("johndoe", "Jane Doe", "Female", "22", "Math", "password123", "password123");
        assertEquals("Username already exists!", result);
    }

    // Test for valid username, name, and valid inputs
    @Test
    public void testValidateInputs_ValidInputs() {
        String result = service.validateInputs("janedoe", "Jane Doe", "Female", "22", "Mathematics", "password123", "password123");
        assertNull(result);  // No errors should be returned
    }

    // Test for invalid age
    @Test
    public void testValidateInputs_AgeIsInvalid() {
        String result = service.validateInputs("janedoe", "Jane Doe", "Female", "-5", "Mathematics", "password123", "password123");
        assertEquals("Age must be a whole number between 1 and 60.", result);
    }

    // Test for age being too high
    @Test
    public void testValidateInputs_AgeIsTooHigh() {
        String result = service.validateInputs("janedoe", "Jane Doe", "Female", "100", "Mathematics", "password123", "password123");
        assertEquals("Age must be a whole number between 1 and 60.", result);
    }

    // Test for invalid password length
    @Test
    public void testValidateInputs_PasswordTooShort() {
        String result = service.validateInputs("janedoe", "Jane Doe", "Female", "22", "Mathematics", "pass", "pass");
        assertEquals("Password must be at least 8 characters long.", result);
    }

    // Test for mismatched passwords
    @Test
    public void testValidateInputs_PasswordsDontMatch() {
        String result = service.validateInputs("janedoe", "Jane Doe", "Female", "22", "Mathematics", "password123", "password456");
        assertEquals("Passwords do not match.", result);
    }

    // Test for department being empty
    @Test
    public void testValidateInputs_DepartmentIsEmpty() {
        String result = service.validateInputs("janedoe", "Jane Doe", "Female", "22", "", "password123", "password123");
        assertEquals("All fields are required.", result);  // Because department is empty
    }

    // Test for successful student registration
    @Test
    public void testRegisterStudent_Success() throws IOException {
        Map<String, String> studentInfo = new HashMap<>();
        studentInfo.put("username", "janedoe");
        studentInfo.put("name", "Jane Doe");
        studentInfo.put("gender", "Female");
        studentInfo.put("age", "22");
        studentInfo.put("department", "Mathematics");
        studentInfo.put("password", "password123");
        studentInfo.put("confirmPassword", "password123");

        // Register student and check that the username and name are cached
        service.registerStudent(studentInfo);
        assertTrue(service.isUserExists("janedoe"));
    }

    // Test for invalid age format (non-integer)
    @Test
    public void testValidateInputs_AgeIsNonInteger() {
        String result = service.validateInputs("janedoe", "Jane Doe", "Female", "twenty", "Mathematics", "password123", "password123");
        assertEquals("Age must be a whole number between 1 and 60.", result);
    }
}
