package comp3111.examsystem.service;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class StudentLoginService {
    private String studentFilePath;

    public StudentLoginService() {
        this.studentFilePath = "data/students.txt";
    }

    public boolean validateLogin(String username, String password) throws IllegalArgumentException {
        try (BufferedReader br = new BufferedReader(new FileReader(studentFilePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] credentials = line.split(",");
                if (credentials.length >= 6) {
                    String storedUsername = credentials[0].trim();
                    String storedPassword = credentials[5].trim();
                    if (storedUsername.equals(username)) {
                        if (storedPassword.equals(password)) {
                            return true;
                        } else {
                            throw new IllegalArgumentException("Incorrect password");
                        }
                    }
                }
            }
            throw new IllegalArgumentException("Username not found");
        } catch (IOException e) {
            e.printStackTrace();
            throw new IllegalArgumentException("Error reading student file");
        }
    }
}

