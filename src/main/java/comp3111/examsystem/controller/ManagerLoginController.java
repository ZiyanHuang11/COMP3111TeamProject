package comp3111.examsystem.controller;

import comp3111.examsystem.Main;
import comp3111.examsystem.service.ManagerLoginService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * Controller for the manager login action
 */

public class ManagerLoginController implements Initializable {
    @FXML
    private TextField usernameTxt; // TextField for entering the username
    @FXML
    private PasswordField passwordTxt; // PasswordField for entering the password

    private ManagerLoginService managerLoginService; // Service for managing login operations

    /**
     * Constructs a ManagerLoginController and initializes the ManagerLoginService.
     */
    public ManagerLoginController() {
        managerLoginService = new ManagerLoginService("data/managers.txt");
    }

    /**
     * Initializes the controller after its root element has been processed.
     *
     * @param location  The location used to resolve relative paths for the root object,
     *                  or null if the location is not known.
     * @param resources The resources used to localize the root object,
     *                  or null if the root object was not localized.
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Initialization logic can be added here if needed
    }

    /**
     * Handles the login action when the user clicks the login button.
     *
     * @param e The ActionEvent triggered by the button click.
     */
    @FXML
    public void login(ActionEvent e) {
        String username = usernameTxt.getText();
        String password = passwordTxt.getText();

        if (managerLoginService.validate(username, password)) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Hint");
            alert.setHeaderText(null);
            alert.setContentText("Login successful");
            alert.showAndWait();

            FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("ManagerMainUI.fxml"));
            Stage stage = new Stage();
            stage.setTitle("Hi " + username + ", Welcome to HKUST Examination System");
            try {
                stage.setScene(new Scene(fxmlLoader.load()));
            } catch (IOException e1) {
                e1.printStackTrace();
            }
            stage.show();
            ((Stage) ((Button) e.getSource()).getScene().getWindow()).close();
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Hint");
            alert.setHeaderText(null);
            alert.setContentText("Invalid username or password");
            alert.showAndWait();
        }
    }
}