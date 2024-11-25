package comp3111.examsystem.service;

import comp3111.examsystem.data.DataManager;
import comp3111.examsystem.entity.Student;
import javafx.collections.ObservableList;
import org.junit.jupiter.api.*;

import java.io.File;
import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class ManageStudentServiceTest {

    private ManageStudentService service;
    private DataManager dataManager;

    @BeforeEach
    public void setUp() {
        // Initialize DataManager and service
        dataManager = new DataManager();
        service = new ManageStudentService(dataManager);
    }

    @Test
    public void testGetStudentList() {
        ObservableList<Student> studentList = service.getStudentList();
        assertNotNull(studentList, "Student list should not be null");
    }

    @Test
    public void testAddStudent() {
        Student newStudent = new Student("user1", "John Doe", 20, "Male", "CS", "password123");
        service.addStudent(newStudent);

        List<Student> students = dataManager.getStudents();
        assertTrue(students.contains(newStudent), "New student should be added to the data manager");
        assertEquals(1, service.getStudentList().size(), "Student list should contain one student");
    }

    @Test
    public void testUpdateStudent() {
        Student existingStudent = new Student("user1", "John Doe", 20, "Male", "CS", "password123");
        service.addStudent(existingStudent);

        Student updatedStudent = new Student("user1", "John Doe Updated", 21, "Male", "Math", "newpassword");
        service.updateStudent(updatedStudent, "user1");

        List<Student> students = dataManager.getStudents();
        assertEquals("John Doe Updated", students.get(0).getName(), "Student name should be updated");
        assertEquals("Math", students.get(0).getDepartment(), "Student department should be updated");
    }

    @Test
    public void testDeleteStudent() {
        Student student = new Student("user1", "John Doe", 20, "Male", "CS", "password123");
        service.addStudent(student);

        service.deleteStudent("user1");
        List<Student> students = dataManager.getStudents();
        assertFalse(students.contains(student), "Student should be deleted from the data manager");
        assertEquals(0, service.getStudentList().size(), "Student list should be empty after deletion");
    }

    @Test
    public void testFilterStudents() {
        service.addStudent(new Student("user1", "John Doe", 20, "Male", "CS", "password123"));
        service.addStudent(new Student("user2", "Jane Smith", 22, "Female", "EE", "password456"));

        List<Student> filtered = service.filterStudents("user1", "", "");
        assertEquals(1, filtered.size(), "Filter should return one student");
        assertEquals("user1", filtered.get(0).getUsername(), "Filtered student should match the username");

        filtered = service.filterStudents("nonexistent", "", "");
        assertEquals(0, filtered.size(), "Filter should return no students for nonexistent username");
    }

    @Test
    public void testValidateUsername() {
        service.addStudent(new Student("user1", "John Doe", 20, "Male", "CS", "password123"));

        String validationResult = service.validateUsername("user1");
        assertEquals("The user name already exists", validationResult, "Validation should return a message for existing username");

        validationResult = service.validateUsername("newuser");
        assertNull(validationResult, "Validation should return null for new username");
    }

    @Test
    public void testValidateInputs() {
        String validationResult = service.validateInputs("user1", "John Doe", "20", "Male", "CS", "password123");
        assertNull(validationResult, "Validation should pass with correct inputs");

        validationResult = service.validateInputs("", "John Doe", "20", "Male", "CS", "password123");
        assertEquals("Each field should be filled in", validationResult, "Validation should fail for empty username");

        validationResult = service.validateInputs("user1", "John Doe", "invalid", "Male", "CS", "password123");
        assertEquals("Age must be a valid number", validationResult, "Validation should fail for invalid age");

        validationResult = service.validateInputs("user1", "John Doe", "20", "Male", "", "password123");
        assertEquals("Each field should be filled in", validationResult, "Validation should fail for empty department");

        validationResult = service.validateInputs("user1", "John Doe", "20", "Male", "CS", "short");
        assertEquals("The password must contain both letters and numbers and be at least eight characters long", validationResult, "Validation should fail for invalid password");
    }

    @Test
    public void testValidateUpdateInputs() {
        String validationResult = service.validateUpdateInputs("John Doe", "20", "Male", "CS", "password123");
        assertNull(validationResult, "Validation should pass with correct inputs");

        validationResult = service.validateUpdateInputs("", "20", "Male", "CS", "password123");
        assertEquals("Each field should be filled in", validationResult, "Validation should fail for empty name");

        validationResult = service.validateUpdateInputs("John Doe", "invalid", "Male", "CS", "password123");
        assertEquals("Age must be a valid number", validationResult, "Validation should fail for invalid age");

        validationResult = service.validateUpdateInputs("John Doe", "20", "Male", "CS", "short");
        assertEquals("The password must contain both letters and numbers and be at least eight characters long", validationResult, "Validation should fail for invalid password");
    }

    @Test
    public void testIsValidPassword() {
        assertFalse(service.isValidPassword("validPass1"), "Password should be valid");
        assertTrue(service.isValidPassword("short1"), "Password should be invalid due to length");
        assertTrue(service.isValidPassword("onlyletters"), "Password should be invalid due to missing numbers");
        assertTrue(service.isValidPassword("12345678"), "Password should be invalid due to missing letters");
    }
}
