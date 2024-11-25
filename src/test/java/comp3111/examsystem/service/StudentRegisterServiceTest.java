package comp3111.examsystem.service;

import comp3111.examsystem.data.DataManager;
import comp3111.examsystem.entity.Student;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class StudentRegisterServiceTest {

    private DataManager dataManager;
    private StudentRegisterService registerService;

    @BeforeEach
    void setUp() {
        // Initialize a mock DataManager
        dataManager = new DataManager() {
            private final List<Student> students = new ArrayList<>();

            @Override
            public List<Student> getStudents() {
                return students;
            }

            @Override
            public void addStudent(Student student) {
                students.add(student);
            }

            @Override
            public void saveStudents() {
                // Mock save behavior (no-op)
            }
        };

        // Initialize the service with the mock DataManager
        registerService = new StudentRegisterService(dataManager);

        // Add some initial data
        Student student = new Student();
        student.setUsername("existing_user");
        student.setPassword("password123");
        student.setName("John Doe");
        student.setGender("Male");
        student.setDepartment("Computer Science");
        dataManager.addStudent(student);
    }

    @Test
    void testIsUsernameTaken() {
        assertTrue(registerService.isUsernameTaken("existing_user"));
        assertFalse(registerService.isUsernameTaken("new_user"));
    }

    @Test
    void testRegisterStudentSuccess() {
        registerService.registerStudent("new_user", "secure_password", "Jane Doe", "Female", "Mathematics");

        List<Student> students = dataManager.getStudents();
        assertEquals(2, students.size());

        Student newStudent = students.get(1);
        assertEquals("new_user", newStudent.getUsername());
        assertEquals("secure_password", newStudent.getPassword());
        assertEquals("Jane Doe", newStudent.getName());
        assertEquals("Female", newStudent.getGender());
        assertEquals("Mathematics", newStudent.getDepartment());
    }

    @Test
    void testRegisterStudentWithExistingUsername() {
        Exception exception = assertThrows(IllegalArgumentException.class, () ->
                registerService.registerStudent("existing_user", "new_password", "Jane Doe", "Female", "Mathematics"));

        assertEquals("Username already exists", exception.getMessage());
    }

    @Test
    void testRegisterStudentWithEmptyFields() {
        Exception exception = assertThrows(IllegalArgumentException.class, () ->
                registerService.registerStudent("", "password", "Jane Doe", "Female", "Mathematics"));

        assertEquals("All fields are required", exception.getMessage());
    }

    @Test
    void testRegisterStudentWithNullFields() {
        Exception exception = assertThrows(IllegalArgumentException.class, () ->
                registerService.registerStudent(null, "password", "Jane Doe", "Female", "Mathematics"));

        assertEquals("All fields are required", exception.getMessage());
    }

    @Test
    void testRegisterStudentWithoutGender() {
        Exception exception = assertThrows(IllegalArgumentException.class, () ->
                registerService.registerStudent("new_user", "password", "Jane Doe", "", "Mathematics"));

        assertEquals("All fields are required", exception.getMessage());
    }

    @Test
    void testSaveStudentsCalled() {
        registerService.registerStudent("new_user", "password123", "Jane Smith", "Female", "Engineering");

        List<Student> students = dataManager.getStudents();
        assertEquals(2, students.size());

        Student newStudent = students.get(1);
        assertEquals("new_user", newStudent.getUsername());
    }
}

