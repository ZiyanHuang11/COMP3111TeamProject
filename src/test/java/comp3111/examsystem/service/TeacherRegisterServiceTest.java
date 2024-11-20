package comp3111.examsystem.service;

import org.junit.jupiter.api.*;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class TeacherRegisterServiceTest {
    private TeacherRegisterService registerService;
    private String testTeacherFilePath = "test_data/test_teachers.txt";

    @BeforeEach
    public void setUp() throws IOException {
        // Create test data directory if it doesn't exist
        Files.createDirectories(Paths.get("test_data"));

        // Write test teacher data to file
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(testTeacherFilePath))) {
            writer.write("teacher1,password1,Name1,Male,30,Professor,CS\n");
            writer.write("teacher2,password2,Name2,Female,35,Lecturer,Math\n");
        }

        // Initialize the service with the test file path
        registerService = new TeacherRegisterService(testTeacherFilePath);
    }

    @Test
    public void testValidateInputsValid() {
        String validationMessage = registerService.validateInputs("teacher3", "Name3", "Male", "40",
                "Professor", "Physics", "pass123", "pass123");
        assertNull(validationMessage);
    }

    @Test
    public void testValidateInputsMissingFields() {
        String validationMessage = registerService.validateInputs("", "Name3", "Male", "40",
                "Professor", "Physics", "pass123", "pass123");
        assertEquals("All fields are required.", validationMessage);
    }

    @Test
    public void testValidateInputsInvalidAge() {
        String validationMessage = registerService.validateInputs("teacher3", "Name3", "Male", "abc",
                "Professor", "Physics", "pass123", "pass123");
        assertEquals("Age must be a valid number.", validationMessage);
    }

    @Test
    public void testValidateInputsPasswordMismatch() {
        String validationMessage = registerService.validateInputs("teacher3", "Name3", "Male", "40",
                "Professor", "Physics", "pass123", "pass456");
        assertEquals("Passwords do not match.", validationMessage);
    }

    @Test
    public void testIsUserExists() {
        assertTrue(registerService.isUserExists("teacher1"));
        assertFalse(registerService.isUserExists("teacher3"));
    }

    @Test
    public void testRegisterTeacher() throws IOException {
        Map<String, String> teacherInfo = new HashMap<>();
        teacherInfo.put("username", "teacher3");
        teacherInfo.put("password", "pass123");
        teacherInfo.put("name", "Name3");
        teacherInfo.put("gender", "Male");
        teacherInfo.put("age", "40");
        teacherInfo.put("position", "Professor");
        teacherInfo.put("department", "Physics");

        registerService.registerTeacher(teacherInfo);

        // Verify that the teacher was added to the file
        assertTrue(registerService.isUserExists("teacher3"));

        // Read the file and verify the content
        try (BufferedReader br = new BufferedReader(new FileReader(testTeacherFilePath))) {
            String line;
            boolean found = false;
            while ((line = br.readLine()) != null) {
                if (line.startsWith("teacher3,")) {
                    found = true;
                    break;
                }
            }
            assertTrue(found, "Teacher3 should be in the file.");
        }
    }

    @AfterEach
    public void tearDown() throws IOException {
        Files.deleteIfExists(Paths.get(testTeacherFilePath));
    }
}
