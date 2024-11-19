package comp3111.examsystem.controller;

import comp3111.examsystem.Main;
import comp3111.examsystem.service.TeacherLoginService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class TeacherLoginController implements Initializable {
    @FXML
    private TextField usernameTxt;
    @FXML
    private PasswordField passwordTxt;

    private TeacherLoginService loginService;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        String teacherFilePath = "data/teachers.txt";
        loginService = new TeacherLoginService(teacherFilePath);
    }

    @FXML
    public void login(ActionEvent e) {
        String username = usernameTxt.getText();
        String password = passwordTxt.getText();

        if (loginService.validate(username, password)) {
            // Login successful, show success alert and proceed
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Hint");
            alert.setHeaderText(null);
            alert.setContentText("Login successful");
            alert.showAndWait();

            // Load the main UI for teachers
            FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("TeacherMainUI.fxml"));
            Stage stage = new Stage();
            stage.setTitle("Hi " + username + ", Welcome to HKUST Examination System");
            try {
                stage.setScene(new Scene(fxmlLoader.load()));
            } catch (IOException e1) {
                e1.printStackTrace();
            }
            stage.show();

            // Close the login window
            ((Stage) ((Button) e.getSource()).getScene().getWindow()).close();
        } else {
            // Invalid credentials, show error alert
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Hint");
            alert.setHeaderText(null);
            alert.setContentText("Invalid username or password");
            alert.showAndWait();
        }
    }

    @FXML
    public void register(ActionEvent e) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/comp3111/examsystem/TeacherRegisterUI.fxml"));
            Stage stage = new Stage();
            stage.setTitle("Teacher Registration");
            stage.setScene(new Scene(fxmlLoader.load()));
            stage.show();

            // Close the login window
            ((Stage) ((Button) e.getSource()).getScene().getWindow()).close();
        } catch (IOException ex) {
            ex.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText(null);
            alert.setContentText("Failed to open registration window");
            alert.showAndWait();
        }
    }
}
