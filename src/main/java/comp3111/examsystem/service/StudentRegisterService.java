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

    public boolean isUsernameTaken(String username) {
        try (BufferedReader reader = new BufferedReader(new FileReader(studentFilePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] credentials = line.split(",");
                if (credentials.length > 0 && credentials[0].trim().equals(username)) {
                    return true;
                }
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return false;
    }

    public void registerStudent(String username, String password, String name, String gender, String department) throws IOException {
        if (isUsernameTaken(username)) {
            throw new IOException("Username already exists");
        }
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(studentFilePath, true))) {
            String newStudent = String.format("%s,%s,%d,%s,%s,%s\n", username, name, 0, gender, department, password);
            writer.write(newStudent);
        }
    }
}

