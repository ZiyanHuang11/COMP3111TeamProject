package comp3111.examsystem.service;

import comp3111.examsystem.data.DataManager;
import comp3111.examsystem.entity.Student;

import java.util.List;

public class StudentLoginService {
    private final DataManager dataManager;

    // 构造函数：通过 DataManager 加载学生数据
    public StudentLoginService(DataManager dataManager) {
        this.dataManager = dataManager;
    }

    /**
     * 验证学生登录
     *
     * @param username 用户名
     * @param password 密码
     * @return 如果用户名和密码匹配，返回 true，否则抛出异常
     * @throws IllegalArgumentException 如果用户名不存在或密码错误
     */
    public boolean validateLogin(String username, String password) throws IllegalArgumentException {
        // 从 DataManager 获取学生列表
        List<Student> students = dataManager.getStudents();

        for (Student student : students) {
            if (student.getUsername().equals(username)) {
                if (student.getPassword().equals(password)) {
                    return true; // 验证成功
                } else {
                    throw new IllegalArgumentException("Incorrect password");
                }
            }
        }

        // 如果未找到匹配的用户名
        throw new IllegalArgumentException("Username not found");
    }
}






