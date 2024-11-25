package comp3111.examsystem.service;

import comp3111.examsystem.data.DataManager;
import comp3111.examsystem.entity.Teacher;
import org.junit.jupiter.api.*;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class TeacherLoginServiceTest {

    private TeacherLoginService loginService;
    private DataManager mockDataManager;

    @BeforeEach
    public void setUp() {
        // 创建模拟的 DataManager
        mockDataManager = new DataManager() {
            @Override
            public List<Teacher> getTeachers() {
                // 返回模拟的教师数据
                List<Teacher> teachers = new ArrayList<>();
                teachers.add(new Teacher("1", "teacher1", "password1", "John", "Male", 30, "Professor", "CSE"));
                teachers.add(new Teacher("2", "teacher2", "password2", "Jane", "Female", 35, "Lecturer", "IT"));
                return teachers;
            }
        };

        // 使用模拟的 DataManager 初始化服务
        loginService = new TeacherLoginService(mockDataManager);
    }

    @Test
    public void testValidateValidCredentials() {
        // 验证正确的用户名和密码
        assertTrue(loginService.validate("teacher1", "password1"), "Valid credentials should pass validation");
        assertTrue(loginService.validate("teacher2", "password2"), "Valid credentials should pass validation");
    }

    @Test
    public void testValidateInvalidCredentials() {
        // 验证用户名或密码错误
        assertFalse(loginService.validate("teacher1", "wrongpassword"), "Invalid password should fail validation");
        assertFalse(loginService.validate("invaliduser", "password1"), "Invalid username should fail validation");
    }

    @Test
    public void testValidateEmptyCredentials() {
        // 验证空用户名和密码
        assertFalse(loginService.validate("", ""), "Empty credentials should fail validation");
    }

    @Test
    public void testValidateNullCredentials() {
        // 验证 null 用户名和密码
        assertFalse(loginService.validate(null, null), "Null credentials should fail validation");
    }
}
