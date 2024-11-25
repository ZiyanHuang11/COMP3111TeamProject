package comp3111.examsystem.controller;

import comp3111.examsystem.entity.Teacher;
import comp3111.examsystem.service.ManageTeacherService;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.io.IOException;
import java.util.List;

/**
 * Controller for the manager to manage teachers
 */

public class ManageTeacherController {

    @FXML
    TextField usernameFilter; // Filter for teacher username
    @FXML
    TextField nameFilter; // Filter for teacher name
    @FXML
    TextField departmentFilter; // Filter for department
    @FXML
    TableView<Teacher> teacherTable; // Table for displaying teachers
    @FXML
    private TableColumn<Teacher, String> usernameColumn; // Column for username
    @FXML
    private TableColumn<Teacher, String> nameColumn; // Column for teacher name
    @FXML
    private TableColumn<Teacher, String> genderColumn; // Column for gender
    @FXML
    private TableColumn<Teacher, Integer> ageColumn; // Column for age
    @FXML
    private TableColumn<Teacher, String> positionColumn; // Column for position
    @FXML
    private TableColumn<Teacher, String> departmentColumn; // Column for department
    @FXML
    private TableColumn<Teacher, String> passwordColumn; // Column for password
    @FXML
    TextField usernameField; // Field for entering username
    @FXML
    TextField nameField; // Field for entering teacher name
    @FXML
    ComboBox<String> genderComboBox; // ComboBox for selecting gender
    @FXML
    TextField ageField; // Field for entering age
    @FXML
    ComboBox<String> positionComboBox; // ComboBox for selecting position
    @FXML
    TextField departmentField; // Field for entering department
    @FXML
    TextField passwordField; // Field for entering password

    private ManageTeacherService manageTeacherService; // Service for managing teacher operations

    /**
     * Constructs a ManageTeacherController and initializes the ManageTeacherService.
     */
    public ManageTeacherController() {
        manageTeacherService = new ManageTeacherService();
    }

    /**
     * Initializes the controller after its root element has been processed.
     * Sets up the table columns and populates the teacher table with data.
     */
    @FXML
    public void initialize() {
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
        displayTeachers(manageTeacherService.getTeacherList());
    }

    /**
     * Displays the list of teachers in the teacher table.
     *
     * @param teachers The list of teachers to display.
     */
    private void displayTeachers(ObservableList<Teacher> teachers) {
        teacherTable.setItems(teachers);
    }

    /**
     * Resets the filter fields and refreshes the teacher table.
     */
    @FXML
    public void resetFilters() {
        usernameFilter.clear();
        nameFilter.clear();
        departmentFilter.clear();
        displayTeachers(manageTeacherService.getTeacherList());
    }

    /**
     * Filters the teachers based on the input in the filter fields and updates the teacher table.
     */
    @FXML
    public void filterTeachers() {
        String username = usernameFilter.getText().toLowerCase();
        String name = nameFilter.getText().toLowerCase();
        String department = departmentFilter.getText().toLowerCase();

        List<Teacher> filteredList = manageTeacherService.filterTeachers(username, name, department);
        displayTeachers(FXCollections.observableArrayList(filteredList));
    }

    /**
     * Adds a new teacher based on the input fields.
     * Validates inputs and displays alerts for success or errors.
     */
    @FXML
    public void addTeacher() {
        String username = usernameField.getText();
        String name = nameField.getText();
        String gender = genderComboBox.getValue();
        String ageText = ageField.getText();
        String position = positionComboBox.getValue();
        String department = departmentField.getText();
        String password = passwordField.getText();

        String validationMessage = manageTeacherService.validateInputs(username, name, gender, ageText, position, department, password);
        if (validationMessage != null) {
            showAlert(validationMessage);
            return;
        }

        int age = Integer.parseInt(ageText);
        Teacher newTeacher = new Teacher(username, password, name, gender, age, position, department);

        try {
            manageTeacherService.addTeacher(newTeacher);
            showAlert("Add teacher success");
            clearFields();
            displayTeachers(manageTeacherService.getTeacherList());
        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Error adding teacher");
        }
    }

    /**
     * Updates the selected teacher with the input from the fields.
     * Validates inputs and displays alerts for success or errors.
     */
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

            String validationMessage = manageTeacherService.validateUpdateInputs(name, gender, ageText, position, department, password);
            if (validationMessage != null) {
                showAlert(validationMessage);
                return;
            }

            Teacher updatedTeacher = new Teacher(newUsername, password, name, gender, Integer.parseInt(ageText), position, department);

            try {
                manageTeacherService.updateTeacher(updatedTeacher, originalUsername);
                showAlert("Update teacher success");
                clearFields();
                displayTeachers(manageTeacherService.getTeacherList());
            } catch (IOException e) {
                e.printStackTrace();
                showAlert("Error updating teacher");
            }
        } else {
            showAlert("Please select a teacher to update.");
        }
    }

    /**
     * Deletes the selected teacher after confirming the action with the user.
     */
    @FXML
    public void deleteTeacher() {
        Teacher selectedTeacher = teacherTable.getSelectionModel().getSelectedItem();
        if (selectedTeacher != null) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Confirm Deletion");
            alert.setHeaderText("Are you sure you want to delete teacher " + selectedTeacher.getName() + "?");
            alert.setContentText("This action cannot be undone.");

            ButtonType result = alert.showAndWait().orElse(ButtonType.CANCEL);
            if (result == ButtonType.OK) {
                try {
                    manageTeacherService.deleteTeacher(selectedTeacher);
                    showAlert("Delete teacher success");
                    displayTeachers(manageTeacherService.getTeacherList());
                } catch (IOException e) {
                    e.printStackTrace();
                    showAlert("Error deleting teacher");
                }
            }
        } else {
            showAlert("Please select a teacher to delete.");
        }
    }

    /**
     * Refreshes the teacher table and clears the input fields.
     */
    @FXML
    public void refreshTeacher() {
        clearFields();
        teacherTable.getSelectionModel().clearSelection();
        displayTeachers(manageTeacherService.getTeacherList());
    }

    /**
     * Updates the input fields with the details of the specified teacher.
     *
     * @param teacher The teacher whose details will be displayed in the input fields.
     */
    private void updateFields(Teacher teacher) {
        usernameField.setText(teacher.getUsername());
        nameField.setText(teacher.getName());
        genderComboBox.setValue(teacher.getGender());
        ageField.setText(String.valueOf(teacher.getAge()));
        positionComboBox.setValue(teacher.getPosition());
        departmentField.setText(teacher.getDepartment());
        passwordField.setText(teacher.getPassword());
    }

    /**
     * Clears the input fields for teacher details.
     */
    private void clearFields() {
        usernameField.clear();
        nameField.clear();
        ageField.clear();
        genderComboBox.getSelectionModel().clearSelection();
        departmentField.clear();
        passwordField.clear();
    }

    /**
     * Displays an alert with the specified message.
     *
     * @param message The message to be displayed in the alert.
     */
    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Hint");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}