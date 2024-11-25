package comp3111.examsystem.controller;

import comp3111.examsystem.Main;
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

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

/**
 * Controller for students to login in the exam system
 */

public class StudentLoginController implements Initializable {
    @FXML
    private TextField usernameTxt;
    @FXML
    private PasswordField passwordTxt;
    // Path to the student information file
    private String studentFilePath;

    /**
     * Initializes the controller and checks for the existence of the student file.
     */
    public void initialize(URL location, ResourceBundle resources) {
        // No specific initialization required at this moment
    }

    /**
     * Constructor for the StudentLoginController.
     * Sets the path to the student file and checks its existence.
     */
    public StudentLoginController() {
        studentFilePath = "data/students.txt";
        File studentFile = new File(studentFilePath);
        if (studentFile.exists()) {
            System.out.println("Student file found at: " + studentFile.getAbsolutePath());
        } else {
            System.out.println("Student file not found!");
        }
    }

    /**
     * Handles the login action when the user attempts to log in.
     * Displays an alert indicating success or failure of the login attempt.
     *
     * @param e the action event triggered by the login button
     */
    @FXML
    public void login(ActionEvent e) {
        String username = usernameTxt.getText();
        String password = passwordTxt.getText();

        if (validate(username, password)) {
            // Show success alert and load the main UI
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Hint");
            alert.setHeaderText(null);
            alert.setContentText("Login successful");
            alert.showAndWait();
            FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("StudentMainUI.fxml"));
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
            // Show error alert for invalid login
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Hint");
            alert.setHeaderText(null);
            alert.setContentText("Invalid username or password");
            alert.showAndWait();
        }
    }

    /**
     * Validates the provided username and password against stored credentials in the student file.
     *
     * @param username the username to validate
     * @param password the password to validate
     * @return true if the username and password match an entry in the file, false otherwise
     */
    private boolean validate(String username, String password) {
        // BufferedReader to read file contents
        try (BufferedReader br = new BufferedReader(new FileReader(studentFilePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                // Split the line into credentials
                String[] credentials = line.split(",");
                if (credentials.length == 2) {
                    // Check if the username and password match
                    String storedUsername = credentials[0].trim();
                    String storedPassword = credentials[1].trim(); // Adjusted index to 1 for password
                    if (storedUsername.equals(username) && storedPassword.equals(password)) {
                        return true;
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false; // No matching username and password found
    }

    /**
     * Handles the registration action for new students.
     * (Currently not implemented)
     */
    @FXML
    public void register() {
        // Registration logic to be implemented
    }
}
