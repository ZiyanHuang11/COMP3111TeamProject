package comp3111.examsystem.controller;

import comp3111.examsystem.Main;
import comp3111.examsystem.service.StudentRegisterService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;

public class StudentRegisterController {
    @FXML
    private TextField usernameTxt;
    @FXML
    private PasswordField passwordTxt;
    @FXML
    private PasswordField confirmPasswordTxt;
    @FXML
    private TextField nameTxt;
    @FXML
    private ComboBox<String> genderBox;
    @FXML
    private TextField departmentTxt;

    private StudentRegisterService registerService;

    public StudentRegisterController() {
        registerService = new StudentRegisterService();
    }

    @FXML
    public void register(ActionEvent e) {
        String username = usernameTxt.getText().trim();
        String password = passwordTxt.getText().trim();
        String confirmPassword = confirmPasswordTxt.getText().trim();
        String name = nameTxt.getText().trim();
        String gender = genderBox.getValue();
        String department = departmentTxt.getText().trim();

        if (username.isEmpty() || password.isEmpty() || confirmPassword.isEmpty() || name.isEmpty() || gender == null || department.isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "Input Required", "Please fill in all fields.");
            return;
        }

        if (!password.equals(confirmPassword)) {
            showAlert(Alert.AlertType.ERROR, "Password Mismatch", "Passwords do not match. Please try again.");
            return;
        }

        if (registerService.isUsernameTaken(username)) {
            showAlert(Alert.AlertType.ERROR, "Username Taken", "This username is already taken. Please choose another one.");
            return;
        }

        try {
            registerService.registerStudent(username, password, name, gender, department);
            showAlert(Alert.AlertType.INFORMATION, "Registration Successful", "Your account has been successfully created!");
            goToLogin();
        } catch (IOException ex) {
            ex.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "File Error", "An error occurred while saving your information. Please try again.");
        }
    }

    @FXML
    public void cancel(ActionEvent e) {
        goToLogin();
    }

    private void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void goToLogin() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("StudentLoginUI.fxml"));
            Stage stage = new Stage();
            stage.setTitle("Login");
            stage.setScene(new Scene(fxmlLoader.load()));
            stage.show();
            Stage currentStage = (Stage) usernameTxt.getScene().getWindow();
            currentStage.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}
