package comp3111.examsystem.service;
import comp3111.examsystem.service.StudentLoginService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

class StudentLoginServiceTest {

    private StudentLoginService studentLoginService;
    private final String testFilePath = "data/test_students.txt";

    @BeforeEach
    public void setUp() throws IOException {
        // 创建测试数据文件
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(testFilePath))) {
            writer.write("john_doe,John Doe,20,Male,Computer Science,password123\n");
            writer.write("jane_smith,Jane Smith,22,Female,Business,password456\n");
            writer.write("alice_johnson,Alice Johnson,33,Female,Mathematics,password789\n");
        }

        // 初始化服务类
        studentLoginService = new StudentLoginService(testFilePath);
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
    public void testFileNotFound() {
        StudentLoginService invalidService = new StudentLoginService() {
            @Override
            public boolean validateLogin(String username, String password) {
                throw new IllegalArgumentException("Error reading student file");
            }
        };
        Exception exception = assertThrows(IllegalArgumentException.class,
                () -> invalidService.validateLogin("user", "pass"));
        assertTrue(exception.getMessage().contains("Error reading student file"));
    }
}

