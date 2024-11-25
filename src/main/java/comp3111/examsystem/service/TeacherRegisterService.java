package comp3111.examsystem.service;

import comp3111.examsystem.data.DataManager;
import comp3111.examsystem.entity.Teacher;

import java.util.Optional;

public class TeacherRegisterService {
    private final DataManager dataManager;

    public TeacherRegisterService(DataManager dataManager) {
        this.dataManager = dataManager;
    }

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

    public boolean isUserExists(String username) {
        return dataManager.getTeachers().stream().anyMatch(teacher -> teacher.getUsername().equals(username));
    }

    public Optional<Teacher> prepareTeacher(String username, String name, String gender, String ageText,
                                            String position, String department, String password) {
        try {
            int age = Integer.parseInt(ageText);
            Teacher teacher = new Teacher();
            teacher.setUsername(username);
            teacher.setName(name);
            teacher.setGender(gender);
            teacher.setAge(age);
            teacher.setTitle(position);
            teacher.setDepartment(department);
            teacher.setPassword(password);

            return Optional.of(teacher);
        } catch (NumberFormatException e) {
            e.printStackTrace();
            return Optional.empty();
        }
    }

    public void registerTeacher(Teacher teacher) {
        dataManager.getTeachers().add(teacher);
        dataManager.saveTeachers(); // Save updated data to the database
    }
}
