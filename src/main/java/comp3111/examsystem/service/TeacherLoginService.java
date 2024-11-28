package comp3111.examsystem.service;

import comp3111.examsystem.entity.Teacher;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class TeacherLoginService {
    private String teacherFilePath;

    public TeacherLoginService(String teacherFilePath) {
        this.teacherFilePath = teacherFilePath;
    }

    /**
     * Validates the teacher's username and password by checking against the stored credentials.
     *
     * @param username The username entered by the teacher.
     * @param password The password entered by the teacher.
     * @return Teacher object if the credentials are valid; null otherwise.
     */
    public Teacher validate(String username, String password) {
        if (username == null || password == null) {
            return null; // 或者根据需求抛出异常
        }

        try (BufferedReader br = new BufferedReader(new FileReader(teacherFilePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] data = line.split(",");
                if (data.length >= 7) { // 确保至少包含7个字段
                    String storedUsername = data[0].trim();
                    String storedPassword = data[1].trim();
                    if (storedUsername.equals(username) && storedPassword.equals(password)) {
                        try {
                            String name = data[2].trim();
                            String gender = data[3].trim();
                            int age = Integer.parseInt(data[4].trim());
                            String position = data[5].trim();
                            String department = data[6].trim();
                            String courseid1 = data.length > 7 ? data[7].trim() : "";
                            String courseid2 = data.length > 8 ? data[8].trim() : "";
                            Teacher teacher = new Teacher(storedUsername, storedPassword, name, gender, age, position, department, courseid1, courseid2);
                            return teacher;
                        } catch (NumberFormatException e) {
                            // 日志记录或其他处理方式
                            System.err.println("Invalid age format for user: " + username);
                            return null;
                        }
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            // 适当处理异常，如记录日志
        }
        return null; // 未找到匹配的凭据
    }
}
