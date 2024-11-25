package comp3111.examsystem.service;

import comp3111.examsystem.data.DataManager;
import comp3111.examsystem.entity.Manager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ManagerLoginServiceTest {

    private ManagerLoginService managerLoginService;

    // 使用 Mock 的 DataManager
    private static class MockDataManager extends DataManager {
        private final List<Manager> mockManagers = new ArrayList<>();

        @Override
        public List<Manager> getManagers() {
            return mockManagers;
        }

        public void addMockManager(String id, String username, String password) {
            Manager manager = new Manager(id, username, password); // 确保匹配 Manager 的构造函数
            mockManagers.add(manager);
        }
    }

    private MockDataManager mockDataManager;

    @BeforeEach
    public void setUp() {
        // 初始化 Mock DataManager
        mockDataManager = new MockDataManager();
        mockDataManager.addMockManager("1", "admin", "admin123");
        mockDataManager.addMockManager("2", "manager1", "pass123");
        mockDataManager.addMockManager("3", "manager2", "123456");

        // 初始化 ManagerLoginService
        managerLoginService = new ManagerLoginService(mockDataManager);
    }

    @Test
    public void testValidateLoginSuccess() {
        // 验证正确的用户名和密码
        assertTrue(managerLoginService.validate("admin", "admin123"),
                "Login should succeed for valid credentials");
        assertTrue(managerLoginService.validate("manager1", "pass123"),
                "Login should succeed for valid credentials");
    }

    @Test
    public void testValidateLoginInvalidPassword() {
        // 验证用户名正确但密码错误
        assertFalse(managerLoginService.validate("admin", "wrongpassword"),
                "Login should fail for invalid password");
        assertFalse(managerLoginService.validate("manager2", "wrongpass"),
                "Login should fail for invalid password");
    }

    @Test
    public void testValidateLoginNonExistentUsername() {
        // 验证不存在的用户名
        assertFalse(managerLoginService.validate("nonexistent", "password"),
                "Login should fail for nonexistent username");
    }

    @Test
    public void testValidateLoginEmptyUsernameOrPassword() {
        // 验证空用户名或密码
        assertFalse(managerLoginService.validate("", "admin123"),
                "Login should fail for empty username");
        assertFalse(managerLoginService.validate("admin", ""),
                "Login should fail for empty password");
        assertFalse(managerLoginService.validate("", ""),
                "Login should fail for empty username and password");
    }
}
