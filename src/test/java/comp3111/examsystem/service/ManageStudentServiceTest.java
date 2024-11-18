package comp3111.examsystem.service;

import comp3111.examsystem.entity.Student;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class ManageStudentServiceTest {
    private ManageStudentService studentService;
    private String testFilePath;

    @BeforeEach
    public void setUp() throws IOException {
        testFilePath = "test_students.txt";
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(testFilePath))) {
            writer.write("user1,John Doe,20,Male,CS,password123\n");
            writer.write("user2,Jane Smith,22,Female,ENG,password456\n");
            writer.write("user3,Bob Johnson,21,Male,MATH,password789\n");
        }
        studentService = new ManageStudentService(testFilePath);
    }

    @Test
    public void testLoadStudentsFromFile() {
        List<Student> students = studentService.getStudentList();
        assertEquals(3, students.size());
        assertEquals("user1", students.get(0).getUsername());
        assertEquals("John Doe", students.get(0).getName());
        assertEquals(20, students.get(0).getAge());
    }

    @Test
    public void testAddStudent() throws IOException {
        Student newStudent = new Student("user4", "Alice Brown", 19, "Female", "CS", "passwordABC");
        studentService.addStudent(newStudent);
        List<Student> students = studentService.getStudentList();
        assertEquals(4, students.size());
        assertEquals(newStudent, students.get(3));
    }

    @Test
    public void testUpdateStudent() throws IOException {
        Student updatedStudent = new Student("user1", "John Doe", 21, "Male", "CS", "newPassword123");
        studentService.updateStudent(updatedStudent, "user1");
        List<Student> students = studentService.getStudentList();
        assertEquals(21, students.get(0).getAge());
        assertEquals("newPassword123", students.get(0).getPassword());
    }

    @Test
    public void testUpdateNonExistentStudent() {
        Student updatedStudent = new Student("user4", "Alice Brown", 19, "Female", "CS", "passwordABC");
        assertThrows(IOException.class, () -> {
            studentService.updateStudent(updatedStudent, "nonexistentUser");
        });
    }

    @Test
    public void testDeleteStudent() throws IOException {
        studentService.deleteStudent("user1");
        List<Student> students = studentService.getStudentList();
        assertEquals(2, students.size());
        assertFalse(students.stream().anyMatch(student -> student.getUsername().equals("user1")));
    }

    @Test
    public void testDeleteNonExistentStudent() {
        assertThrows(IOException.class, () -> {
            studentService.deleteStudent("nonexistentUser");
        });
    }

    @Test
    public void testFilterStudents() {
        List<Student> filteredStudents = studentService.filterStudents("user", "", "");
        assertEquals(3, filteredStudents.size());
    }

    @Test
    public void testValidateUsername() {
        String validationMessage = studentService.validateUsername("user1");
        assertEquals("The user name already exists", validationMessage);

        validationMessage = studentService.validateUsername("user4");
        assertNull(validationMessage);
    }

    @Test
    public void testValidateInputs() {
        String validationMessage = studentService.validateInputs("", "Name", "20", "Male", "CS", "password");
        assertEquals("Each field should be filled in", validationMessage);

        validationMessage = studentService.validateInputs("user1", "Name", "20", "Male", "CS", "password");
        assertEquals("The user name already exists", validationMessage);

        validationMessage = studentService.validateInputs("user4", "Name", "notANumber", "Male", "CS", "password");
        assertEquals("Age must be a valid number", validationMessage);

        validationMessage = studentService.validateInputs("user4", "Name", "20", "Male", "CS", "short");
        assertEquals("The password must contain both letters and numbers and be at least eight characters long", validationMessage);

        validationMessage = studentService.validateInputs("user4", "Name", "20", "Male", "CS", "password123");
        assertNull(validationMessage);
    }

    @Test
    public void testValidateUpdateInputs() {
        String validationMessage = studentService.validateUpdateInputs("", "20", "Male", "CS", "password");
        assertEquals("Each field should be filled in", validationMessage);

        validationMessage = studentService.validateUpdateInputs("Name", "notANumber", "Male", "CS", "password");
        assertEquals("Age must be a valid number", validationMessage);

        validationMessage = studentService.validateUpdateInputs("Name", "20", "Male", "CS", "short");
        assertEquals("The password must contain both letters and numbers and be at least eight characters long", validationMessage);

        validationMessage = studentService.validateUpdateInputs("Name", "20", "Male", "CS", "password123");
        assertNull(validationMessage);
    }

    @AfterEach
    public void tearDown() throws IOException {
        Files.deleteIfExists(Paths.get(testFilePath));
    }
}