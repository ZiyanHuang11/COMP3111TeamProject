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
    public void testAssignCourseToTeacherAlreadyAssigned() throws IOException {
        Teacher teacher = new Teacher("john_doe", "password123", "John Doe", "Male", 30, "Professor", "CS", "CS101", null);
        service.addTeacher(teacher);

        // 尝试再次分配已经分配的课程
        Exception exception = assertThrows(IOException.class, () -> service.assignCourseToTeacher(teacher, "CS101"));
        assertEquals("This course is already assigned to the teacher.", exception.getMessage());
    }

    @Test
    public void testAssignCourseToTeacherExceedLimit() throws IOException {
        Teacher teacher = new Teacher("john_doe", "password123", "John Doe", "Male", 30, "Professor", "CS", "CS101", "CS102");
        service.addTeacher(teacher);

        // 尝试为教师分配超过最大课程限制的课程
        Exception exception = assertThrows(IOException.class, () -> service.assignCourseToTeacher(teacher, "CS103"));
        assertEquals("Teacher has reached maximum course limit.", exception.getMessage());
    }

    @Test
    public void testValidateInputsAllValid() {
        String result = service.validateInputs("john_doe", "John Doe", "Male", "30", "Professor", "CS", "password123");
        assertNull(result);  // 应返回 null，表示没有错误
    }

    @Test
    public void testValidateInputsEmptyFields() {
        String result = service.validateInputs("", "John Doe", "Male", "30", "Professor", "CS", "password123");
        assertEquals("Each field should be filled in", result);

        result = service.validateInputs("john_doe", "", "Male", "30", "Professor", "CS", "password123");
        assertEquals("Each field should be filled in", result);

        result = service.validateInputs("john_doe", "John Doe", null, "30", "Professor", "CS", "password123");
        assertEquals("Each field should be filled in", result);

        result = service.validateInputs("john_doe", "John Doe", "Male", "", "Professor", "CS", "password123");
        assertEquals("Each field should be filled in", result);

        result = service.validateInputs("john_doe", "John Doe", "Male", "30", null, "CS", "password123");
        assertEquals("Each field should be filled in", result);

        result = service.validateInputs("john_doe", "John Doe", "Male", "30", "Professor", "", "password123");
        assertEquals("Each field should be filled in", result);

        result = service.validateInputs("john_doe", "John Doe", "Male", "30", "Professor", "CS", "");
        assertEquals("Each field should be filled in", result);
    }

    @Test
    public void testValidateInputsInvalidAge() {
        String result = service.validateInputs("john_doe", "John Doe", "Male", "thirty", "Professor", "CS", "password123");
        assertEquals("Age must be a valid number", result);
    }

    @Test
    public void testValidateInputsUsernameExists() throws IOException {
        // 假设已经存在的用户
        service.addTeacher(new Teacher("john_doe", "password123", "John Doe", "Male", 30, "Professor", "CS", null, null));

        String result = service.validateInputs("john_doe", "John Doe", "Male", "30", "Professor", "CS", "password123");
        assertEquals("The user name already exists", result);
    }

    @Test
    public void testValidateInputsInvalidPassword() {
        String result =service.validateInputs("john_doe", "John Doe", "Male", "30", "Professor", "CS", "short");
        assertEquals("The password must contain both letters and numbers and be at least eight characters long", result);

        result = service.validateInputs("john_doe", "John Doe", "Male", "30", "Professor", "CS", "12345678");
        assertEquals("The password must contain both letters and numbers and be at least eight characters long", result);
    }


    @Test
    void testValidateUpdateInputs() {
        String result = service.validateUpdateInputs("John Doe", "Male", "30", "Teacher", "Math", "password123");
        assertNull(result);

        result = service.validateUpdateInputs("", "Male", "30", "Teacher", "Math", "password123");
        assertEquals("Each field should be filled in", result);
    }
}