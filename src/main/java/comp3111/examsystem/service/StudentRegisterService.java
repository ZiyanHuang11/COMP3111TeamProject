package comp3111.examsystem.service;

import comp3111.examsystem.data.DataManager;
import comp3111.examsystem.entity.Student;

import java.util.List;

public class StudentRegisterService {
    private final DataManager dataManager;

    // 构造函数，注入 DataManager
    public StudentRegisterService(DataManager dataManager) {
        this.dataManager = dataManager;
    }

    /**
     * 检查用户名是否已存在
     *
     * @param username 用户名
     * @return 如果用户名已存在，返回 true，否则返回 false
     */
    public boolean isUsernameTaken(String username) {
        List<Student> students = dataManager.getStudents();
        for (Student student : students) {
            if (student.getUsername().equals(username)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 注册新学生
     *
     * @param username   用户名
     * @param password   密码
     * @param name       姓名
     * @param gender     性别
     * @param department 所属学院
     */
    public void registerStudent(String username, String password, String name, String gender, String department) {
        // 检查空字段
        if (username == null || username.isEmpty() ||
                password == null || password.isEmpty() ||
                name == null || name.isEmpty() ||
                gender == null || gender.isEmpty() ||
                department == null || department.isEmpty()) {
            throw new IllegalArgumentException("All fields are required");
        }

        if (isUsernameTaken(username)) {
            throw new IllegalArgumentException("Username already exists");
        }

        // 创建新学生实体并添加到 DataManager
        Student newStudent = new Student();
        newStudent.setUsername(username);
        newStudent.setPassword(password);
        newStudent.setName(name);
        newStudent.setGender(gender);
        newStudent.setDepartment(department);

        dataManager.addStudent(newStudent); // 使用 DataManager 的 addStudent 方法
        dataManager.saveStudents(); // 保存更改
    }
}

