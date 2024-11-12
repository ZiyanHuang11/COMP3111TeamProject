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

import java.io.*;
import java.net.URL;
import java.util.ResourceBundle;


public class ManagerLoginController implements Initializable {
    @FXML
    private TextField usernameTxt;
    @FXML
    private PasswordField passwordTxt;

    private String managerFilePath;

    public void initialize(URL location, ResourceBundle resources) {

    }
    // using three different txt for three types of user
    public ManagerLoginController() {
        managerFilePath = "data/managers.txt";
        File studentFile = new File(managerFilePath);
        if (studentFile.exists()) {
            System.out.println("Student file found at: " + studentFile.getAbsolutePath());
        } else {
            System.out.println("Student file not found!");
        }
    }

    @FXML
    public void login(ActionEvent e) {
        String username = usernameTxt.getText();
        String password = passwordTxt.getText();

        if (validate(username, password)) {
            // first show a message and after click show the FXML
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
            // if the password or username is wrong
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Hint");
            alert.setHeaderText(null);
            alert.setContentText("Invalid username or password");
            alert.showAndWait();
        }
    }

    private boolean validate(String username, String password) {
        try (BufferedReader br = new BufferedReader(new FileReader(managerFilePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                // using ',' to split text in the txt
                String[] credentials = line.split(",");
                if (credentials.length == 2) {
                    String storedUsername = credentials[0].trim();
                    String storedPassword = credentials[1].trim();
                    // if match return true
                    if (storedUsername.equals(username) && storedPassword.equals(password)) {
                        return true;
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

}
