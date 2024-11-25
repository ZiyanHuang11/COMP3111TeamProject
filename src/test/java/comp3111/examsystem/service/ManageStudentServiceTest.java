package comp3111.examsystem.service;

import comp3111.examsystem.data.DataManager;
import comp3111.examsystem.entity.Student;
import javafx.collections.ObservableList;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;

public class ManageStudentServiceTest {

    private ManageStudentService service;
    private DataManager dataManager;

    @BeforeEach
    public void setUp() {
        // 使用 MockDataManager 初始化数据，避免依赖实际的数据文件
        dataManager = new MockDataManager();
        service = new ManageStudentService(dataManager);
    }

    @Test
    public void testGetStudentList() {
        ObservableList<Student> studentList = service.getStudentList();
        assertNotNull(studentList, "Student list should not be null");
        assertEquals(0, studentList.size(), "Student list should be empty initially");
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
        service.addStudent(new Student("user1", "John Doe", 20, "Male", "CS", "password123"));

        String validationResult = service.validateInputs("", "John Doe", "20", "Male", "CS", "password123");
        assertEquals("Each field should be filled in", validationResult, "Validation should fail for empty username");

        validationResult = service.validateInputs("user1", "John Doe", "20", "Male", "CS", "password123");
        assertEquals("The user name already exists", validationResult, "Validation should fail for existing username");

        validationResult = service.validateInputs("user2", "John Doe", "invalid", "Male", "CS", "password123");
        assertEquals("Age must be a valid number", validationResult, "Validation should fail for invalid age");

        validationResult = service.validateInputs("user2", "John Doe", "20", "Male", "", "password123");
        assertEquals("Each field should be filled in", validationResult, "Validation should fail for empty department");

        validationResult = service.validateInputs("user2", "John Doe", "20", "Male", "CS", "short");
        assertEquals("The password must contain both letters and numbers and be at least eight characters long", validationResult, "Validation should fail for invalid password");

        validationResult = service.validateInputs("user2", "John Doe", "20", "Male", "CS", "validPass1");
        assertNull(validationResult, "Validation should pass for valid inputs");
    }

    @Test
    public void testValidateUpdateInputs() {
        String validationResult = service.validateUpdateInputs("John Doe", "20", "Male", "CS", "validPass1");
        assertNull(validationResult, "Validation should pass with correct inputs");

        validationResult = service.validateUpdateInputs("", "20", "Male", "CS", "validPass1");
        assertEquals("Each field should be filled in", validationResult, "Validation should fail for empty name");

        validationResult = service.validateUpdateInputs("John Doe", "invalid", "Male", "CS", "validPass1");
        assertEquals("Age must be a valid number", validationResult, "Validation should fail for invalid age");

        validationResult = service.validateUpdateInputs("John Doe", "20", "Male", "CS", "short");
        assertEquals("The password must contain both letters and numbers and be at least eight characters long", validationResult, "Validation should fail for invalid password");
    }

    @Test
    public void testIsValidPassword() {
        assertTrue(service.isValidPassword("validPass1"), "Password should be valid");
        assertFalse(service.isValidPassword("short1"), "Password should be invalid due to length");
        assertFalse(service.isValidPassword("onlyletters"), "Password should be invalid due to missing numbers");
        assertFalse(service.isValidPassword("12345678"), "Password should be invalid due to missing letters");
    }

    // MockDataManager 实现，避免依赖实际的数据文件
    class MockDataManager extends DataManager {
        private List<Student> mockStudents;

        public MockDataManager() {
            this.mockStudents = new ArrayList<>();
        }

        @Override
        public List<Student> getStudents() {
            return mockStudents;
        }

        @Override
        public void addStudent(Student student) {
            mockStudents.add(student);
        }


        @Override
        public void saveStudents() {
            // 模拟保存操作，不进行实际的文件写入
        }
    }
}
