package comp3111.examsystem.service;

import org.junit.jupiter.api.*;
import java.io.*;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

class TeacherRegisterServiceTest {

    private static final String TEST_FILE_PATH = "data/teachers_test.txt";
    private TeacherRegisterService registerService;

    @BeforeEach
    void setUp() {
        registerService = new TeacherRegisterService(TEST_FILE_PATH);
    }

    @AfterEach
    void tearDown() {
        // Clean up the test file after each test
        File file = new File(TEST_FILE_PATH);
        if (file.exists()) {
            file.delete();
        }
    }

    @Test
    @DisplayName("Test valid registration inputs")
    void testValidRegistration() {
        String username = "validUser";
        String name = "John Doe";
        String gender = "Male";
        String ageText = "30";
        String position = "Professor";
        String department = "Computer Science";
        String password = "Password123";
        String confirmPassword = "Password123";

        // No validation errors should occur
        String validationMessage = registerService.validateInputs(username, name, gender, ageText, position,
                department, password, confirmPassword);
        assertNull(validationMessage, "Valid inputs should return no validation error.");
    }

    @Test
    @DisplayName("Test registration with empty fields")
    void testEmptyFields() {
        String username = "";
        String name = "";
        String gender = null;
        String ageText = "";
        String position = null;
        String department = "";
        String password = "";
        String confirmPassword = "";

        String validationMessage = registerService.validateInputs(username, name, gender, ageText, position,
                department, password, confirmPassword);
        assertEquals("All fields are required.", validationMessage, "Empty fields should trigger a validation error.");
    }

    @Test
    @DisplayName("Test registration with mismatched passwords")
    void testMismatchedPasswords() {
        String username = "user1";
        String name = "Alice";
        String gender = "Female";
        String ageText = "25";
        String position = "Lecturer";
        String department = "Mathematics";
        String password = "Password123";
        String confirmPassword = "Password321";

        String validationMessage = registerService.validateInputs(username, name, gender, ageText, position,
                department, password, confirmPassword);
        assertEquals("Passwords do not match.", validationMessage, "Mismatched passwords should trigger a validation error.");
    }

    @Test
    @DisplayName("Test registration with invalid age")
    void testInvalidAge() {
        String username = "user2";
        String name = "Bob";
        String gender = "Male";
        String ageText = "invalidAge";
        String position = "Professor";
        String department = "Physics";
        String password = "Password123";
        String confirmPassword = "Password123";

        String validationMessage = registerService.validateInputs(username, name, gender, ageText, position,
                department, password, confirmPassword);
        assertEquals("Age must be a valid number.", validationMessage, "Invalid age format should trigger a validation error.");
    }

    @Test
    @DisplayName("Test registration with age out of range")
    void testAgeOutOfRange() {
        String username = "user3";
        String name = "Charlie";
        String gender = "Other";
        String ageText = "18"; // Below the valid age range
        String position = "Researcher";
        String department = "Chemistry";
        String password = "Password123";
        String confirmPassword = "Password123";

        String validationMessage = registerService.validateInputs(username, name, gender, ageText, position,
                department, password, confirmPassword);
        assertEquals("Age must be between 20 and 80.", validationMessage, "Age outside valid range should trigger a validation error.");
    }

    @Test
    @DisplayName("Test registration with invalid password")
    void testInvalidPassword() {
        String username = "user4";
        String name = "David";
        String gender = "Male";
        String ageText = "40";
        String position = "Assistant Professor";
        String department = "Engineering";
        String password = "short"; // Invalid password
        String confirmPassword = "short";

        String validationMessage = registerService.validateInputs(username, name, gender, ageText, position,
                department, password, confirmPassword);
        assertEquals("Password must be at least 8 characters long and contain both letters and numbers.", validationMessage,
                "Invalid password should trigger a validation error.");
    }

    @Test
    @DisplayName("Test user existence check")
    void testUserExistenceCheck() throws IOException {
        // First, write a user into the file manually for testing
        Map<String, String> teacherInfo = new HashMap<>();
        teacherInfo.put("username", "existingUser");
        teacherInfo.put("password", "Password123");
        teacherInfo.put("name", "Eva");
        teacherInfo.put("gender", "Female");
        teacherInfo.put("age", "35");
        teacherInfo.put("position", "Lecturer");
        teacherInfo.put("department", "Biology");

        registerService.registerTeacher(teacherInfo);

        // Now check if the user already exists
        boolean userExists = registerService.isUserExists("existingUser");
        assertTrue(userExists, "User that exists in the file should be detected.");
    }

    @Test
    @DisplayName("Test registration when user already exists")
    void testUserAlreadyExists() throws IOException {
        // Register a user
        Map<String, String> teacherInfo = new HashMap<>();
        teacherInfo.put("username", "existingUser");
        teacherInfo.put("password", "Password123");
        teacherInfo.put("name", "Eva");
        teacherInfo.put("gender", "Female");
        teacherInfo.put("age", "35");
        teacherInfo.put("position", "Lecturer");
        teacherInfo.put("department", "Biology");

        registerService.registerTeacher(teacherInfo);

        // Attempt to register with the same username
        String username = "existingUser";
        String name = "Jane";
        String gender = "Female";
        String ageText = "30";
        String position = "Professor";
        String department = "Physics";
        String password = "Password123";
        String confirmPassword = "Password123";

        String validationMessage = registerService.validateInputs(username, name, gender, ageText, position,
                department, password, confirmPassword);
        assertEquals("Username already exists", validationMessage, "Attempt to register an existing user should fail.");
    }

    @Test
    @DisplayName("Test successful teacher registration")
    void testSuccessfulTeacherRegistration() throws IOException {
        String username = "newUser";
        String name = "Tom";
        String gender = "Male";
        String ageText = "40";
        String position = "Professor";
        String department = "Computer Science";
        String password = "Password123";
        String confirmPassword = "Password123";

        // Validate inputs should pass
        String validationMessage = registerService.validateInputs(username, name, gender, ageText, position,
                department, password, confirmPassword);
        assertNull(validationMessage, "Valid inputs should return no validation error.");

        // Register the teacher
        Map<String, String> teacherInfo = new HashMap<>();
        teacherInfo.put("username", username);
        teacherInfo.put("password", password);
        teacherInfo.put("name", name);
        teacherInfo.put("gender", gender);
        teacherInfo.put("age", ageText);
        teacherInfo.put("position", position);
        teacherInfo.put("department", department);

        registerService.registerTeacher(teacherInfo);

        // Verify the teacher was saved
        boolean userExists = registerService.isUserExists(username);
        assertTrue(userExists, "The teacher should be successfully registered and exist in the file.");
    }
}
