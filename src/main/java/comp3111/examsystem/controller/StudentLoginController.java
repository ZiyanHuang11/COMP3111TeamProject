package comp3111.examsystem.controller;

import comp3111.examsystem.service.StudentLoginService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class StudentLoginController implements Initializable {
    @FXML
    private TextField usernameTxt;

    @FXML
    private PasswordField passwordTxt;

    private StudentLoginService studentLoginService;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // 初始化服务类，提供文件路径
        String studentFilePath = "data/students.txt";
        studentLoginService = new StudentLoginService(studentFilePath);

        File studentFile = new File(studentFilePath);
        if (studentFile.exists()) {
            System.out.println("Student file found at: " + studentFile.getAbsolutePath());
        } else {
            System.out.println("Student file not found!");
        }
    }

    @FXML
    public void login(ActionEvent e) {
        String username = usernameTxt.getText().trim();
        String password = passwordTxt.getText().trim();

        // 验证用户名和密码
        if (studentLoginService.validateLogin(username, password)) {
            showAlert("Hint", "Login successful", Alert.AlertType.INFORMATION);

            // 加载主界面并传递登录用户名
            try {
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/comp3111/examsystem/StudentMainUI.fxml"));
                Stage stage = new Stage();
                stage.setScene(new Scene(fxmlLoader.load()));

                // 获取 StudentMainController 实例，并传递用户名
                StudentMainController controller = fxmlLoader.getController();
                controller.setLoggedInUsername(username);

                stage.setTitle("Hi " + username + ", Welcome to HKUST Examination System");
                stage.show();

                // 关闭当前登录窗口
                ((Stage) ((Button) e.getSource()).getScene().getWindow()).close();
            } catch (IOException ex) {
                ex.printStackTrace();
                showAlert("Error", "Failed to load the main interface.", Alert.AlertType.ERROR);
            }
        } else {
            // 登录失败提示
            showAlert("Hint", "Invalid username or password", Alert.AlertType.ERROR);
        }
    }

    @FXML
    public void register(ActionEvent e) {
        // 打开注册界面
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/comp3111/examsystem/StudentRegisterUI.fxml"));
            Stage stage = new Stage();
            stage.setTitle("Student Register");
            stage.setScene(new Scene(fxmlLoader.load()));
            stage.show();

            // 关闭当前登录窗口
            ((Stage) ((Button) e.getSource()).getScene().getWindow()).close();
        } catch (IOException ex) {
            ex.printStackTrace();
            showAlert("Error", "Failed to open registration window", Alert.AlertType.ERROR);
        }
    }

    private void showAlert(String title, String message, Alert.AlertType type) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
