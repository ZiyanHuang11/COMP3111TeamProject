package comp3111.examsystem.controller;

import comp3111.examsystem.Main;
import comp3111.examsystem.data.DataManager;
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
        // 使用 DataManager 初始化 TeacherLoginService
        DataManager dataManager = new DataManager();
        loginService = new TeacherLoginService(dataManager);
    }

    @FXML
    public void login(ActionEvent e) {
        String username = usernameTxt.getText();
        String password = passwordTxt.getText();

        try {
            if (loginService.validate(username, password)) {
                // 登录成功，显示成功提示并跳转主界面
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Hint");
                alert.setHeaderText(null);
                alert.setContentText("Login successful");
                alert.showAndWait();

                // 加载教师主界面
                FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("TeacherMainUI.fxml"));
                Stage stage = new Stage();
                stage.setTitle("Hi " + username + ", Welcome to HKUST Examination System");
                stage.setScene(new Scene(fxmlLoader.load()));
                stage.show();

                // 关闭登录窗口
                ((Stage) ((Button) e.getSource()).getScene().getWindow()).close();
            } else {
                // 登录失败，显示错误提示
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Hint");
                alert.setHeaderText(null);
                alert.setContentText("Invalid username or password");
                alert.showAndWait();
            }
        } catch (IOException ex) {
            ex.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText(null);
            alert.setContentText("An error occurred while processing your login.");
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

            // 关闭登录窗口
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
