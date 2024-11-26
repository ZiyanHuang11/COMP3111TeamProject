package comp3111.examsystem.service;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.FileWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class StudentRegisterServiceTest {

    private static final String TEST_FILE_PATH = "test_students.txt";
    private StudentRegisterService service;

    @BeforeEach
    void setUp() throws Exception {
        try (FileWriter writer = new FileWriter(TEST_FILE_PATH)) {
            writer.write("user1,John,Male,20,CS,password1\n");
            writer.write("user2,Jane,Female,21,IT,password2\n");
        }
        service = new StudentRegisterService(TEST_FILE_PATH);
    }

    @AfterEach
    void tearDown() throws Exception {
        Files.deleteIfExists(Path.of(TEST_FILE_PATH));
    }

    @Test
    void testValidateInputs_Valid() {
        String result = service.validateInputs("user3", "Alice", "Female", "22", "Math", "pass123", "pass123");
        assertNull(result);
    }

    @Test
    void testValidateInputs_InvalidGender() {
        String result = service.validateInputs("user3", "Alice", "Gender", "22", "Math", "pass123", "pass123");
        assertEquals("Please select your gender.", result);
    }

    @Test
    void testIsUserExists_UserExists() {
        assertTrue(service.isUserExists("user1"));
    }

    @Test
    void testIsUserExists_UserDoesNotExist() {
        assertFalse(service.isUserExists("user3"));
    }

    @Test
    void testRegisterStudent() throws Exception {
        Map<String, String> studentInfo = new HashMap<>();
        studentInfo.put("username", "user3");
        studentInfo.put("name", "Alice");
        studentInfo.put("age", "22");
        studentInfo.put("gender", "Female");
        studentInfo.put("department", "Math");
        studentInfo.put("password", "pass123");

        service.registerStudent(studentInfo);

        assertTrue(service.isUserExists("user3"));
        Map<String, String> students = service.getAllStudents();
        assertEquals(3, students.size());
    }

    @Test
    void testRegisterStudent_FileCreation() throws Exception {
        String newFilePath = "new_students.txt";
        Files.deleteIfExists(Path.of(newFilePath));
        StudentRegisterService newService = new StudentRegisterService(newFilePath);

        Map<String, String> studentInfo = new HashMap<>();
        studentInfo.put("username", "user3");
        studentInfo.put("name", "Alice");
        studentInfo.put("age", "22");
        studentInfo.put("gender", "Female");
        studentInfo.put("department", "Math");
        studentInfo.put("password", "pass123");

        newService.registerStudent(studentInfo);

        assertTrue(Files.exists(Path.of(newFilePath)));
        Files.deleteIfExists(Path.of(newFilePath));
    }

    @Test
    void testGetAllStudents() {
        Map<String, String> students = service.getAllStudents();
        assertEquals(2, students.size());
        assertTrue(students.containsKey("user1"));
        assertTrue(students.containsKey("user2"));
    }

    @Test
    void testGetAllStudents_EmptyFile() throws Exception {
        Files.writeString(Path.of(TEST_FILE_PATH), "");
        Map<String, String> students = service.getAllStudents();
        assertTrue(students.isEmpty());
    }

    @Test
    void testGetAllStudents_PartiallyCorruptedData() throws Exception {
        Files.writeString(Path.of(TEST_FILE_PATH), "user1,John,Male,20,CS,password1\nInvalidData\n");
        Map<String, String> students = service.getAllStudents();
        assertEquals(1, students.size());
        assertTrue(students.containsKey("user1"));
    }
}
