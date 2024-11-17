package comp3111.examsystem.controller;

import comp3111.examsystem.entity.Teacher;
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

public class ManageTeacherController {

    @FXML
    TextField usernameFilter;
    @FXML
    TextField nameFilter;
    @FXML
    TextField departmentFilter;
    @FXML
    TableView<Teacher> teacherTable;
    @FXML
    private TableColumn<Teacher, String> usernameColumn;
    @FXML
    private TableColumn<Teacher, String> nameColumn;
    @FXML
    private TableColumn<Teacher, String> genderColumn;
    @FXML
    private TableColumn<Teacher, Integer> ageColumn;
    @FXML
    private TableColumn<Teacher, String> positionColumn;
    @FXML
    private TableColumn<Teacher, String> departmentColumn;
    @FXML
    private TableColumn<Teacher, String> passwordColumn;
    @FXML
    TextField usernameField;
    @FXML
    TextField nameField;
    @FXML
    ComboBox<String> genderComboBox;
    @FXML
    TextField ageField;
    @FXML
    ComboBox<String> positionComboBox;
    @FXML
    TextField departmentField;
    @FXML
    TextField passwordField;

    String teacherFilePath;

    ObservableList<Teacher> teacherList = FXCollections.observableArrayList();

    public void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Hint");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    @FXML
    public void initialize() {
        loadTeachersFromFile();
        usernameColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getUsername()));
        nameColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getName()));
        genderColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getGender()));
        ageColumn.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getAge()).asObject());
        positionColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getPosition()));
        departmentColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getDepartment()));
        passwordColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getPassword()));
        teacherTable.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                updateFields(newValue);
            } else {
                clearFields();
            }
        });
    }

    public ManageTeacherController() {
        teacherFilePath = "data/teachers.txt";
        File teacherFile = new File(teacherFilePath);
        if (teacherFile.exists()) {
            System.out.println("Teacher file found at: " + teacherFile.getAbsolutePath());
        } else {
            System.out.println("Teacher file not found!");
        }
    }

    public void loadTeachersFromFile() {
        try (BufferedReader br = new BufferedReader(new FileReader(teacherFilePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] data = line.split(",");
                if (data.length == 7) {
                    Teacher teacher = new Teacher(data[0], data[1], data[2], Integer.parseInt(data[3]), data[4], data[5], data[6]);
                    teacherList.add(teacher);
                }
            }
            displayTeachers(teacherList);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // display teachers info in table
    private void displayTeachers(ObservableList<Teacher> teachers) {
        teacherTable.setItems(teachers);
    }

    @FXML
    public void resetFilters() {
        usernameFilter.clear();
        nameFilter.clear();
        departmentFilter.clear();
        displayTeachers(teacherList);
    }

    @FXML
    public void filterTeachers() {
        String username = usernameFilter.getText().toLowerCase();
        String name = nameFilter.getText().toLowerCase();
        String department = departmentFilter.getText().toLowerCase();

        List<Teacher> filteredList = teacherList.stream()
                .filter(teacher -> teacher.getUsername().toLowerCase().contains(username) &&
                        teacher.getName().toLowerCase().contains(name) &&
                        teacher.getDepartment().toLowerCase().contains(department))
                .collect(Collectors.toList());
        displayTeachers(FXCollections.observableArrayList(filteredList));
    }

    @FXML
    public void addTeacher() {
        String username = usernameField.getText();
        String name = nameField.getText();
        String gender = genderComboBox.getValue();
        String ageText = ageField.getText();
        String position = positionComboBox.getValue();
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
        String validationMessage = validateInputs(username, name, gender, ageText, position, department, password);
        if (validationMessage != null) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Hint");
            alert.setHeaderText(null);
            alert.setContentText(validationMessage);
            alert.showAndWait();
            return;
        }

        int age = Integer.parseInt(ageText);

        Teacher newTeacher = new Teacher(username, name, gender, age, position, department, password);
        teacherList.add(newTeacher);
        String teacherInput = username + ',' + name + ',' + gender + ',' + age + ',' + position + ',' + department + ',' + password;
        // write to txt
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(teacherFilePath, true))) {
            bw.write(teacherInput);
            bw.newLine();
            displayTeachers(teacherList);
        } catch (IOException e) {
            e.printStackTrace();
        }
        showAlert("Add teacher success");
        clearFields();
    }

    String validateInputs(String username, String name, String gender, String ageText, String position, String department, String password) {
        if (username.isEmpty() || name.isEmpty() || ageText.isEmpty() || gender == null || position == null || department.isEmpty() || password.isEmpty()) {
            return "Each field should be filled in";
        }
        try {
            int age = Integer.parseInt(ageText);
        } catch (NumberFormatException e) {
            return "Age must be a valid number";
        }
        for (Teacher teacher : teacherList) {
            if (teacher.getUsername().equals(username)) {
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
        for (Teacher teacher : teacherList) {
            if (teacher.getUsername().equals(username)) {
                return "The user name already exists";
            }
        }
        return null;
    }

    public void updateFields(Teacher teacher) {
        usernameField.setText(teacher.getUsername());
        nameField.setText(teacher.getName());
        genderComboBox.setValue(teacher.getGender());
        ageField.setText(String.valueOf(teacher.getAge()));
        positionComboBox.setValue(teacher.getPosition());
        departmentField.setText(teacher.getDepartment());
        passwordField.setText(teacher.getPassword());
    }

    @FXML
    public void updateTeacher() {
        Teacher selectedTeacher = teacherTable.getSelectionModel().getSelectedItem();
        if (selectedTeacher != null) {

            String originalUsername = selectedTeacher.getUsername();
            String newUsername = usernameField.getText();
            String name = nameField.getText();
            String ageText = ageField.getText();
            String gender = genderComboBox.getValue();
            String position = positionComboBox.getValue();
            String department = departmentField.getText();
            String password = passwordField.getText();
            String validationMessage = validateUpdateInputs(name, gender, ageText, position, department, password);

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

            selectedTeacher.setUsername(newUsername);
            selectedTeacher.setName(name);
            selectedTeacher.setAge(Integer.parseInt(ageText));
            selectedTeacher.setGender(gender);
            selectedTeacher.setDepartment(department);
            selectedTeacher.setPassword(password);
            try {
                List<Teacher> teachers = new ArrayList<>();
                try (BufferedReader br = new BufferedReader(new FileReader(teacherFilePath))) {
                    String line;
                    while ((line = br.readLine()) != null) {
                        String[] parts = line.split(",");
                        if (parts.length == 7) {
                            Teacher teacher = new Teacher(parts[0], parts[1], parts[2], Integer.parseInt(parts[3]), parts[4], parts[5], parts[6]);
                            teachers.add(teacher);
                        }
                    }
                }
                for (int i = 0; i < teachers.size(); i++) {
                    if (teachers.get(i).getUsername().equals(originalUsername)) {
                        teachers.set(i, selectedTeacher);
                        break;
                    }
                }
                try (BufferedWriter bw = new BufferedWriter(new FileWriter(teacherFilePath))) {
                    for (Teacher teacher : teachers) {
                        String teacherInput = String.join(",", teacher.getUsername(), teacher.getName(), teacher.getGender(), String.valueOf(teacher.getAge()), teacher.getPosition(), teacher.getDepartment(), teacher.getPassword());
                        bw.write(teacherInput);
                        bw.newLine();
                    }
                }
                System.out.println("success");
                teacherList.clear();
                teacherList.addAll(teachers);
                displayTeachers(teacherList);

            } catch (IOException e) {
                e.printStackTrace();
            }
            showAlert("Update teacher success");
            clearFields();
            teacherTable.getSelectionModel().clearSelection();
        } else {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Warning");
            alert.setHeaderText(null);
            alert.setContentText("Please select a teacher to update.");
            alert.showAndWait();
        }
    }

    String validateUpdateInputs(String name, String gender, String ageText, String position, String department, String password) {
        if (name.isEmpty() || ageText.isEmpty() || gender == null || position == null || department.isEmpty() || password.isEmpty()) {
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
    public void deleteTeacher() {
        Teacher selectedTeacher = teacherTable.getSelectionModel().getSelectedItem();
        if (selectedTeacher != null) {
            // Show confirmation dialog
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Confirm Deletion");
            alert.setHeaderText("Are you sure you want to delete teacher " + selectedTeacher.getName() + "?");
            alert.setContentText("This action cannot be undone.");

            // Get the user's choice
            ButtonType result = alert.showAndWait().orElse(ButtonType.CANCEL);
            if (result == ButtonType.OK) {
                teacherList.remove(selectedTeacher);
                try {
                    List<Teacher> teachers = new ArrayList<>();
                    try (BufferedReader br = new BufferedReader(new FileReader(teacherFilePath))) {
                        String line;
                        while ((line = br.readLine()) != null) {
                            String[] parts = line.split(",");
                            if (parts.length == 7) {
                                Teacher teacher = new Teacher(parts[0], parts[1], parts[2], Integer.parseInt(parts[3]), parts[4], parts[5], parts[6]);
                                teachers.add(teacher);
                            }
                        }
                    }

                    teachers.removeIf(teacher -> teacher.getUsername().equals(selectedTeacher.getUsername()));

                    try (BufferedWriter bw = new BufferedWriter(new FileWriter(teacherFilePath))) {
                        for (Teacher teacher : teachers) {
                            String teacherInput = String.join(",", teacher.getUsername(), teacher.getName(), teacher.getGender(), String.valueOf(teacher.getAge()), teacher.getPosition(), teacher.getDepartment(), teacher.getPassword());
                            bw.write(teacherInput);
                            bw.newLine();
                        }
                    }

                    System.out.println("Teacher " + selectedTeacher.getName() + " has been deleted.");
                    displayTeachers(teacherList);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        } else {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Warning");
            alert.setHeaderText(null);
            alert.setContentText("Please select a teacher to delete.");
            alert.showAndWait();
        }
        showAlert("Delete teacher success");
    }

    @FXML
    public void refreshTeacher() {
        clearFields();
        teacherTable.getSelectionModel().clearSelection();
        displayTeachers(teacherList);
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