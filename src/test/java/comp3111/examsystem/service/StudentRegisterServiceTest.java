package comp3111.examsystem.service;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.*;

public class StudentRegisterServiceTest {

    private final String testFilePath = "data/test_students.txt";
    private StudentRegisterService studentRegisterService;

    @BeforeEach
    public void setUp() throws IOException {
        // 创建测试文件
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(testFilePath))) {
            writer.write("john_doe,John Doe,20,Male,Computer Science,password123\n");
            writer.write("jane_smith,Jane Smith,22,Female,Business,password456\n");
        }

        // 初始化服务类
        studentRegisterService = new StudentRegisterService(testFilePath);
    }

    @AfterEach
    public void tearDown() throws IOException {
        // 删除测试文件
        Files.deleteIfExists(Paths.get(testFilePath));
    }

    @Test
    public void testIsUsernameTaken() {
        try {
            assertTrue(studentRegisterService.isUsernameTaken("john_doe"), "Username 'john_doe' should be taken");
            assertTrue(studentRegisterService.isUsernameTaken("jane_smith"), "Username 'jane_smith' should be taken");
            assertFalse(studentRegisterService.isUsernameTaken("new_user"), "Username 'new_user' should not be taken");
        } catch (IOException e) {
            fail("IOException was thrown during testIsUsernameTaken: " + e.getMessage());
        }
    }

    @Test
    public void testRegisterStudentSuccess() throws IOException {
        String newUsername = "alice_johnson";
        String password = "password789";
        String name = "Alice Johnson";
        String gender = "Female";
        String department = "Mathematics";

        // 注册新用户
        studentRegisterService.registerStudent(newUsername, password, name, gender, department);

        // 验证新用户是否成功注册
        assertTrue(studentRegisterService.isUsernameTaken(newUsername), "Username 'alice_johnson' should be taken after registration");

        // 验证文件中是否包含新用户
        try (BufferedReader reader = new BufferedReader(new FileReader(testFilePath))) {
            boolean userFound = false;
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.startsWith(newUsername)) {
                    userFound = true;
                    break;
                }
            }
            assertTrue(userFound, "Newly registered user should exist in the file");
        }
    }

    @Test
    public void testRegisterExistingUsername() {
        String existingUsername = "john_doe";

        // 尝试注册已存在的用户名
        Exception exception = assertThrows(IOException.class, () -> {
            studentRegisterService.registerStudent(existingUsername, "new_password", "John Updated", "Male", "Updated Department");
        });

        assertEquals("Username already exists", exception.getMessage(), "Should throw 'Username already exists' exception");
    }

    @Test
    public void testFileNotFoundDuringUsernameCheck() {
        // 使用不存在的文件路径
        String nonexistentFilePath = "data/nonexistent_file.txt";
        StudentRegisterService invalidService = new StudentRegisterService(nonexistentFilePath);

        // 确保文件路径上没有文件
        File file = new File(nonexistentFilePath);
        if (file.exists()) {
            file.delete();
        }

        Exception exception = assertThrows(IOException.class, () -> {
            invalidService.isUsernameTaken("nonexistent_user");
        });

        assertEquals("Student file not found", exception.getMessage());
    }

    @Test
    public void testFileNotFoundDuringRegistration() {
        // 使用不存在的文件路径
        String nonexistentFilePath = "data/nonexistent_file.txt";
        StudentRegisterService invalidService = new StudentRegisterService(nonexistentFilePath);

        // 确保文件路径上没有文件
        File file = new File(nonexistentFilePath);
        if (file.exists()) {
            file.delete();
        }

        Exception exception = assertThrows(IOException.class, () -> {
            invalidService.registerStudent("new_user", "password", "New User", "Male", "New Department");
        });

        assertEquals("Student file not found", exception.getMessage());
    }
}
