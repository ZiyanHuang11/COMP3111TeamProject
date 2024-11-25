package comp3111.examsystem.controller;

import comp3111.examsystem.data.DataManager;
import comp3111.examsystem.entity.Teacher;
import comp3111.examsystem.service.TeacherRegisterService;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.util.Optional;

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

    @FXML
    public void initialize() {
        // Initialize gender ComboBox
        genderComboBox.getItems().addAll("Male", "Female", "Other");
        genderComboBox.setValue("Gender");

        // Initialize position ComboBox
        positionComboBox.getItems().addAll("Professor", "Lecturer", "Assistant Professor", "Researcher");
        positionComboBox.setValue("Position");

        // Initialize the service with DataManager
        DataManager dataManager = new DataManager();
        registerService = new TeacherRegisterService(dataManager);
    }

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

        // Check if the username already exists
        if (registerService.isUserExists(username)) {
            showAlert("Registration Failed", "Username already exists", Alert.AlertType.ERROR);
            return;
        }

        // Prepare teacher information
        Optional<Teacher> teacherOptional = registerService.prepareTeacher(username, name, gender, ageText, position, department, password);

        if (teacherOptional.isPresent()) {
            registerService.registerTeacher(teacherOptional.get());
            showAlert("Registration Successful", "You have successfully registered", Alert.AlertType.INFORMATION);

            // Close the registration window
            Stage stage = (Stage) usernameTxt.getScene().getWindow();
            stage.close();
        } else {
            showAlert("Registration Failed", "Error preparing teacher data.", Alert.AlertType.ERROR);
        }
    }

    @FXML
    private void handleClose() {
        // Close the registration window
        Stage stage = (Stage) usernameTxt.getScene().getWindow();
        stage.close();
    }

    private void showAlert(String title, String message, Alert.AlertType type) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
