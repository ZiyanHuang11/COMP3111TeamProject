package comp3111.examsystem.service;

import comp3111.examsystem.entity.Student;
import comp3111.examsystem.service.ManageStudentService;
import javafx.collections.ObservableList;
import org.junit.jupiter.api.*;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

public class ManageStudentServiceTest {
    private static final String TEST_STUDENT_FILE = "test_data/test_students.txt";
    private static final String TEST_EXAMS_FILE = "test_data/test_exams.txt";
    private ManageStudentService service;

    @BeforeEach
    public void setUp() throws IOException {

        new File(TEST_STUDENT_FILE).createNewFile();
        new File(TEST_EXAMS_FILE).createNewFile();
        service = new ManageStudentService(TEST_STUDENT_FILE, TEST_EXAMS_FILE);
    }

    @AfterEach
    public void tearDown() {

        new File(TEST_STUDENT_FILE).delete();
        new File(TEST_EXAMS_FILE).delete();
    }

    @Test
    public void testLoadStudentsFromFile() throws IOException {

        try (var bw = new BufferedWriter(new FileWriter(TEST_STUDENT_FILE))) {
            bw.write("user1,John Doe,20,Male,CS,password123");
            bw.newLine();
            bw.write("user2,Jane Smith,22,Female,EE,password456");
        }

        service.loadStudentsFromFile();
        ObservableList<Student> students = service.getStudentList();

        assertEquals(2, students.size());
        assertEquals("user1", students.get(0).getUsername());
        assertEquals("John Doe", students.get(0).getName());
    }

    @Test
    public void testAddStudent() throws IOException {
        Student student = new Student("user1", "John Doe", 20, "Male", "CS", "password123");
        service.addStudent(student);

        ObservableList<Student> students = service.getStudentList();
        assertEquals(1, students.size());
        assertEquals("user1", students.get(0).getUsername());
    }

    @Test
    public void testUpdateStudent() throws IOException {
        Student student = new Student("user1", "John Doe", 20, "Male", "CS", "password123");
        service.addStudent(student);

        Student updatedStudent = new Student("user1", "John Doe Updated", 21, "Male", "CS", "newpassword123");
        service.updateStudent(updatedStudent, "user1");

        assertEquals("John Doe Updated", service.getStudentList().get(0).getName());
    }

    @Test
    public void testUpdateStudentNotFound() {
        Student updatedStudent = new Student("user1", "John Doe Updated", 21, "Male", "CS", "newpassword123");
        IOException exception = assertThrows(IOException.class, () -> {
            service.updateStudent(updatedStudent, "nonexistentUser");
        });
        assertEquals("Student with username nonexistentUser does not exist.", exception.getMessage());
    }

    @Test
    public void testDeleteStudent() throws IOException {
        Student student = new Student("user1", "John Doe", 20, "Male", "CS", "password123");
        service.addStudent(student);

        service.deleteStudent("user1");
        assertEquals(0, service.getStudentList().size());
    }

    @Test
    public void testDeleteStudentNotFound() {
        IOException exception = assertThrows(IOException.class, () -> {
            service.deleteStudent("nonexistentUser");
        });
        assertEquals("Student with username nonexistentUser does not exist.", exception.getMessage());
    }

    @Test
    public void testFilterStudents() throws IOException {
        service.addStudent(new Student("user1", "John Doe", 20, "Male", "CS", "password123"));
        service.addStudent(new Student("user2", "Jane Smith", 22, "Female", "EE", "password456"));

        var filtered = service.filterStudents("user1", "", "");
        assertEquals(1, filtered.size());
        assertEquals("user1", filtered.get(0).getUsername());

        filtered = service.filterStudents("nonexistent", "", "");
        assertEquals(0, filtered.size());
    }

    @Test
    public void testValidateUsername() throws IOException {
        service.addStudent(new Student("user1", "John Doe", 20, "Male", "CS", "password123"));
        String result = service.validateUsername("user1");
        assertEquals("The user name already exists", result);

        result = service.validateUsername("newuser");
        assertNull(result);
    }

    @Test
    public void testValidateInputs() {
        String result = service.validateInputs("user1", "John Doe", "20", "Male", "CS", "password123");
        assertNull(result);

        result = service.validateInputs("", "John Doe", "20", "Male", "CS", "password123");
        assertEquals("Each field should be filled in", result);

        result = service.validateInputs("user1", "John Doe", "invalidAge", "Male", "CS", "password123");
        assertEquals("Age must be a valid number", result);

        result = service.validateInputs("user1", "John Doe", "20", "Male", "", "password123");
        assertEquals("Each field should be filled in", result);

        result = service.validateInputs("user1", "John Doe", "20", "Male", "CS", "short");
        assertEquals("The password must contain both letters and numbers and be at least eight characters long", result);
    }

    @Test
    public void testIsValidPassword() {
        assertFalse(service.isValidPassword("validPass1"));
        assertTrue(service.isValidPassword("short1"));
        assertTrue(service.isValidPassword("onlyletters"));
        assertTrue(service.isValidPassword("12345678"));
    }

    @Test
    public void testValidateUpdateInputs() {
        String result = service.validateUpdateInputs("John Doe", "20", "Male", "CS", "password123");
        assertNull(result);

        result = service.validateUpdateInputs("", "20", "Male", "CS", "password123");
        assertEquals("Each field should be filled in", result);

        result = service.validateUpdateInputs("John Doe", "invalidAge", "Male", "CS", "password123");
        assertEquals("Age must be a valid number", result);

        result = service.validateUpdateInputs("John Doe", "20", "Male", "CS", "short");
        assertEquals("The password must contain both letters and numbers and be at least eight characters long", result);
    }



    @Test
    public void testUpdateStudentExams() throws IOException {
        service.addStudent(new Student("user1", "John Doe", 20, "Male", "CS", "password123"));
        service.updateStudentExams("user1", "user1_updated");
    }

    @Test
    public void testDeleteStudentFromExams() throws IOException {
        // Add a student and simulate exam deletion
        service.addStudent(new Student("user1", "John Doe", 20, "Male", "CS", "password123"));
        service.deleteStudentFromExams("user1");
    }
}