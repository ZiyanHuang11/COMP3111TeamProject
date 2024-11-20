package comp3111.examsystem.controller;

import comp3111.examsystem.Main;
import comp3111.examsystem.service.StudentLoginService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;

public class StudentLoginController {
    @FXML
    private TextField usernameTxt;
    @FXML
    private PasswordField passwordTxt;
    public TextField usernameField;
    public PasswordField passwordField;

    public void handleLogin() {
        String username = usernameField.getText();
        String password = passwordField.getText();

        // 简单校验用户名和密码
        if ("student".equals(username) && "password".equals(password)) {
            try {
                FXMLLoader loader = new FXMLLoader(Main.class.getResource("StudentMainUI.fxml"));
                Stage stage = new Stage();
                stage.setTitle("Student Main");
                stage.setScene(new Scene(loader.load()));
                stage.show();

                // 关闭当前窗口
                Stage currentStage = (Stage) usernameField.getScene().getWindow();
                currentStage.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Login Failed");
            alert.setHeaderText(null);
            alert.setContentText("Invalid username or password.");
            alert.showAndWait();
        }
    }

    private StudentLoginService loginService;

    public StudentLoginController() {
        loginService = new StudentLoginService();
    }

    @FXML
    public void login(ActionEvent e) {
        String username = usernameTxt.getText().trim();
        String password = passwordTxt.getText().trim();

        try {
            if (loginService.validateLogin(username, password)) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Hint");
                alert.setHeaderText(null);
                alert.setContentText("Login successful");
                alert.showAndWait();

                FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("StudentMainUI.fxml"));
                Stage stage = new Stage();
                stage.setTitle("Hi " + username + ", Welcome to HKUST Examination System");
                stage.setScene(new Scene(fxmlLoader.load()));
                stage.show();
                ((Stage) ((Button) e.getSource()).getScene().getWindow()).close();
            }
        } catch (IllegalArgumentException ex) {
            showAlert(Alert.AlertType.ERROR, "Login Failed", ex.getMessage());
            usernameTxt.clear();
            passwordTxt.clear();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    @FXML
    public void register(ActionEvent e) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("StudentRegisterUI.fxml"));
            Stage stage = new Stage();
            stage.setTitle("Student Registration");
            stage.setScene(new Scene(fxmlLoader.load()));
            stage.show();
            ((Stage) ((Button) e.getSource()).getScene().getWindow()).close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    };

    private void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

}
