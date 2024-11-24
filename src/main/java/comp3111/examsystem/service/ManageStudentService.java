package comp3111.examsystem.service;

import comp3111.examsystem.data.DataManager;
import comp3111.examsystem.entity.Student;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.List;
import java.util.stream.Collectors;

public class ManageStudentService {
    private final DataManager dataManager;
    private ObservableList<Student> studentList;

    // 使用 DataManager 构造服务类
    public ManageStudentService(DataManager dataManager) {
        this.dataManager = dataManager;
        this.studentList = FXCollections.observableArrayList(dataManager.getStudents());
    }

    public ObservableList<Student> getStudentList() {
        return studentList;
    }

    public void addStudent(Student newStudent) {
        dataManager.getStudents().add(newStudent);
        studentList.add(newStudent);
        dataManager.saveStudents(); // 调用 DataManager 保存更改
    }

    public void updateStudent(Student updatedStudent, String originalUsername) {
        List<Student> students = dataManager.getStudents();
        for (int i = 0; i < students.size(); i++) {
            if (students.get(i).getUsername().equals(originalUsername)) {
                students.set(i, updatedStudent);
                break;
            }
        }
        studentList.setAll(dataManager.getStudents());
        dataManager.saveStudents(); // 调用 DataManager 保存更改
    }

    public void deleteStudent(String username) {
        List<Student> students = dataManager.getStudents();
        students.removeIf(student -> student.getUsername().equals(username));
        studentList.setAll(students);
        dataManager.saveStudents(); // 调用 DataManager 保存更改
    }

    public List<Student> filterStudents(String username, String name, String department) {
        return studentList.stream()
                .filter(student -> student.getUsername().toLowerCase().contains(username.toLowerCase()) &&
                        student.getName().toLowerCase().contains(name.toLowerCase()) &&
                        student.getDepartment().toLowerCase().contains(department.toLowerCase()))
                .collect(Collectors.toList());
    }

    public String validateUsername(String username) {
        for (Student student : studentList) {
            if (student.getUsername().equals(username)) {
                return "The user name already exists";
            }
        }
        return null;
    }

    public String validateInputs(String username, String name, String ageText, String gender, String department, String password) {
        if (username.isEmpty() || name.isEmpty() || ageText.isEmpty() || gender == null || department.isEmpty() || password.isEmpty()) {
            return "Each field should be filled in";
        }
        try {
            Integer.parseInt(ageText);
        } catch (NumberFormatException e) {
            return "Age must be a valid number";
        }
        for (Student student : studentList) {
            if (student.getUsername().equals(username)) {
                return "The user name already exists";
            }
        }
        if (isValidPassword(password)) {
            return "The password must contain both letters and numbers and be at least eight characters long";
        }
        return null;
    }

    public boolean isValidPassword(String password) {
        return password.length() < 8 || !password.matches("^(?=.*[a-zA-Z])(?=.*[0-9]).+$");
    }

    public String validateUpdateInputs(String name, String ageText, String gender, String department, String password) {
        if (name.isEmpty() || ageText.isEmpty() || gender == null || department.isEmpty() || password.isEmpty()) {
            return "Each field should be filled in";
        }
        try {
            Integer.parseInt(ageText);
        } catch (NumberFormatException e) {
            return "Age must be a valid number";
        }
        if (isValidPassword(password)) {
            return "The password must contain both letters and numbers and be at least eight characters long";
        }
        return null;
    }
}
