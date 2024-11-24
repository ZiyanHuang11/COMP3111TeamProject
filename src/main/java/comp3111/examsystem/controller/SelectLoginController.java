package comp3111.examsystem.controller;

import java.io.IOException;

import comp3111.examsystem.Main;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.control.Button;


public class SelectLoginController {
    @FXML
    private Button studentLoginButton;
    @FXML
    private Button teacherLoginButton;
    @FXML
    private Button managerLoginButton;
    @FXML
    public void studentLogin() {
        try {
            Stage stage = (Stage) studentLoginButton.getScene().getWindow(); // 获取当前窗口
            stage.close(); // 关闭当前窗口
            Stage newStage = new Stage();
            FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("StudentLoginUI.fxml"));
            Scene scene = new Scene(fxmlLoader.load());
            newStage.setTitle("Student Login");
            newStage.setScene(scene);
            newStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void teacherLogin() {
        try {
            Stage stage = (Stage) teacherLoginButton.getScene().getWindow(); // 获取当前窗口
            stage.close(); // 关闭当前窗口
            Stage newStage = new Stage();
            FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("TeacherLoginUI.fxml"));
            Scene scene = new Scene(fxmlLoader.load());
            newStage.setTitle("Teacher Login");
            newStage.setScene(scene);
            newStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void managerLogin() {
        try {
            Stage stage = (Stage) managerLoginButton.getScene().getWindow(); // 获取当前窗口
            stage.close(); // 关闭当前窗口
            Stage newStage = new Stage();
            FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("ManagerLoginUI.fxml"));
            Scene scene = new Scene(fxmlLoader.load());
            newStage.setTitle("Manager Login");
            newStage.setScene(scene);
            newStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
