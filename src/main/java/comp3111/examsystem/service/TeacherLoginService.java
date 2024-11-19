package comp3111.examsystem.service;

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
     * @return true if the credentials are valid; false otherwise.
     */
    public boolean validate(String username, String password) {
        try (BufferedReader br = new BufferedReader(new FileReader(teacherFilePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                // Assuming the format: username,password
                String[] credentials = line.split(",");
                if (credentials.length >= 2) {
                    String storedUsername = credentials[0].trim();
                    String storedPassword = credentials[1].trim();
                    if (storedUsername.equals(username) && storedPassword.equals(password)) {
                        return true;
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            // Handle exception appropriately, possibly rethrow or log
        }
        return false; // No matching credentials found
    }
}
