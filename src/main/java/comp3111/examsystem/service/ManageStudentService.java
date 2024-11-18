package comp3111.examsystem.service;

import comp3111.examsystem.entity.Student;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.io.*;
import java.util.List;
import java.util.stream.Collectors;

public class ManageStudentService {
    private String studentFilePath;
    private ObservableList<Student> studentList;

    public ManageStudentService(String studentFilePath) {
        this.studentFilePath = studentFilePath;
        this.studentList = FXCollections.observableArrayList();
        loadStudentsFromFile();
    }

    public ObservableList<Student> getStudentList() {
        return studentList;
    }

    public void loadStudentsFromFile() {
        try (BufferedReader br = new BufferedReader(new FileReader(studentFilePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] data = line.split(",");
                if (data.length == 6 && !data[0].trim().isEmpty() && !data[1].trim().isEmpty() && !data[2].trim().isEmpty() &&
                        !data[3].trim().isEmpty() && !data[4].trim().isEmpty() && !data[5].trim().isEmpty()) {
                    Student student = new Student(data[0].trim(), data[1].trim(), Integer.parseInt(data[2].trim()), data[3].trim(), data[4].trim(), data[5].trim());
                    studentList.add(student);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void addStudent(Student newStudent) throws IOException {
        studentList.add(newStudent);
        String studentInput = String.join(",", newStudent.getUsername(), newStudent.getName(), String.valueOf(newStudent.getAge()), newStudent.getGender(), newStudent.getDepartment(), newStudent.getPassword());
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(studentFilePath, true))) {
            bw.write(studentInput);
            bw.newLine();
        }
    }

    public void updateStudent(Student updatedStudent, String originalUsername) throws IOException {
        boolean found = false;
        for (int i = 0; i < studentList.size(); i++) {
            if (studentList.get(i).getUsername().equals(originalUsername)) {
                studentList.set(i, updatedStudent);
                found = true;
                break;
            }
        }
        if (!found) {
            throw new IOException("Student with username " + originalUsername + " does not exist.");
        }
        saveStudentsToFile();
    }

    public void deleteStudent(String username) throws IOException {
        boolean removed = studentList.removeIf(student -> student.getUsername().equals(username));
        if (!removed) {
            throw new IOException("Student with username " + username + " does not exist.");
        }
        saveStudentsToFile();
    }

    private void saveStudentsToFile() throws IOException {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(studentFilePath))) {
            for (Student student : studentList) {
                String studentInput = String.join(",", student.getUsername(), student.getName(), String.valueOf(student.getAge()), student.getGender(), student.getDepartment(), student.getPassword());
                bw.write(studentInput);
                bw.newLine();
            }
        }
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