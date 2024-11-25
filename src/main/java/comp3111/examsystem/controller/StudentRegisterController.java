package comp3111.examsystem.controller;

import comp3111.examsystem.service.StudentRegisterService;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

import java.util.HashMap;
import java.util.Map;

public class StudentRegisterController {

    @FXML
    private TextField usernameField;
    @FXML
    private TextField nameField;
    @FXML
    private ComboBox<String> genderComboBox;
    @FXML
    private TextField ageField;
    @FXML
    private TextField departmentField;
    @FXML
    private PasswordField passwordField;
    @FXML
    private PasswordField passwordConfirmField;

    private StudentRegisterService registerService;

    @FXML
    public void initialize() {
        // Initialize the gender ComboBox programmatically
        genderComboBox.getItems().addAll("Male", "Female", "Other");
        genderComboBox.setValue("Gender");

        // Initialize the service
        String studentFilePath = "data/students.txt";
        registerService = new StudentRegisterService(studentFilePath);
    }

    @FXML
    private void handleRegister() {
        // Retrieve form inputs
        String username = usernameField.getText().trim();
        String name = nameField.getText().trim();
        String gender = genderComboBox.getValue();
        String ageText = ageField.getText().trim();
        String department = departmentField.getText().trim();
        String password = passwordField.getText();
        String confirmPassword = passwordConfirmField.getText();

        // Validate inputs
        String validationMessage = registerService.validateInputs(username, name, gender, ageText, department, password, confirmPassword);
        if (validationMessage != null) {
            showAlert("Registration Failed", validationMessage, Alert.AlertType.ERROR);
            return;
        }

        // Check if user exists
        if (registerService.isUserExists(username)) {
            showAlert("Registration Failed", "Username already exists!", Alert.AlertType.ERROR);
            return;
        }

        // Prepare student information
        Map<String, String> studentInfo = new HashMap<>();
        studentInfo.put("username", username);
        studentInfo.put("name", name);
        studentInfo.put("gender", gender);
        studentInfo.put("age", ageText);
        studentInfo.put("department", department);
        studentInfo.put("password", password);

        // Register the student
        try {
            registerService.registerStudent(studentInfo);
            showAlert("Hint", "Student registered successfully!", Alert.AlertType.INFORMATION);

            // Close the registration window
            Stage stage = (Stage) usernameField.getScene().getWindow();
            stage.close();
        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Hint", "Error occurred while saving user data.", Alert.AlertType.ERROR);
        }
    }


    @FXML
    private void handleClose() {
        // Close the registration window
        Stage stage = (Stage) usernameField.getScene().getWindow();
        stage.close();
    }

    private void showAlert(String title, String message, Alert.AlertType type) {
        Alert alert = new Alert(type);
        alert.setTitle("Hint");
        alert.setHeaderText(null);
        alert.setContentText(message);

        ButtonType buttonTypeOk = new ButtonType("OK", ButtonBar.ButtonData.OK_DONE);
        alert.getButtonTypes().setAll(buttonTypeOk);
        alert.showAndWait();
    }
}
