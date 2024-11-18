package comp3111.examsystem.service;

import comp3111.examsystem.entity.Teacher;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.*;

public class ManageTeacherServiceTest {

    private ManageTeacherService manageTeacherService;
    private final String testFilePath = "data/test_teachers.txt";

    @BeforeEach
    public void setUp() throws IOException {

        manageTeacherService = new ManageTeacherService();
        manageTeacherService.teacherFilePath = testFilePath;

        Files.write(Paths.get(testFilePath), "username1,John Doe,Male,30,Professor,CS,password1\n".getBytes());
        manageTeacherService.loadTeachersFromFile();
    }

    @Test
    public void testAddTeacher() throws IOException {
        Teacher newTeacher = new Teacher("username2", "Jane Doe", "Female", 28, "Assistant", "Math", "password2");
        manageTeacherService.addTeacher(newTeacher);

        assertTrue(manageTeacherService.getTeacherList().contains(newTeacher));
    }

    @Test
    public void testAddTeacherWithExistingUsername() {
        Teacher newTeacher = new Teacher("username1", "Jane Doe", "Female", 28, "Assistant", "Math", "password2");
        Exception exception = assertThrows(IOException.class, () -> {
            manageTeacherService.addTeacher(newTeacher);
        });

        String expectedMessage = "The user name already exists";
        String actualMessage = exception.getMessage();
        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    public void testUpdateTeacher() throws IOException {
        Teacher updatedTeacher = new Teacher("username1", "John Smith", "Male", 31, "Professor", "CS", "newpassword");
        manageTeacherService.updateTeacher(updatedTeacher, "username1");

        assertTrue(manageTeacherService.getTeacherList().contains(updatedTeacher));
        assertFalse(manageTeacherService.getTeacherList().stream().anyMatch(t -> t.getUsername().equals("username1") && t.getName().equals("John Doe")));
    }

    @Test
    public void testUpdateNonExistentTeacher() {
        Teacher updatedTeacher = new Teacher("nonexistent", "Non Existent", "Male", 31, "Professor", "CS", "newpassword");
        Exception exception = assertThrows(IOException.class, () -> {
            manageTeacherService.updateTeacher(updatedTeacher, "nonexistent");
        });

        String expectedMessage = "Teacher not found";
        String actualMessage = exception.getMessage();
        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    public void testDeleteNonExistentTeacher() {
        Teacher teacherToDelete = new Teacher("nonexistent", "Non Existent", "Male", 30, "Professor", "CS", "password1");
        Exception exception = assertThrows(IOException.class, () -> {
            manageTeacherService.deleteTeacher(teacherToDelete);
        });

        String expectedMessage = "Teacher not found";
        String actualMessage = exception.getMessage();
        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    public void testFilterTeachers() {
        var filteredList = manageTeacherService.filterTeachers("username1", "", "");
        assertEquals(1, filteredList.size());
        assertEquals("username1", filteredList.get(0).getUsername());

        var emptyList = manageTeacherService.filterTeachers("nonexistent", "", "");
        assertEquals(0, emptyList.size());
    }

    @Test
    public void testValidateInputs() {
        String result = manageTeacherService.validateInputs("username3", "Alice", "Female", "25", "Lecturer", "Physics", "pass1234");
        assertNull(result);

        result = manageTeacherService.validateInputs("", "Alice", "Female", "25", "Lecturer", "Physics", "pass1234");
        assertEquals("Each field should be filled in", result);

        result = manageTeacherService.validateInputs("username1", "Alice", "Female", "25", "Lecturer", "Physics", "pass123");
        assertEquals("The user name already exists", result);
    }

    @Test
    public void testValidateUpdateInputs() {
        String result = manageTeacherService.validateUpdateInputs("Alice", "Female", "25", "Lecturer", "Physics", "pass1234");
        assertNull(result);

        result = manageTeacherService.validateUpdateInputs("", "Female", "25", "Lecturer", "Physics", "pass123");
        assertEquals("Each field should be filled in", result);
    }

    @Test
    public void testIsUsernameExists() {
        assertTrue(manageTeacherService.isUsernameExists("username1"));
        assertFalse(manageTeacherService.isUsernameExists("username2"));
    }

    @Test
    public void testIsValidPassword() {
        assertTrue(manageTeacherService.isValidPassword("pass"));
        assertFalse(manageTeacherService.isValidPassword("pass1234"));
    }

    @Test
    public void testWriteAllTeachersToFile() throws IOException {
        Teacher newTeacher = new Teacher("username2", "Jane Doe", "Female", 28, "Assistant", "Math", "password2");
        manageTeacherService.addTeacher(newTeacher);
        manageTeacherService.writeAllTeachersToFile();

        assertTrue(Files.readAllLines(Paths.get(testFilePath)).stream().anyMatch(line -> line.contains("username2")));
    }

    @AfterEach
    public void tearDown() throws IOException {

        Files.deleteIfExists(Paths.get(testFilePath));
    }
}