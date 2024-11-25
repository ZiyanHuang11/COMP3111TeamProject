package comp3111.examsystem.service;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class StudentLoginService {
    private final String studentFilePath;

    public StudentLoginService(String studentFilePath) {
        this.studentFilePath = studentFilePath;

        // 确保文件存在，如果不存在则提示警告
        File studentFile = new File(studentFilePath);
        if (!studentFile.exists()) {
            System.err.println("Warning: Student file not found at: " + studentFile.getAbsolutePath());
        }
    }

    /**
     * 验证用户名和密码是否有效
     *
     * @param username 用户输入的用户名
     * @param password 用户输入的密码
     * @return 如果用户名和密码匹配，则返回 true；否则返回 false
     */
    public boolean validateLogin(String username, String password) {
        try (BufferedReader br = new BufferedReader(new FileReader(studentFilePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                // 假设文件中的格式为: username,name,gender,age,department,password
                String[] credentials = line.split(",");
                if (credentials.length >= 6) {
                    String storedUsername = credentials[0].trim();
                    String storedPassword = credentials[5].trim();
                    if (storedUsername.equals(username) && storedPassword.equals(password)) {
                        return true;
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false; // 没有找到匹配的用户名和密码
    }
}


