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

/**
 * Controller for the manager to manage students
 */

public class ManageStudentController {

    @FXML
    TextField usernameFilter; // Filter for username
    @FXML
    TextField nameFilter; // Filter for student name
    @FXML
    TextField departmentFilter; // Filter for department
    @FXML
    TableView<Student> studentTable; // Table for displaying students
    @FXML
    private TableColumn<Student, String> usernameColumn; // Column for username
    @FXML
    private TableColumn<Student, String> nameColumn; // Column for student name
    @FXML
    private TableColumn<Student, Integer> ageColumn; // Column for age
    @FXML
    private TableColumn<Student, String> genderColumn; // Column for gender
    @FXML
    private TableColumn<Student, String> departmentColumn; // Column for department
    @FXML
    private TableColumn<Student, String> passwordColumn; // Column for password
    @FXML
    TextField usernameField; // Field for entering username
    @FXML
    TextField nameField; // Field for entering student name
    @FXML
    TextField ageField; // Field for entering age
    @FXML
    ComboBox<String> genderComboBox; // ComboBox for selecting gender
    @FXML
    TextField departmentField; // Field for entering department
    @FXML
    TextField passwordField; // Field for entering password

    private ManageStudentService studentService; // Service for managing student operations

    /**
     * Constructs a ManageStudentController and initializes the ManageStudentService.
     */
    public ManageStudentController() {
        studentService = new ManageStudentService("data/students.txt", "data/students_exams.txt");
    }

    /**
     * Displays an alert with the specified message.
     *
     * @param message The message to be displayed in the alert.
     */
    public void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Hint");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    /**
     * Initializes the controller after its root element has been processed.
     * Sets up the table columns and populates the student table with data.
     */
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

    /**
     * Displays the list of students in the student table.
     *
     * @param students The list of students to display.
     */
    private void displayStudents(ObservableList<Student> students) {
        studentTable.setItems(students);
    }

    /**
     * Resets the filter fields and refreshes the student table.
     */
    @FXML
    public void resetFilters() {
        usernameFilter.clear();
        nameFilter.clear();
        departmentFilter.clear();
        displayStudents(studentService.getStudentList());
    }

    /**
     * Filters the students based on the input in the filter fields and updates the student table.
     */
    @FXML
    public void filterStudents() {
        String username = usernameFilter.getText().toLowerCase();
        String name = nameFilter.getText().toLowerCase();
        String department = departmentFilter.getText().toLowerCase();

        List<Student> filteredList = studentService.filterStudents(username, name, department);
        displayStudents(FXCollections.observableArrayList(filteredList));
    }

    /**
     * Adds a new student based on the input fields.
     * Validates inputs and displays alerts for success or errors.
     */
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

    /**
     * Updates the selected student with the input from the fields.
     * Validates inputs and displays alerts for success or errors.
     */
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

    /**
     * Deletes the selected student after confirming the action with the user.
     */
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

    /**
     * Refreshes the student table and clears the input fields.
     */
    @FXML
    public void refreshStudent() {
        clearFields();
        studentTable.getSelectionModel().clearSelection();
        displayStudents(studentService.getStudentList());
    }

    /**
     * Clears the input fields for student details.
     */

    void clearFields() {
        usernameField.clear();
        nameField.clear();
        ageField.clear();
        genderComboBox.getSelectionModel().clearSelection();
        departmentField.clear();
        passwordField.clear();
    }

    /**
     * Updates the input fields with the details of the specified student.
     *
     * @param student The student whose details will be displayed in the input fields.
     */
    private void updateFields(Student student) {
        usernameField.setText(student.getUsername());
        nameField.setText(student.getName());
        ageField.setText(String.valueOf(student.getAge()));
        genderComboBox.setValue(student.getGender());
        departmentField.setText(student.getDepartment());
        passwordField.setText(student.getPassword());
    }
}