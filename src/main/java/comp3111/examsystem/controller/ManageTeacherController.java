package comp3111.examsystem.controller;

import comp3111.examsystem.data.DataManager;
import comp3111.examsystem.entity.Teacher;
import comp3111.examsystem.service.ManageTeacherService;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.util.List;

public class ManageTeacherController {

    @FXML
    private TextField usernameFilter;
    @FXML
    private TextField nameFilter;
    @FXML
    private TextField departmentFilter;
    @FXML
    private TableView<Teacher> teacherTable;
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
    private TextField usernameField;
    @FXML
    private TextField nameField;
    @FXML
    private ComboBox<String> genderComboBox;
    @FXML
    private TextField ageField;
    @FXML
    private ComboBox<String> positionComboBox;
    @FXML
    private TextField departmentField;
    @FXML
    private TextField passwordField;

    private ManageTeacherService teacherService;

    public void setDataManager(DataManager dataManager) {
        this.teacherService = new ManageTeacherService(dataManager);
        initialize();
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
        refreshTeacher();
    }

    @FXML
    public void filterTeachers() {
        String username = usernameFilter.getText().trim().toLowerCase();
        String name = nameFilter.getText().trim().toLowerCase();
        String department = departmentFilter.getText().trim().toLowerCase();

        List<Teacher> filteredList = teacherService.filterTeachers(username, name, department);
        displayTeachers(FXCollections.observableArrayList(filteredList));
    }

    @FXML
    public void resetFilters() {
        usernameFilter.clear();
        nameFilter.clear();
        departmentFilter.clear();
        refreshTeacher();
    }

    @FXML
    public void addTeacher() {
        String username = usernameField.getText().trim();
        String name = nameField.getText().trim();
        String gender = genderComboBox.getValue();
        String ageText = ageField.getText().trim();
        String position = positionComboBox.getValue();
        String department = departmentField.getText().trim();
        String password = passwordField.getText().trim();

        String validationMessage = teacherService.validateInputs(username, name, gender, ageText, position, department, password);
        if (validationMessage != null) {
            showAlert(validationMessage);
            return;
        }

        int age = Integer.parseInt(ageText);
        Teacher newTeacher = new Teacher(null, username, password, name, gender, age, position, department);

        teacherService.addTeacher(newTeacher);
        showAlert("Teacher added successfully.");
        clearFields();
        refreshTeacher();
    }

    @FXML
    public void updateTeacher() {
        Teacher selectedTeacher = teacherTable.getSelectionModel().getSelectedItem();
        if (selectedTeacher != null) {
            String originalUsername = selectedTeacher.getUsername();
            String username = usernameField.getText().trim();
            String name = nameField.getText().trim();
            String gender = genderComboBox.getValue();
            String ageText = ageField.getText().trim();
            String position = positionComboBox.getValue();
            String department = departmentField.getText().trim();
            String password = passwordField.getText().trim();

            String validationMessage = teacherService.validateUpdateInputs(name, gender, ageText, position, department, password);
            if (validationMessage != null) {
                showAlert(validationMessage);
                return;
            }

            int age = Integer.parseInt(ageText);
            Teacher updatedTeacher = new Teacher(selectedTeacher.getId(), username, password, name, gender, age, position, department);
            teacherService.updateTeacher(updatedTeacher, originalUsername);

            showAlert("Teacher updated successfully.");
            clearFields();
            refreshTeacher();
        } else {
            showAlert("Please select a teacher to update.");
        }
    }

    @FXML
    public void deleteTeacher() {
        Teacher selectedTeacher = teacherTable.getSelectionModel().getSelectedItem();
        if (selectedTeacher != null) {
            teacherService.deleteTeacher(selectedTeacher);
            showAlert("Teacher deleted successfully.");
            refreshTeacher();
        } else {
            showAlert("Please select a teacher to delete.");
        }
    }

    @FXML
    public void refreshTeacher() {
        displayTeachers(FXCollections.observableArrayList(teacherService.getAllTeachers()));
        clearFields();
    }

    private void displayTeachers(ObservableList<Teacher> teachers) {
        teacherTable.setItems(teachers);
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
        positionComboBox.getSelectionModel().clearSelection();
        departmentField.clear();
        passwordField.clear();
    }

    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Message");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
