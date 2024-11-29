package comp3111.examsystem.service;

import java.io.*;
import java.util.*;
import java.util.regex.Pattern;

public class StudentRegisterService {

    private final String studentFilePath;
    private List<String> cachedUsernames; // Cached usernames for efficiency
    private List<String> cachedNames; // Cached names to check for duplicates

    public StudentRegisterService(String studentFilePath) {
        this.studentFilePath = studentFilePath;
        this.cachedUsernames = new ArrayList<>();
        this.cachedNames = new ArrayList<>();

        // Ensure the file exists; create a new one if it doesn't
        File studentFile = new File(studentFilePath);
        if (!studentFile.exists()) {
            try {
                studentFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        // Load existing data into memory for faster checks
        loadExistingUsers();
    }

    // Load existing usernames and names from the student file
    private void loadExistingUsers() {
        try (BufferedReader reader = new BufferedReader(new FileReader(studentFilePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length >= 6) {  // Ensure each record has enough fields
                    cachedUsernames.add(parts[0].trim());
                    cachedNames.add(parts[1].trim());
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Validate the inputs for registration
    public String validateInputs(String username, String name, String gender, String ageText, String department, String password, String confirmPassword) {
        if (username.isEmpty() || name.isEmpty() || gender == null || ageText.isEmpty() || department.isEmpty() || password.isEmpty()) {
            return "All fields are required.";
        }

        if (isInvalidUsername(username)) {
            return "Username cannot be numeric.";
        }

        if (isInvalidName(name)) {
            return "Name cannot be numeric.";
        }

        if (isUserExists(username)) {
            return "Username already exists!";
        }

        if (isNameExists(name)) {
            return "Name already exists!";
        }

        if (isInvalidAge(ageText)) {
            return "Age must be a whole number between 1 and 60.";
        }

        if (isInvalidPassword(password)) {
            return "Password must be at least 8 characters long.";
        }

        if (!password.equals(confirmPassword)) {
            return "Passwords do not match.";
        }

        return null;  // No errors
    }

    private boolean isInvalidUsername(String username) {
        return username.matches("\\d+");  // Check if username is numeric
    }

    private boolean isInvalidName(String name) {
        return name.matches("\\d+");  // Check if name is numeric
    }

    private boolean isInvalidAge(String ageText) {
        try {
            int age = Integer.parseInt(ageText);
            return age < 1 || age > 60;
        } catch (NumberFormatException e) {
            return true;  // Age is not a valid integer
        }
    }

    private boolean isInvalidPassword(String password) {
        return password.length() < 8;  // Password must be at least 8 characters
    }

    public boolean isUserExists(String username) {
        return cachedUsernames.contains(username);
    }

    private boolean isNameExists(String name) {
        return cachedNames.contains(name);
    }

    // Register student by saving data to file
    public void registerStudent(Map<String, String> studentInfo) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(studentFilePath, true))) {
            String studentData = String.join(",", studentInfo.values());
            writer.write(studentData);
            writer.newLine();
        }
        // Update the cached usernames and names for future checks
        cachedUsernames.add(studentInfo.get("username"));
        cachedNames.add(studentInfo.get("name"));
    }
}
