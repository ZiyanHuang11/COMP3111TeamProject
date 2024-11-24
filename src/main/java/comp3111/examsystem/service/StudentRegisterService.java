package comp3111.examsystem.service;

import java.io.*;

public class StudentRegisterService {
    private String studentFilePath;

    // 默认构造函数
    public StudentRegisterService() {
        this.studentFilePath = "data/students.txt";
    }

    // 接收文件路径的构造函数
    public StudentRegisterService(String studentFilePath) {
        this.studentFilePath = studentFilePath;
    }

    public void setStudentFilePath(String studentFilePath) {
        this.studentFilePath = studentFilePath;
    }

    public boolean isUsernameTaken(String username) throws IOException {
        File file = new File(studentFilePath);

        // 检查文件是否存在
        if (!file.exists()) {
            throw new IOException("Student file not found");
        }

        // 读取文件并检查用户名是否存在
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] credentials = line.split(",");
                if (credentials.length > 0 && credentials[0].trim().equals(username)) {
                    return true;
                }
            }
        }
        return false;
    }

    public void registerStudent(String username, String password, String name, String gender, String department) throws IOException {
        File file = new File(studentFilePath);

        // 检查文件是否存在
        if (!file.exists()) {
            throw new IOException("Student file not found");
        }

        // 检查用户名是否已存在
        if (isUsernameTaken(username)) {
            throw new IOException("Username already exists");
        }

        // 将新学生信息写入文件
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file, true))) {
            String newStudent = String.format("%s,%s,%d,%s,%s,%s\n", username, name, 0, gender, department, password);
            writer.write(newStudent);
        }
    }
}
