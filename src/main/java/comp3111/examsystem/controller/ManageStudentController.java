package comp3111.examsystem.controller;

import comp3111.examsystem.entity.Student;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ManageStudentController {

    @FXML
    TextField usernameFilter;
    @FXML
    TextField nameFilter;
    @FXML
    TextField departmentFilter;
    @FXML
    TableView<Student> studentTable;
    @FXML
    private TableColumn<Student, String> usernameColumn;
    @FXML
    private TableColumn<Student, String> nameColumn;
    @FXML
    private TableColumn<Student, Integer> ageColumn;
    @FXML
    private TableColumn<Student, String> genderColumn;
    @FXML
    private TableColumn<Student, String> departmentColumn;
    @FXML
    private TableColumn<Student, String> passwordColumn;
    @FXML
    TextField usernameField;
    @FXML
    TextField nameField;
    @FXML
    TextField ageField;
    @FXML
    ComboBox<String> genderComboBox;
    @FXML
    TextField departmentField;
    @FXML
    TextField passwordField;

    String studentFilePath;

    ObservableList<Student> studentList = FXCollections.observableArrayList();

    public void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Hint");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    @FXML
    public void initialize() {
        loadStudentsFromFile();
        usernameColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getUsername()));
        nameColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getName()));
        ageColumn.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getAge()).asObject());
        genderColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getGender()));
        departmentColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getDepartment()));
        passwordColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getPassword()));
        studentTable.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                updateFields(newValue);
            } else {
                clearFields();
            }
        });
    }

    public ManageStudentController() {
        studentFilePath = "data/students.txt";
        File studentFile = new File(studentFilePath);
        if (studentFile.exists()) {
            System.out.println("Student file found at: " + studentFile.getAbsolutePath());
        } else {
            System.out.println("Student file not found!");
        }
    }

    public void loadStudentsFromFile() {
        try (BufferedReader br = new BufferedReader(new FileReader(studentFilePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] data = line.split(",");
                if (data.length == 6) {
                    Student student = new Student(data[0], data[1], Integer.parseInt(data[2]), data[3], data[4], data[5]);
                    studentList.add(student);
                }
            }
            displayStudents(studentList);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // display students info in table
    private void displayStudents(ObservableList<Student> students) {
        studentTable.setItems(students);
    }

    @FXML
    public void resetFilters() {
        usernameFilter.clear();
        nameFilter.clear();
        departmentFilter.clear();
        displayStudents(studentList);
    }

    @FXML
    public void filterStudents() {
        String username = usernameFilter.getText().toLowerCase();
        String name = nameFilter.getText().toLowerCase();
        String department = departmentFilter.getText().toLowerCase();

        List<Student> filteredList = studentList.stream()
                .filter(student -> student.getUsername().toLowerCase().contains(username) &&
                        student.getName().toLowerCase().contains(name) &&
                        student.getDepartment().toLowerCase().contains(department))
                .collect(Collectors.toList());
        displayStudents(FXCollections.observableArrayList(filteredList));
    }

    @FXML
    public void addStudent() {
        String username = usernameField.getText();
        String name = nameField.getText();
        String ageText = ageField.getText();
        String gender = genderComboBox.getValue();
        String department = departmentField.getText();
        String password = passwordField.getText();

        // validate username
        String usernameValidationMessage = validateUsername(username);
        if (usernameValidationMessage != null) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Hint");
            alert.setHeaderText(null);
            alert.setContentText(usernameValidationMessage);
            alert.showAndWait();
            return;
        }

        // validate other inputs
        String validationMessage = validateInputs(username, name, ageText, gender, department, password);
        if (validationMessage != null) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Hint");
            alert.setHeaderText(null);
            alert.setContentText(validationMessage);
            alert.showAndWait();
            return;
        }

        int age = Integer.parseInt(ageText);

        Student newStudent = new Student(username, name, age, gender, department, password);
        studentList.add(newStudent);
        String studentInput = username + ',' + name + ',' + age + ',' + gender + ',' + department + ',' + password;
        // write to txt
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(studentFilePath, true))) {
            bw.write(studentInput);
            bw.newLine();
            displayStudents(studentList);
        } catch (IOException e) {
            e.printStackTrace();
        }
        showAlert("Add student success");
        clearFields();
    }

    String validateInputs(String username, String name, String ageText, String gender, String department, String password) {
        if (username.isEmpty() || name.isEmpty() || ageText.isEmpty() || gender == null || department.isEmpty() || password.isEmpty()) {
            return "Each field should be filled in";
        }
        try {
            int age = Integer.parseInt(ageText);
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

    boolean isValidPassword(String password) {
        return password.length() < 8 || !password.matches("^(?=.*[a-zA-Z])(?=.*[0-9]).+$");
    }

    private String validateUsername(String username) {
        for (Student student : studentList) {
            if (student.getUsername().equals(username)) {
                return "The user name already exists";
            }
        }
        return null;
    }

    public void updateFields(Student student) {
        usernameField.setText(student.getUsername());
        nameField.setText(student.getName());
        ageField.setText(String.valueOf(student.getAge()));
        genderComboBox.setValue(student.getGender());
        departmentField.setText(student.getDepartment());
        passwordField.setText(student.getPassword());
    }

    @FXML
    public void updateStudent() {
        Student selectedStudent = studentTable.getSelectionModel().getSelectedItem();
        if (selectedStudent != null) {

            String originalUsername = selectedStudent.getUsername();
            String newUsername = usernameField.getText();
            String name = nameField.getText();
            String ageText = ageField.getText();
            String gender = genderComboBox.getValue();
            String department = departmentField.getText();
            String password = passwordField.getText();
            String validationMessage = validateUpdateInputs(name, ageText, gender, department, password);

            if (validationMessage != null) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Hint");
                alert.setHeaderText(null);
                alert.setContentText(validationMessage);
                alert.showAndWait();
                return;
            }

            // validate new username
            if (!newUsername.equals(originalUsername)) {
                String usernameValidationMessage = validateUsername(newUsername);
                if (usernameValidationMessage != null) {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Hint");
                    alert.setHeaderText(null);
                    alert.setContentText(usernameValidationMessage);
                    alert.showAndWait();
                    return;
                }
            }

            selectedStudent.setUsername(newUsername);
            selectedStudent.setName(name);
            selectedStudent.setAge(Integer.parseInt(ageText));
            selectedStudent.setGender(gender);
            selectedStudent.setDepartment(department);
            selectedStudent.setPassword(password);
            try {
                List<Student> students = new ArrayList<>();
                try (BufferedReader br = new BufferedReader(new FileReader(studentFilePath))) {
                    String line;
                    while ((line = br.readLine()) != null) {
                        String[] parts = line.split(",");
                        if (parts.length == 6) {
                            Student student = new Student(parts[0], parts[1], Integer.parseInt(parts[2]), parts[3], parts[4], parts[5]);
                            students.add(student);
                        }
                    }
                }
                for (int i = 0; i < students.size(); i++) {
                    if (students.get(i).getUsername().equals(originalUsername)) {
                        students.set(i, selectedStudent);
                        break;
                    }
                }
                try (BufferedWriter bw = new BufferedWriter(new FileWriter(studentFilePath))) {
                    for (Student student : students) {
                        String studentInput = String.join(",", student.getUsername(), student.getName(), String.valueOf(student.getAge()), student.getGender(), student.getDepartment(), student.getPassword());
                        bw.write(studentInput);
                        bw.newLine();
                    }
                }
                System.out.println("success");
                studentList.clear();
                studentList.addAll(students);
                displayStudents(studentList);

            } catch (IOException e) {
                e.printStackTrace();
            }
            showAlert("Update student success");
            clearFields();
            studentTable.getSelectionModel().clearSelection();
        } else {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Warning");
            alert.setHeaderText(null);
            alert.setContentText("Please select a student to update.");
            alert.showAndWait();
        }
    }

    String validateUpdateInputs(String name, String ageText, String gender, String department, String password) {
        if (name.isEmpty() || ageText.isEmpty() || gender == null || department.isEmpty() || password.isEmpty()) {
            return "Each field should be filled in";
        }
        try {
            int age = Integer.parseInt(ageText);
        } catch (NumberFormatException e) {
            return "Age must be a valid number";
        }
        if (isValidPassword(password)) {
            return "The password must contain both letters and numbers and be at least eight characters long";
        }
        return null;
    }

    @FXML
    public void deleteStudent() {
        Student selectedStudent = studentTable.getSelectionModel().getSelectedItem();
        if (selectedStudent != null) {
            // Show confirmation dialog
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Confirm Deletion");
            alert.setHeaderText("Are you sure you want to delete student " + selectedStudent.getName() + "?");
            alert.setContentText("This action cannot be undone.");

            // Get the user's choice
            ButtonType result = alert.showAndWait().orElse(ButtonType.CANCEL);
            if (result == ButtonType.OK) {
                studentList.remove(selectedStudent);
                try {
                    List<Student> students = new ArrayList<>();
                    try (BufferedReader br = new BufferedReader(new FileReader(studentFilePath))) {
                        String line;
                        while ((line = br.readLine()) != null) {
                            String[] parts = line.split(",");
                            if (parts.length == 6) {
                                Student student = new Student(parts[0], parts[1], Integer.parseInt(parts[2]), parts[3], parts[4], parts[5]);
                                students.add(student);
                            }
                        }
                    }

                    students.removeIf(student -> student.getUsername().equals(selectedStudent.getUsername()));

                    try (BufferedWriter bw = new BufferedWriter(new FileWriter(studentFilePath))) {
                        for (Student student : students) {
                            String studentInput = String.join(",", student.getUsername(), student.getName(), String.valueOf(student.getAge()), student.getGender(), student.getDepartment(), student.getPassword());
                            bw.write(studentInput);
                            bw.newLine();
                        }
                    }

                    System.out.println("Student " + selectedStudent.getName() + " has been deleted.");
                    displayStudents(studentList);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        } else {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Warning");
            alert.setHeaderText(null);
            alert.setContentText("Please select a student to delete.");
            alert.showAndWait();
        }
        showAlert("Delete student success");
    }

    @FXML
    public void refreshStudent() {
        clearFields();
        studentTable.getSelectionModel().clearSelection();
        displayStudents(studentList);
    }

    void clearFields() {
        usernameField.clear();
        nameField.clear();
        ageField.clear();
        genderComboBox.getSelectionModel().clearSelection();
        departmentField.clear();
        passwordField.clear();
    }
}