package comp3111.examsystem.controller;

import comp3111.examsystem.service.TeacherRegisterService;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Controller for managing the teacher registration interface in the examination system.
 */
public class TeacherRegisterController {

    @FXML
    private TextField usernameTxt;
    @FXML
    private TextField nameTxt;
    @FXML
    private ComboBox<String> genderComboBox;
    @FXML
    private TextField ageTxt;
    @FXML
    private ComboBox<String> positionComboBox;
    @FXML
    private TextField departmentTxt;
    @FXML
    private PasswordField passwordTxt;
    @FXML
    private PasswordField confirmPasswordTxt;

    private TeacherRegisterService registerService;

    /**
     * Initializes the controller and sets up the gender and position ComboBoxes.
     */
    @FXML
    public void initialize() {
        // Initialize gender ComboBox
        genderComboBox.getItems().addAll("Male", "Female", "Other");
        genderComboBox.setValue("Gender");

        // Initialize position ComboBox
        positionComboBox.getItems().addAll("Professor", "Lecturer", "Assistant Professor", "Researcher");
        positionComboBox.setValue("Position");

        // Initialize the service
        String teacherFilePath = "data/teachers.txt";
        registerService = new TeacherRegisterService(teacherFilePath);
    }

    /**
     * Handles the registration of a teacher when the register button is clicked.
     */
    @FXML
    private void handleRegister() {
        String username = usernameTxt.getText().trim();
        String name = nameTxt.getText().trim();
        String gender = genderComboBox.getValue();
        String ageText = ageTxt.getText().trim();
        String position = positionComboBox.getValue();
        String department = departmentTxt.getText().trim();
        String password = passwordTxt.getText();
        String confirmPassword = confirmPasswordTxt.getText();

        // Validate inputs
        String validationMessage = registerService.validateInputs(username, name, gender, ageText, position,
                department, password, confirmPassword);
        if (validationMessage != null) {
            showAlert("Registration Failed", validationMessage, Alert.AlertType.ERROR);
            return;
        }

        // Check if user exists
        if (registerService.isUserExists(username)) {
            showAlert("Registration Failed", "Username already exists", Alert.AlertType.ERROR);
            return;
        }

        // Prepare teacher information
        Map<String, String> teacherInfo = new HashMap<>();
        teacherInfo.put("username", username);
        teacherInfo.put("name", name);
        teacherInfo.put("gender", gender);
        teacherInfo.put("age", ageText);
        teacherInfo.put("position", position);
        teacherInfo.put("department", department);
        teacherInfo.put("password", password);

        // Register the teacher
        try {
            registerService.registerTeacher(teacherInfo);
            showAlert("Registration Successful", "You have successfully registered", Alert.AlertType.INFORMATION);

            // Close the registration window
            Stage stage = (Stage) usernameTxt.getScene().getWindow();
            stage.close();
        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Registration Failed", "Error occurred while saving user data", Alert.AlertType.ERROR);
        }
    }

    /**
     * Handles the action of closing the registration window.
     */
    @FXML
    private void handleClose() {
        // Close the registration window
        Stage stage = (Stage) usernameTxt.getScene().getWindow();
        stage.close();
    }

    /**
     * Displays an alert dialog with the specified title, message, and alert type.
     *
     * @param title   the title of the alert dialog
     * @param message the message to be displayed in the alert dialog
     * @param type    the type of alert (e.g., ERROR, INFORMATION)
     */
    private void showAlert(String title, String message, Alert.AlertType type) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }
}