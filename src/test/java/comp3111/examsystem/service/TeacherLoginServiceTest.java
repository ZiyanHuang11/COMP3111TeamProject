package comp3111.examsystem.service;

import comp3111.examsystem.entity.Teacher;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.io.TempDir;

import java.io.IOException;
import java.nio.file.*;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

class TeacherLoginServiceTest {

    private TeacherLoginService loginService;

    @TempDir
    Path tempDir;

    private Path teacherFile;

    @BeforeEach
    void setUp() throws IOException {
        // Create a temporary teacher file
        teacherFile = tempDir.resolve("teachers.txt");
        List<String> teacherData = Arrays.asList(
                "john_doe, password123, John Doe, Male, 35, Professor, Computer Science, CS101, CS102",
                "jane_smith, securePass, Jane Smith, Female, 40, Associate Professor, Mathematics, MA201, ",
                "invalid_line_with_insufficient_fields",
                "bob_brown, pass, Bob Brown, Male, thirty, Lecturer, Physics, PH301, PH302" // Invalid age
        );
        Files.write(teacherFile, teacherData, StandardOpenOption.CREATE);
        loginService = new TeacherLoginService(teacherFile.toString());
    }

    @Test
    @DisplayName("Test valid login with existing username and correct password")
    void testValidLogin() {
        Teacher teacher = loginService.validate("john_doe", "password123");
        assertNotNull(teacher, "The teacher should be able to log in successfully");
        assertEquals("john_doe", teacher.getUsername());
        assertEquals("John Doe", teacher.getName());
        assertEquals("Male", teacher.getGender());
        assertEquals(35, teacher.getAge());
        assertEquals("Professor", teacher.getPosition());
        assertEquals("Computer Science", teacher.getDepartment());
        assertEquals("CS101", teacher.getCourseid1());
        assertEquals("CS102", teacher.getCourseid2());
    }

    @Test
    @DisplayName("Test existing username but incorrect password")
    void testInvalidPassword() {
        Teacher teacher = loginService.validate("john_doe", "wrongPassword");
        assertNull(teacher, "Login with incorrect password should fail");
    }

    @Test
    @DisplayName("Test non-existing username")
    void testNonExistingUser() {
        Teacher teacher = loginService.validate("non_existing_user", "password123");
        assertNull(teacher, "Login with non-existing username should fail");
    }

    @Test
    @DisplayName("Test login when teacher file does not exist")
    void testFileNotFound() {
        TeacherLoginService serviceWithInvalidPath = new TeacherLoginService(tempDir.resolve("non_existing_file.txt").toString());
        Teacher teacher = serviceWithInvalidPath.validate("john_doe", "password123");
        assertNull(teacher, "Login should fail if the teacher file does not exist");
    }

    @Test
    @DisplayName("Test invalid file format (insufficient fields)")
    void testInvalidFileFormatInsufficientFields() {
        // Attempt to log in with a username from a line with insufficient fields
        Teacher teacher = loginService.validate("invalid_line_with_insufficient_fields", "anyPassword");
        assertNull(teacher, "Login should fail due to insufficient fields in the teacher data");
    }

    @Test
    @DisplayName("Test invalid file format (invalid age format)")
    void testInvalidFileFormatInvalidAge() {
        // Attempt to log in with a username that has an invalid age format
        Teacher teacher = loginService.validate("bob_brown", "pass");
        assertNull(teacher, "Login should fail due to invalid age format in the teacher data");
    }

    @Test
    @DisplayName("Test login with missing course IDs")
    void testLoginWithMissingCourseIDs() throws IOException {
        // Add a teacher record missing course IDs
        List<String> additionalData = Collections.singletonList(
                "alice_green, alicePass, Alice Green, Female, 30, Assistant Professor, Chemistry"
        );
        Files.write(teacherFile, additionalData, StandardOpenOption.APPEND);

        Teacher teacher = loginService.validate("alice_green", "alicePass");
        assertNotNull(teacher, "The teacher should be able to log in even with missing course IDs");
        assertEquals("alice_green", teacher.getUsername());
        assertEquals("Alice Green", teacher.getName());
        assertEquals("Female", teacher.getGender());
        assertEquals(30, teacher.getAge());
        assertEquals("Assistant Professor", teacher.getPosition());
        assertEquals("Chemistry", teacher.getDepartment());
        assertEquals("", teacher.getCourseid1());
        assertEquals("", teacher.getCourseid2());
    }

    @Test
    @DisplayName("Test login with extra fields in file")
    void testLoginWithExtraFields() throws IOException {
        // Add a teacher record with extra fields
        List<String> additionalData = Collections.singletonList(
                "charlie_black, charliePass, Charlie Black, Male, 45, Senior Lecturer, Biology, BI401, BI402, ExtraField"
        );
        Files.write(teacherFile, additionalData, StandardOpenOption.APPEND);

        Teacher teacher = loginService.validate("charlie_black", "charliePass");
        assertNotNull(teacher, "The teacher should be able to log in even with extra fields");
        assertEquals("charlie_black", teacher.getUsername());
        assertEquals("Charlie Black", teacher.getName());
        assertEquals("Male", teacher.getGender());
        assertEquals(45, teacher.getAge());
        assertEquals("Senior Lecturer", teacher.getPosition());
        assertEquals("Biology", teacher.getDepartment());
        assertEquals("BI401", teacher.getCourseid1());
        assertEquals("BI402", teacher.getCourseid2());
        // ExtraField is ignored based on current implementation
    }

    @Test
    @DisplayName("Test login with empty username and password")
    void testEmptyUsernameAndPassword() {
        Teacher teacher = loginService.validate("", "");
        assertNull(teacher, "Login with empty username and password should fail");
    }

    @Test
    @DisplayName("Test login with null username and password")
    void testNullUsernameAndPassword() {
        // Since the validate method now returns null instead of throwing NullPointerException, adjust the test expectation
        Teacher teacher = loginService.validate(null, null);
        assertNull(teacher, "Login with null username and password should return null");
    }
}
