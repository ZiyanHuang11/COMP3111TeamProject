package comp3111.examsystem.service;

import comp3111.examsystem.data.DataManager;
import comp3111.examsystem.entity.Teacher;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class DummyDataManager extends DataManager {
    private List<Teacher> teachers = new ArrayList<>();

    @Override
    public List<Teacher> getTeachers() {
        return teachers;
    }

    @Override
    public void saveTeachers() {
        // Dummy implementation for saving teachers
    }
}

public class TeacherRegisterServiceTest {
    private TeacherRegisterService teacherRegisterService;
    private DummyDataManager dataManager;

    @BeforeEach
    public void setUp() {
        dataManager = new DummyDataManager();
        teacherRegisterService = new TeacherRegisterService(dataManager);
    }

    @Test
    public void testValidateInputs_AllFieldsValid() {
        String result = teacherRegisterService.validateInputs("user1", "John Doe", "Male", "30", "Professor", "CS", "password", "password");
        assertNull(result);
    }

    @Test
    public void testValidateInputs_EmptyFields() {
        String result = teacherRegisterService.validateInputs("", "John Doe", "Male", "30", "Professor", "CS", "password", "password");
        assertEquals("All fields are required.", result);
    }

    @Test
    public void testValidateInputs_InvalidGenderPosition() {
        String result = teacherRegisterService.validateInputs("user1", "John Doe", "Gender", "30", "Position", "CS", "password", "password");
        assertEquals("Please select your gender and position.", result);
    }

    @Test
    public void testValidateInputs_PasswordsDoNotMatch() {
        String result = teacherRegisterService.validateInputs("user1", "John Doe", "Male", "30", "Professor", "CS", "password", "wrongpassword");
        assertEquals("Passwords do not match.", result);
    }

    @Test
    public void testValidateInputs_InvalidAge() {
        String result = teacherRegisterService.validateInputs("user1", "John Doe", "Male", "thirty", "Professor", "CS", "password", "password");
        assertEquals("Age must be a valid number.", result);
    }

    @Test
    public void testIsUserExists_UserExists() {
        Teacher teacher = new Teacher();
        teacher.setUsername("user1");
        dataManager.getTeachers().add(teacher);

        assertTrue(teacherRegisterService.isUserExists("user1"));
    }

    @Test
    public void testIsUserExists_UserDoesNotExist() {
        assertFalse(teacherRegisterService.isUserExists("user1"));
    }

    @Test
    public void testPrepareTeacher_ValidInput() {
        Optional<Teacher> teacherOptional = teacherRegisterService.prepareTeacher("user1", "John Doe", "Male", "30", "Professor", "CS", "password");
        assertTrue(teacherOptional.isPresent());
        Teacher teacher = teacherOptional.get();
        assertEquals("user1", teacher.getUsername());
        assertEquals("John Doe", teacher.getName());
        assertEquals("Male", teacher.getGender());
        assertEquals(30, teacher.getAge());
        assertEquals("Professor", teacher.getTitle());
        assertEquals("CS", teacher.getDepartment());
        assertEquals("password", teacher.getPassword());
    }

    @Test
    public void testPrepareTeacher_InvalidAge() {
        Optional<Teacher> teacherOptional = teacherRegisterService.prepareTeacher("user1", "John Doe", "Male", "thirty", "Professor", "CS", "password");
        assertFalse(teacherOptional.isPresent());
    }

    @Test
    public void testRegisterTeacher() {
        Teacher teacher = new Teacher();
        teacher.setUsername("user1");

        teacherRegisterService.registerTeacher(teacher);
        assertEquals(1, dataManager.getTeachers().size());
        assertEquals("user1", dataManager.getTeachers().get(0).getUsername());
    }
}