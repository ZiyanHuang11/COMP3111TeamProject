package comp3111.examsystem.service;

import java.io.*;

public class StudentRegisterService {
    private String studentFilePath;

    public StudentRegisterService() {
        this.studentFilePath = "data/students.txt";
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
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(studentFilePath, true))) {
            String newStudent = String.format("%s,%s,%d,%s,%s,%s\n", username, name, 0, gender, department, password);
            writer.write(newStudent);
        }
    }
}

