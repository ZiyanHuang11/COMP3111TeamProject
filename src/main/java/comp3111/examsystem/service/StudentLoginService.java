package comp3111.examsystem.service;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Service class for validating student login credentials.
 */
public class StudentLoginService {
    private static final Logger LOGGER = Logger.getLogger(StudentLoginService.class.getName());
    private final String studentFilePath;

    /**
     * Default constructor that initializes the student file path.
     */
    public StudentLoginService() {
        this.studentFilePath = "data/students.txt"; // 默认文件路径
    }

    /**
     * Constructor with custom file path.
     *
     * @param studentFilePath Path to the student data file.
     */
    public StudentLoginService(String studentFilePath) {
        this.studentFilePath = studentFilePath;
    }

    /**
     * Validates the login credentials of a student.
     *
     * @param username The username to validate.
     * @param password The password to validate.
     * @return True if the credentials are valid, otherwise throws an exception.
     * @throws IllegalArgumentException if the username or password is incorrect or file errors occur.
     */
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
                            LOGGER.log(Level.INFO, "Login successful for username: {0}", username);
                            return true;
                        } else {
                            LOGGER.log(Level.WARNING, "Incorrect password for username: {0}", username);
                            throw new IllegalArgumentException("Incorrect password");
                        }
                    }
                }
            }
            LOGGER.log(Level.WARNING, "Username not found: {0}", username);
            throw new IllegalArgumentException("Username not found");
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "Error reading student file", e);
            throw new IllegalArgumentException("Error reading student file");
        }
    }
}


