package comp3111.examsystem.service;

import comp3111.examsystem.entity.Teacher;
import javafx.collections.ObservableList;
import org.junit.jupiter.api.*;
import java.io.*;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ManageTeacherServiceTest {

    private ManageTeacherService service;

    @BeforeEach
    void setUp() {
        // Initialize the service before each test
        service = new ManageTeacherService();
        // Clear the teacher file for testing purposes
        clearTeacherFile();
    }

    @AfterEach
    void tearDown() {
        // Clean up after each test
        clearTeacherFile();
    }

    private void clearTeacherFile() {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter("data/teachers.txt"))) {
            bw.write("");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    void testLoadTeachersFromFile() throws IOException {
        // Prepare test data
        try (BufferedWriter bw = new BufferedWriter(new FileWriter("data/teachers.txt"))) {
            bw.write("john_doe,password123,John Doe,Male,30,Teacher,Math\n");
            bw.write("jane_doe,password456,Jane Doe,Female,28,Teacher,Science\n");
        }

        // Reload service to load teachers from file
        service = new ManageTeacherService();
        ObservableList<Teacher> teachers = service.getTeacherList();

        assertEquals(2, teachers.size());
        assertEquals("john_doe", teachers.get(0).getUsername());
        assertEquals("jane_doe", teachers.get(1).getUsername());
    }

    @Test
    void testAddTeacher() throws IOException {
        Teacher newTeacher = new Teacher("john_doe", "password123", "John Doe", "Male", 30, "Teacher", "Math");
        service.addTeacher(newTeacher);

        assertEquals(1, service.getTeacherList().size());
        assertEquals("john_doe", service.getTeacherList().get(0).getUsername());
    }

    @Test
    void testAddTeacherWithExistingUsername() throws IOException {
        Teacher newTeacher = new Teacher("john_doe", "password123", "John Doe", "Male", 30, "Teacher", "Math");
        service.addTeacher(newTeacher);

        Teacher anotherTeacher = new Teacher("john_doe", "password456", "Johnny Doe", "Male", 31, "Teacher", "History");
        assertThrows(IOException.class, () -> service.addTeacher(anotherTeacher));
    }

    @Test
    void testUpdateTeacher() throws IOException {
        Teacher newTeacher = new Teacher("john_doe", "password123", "John Doe", "Male", 30, "Teacher", "Math");
        service.addTeacher(newTeacher);

        Teacher updatedTeacher = new Teacher("john_doe", "newpassword", "John Smith", "Male", 31, "Senior Teacher", "Math");
        service.updateTeacher(updatedTeacher, "john_doe");

        assertEquals("newpassword", service.getTeacherList().get(0).getPassword());
        assertEquals("John Smith", service.getTeacherList().get(0).getName());
    }

    @Test
    void testDeleteTeacher() throws IOException {
        Teacher newTeacher = new Teacher("john_doe", "password123", "John Doe", "Male", 30, "Teacher", "Math");
        service.addTeacher(newTeacher);
        assertEquals(1, service.getTeacherList().size());

        service.deleteTeacher(newTeacher);
        assertEquals(0, service.getTeacherList().size());
    }

    @Test
    void testFilterTeachers() throws IOException {
        service.addTeacher(new Teacher("john_doe", "password123", "John Doe", "Male", 30, "Teacher", "Math"));
        service.addTeacher(new Teacher("jane_doe", "password456", "Jane Doe", "Female", 28, "Teacher", "Science"));

        List<Teacher> filteredTeachers = service.filterTeachers("jane", "", "");
        assertEquals(1, filteredTeachers.size());
        assertEquals("jane_doe", filteredTeachers.get(0).getUsername());
    }

    @Test
    void testvalidateInputs() {
        String result = service.validateInputs("John Doe", "aa","Male", "30", "Teacher", "Math", "123");
        assertEquals("The password must contain both letters and numbers and be at least eight characters long",result);
        result = service.validateInputs("", "aa","Male", "30", "Teacher", "Math", "1aaa23d23");
        assertEquals("Each field should be filled in",result);
        result = service.validateInputs("aaaaad", "aa","Male", "aaa", "Teacher", "Math", "123dddsse");
        assertEquals("Age must be a valid number",result);
    }

    @Test
    void testValidateUpdateInputs() {
        String result = service.validateUpdateInputs("John Doe", "Male", "30", "Teacher", "Math", "password123");
        assertNull(result);

        result = service.validateUpdateInputs("", "Male", "30", "Teacher", "Math", "password123");
        assertEquals("Each field should be filled in", result);
    }
}