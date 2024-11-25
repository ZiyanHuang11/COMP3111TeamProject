package comp3111.examsystem.controller;

import comp3111.examsystem.entity.Student;
import comp3111.examsystem.service.ManageStudentService;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.io.IOException;
import java.util.List;

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

    private ManageStudentService studentService;

    public ManageStudentController() {
        studentService = new ManageStudentService("data/students.txt", "data/students_exams.txt");
    }

    public void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Hint");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    @FXML
    public void initialize() {
        usernameColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getUsername()));
        nameColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getName()));
        ageColumn.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getAge()).asObject());
        genderColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getGender()));
        departmentColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getDepartment()));
        passwordColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getPassword()));
        displayStudents(studentService.getStudentList());
        studentTable.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                updateFields(newValue);
            } else {
                clearFields();
            }
        });
    }

    private void displayStudents(ObservableList<Student> students) {
        studentTable.setItems(students);
    }

    @FXML
    public void resetFilters() {
        usernameFilter.clear();
        nameFilter.clear();
        departmentFilter.clear();
        displayStudents(studentService.getStudentList());
    }

    @FXML
    public void filterStudents() {
        String username = usernameFilter.getText().toLowerCase();
        String name = nameFilter.getText().toLowerCase();
        String department = departmentFilter.getText().toLowerCase();

        List<Student> filteredList = studentService.filterStudents(username, name, department);
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

        String validationMessage = studentService.validateInputs(username, name, ageText, gender, department, password);
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
        try {
            studentService.addStudent(newStudent);
            displayStudents(studentService.getStudentList());
            showAlert("Add student success");
            clearFields();
        } catch (IOException e) {
            e.printStackTrace();
        }
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

            String validationMessage = studentService.validateUpdateInputs(name, ageText, gender, department, password);
            if (validationMessage != null) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Hint");
                alert.setHeaderText(null);
                alert.setContentText(validationMessage);
                alert.showAndWait();
                return;
            }

            if (!newUsername.equals(originalUsername)) {
                String usernameValidationMessage = studentService.validateUsername(newUsername);
                if (usernameValidationMessage != null) {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Hint");
                    alert.setHeaderText(null);
                    alert.setContentText(usernameValidationMessage);
                    alert.showAndWait();
                    return;
                }
            }
            Student updatedStudent = new Student(newUsername, name, Integer.parseInt(ageText), gender, department, password);
            try {
                studentService.updateStudent(updatedStudent, originalUsername);
                displayStudents(studentService.getStudentList());
                showAlert("Update student success");
                clearFields();
                studentTable.getSelectionModel().clearSelection();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Warning");
            alert.setHeaderText(null);
            alert.setContentText("Please select a student to update.");
            alert.showAndWait();
        }
    }

    @FXML
    public void deleteStudent() {
        Student selectedStudent = studentTable.getSelectionModel().getSelectedItem();
        if (selectedStudent != null) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Confirm Deletion");
            alert.setHeaderText("Are you sure you want to delete student " + selectedStudent.getName() + "?");
            alert.setContentText("This action cannot be undone.");

            ButtonType result = alert.showAndWait().orElse(ButtonType.CANCEL);
            if (result == ButtonType.OK) {
                try {
                    studentService.deleteStudent(selectedStudent.getUsername());
                    displayStudents(studentService.getStudentList());
                    showAlert("Delete student success");
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
    }

    @FXML
    public void refreshStudent() {
        clearFields();
        studentTable.getSelectionModel().clearSelection();
        displayStudents(studentService.getStudentList());
    }

    void clearFields() {
        usernameField.clear();
        nameField.clear();
        ageField.clear();
        genderComboBox.getSelectionModel().clearSelection();
        departmentField.clear();
        passwordField.clear();
    }

    private void updateFields(Student student) {
        usernameField.setText(student.getUsername());
        nameField.setText(student.getName());
        ageField.setText(String.valueOf(student.getAge()));
        genderComboBox.setValue(student.getGender());
        departmentField.setText(student.getDepartment());
        passwordField.setText(student.getPassword());
    }
}