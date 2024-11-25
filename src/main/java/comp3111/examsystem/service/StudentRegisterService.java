package comp3111.examsystem.service;

import java.io.*;
import java.util.*;

public class StudentRegisterService {

    private final String studentFilePath;
    private List<String> cachedUsernames; // Cached usernames for efficiency

    public StudentRegisterService(String studentFilePath) {
        this.studentFilePath = studentFilePath;

        // Ensure the file exists; create a new one if it doesn't
        File studentFile = new File(studentFilePath);
        if (!studentFile.exists()) {
            try {
                studentFile.createNewFile();
                System.out.println("Student file created at: " + studentFile.getAbsolutePath());
            } catch (IOException e) {
                e.printStackTrace();
                System.err.println("Failed to create student file");
            }
        }
    }

    /**
     * Validates the registration inputs.
     *
     * @param username        The username entered.
     * @param name            The name entered.
     * @param gender          The gender selected.
     * @param ageText         The age entered as text.
     * @param department      The department entered.
     * @param password        The password entered.
     * @param confirmPassword The password confirmation entered.
     * @return null if all inputs are valid; otherwise, an error message.
     */
    public String validateInputs(String username, String name, String gender, String ageText,
                                 String department, String password, String confirmPassword) {
        String[] fields = {username, name, gender, ageText, department, password, confirmPassword};
        for (String field : fields) {
            if (field == null || field.isEmpty()) {
                return "All fields are required.";
            }
        }

        if ("Gender".equals(gender)) {
            return "Please select your gender.";
        }

        if (!password.equals(confirmPassword)) {
            return "Passwords do not match.";
        }

        try {
            Integer.parseInt(ageText);
        } catch (NumberFormatException e) {
            return "Age must be a valid number.";
        }

        return null; // All inputs are valid
    }

    /**
     * Checks if a user with the given username already exists.
     *
     * @param username The username to check.
     * @return true if the user exists; false otherwise.
     */
    public boolean isUserExists(String username) {
        if (cachedUsernames == null) {
            cachedUsernames = loadAllUsernames();
        }
        return cachedUsernames.contains(username);
    }

    private List<String> loadAllUsernames() {
        List<String> usernames = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(studentFilePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] credentials = line.split(",");
                if (credentials.length > 0) {
                    usernames.add(credentials[0].trim());
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return usernames;
    }

    /**
     * Registers a new student by writing their information to the file.
     *
     * @param studentInfo A map containing the student's information.
     * @throws IOException If an I/O error occurs.
     */
    public void registerStudent(Map<String, String> studentInfo) throws IOException {
        try (FileWriter writer = new FileWriter(studentFilePath, true)) {
            String record = String.join(",",
                    studentInfo.get("username"),
                    studentInfo.get("name"),
                    studentInfo.get("age"),
                    studentInfo.get("gender"),
                    studentInfo.get("department"),
                    studentInfo.get("password")) + "\n";
            writer.write(record);

            // Update cached usernames
            if (cachedUsernames != null) {
                cachedUsernames.add(studentInfo.get("username"));
            }
        }
    }

    /**
     * Retrieves all students' data from the file.
     *
     * @return A map where the key is the username and the value is the full record as a string.
     */
    public Map<String, String> getAllStudents() {
        Map<String, String> students = new HashMap<>();
        try (BufferedReader br = new BufferedReader(new FileReader(studentFilePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] credentials = line.split(",");
                if (credentials.length >= 6) {
                    students.put(credentials[0].trim(), line);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return students;
    }
}
