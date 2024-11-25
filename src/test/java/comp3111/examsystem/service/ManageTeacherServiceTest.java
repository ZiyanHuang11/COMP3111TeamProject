package comp3111.examsystem.service;

import comp3111.examsystem.data.DataManager;
import comp3111.examsystem.entity.Teacher;
import comp3111.examsystem.entity.Question;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

class ManageTeacherServiceTest {

    private ManageTeacherService manageTeacherService;
    private DataManager dataManager;

    @BeforeEach
    void setUp() {
        // 使用 MockDataManager 而不是 DataManager
        dataManager = new MockDataManager();
        manageTeacherService = new ManageTeacherService(dataManager);

        // 初始化测试数据
        dataManager.getTeachers().add(new Teacher("1", "teacher1", "password123", "John Doe", "Male", 35, "Professor", "CSE"));
        dataManager.getTeachers().add(new Teacher("2", "teacher2", "password456", "Jane Smith", "Female", 40, "Lecturer", "IT"));
    }

    @Test
    void testGetAllTeachers() {
        List<Teacher> teachers = manageTeacherService.getAllTeachers();
        assertEquals(2, teachers.size(), "There should be 2 teachers initially.");
    }

    @Test
    void testAddTeacher() {
        Teacher newTeacher = new Teacher("3", "teacher3", "password789", "Michael Brown", "Male", 30, "Assistant Professor", "EE");
        manageTeacherService.addTeacher(newTeacher);

        List<Teacher> teachers = manageTeacherService.getAllTeachers();
        assertEquals(3, teachers.size(), "Teacher list size should increase to 3.");
        assertTrue(teachers.contains(newTeacher), "The new teacher should be in the list.");
    }

    @Test
    void testUpdateTeacher() {
        Teacher updatedTeacher = new Teacher("1", "teacher1", "newPassword123", "John Doe Updated", "Male", 36, "Professor", "CSE");
        manageTeacherService.updateTeacher(updatedTeacher, "teacher1");

        List<Teacher> teachers = manageTeacherService.getAllTeachers();
        assertEquals(2, teachers.size(), "Teacher list size should remain the same.");
        assertEquals("John Doe Updated", teachers.get(0).getName(), "The teacher's name should be updated.");
        assertEquals("newPassword123", teachers.get(0).getPassword(), "The teacher's password should be updated.");
    }

    @Test
    void testDeleteTeacher() {
        Teacher teacherToDelete = dataManager.getTeachers().get(0); // 获取第一个教师
        manageTeacherService.deleteTeacher(teacherToDelete);

        List<Teacher> teachers = manageTeacherService.getAllTeachers();
        assertEquals(1, teachers.size(), "Teacher list size should decrease to 1.");
        assertFalse(teachers.contains(teacherToDelete), "The deleted teacher should not be in the list.");
    }

    @Test
    void testFilterTeachers() {
        List<Teacher> filteredTeachers = manageTeacherService.filterTeachers("teacher1", "John", "CSE");
        assertEquals(1, filteredTeachers.size(), "Filter should return 1 teacher.");
        assertEquals("teacher1", filteredTeachers.get(0).getUsername(), "Filtered teacher's username should be 'teacher1'.");

        filteredTeachers = manageTeacherService.filterTeachers("teacher3", "", "");
        assertTrue(filteredTeachers.isEmpty(), "Filter should return no teachers for non-existent username.");
    }


    @Test
    void testValidateInputs() {
        String validationMessage = manageTeacherService.validateInputs(
                "teacher3", "Michael Brown", "Male", "30", "Assistant Professor", "EE", "password789");
        assertNull(validationMessage, "Valid inputs should return no validation error.");

        validationMessage = manageTeacherService.validateInputs("", "Michael Brown", "Male", "30", "Assistant Professor", "EE", "password789");
        assertEquals("All fields must be filled.", validationMessage, "Empty username should trigger validation error.");

        validationMessage = manageTeacherService.validateInputs("teacher1", "Michael Brown", "Male", "30", "Assistant Professor", "EE", "password789");
        assertEquals("Username already exists.", validationMessage, "Duplicate username should trigger validation error.");

        validationMessage = manageTeacherService.validateInputs("teacher3", "Michael Brown", "Male", "thirty", "Assistant Professor", "EE", "password789");
        assertEquals("Age must be a number.", validationMessage, "Non-numeric age should trigger validation error.");

        validationMessage = manageTeacherService.validateInputs("teacher3", "Michael Brown", "Male", "30", "Assistant Professor", "EE", "pass");
        assertEquals("Password must be at least 8 characters, containing letters and numbers.", validationMessage, "Invalid password should trigger validation error.");
    }

    @Test
    void testValidateUpdateInputs() {
        String validationMessage = manageTeacherService.validateUpdateInputs(
                "Michael Brown Updated", "Male", "31", "Associate Professor", "EE", "newPassword123");
        assertNull(validationMessage, "Valid update inputs should return no validation error.");

        validationMessage = manageTeacherService.validateUpdateInputs("", "Male", "31", "Associate Professor", "EE", "newPassword123");
        assertEquals("All fields must be filled.", validationMessage, "Empty name should trigger validation error.");

        validationMessage = manageTeacherService.validateUpdateInputs("Michael Brown Updated", "Male", "thirty-one", "Associate Professor", "EE", "newPassword123");
        assertEquals("Age must be a number.", validationMessage, "Non-numeric age should trigger validation error.");

        validationMessage = manageTeacherService.validateUpdateInputs("Michael Brown Updated", "Male", "31", "Associate Professor", "EE", "short");
        assertEquals("Password must be at least 8 characters, containing letters and numbers.", validationMessage, "Invalid password should trigger validation error.");
    }

    // MockDataManager 实现
    class MockDataManager extends DataManager {
        private List<Teacher> mockTeachers;
        private List<Question> mockQuestions;

        public MockDataManager() {
            this.mockTeachers = new ArrayList<>();
            this.mockQuestions = new ArrayList<>();
        }

        @Override
        public List<Teacher> getTeachers() {
            return mockTeachers;
        }

        @Override
        public void addTeacher(Teacher teacher) {
            mockTeachers.add(teacher);
        }

        @Override
        public void saveTeachers() {
            // 模拟保存操作，不进行实际的文件写入
        }

        @Override
        public Teacher getTeacherByUsername(String username) {
            return mockTeachers.stream()
                    .filter(t -> t.getUsername().equals(username))
                    .findFirst()
                    .orElse(null);
        }

        @Override
        public List<Question> getQuestions() {
            return mockQuestions;
        }

        @Override
        public void saveQuestions() {
            // 模拟保存操作，不进行实际的文件写入
        }

        // 根据需要重写其他方法，避免访问真实的数据文件
        @Override
        public void save() {
            // 模拟保存操作
        }

        // 如果在测试中需要其他数据，可以在此处添加
    }
}
