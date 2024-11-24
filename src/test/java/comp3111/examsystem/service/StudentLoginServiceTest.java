package comp3111.examsystem.service;

import comp3111.examsystem.data.DataManager;
import comp3111.examsystem.entity.Student;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class StudentLoginServiceTest {

    private StudentLoginService studentLoginService;
    private DataManager mockDataManager;

    @BeforeEach
    public void setUp() {
        // 创建模拟学生数据
        List<Student> mockStudents = new ArrayList<>();
        mockStudents.add(new Student("1", "john_doe", "John Doe", 20, "Male", "Computer Science", "password123"));
        mockStudents.add(new Student("2", "jane_smith", "Jane Smith", 22, "Female", "Business", "password456"));
        mockStudents.add(new Student("3", "alice_johnson", "Alice Johnson", 33, "Female", "Mathematics", "password789"));

        // 模拟 DataManager 的行为
        mockDataManager = new DataManager() {
            @Override
            public List<Student> getStudents() {
                return mockStudents;
            }
        };

        // 使用模拟的 DataManager 初始化服务
        studentLoginService = new StudentLoginService(mockDataManager);
    }

    @Test
    public void testValidateLoginSuccess() {
        // 验证正确的用户名和密码
        assertDoesNotThrow(() -> {
            boolean isValid = studentLoginService.validateLogin("john_doe", "password123");
            assertTrue(isValid, "Login should be successful for valid credentials");
        });
    }

    @Test
    public void testValidateLoginIncorrectPassword() {
        // 验证用户名正确但密码错误
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            studentLoginService.validateLogin("john_doe", "wrong_password");
        });
        assertEquals("Incorrect password", exception.getMessage(), "Should throw 'Incorrect password' exception");
    }

    @Test
    public void testValidateLoginUsernameNotFound() {
        // 验证用户名不存在
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            studentLoginService.validateLogin("nonexistent_user", "password123");
        });
        assertEquals("Username not found", exception.getMessage(), "Should throw 'Username not found' exception");
    }

    @Test
    public void testValidateLoginWithEmptyFields() {
        // 验证空的用户名或密码
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            studentLoginService.validateLogin("", "password123");
        });
        assertEquals("Username not found", exception.getMessage(), "Empty username should throw 'Username not found'");

        exception = assertThrows(IllegalArgumentException.class, () -> {
            studentLoginService.validateLogin("john_doe", "");
        });
        assertEquals("Incorrect password", exception.getMessage(), "Empty password should throw 'Incorrect password'");
    }
}
