package comp3111.examsystem.service;

import comp3111.examsystem.data.DataManager;
import comp3111.examsystem.entity.Teacher;

import java.util.List;
import java.util.stream.Collectors;

public class ManageTeacherService {

    private final DataManager dataManager;

    public ManageTeacherService(DataManager dataManager) {
        this.dataManager = dataManager;
    }

    public List<Teacher> getAllTeachers() {
        return dataManager.getTeachers();
    }

    public void addTeacher(Teacher newTeacher) {
        dataManager.getTeachers().add(newTeacher);
        dataManager.save();
    }

    public void updateTeacher(Teacher updatedTeacher, String originalUsername) {
        List<Teacher> teachers = dataManager.getTeachers();
        for (int i = 0; i < teachers.size(); i++) {
            if (teachers.get(i).getUsername().equals(originalUsername)) {
                teachers.set(i, updatedTeacher);
                break;
            }
        }
        dataManager.save();
    }

    public void deleteTeacher(Teacher teacher) {
        dataManager.getTeachers().remove(teacher);
        dataManager.save();
    }

    public List<Teacher> filterTeachers(String username, String name, String department) {
        return dataManager.getTeachers().stream()
                .filter(t -> t.getUsername().toLowerCase().contains(username) &&
                        t.getName().toLowerCase().contains(name) &&
                        t.getDepartment().toLowerCase().contains(department))
                .collect(Collectors.toList());
    }

    public String validateInputs(String username, String name, String gender, String ageText, String position, String department, String password) {
        if (username.isEmpty() || name.isEmpty() || ageText.isEmpty() || gender == null || position == null || department.isEmpty() || password.isEmpty()) {
            return "All fields must be filled.";
        }
        try {
            Integer.parseInt(ageText);
        } catch (NumberFormatException e) {
            return "Age must be a number.";
        }
        if (dataManager.getTeachers().stream().anyMatch(t -> t.getUsername().equals(username))) {
            return "Username already exists.";
        }
        if (!password.matches("^(?=.*[a-zA-Z])(?=.*\\d).{8,}$")) {
            return "Password must be at least 8 characters, containing letters and numbers.";
        }
        return null;
    }

    public String validateUpdateInputs(String name, String gender, String ageText, String position, String department, String password) {
        if (name.isEmpty() || ageText.isEmpty() || gender == null || position == null || department.isEmpty() || password.isEmpty()) {
            return "All fields must be filled.";
        }
        try {
            Integer.parseInt(ageText);
        } catch (NumberFormatException e) {
            return "Age must be a number.";
        }
        if (!password.matches("^(?=.*[a-zA-Z])(?=.*\\d).{8,}$")) {
            return "Password must be at least 8 characters, containing letters and numbers.";
        }
        return null;
    }
}
