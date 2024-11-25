package comp3111.examsystem.service;

import comp3111.examsystem.entity.Student;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.io.*;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Service class for managing students in the examination system.
 */
public class ManageStudentService {
    private String studentFilePath;
    private String studentExamsFilePath;
    private ObservableList<Student> studentList;

    /**
     * Constructs a ManageStudentService with the specified file paths for student data and student exams.
     *
     * @param studentFilePath    the file path for storing student data
     * @param studentExamsFilePath the file path for storing student exam data
     */
    public ManageStudentService(String studentFilePath, String studentExamsFilePath) {
        this.studentFilePath = studentFilePath;
        this.studentExamsFilePath = studentExamsFilePath;
        this.studentList = FXCollections.observableArrayList();
        loadStudentsFromFile();
    }

    /**
     * Returns the list of students.
     *
     * @return an observable list of students
     */
    public ObservableList<Student> getStudentList() {
        return studentList;
    }

    /**
     * Loads students from a file into the student list.
     */
    public void loadStudentsFromFile() {
        try (BufferedReader br = new BufferedReader(new FileReader(studentFilePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] data = line.split(",");
                if (data.length == 6 && !data[0].trim().isEmpty() && !data[1].trim().isEmpty() &&
                        !data[2].trim().isEmpty() && !data[3].trim().isEmpty() &&
                        !data[4].trim().isEmpty() && !data[5].trim().isEmpty()) {
                    Student student = new Student(data[0].trim(), data[1].trim(),
                            Integer.parseInt(data[2].trim()), data[3].trim(),
                            data[4].trim(), data[5].trim());
                    studentList.add(student);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Adds a new student to the student list and saves it to the file.
     *
     * @param newStudent the new student to be added
     * @throws IOException if an error occurs while saving to the file
     */
    public void addStudent(Student newStudent) throws IOException {
        studentList.add(newStudent);
        String studentInput = String.join(",", newStudent.getUsername(), newStudent.getName(),
                String.valueOf(newStudent.getAge()), newStudent.getGender(),
                newStudent.getDepartment(), newStudent.getPassword());
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(studentFilePath, true))) {
            bw.write(studentInput);
            bw.newLine();
        }
    }

    /**
     * Updates an existing student in the student list and saves the changes to the file.
     *
     * @param updatedStudent   the student with updated details
     * @param originalUsername the original username of the student to be updated
     * @throws IOException if an error occurs while saving to the file
     */
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
        updateStudentExams(originalUsername, updatedStudent.getUsername());
    }

    /**
     * Deletes a student from the student list and saves the changes to the file.
     *
     * @param username the username of the student to be deleted
     * @throws IOException if an error occurs while saving to the file
     */
    public void deleteStudent(String username) throws IOException {
        boolean removed = studentList.removeIf(student -> student.getUsername().equals(username));
        if (!removed) {
            throw new IOException("Student with username " + username + " does not exist.");
        }
        saveStudentsToFile();
        deleteStudentFromExams(username);
    }

    /**
     * Saves the current list of students to the student file.
     *
     * @throws IOException if an error occurs while writing to the file
     */
    void saveStudentsToFile() throws IOException {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(studentFilePath))) {
            for (Student student : studentList) {
                String studentInput = String.join(",", student.getUsername(),
                        student.getName(), String.valueOf(student.getAge()),
                        student.getGender(), student.getDepartment(), student.getPassword());
                bw.write(studentInput);
                bw.newLine();
            }
        }
    }

    /**
     * Updates the username in the student exams file for a given student.
     *
     * @param originalUsername the original username of the student
     * @param newUsername      the new username of the student
     * @throws IOException if an error occurs while updating the exams file
     */
    void updateStudentExams(String originalUsername, String newUsername) throws IOException {
        List<String> lines = new BufferedReader(new FileReader(studentExamsFilePath)).lines().collect(Collectors.toList());
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(studentExamsFilePath))) {
            for (String line : lines) {
                String[] data = line.split(",");
                if (data[0].equals(originalUsername)) {
                    data[0] = newUsername;
                }
                bw.write(String.join(",", data));
                bw.newLine();
            }
        }
    }

    /**
     * Deletes a student from the student exams file.
     *
     * @param username the username of the student to be deleted
     * @throws IOException if an error occurs while updating the exams file
     */
    void deleteStudentFromExams(String username) throws IOException {
        List<String> lines = new BufferedReader(new FileReader(studentExamsFilePath)).lines().collect(Collectors.toList());
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(studentExamsFilePath))) {
            for (String line : lines) {
                String[] data = line.split(",");
                if (!data[0].equals(username)) {
                    bw.write(line);
                    bw.newLine();
                }
            }
        }
    }

    /**
     * Filters students based on the specified criteria.
     *
     * @param username   the username to filter by
     * @param name       the name to filter by
     * @param department the department to filter by
     * @return a list of students that match the specified criteria
     */
    public List<Student> filterStudents(String username, String name, String department) {
        return studentList.stream()
                .filter(student -> student.getUsername().toLowerCase().contains(username.toLowerCase()) &&
                        student.getName().toLowerCase().contains(name.toLowerCase()) &&
                        student.getDepartment().toLowerCase().contains(department.toLowerCase()))
                .collect(Collectors.toList());
    }

    /**
     * Validates the username to check if it already exists in the student list.
     *
     * @param username the username to validate
     * @return an error message if the username exists, or null if it does not
     */
    public String validateUsername(String username) {
        for (Student student : studentList) {
            if (student.getUsername().equals(username)) {
                return "The user name already exists";
            }
        }
        return null;
    }

    /**
     * Validates input fields for adding a new student.
     *
     * @param username   the username to validate
     * @param name       the name to validate
     * @param ageText    the age to validate
     * @param gender     the gender to validate
     * @param department  the department to validate
     * @param password   the password to validate
     * @return an error message if any field is invalid, or null if all fields are valid
     */
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

    /**
     * Checks if the specified password is valid.
     *
     * @param password the password to check
     * @return true if the password is valid, false otherwise
     */

    public boolean isValidPassword(String password) {
        return password.length() < 8 || !password.matches("^(?=.*[a-zA-Z])(?=.*[0-9]).+$");
    }

    /**
     * Validates input fields for updating an existing student's information.
     *
     * @param name       the name to validate
     * @param ageText    the age to validate
     * @param gender     the gender to validate
     * @param department  the department to validate
     * @param password   the password to validate
     * @return an error message if any field is invalid, or null if all fields are valid
     */

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