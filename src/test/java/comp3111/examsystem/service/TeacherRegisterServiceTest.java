package comp3111.examsystem.service;

import comp3111.examsystem.data.DataManager;
import comp3111.examsystem.entity.Teacher;
import org.junit.jupiter.api.*;

import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class TeacherRegisterServiceTest {
    private TeacherRegisterService registerService;
    private DataManager dataManager;

    @BeforeEach
    public void setUp() throws IOException {
        // Initialize DataManager and clear existing teachers
        dataManager = new DataManager();
        List<Teacher> existingTeachers = dataManager.getTeachers();
        for (Teacher teacher : existingTeachers) {
            dataManager.deleteTeacher(teacher.getId());
        }

        // Add initial test data
        dataManager.addTeacher(new Teacher("1", "teacher1", "password1", "Name1", "Male", 30, "Professor", "CS"));
        dataManager.addTeacher(new Teacher("2", "teacher2", "password2", "Name2", "Female", 35, "Lecturer", "Math"));

        // Initialize the service with DataManager
        registerService = new TeacherRegisterService(dataManager);
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
    public void testRegisterTeacher() {
        // Create a new teacher
        Teacher teacher = new Teacher(null, "teacher3", "pass123", "Name3", "Male", 40, "Professor", "Physics");

        // Register the teacher
        registerService.registerTeacher(teacher);

        // Verify that the teacher was added to DataManager
        assertTrue(registerService.isUserExists("teacher3"));

        // Verify the teacher's properties
        Teacher addedTeacher = dataManager.getTeachers().stream()
                .filter(t -> t.getUsername().equals("teacher3"))
                .findFirst()
                .orElse(null);
        assertNotNull(addedTeacher);
        assertEquals("Name3", addedTeacher.getName());
        assertEquals("Physics", addedTeacher.getDepartment());
    }

    @AfterEach
    public void tearDown() {
        // Clear all test data from DataManager
        List<Teacher> teachers = dataManager.getTeachers();
        for (Teacher teacher : teachers) {
            dataManager.deleteTeacher(teacher.getId());
        }
    }
}
