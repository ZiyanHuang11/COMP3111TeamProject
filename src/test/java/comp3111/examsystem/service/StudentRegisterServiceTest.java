package comp3111.examsystem.service;

import comp3111.examsystem.data.DataManager;
import comp3111.examsystem.entity.Student;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

public class StudentRegisterServiceTest {

    private StudentRegisterService registerService;
    private DataManager dataManager;

    @BeforeEach
    public void setUp() {
        // 使用 MockDataManager
        dataManager = new MockDataManager();
        registerService = new StudentRegisterService(dataManager);

        // 添加测试数据
        dataManager.addStudent(new Student("existing_user", "John Doe", 20, "Male", "Computer Science", "password123"));
    }

    @Test
    public void testRegisterStudentSuccess() {
        // 测试成功注册
        assertDoesNotThrow(() -> {
            registerService.registerStudent("new_user", "new_password", "Jane Smith", "Female", "Mathematics");
        });

        List<Student> students = dataManager.getStudents();
        assertEquals(2, students.size(), "There should be 2 students after registration");
        Student newStudent = students.stream()
                .filter(student -> "new_user".equals(student.getUsername()))
                .findFirst()
                .orElse(null);
        assertNotNull(newStudent, "The new user should exist in the student list");
        assertEquals("Jane Smith", newStudent.getName(), "Name should match");
        assertEquals("Female", newStudent.getGender(), "Gender should match");
        assertEquals("Mathematics", newStudent.getDepartment(), "Department should match");
        assertEquals("new_password", newStudent.getPassword(), "Password should match");
    }

    @Test
    public void testRegisterStudentDuplicateUsername() {
        // 测试重复用户名
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            registerService.registerStudent("existing_user", "new_password", "Jane Smith", "Female", "Mathematics");
        });
        assertEquals("Username already exists", exception.getMessage(), "Exception message should indicate duplicate username");
    }

    @Test
    public void testRegisterStudentEmptyFields() {
        // 测试空字段注册
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            registerService.registerStudent("", "password", "Jane Smith", "Female", "Mathematics");
        });
        assertEquals("All fields are required", exception.getMessage(), "Exception message should indicate missing fields");
    }

    // MockDataManager 实现
    class MockDataManager extends DataManager {
        private List<Student> mockStudents;

        public MockDataManager() {
            this.mockStudents = new ArrayList<>();
        }

        @Override
        public List<Student> getStudents() {
            return mockStudents;
        }

        @Override
        public void addStudent(Student student) {
            mockStudents.add(student);
        }

        @Override
        public void saveStudents() {
            // 不执行实际的文件保存操作
        }

        @Override
        public Student getStudentByUsername(String username) {
            return mockStudents.stream()
                    .filter(s -> s.getUsername().equals(username))
                    .findFirst()
                    .orElse(null);
        }

        // 根据需要重写其他方法，防止测试访问真实的数据文件
        @Override
        public void save() {
            // 模拟保存操作
        }
    }
}


