package comp3111.examsystem.service;

import comp3111.examsystem.entity.Student;
import comp3111.examsystem.service.ManageStudentService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

public class ManageStudentServiceTest {
    private ManageStudentService service;
    private final String studentFilePath = "test_students.txt";
    private final String studentExamsFilePath = "test_studentsexams.txt";

    @BeforeEach
    public void setUp() throws IOException {
        // 创建测试文件
        new File(studentFilePath).createNewFile();
        new File(studentExamsFilePath).createNewFile();

        service = new ManageStudentService(studentFilePath, studentExamsFilePath);
    }

    @Test
    public void testAddStudent() throws IOException {
        Student student = new Student("john_doe", "John Doe", 20, "Male", "CS", "password123");
        service.addStudent(student);

        assertEquals(1, service.getStudentList().size());
        assertEquals("john_doe", service.getStudentList().get(0).getUsername());
    }

    @Test
    public void testUpdateStudent() throws IOException {
        Student student = new Student("john_doe", "John Doe", 20, "Male", "CS", "password123");
        service.addStudent(student);

        Student updatedStudent = new Student("john_doe", "Johnathan Doe", 21, "Male", "CS", "newpassword123");
        service.updateStudent(updatedStudent, "john_doe");

        assertEquals("Johnathan Doe", service.getStudentList().get(0).getName());
        assertEquals(21, service.getStudentList().get(0).getAge());
    }

    @Test
    public void testUpdateNonExistentStudent() {
        Student updatedStudent = new Student("nonexistent", "Non Existent", 21, "Male", "CS", "newpassword123");
        try {
            service.updateStudent(updatedStudent, "nonexistent");
            fail("Expected IOException for non-existent student");
        } catch (IOException e) {
            assertEquals("Student with username nonexistent does not exist.", e.getMessage());
        }
    }

    @Test
    public void testDeleteStudent() throws IOException {
        Student student = new Student("john_doe", "John Doe", 20, "Male", "CS", "password123");
        service.addStudent(student);
        assertEquals(1, service.getStudentList().size());

        service.deleteStudent("john_doe");
        assertEquals(0, service.getStudentList().size());
    }

    @Test
    public void testDeleteNonExistentStudent() {
        try {
            service.deleteStudent("nonexistent");
            fail("Expected IOException for non-existent student");
        } catch (IOException e) {
            assertEquals("Student with username nonexistent does not exist.", e.getMessage());
        }
    }

    @Test
    public void testAssignCourseToStudent() throws IOException {
        Student student = new Student("john_doe", "John Doe", 20, "Male", "CS", "password123");
        service.addStudent(student);
        service.loadCoursesFromFile(); // Ensure courses are loaded

        service.assignCourseToStudent("john_doe", "CS101");

        assertEquals(1, service.getCoursesForStudent("john_doe").size());
        assertEquals("CS101", service.getCoursesForStudent("john_doe").get(0));
    }

    @Test
    public void testAssignDuplicateCourseToStudent() throws IOException {
        Student student = new Student("john_doe", "John Doe", 20, "Male", "CS", "password123");
        service.addStudent(student);
        service.loadCoursesFromFile(); // Ensure courses are loaded
        service.assignCourseToStudent("john_doe", "CS101");

        try {
            service.assignCourseToStudent("john_doe", "CS101"); // Attempt to assign the same course again
            fail("Expected IOException for duplicate course assignment");
        } catch (IOException e) {
            assertEquals("The student is already assigned this course.", e.getMessage());
        }
    }

    @Test
    public void testRemoveCourseFromStudent() throws IOException {
        Student student = new Student("john_doe", "John Doe", 20, "Male", "CS", "password123");
        service.addStudent(student);
        service.loadCoursesFromFile(); // Ensure courses are loaded
        service.assignCourseToStudent("john_doe", "CS101");

        service.removeCourseFromStudent("john_doe", "CS101");

        assertEquals(0, service.getCoursesForStudent("john_doe").size());
    }

    @Test
    public void testValidateUsername() throws IOException {
        service.addStudent(new Student("john_doe", "John Doe", 20, "Male", "CS", "password123"));
        assertEquals("The user name already exists", service.validateUsername("john_doe"));
        assertNull(service.validateUsername("jane_doe"));
    }

    @Test
    public void testValidateInputs() {
        String result = service.validateInputs("john_doe", "John Doe", "20", "Male", "CS", "password123");
        assertNull(result);

        result = service.validateInputs("", "John Doe", "20", "Male", "CS", "password123");
        assertEquals("Each field should be filled in", result);

        result = service.validateInputs("john_doe", "John Doe", "twenty", "Male", "CS", "password123");
        assertEquals("Age must be a valid number", result);

        result = service.validateInputs("john_doe", "John Doe", "20", "Male", "CS", "pass");
        assertEquals("The password must contain both letters and numbers and be at least eight characters long", result);
    }

    @Test
    public void testIsValidPassword() {
        assertTrue(service.isValidPassword("short"));
        assertFalse(service.isValidPassword("valid123"));
    }

    @Test
    public void testUpdateInputs() {
        assertEquals("Each field should be filled in",service.validateUpdateInputs("","17","Male","CSE","aaaa1111"));
        assertEquals("Age must be a valid number",service.validateUpdateInputs("aaa","b","Male","CSE","aaaa1111"));
        assertEquals("The password must contain both letters and numbers and be at least eight characters long",service.validateUpdateInputs("aaa","18","Male","CSE","11"));
        assertNull(service.validateUpdateInputs("aaa","21","Male","CSE","aaaa1111"));
    }

    @Test
    public void testFilterStudents() throws IOException {
        Student student1 = new Student("john_doe", "John Doe", 20, "Male","CS", "password123");
        Student student2 = new Student("jane_doe", "Jane Doe", 22, "Female","Math", "password456");
        service.addStudent(student1);
        service.addStudent(student2);

        assertEquals(1, service.filterStudents("john", "", "").size());
        assertEquals(1, service.filterStudents("", "Doe", "Math").size());
        assertEquals(1, service.filterStudents("john", "Doe", "").size());
    }

    @AfterEach
    public void tearDown() {
        new File(studentFilePath).delete();
        new File(studentExamsFilePath).delete();
    }
}