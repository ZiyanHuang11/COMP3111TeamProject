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

/**
 * Controller for managing the teacher login interface in the examination system.
 */
public class TeacherLoginController implements Initializable {

    @FXML
    private TextField usernameTxt;
    @FXML
    private PasswordField passwordTxt;

    private TeacherLoginService loginService;

    /**
     * Initializes the controller and sets up the teacher login service.
     *
     * @param location  the location used to resolve relative paths for the root object,
     *                  or null if the location is not known
     * @param resources the resources used to localize the root object,
     *                  or null if the root object was not localized
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        String teacherFilePath = "data/teachers.txt";
        loginService = new TeacherLoginService(teacherFilePath);
    }

    /**
     * Handles the login action when the user attempts to log in.
     *
     * @param e the action event triggered by the login button
     */
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

    /**
     * Handles the registration action for new teachers.
     *
     * @param e the action event triggered by the register button
     */
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