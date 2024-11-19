package comp3111.examsystem.service;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

public class TeacherRegisterService {
    private String teacherFilePath;

    public TeacherRegisterService(String teacherFilePath) {
        this.teacherFilePath = teacherFilePath;
    }

    /**
     * Validates the registration inputs.
     *
     * @param username        The username entered.
     * @param name            The name entered.
     * @param gender          The gender selected.
     * @param ageText         The age entered as text.
     * @param position        The position selected.
     * @param department      The department entered.
     * @param password        The password entered.
     * @param confirmPassword The password confirmation entered.
     * @return null if all inputs are valid; otherwise, an error message.
     */
    public String validateInputs(String username, String name, String gender, String ageText, String position,
                                 String department, String password, String confirmPassword) {
        if (username.isEmpty() || name.isEmpty() || gender == null || ageText.isEmpty() ||
                position == null || department.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
            return "All fields are required.";
        }

        if ("Gender".equals(gender) || "Position".equals(position)) {
            return "Please select your gender and position.";
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
        try (BufferedReader br = new BufferedReader(new FileReader(teacherFilePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] credentials = line.split(",");
                if (credentials.length > 0) {
                    String storedUsername = credentials[0].trim();
                    if (storedUsername.equals(username)) {
                        return true; // User already exists
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            // Handle exception appropriately, possibly rethrow or log
        }
        return false; // User does not exist
    }

    /**
     * Registers a new teacher by writing their information to the file.
     *
     * @param teacherInfo A map containing the teacher's information.
     * @throws IOException If an I/O error occurs.
     */
    public void registerTeacher(Map<String, String> teacherInfo) throws IOException {
        File file = new File(teacherFilePath);
        if (!file.exists()) {
            file.createNewFile();
        }

        try (FileWriter writer = new FileWriter(file, true)) {
            // Build the line to write to the file
            StringBuilder sb = new StringBuilder();
            sb.append(teacherInfo.get("username")).append(",");
            sb.append(teacherInfo.get("password")).append(",");
            sb.append(teacherInfo.get("name")).append(",");
            sb.append(teacherInfo.get("gender")).append(",");
            sb.append(teacherInfo.get("age")).append(",");
            sb.append(teacherInfo.get("position")).append(",");
            sb.append(teacherInfo.get("department")).append("\n");

            writer.write(sb.toString());
        }
    }
}
