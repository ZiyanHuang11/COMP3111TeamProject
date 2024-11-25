package comp3111.examsystem.service;

import comp3111.examsystem.data.DataManager;
import comp3111.examsystem.entity.Teacher;

import java.util.List;

public class TeacherLoginService {
    private final DataManager dataManager;

    // 使用 DataManager 初始化
    public TeacherLoginService(DataManager dataManager) {
        this.dataManager = dataManager;
    }

    /**
     * 验证教师的用户名和密码
     *
     * @param username 教师用户名
     * @param password 教师密码
     * @return 如果用户名和密码匹配，返回 true；否则返回 false
     */
    public boolean validate(String username, String password) {
        List<Teacher> teachers = dataManager.getTeachers();

        for (Teacher teacher : teachers) {
            if (teacher.getUsername().equals(username) && teacher.getPassword().equals(password)) {
                return true;
            }
        }

        return false; // 无匹配的用户名和密码
    }
}
