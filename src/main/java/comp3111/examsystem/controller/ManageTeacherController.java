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

    private ManageTeacherService manageTeacherService;

    public ManageTeacherController() {
        manageTeacherService = new ManageTeacherService();
    }

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

    private void displayTeachers(ObservableList<Teacher> teachers) {
        teacherTable.setItems(teachers);
    }

    @FXML
    public void resetFilters() {
        usernameFilter.clear();
        nameFilter.clear();
        departmentFilter.clear();
        displayTeachers(manageTeacherService.getTeacherList());
    }

    @FXML
    public void filterTeachers() {
        String username = usernameFilter.getText().toLowerCase();
        String name = nameFilter.getText().toLowerCase();
        String department = departmentFilter.getText().toLowerCase();

        List<Teacher> filteredList = manageTeacherService.filterTeachers(username, name, department);
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

        String validationMessage = manageTeacherService.validateInputs(username, name, gender, ageText, position, department, password);
        if (validationMessage != null) {
            showAlert(validationMessage);
            return;
        }

        int age = Integer.parseInt(ageText);
        Teacher newTeacher = new Teacher(username, name, gender, age, position, department, password);

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

            Teacher updatedTeacher = new Teacher(newUsername, name, gender, Integer.parseInt(ageText), position, department, password);

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

    @FXML
    public void refreshTeacher() {
        clearFields();
        teacherTable.getSelectionModel().clearSelection();
        displayTeachers(manageTeacherService.getTeacherList());
    }

    private void updateFields(Teacher teacher) {
        usernameField.setText(teacher.getUsername());
        nameField.setText(teacher.getName());
        genderComboBox.setValue(teacher.getGender());
        ageField.setText(String.valueOf(teacher.getAge()));
        positionComboBox.setValue(teacher.getPosition());
        departmentField.setText(teacher.getDepartment());
        passwordField.setText(teacher.getPassword());
    }

    private void clearFields() {
        usernameField.clear();
        nameField.clear();
        ageField.clear();
        genderComboBox.getSelectionModel().clearSelection();
        departmentField.clear();
        passwordField.clear();
    }

    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Hint");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}