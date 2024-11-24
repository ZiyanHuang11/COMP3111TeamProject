package comp3111.examsystem.controller;

import comp3111.examsystem.Main;
import comp3111.examsystem.data.DataManager;
import comp3111.examsystem.entity.Student;
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

    private final DataManager dataManager;

    // 构造函数：初始化 DataManager
    public StudentLoginController() {
        dataManager = new DataManager();
    }

    /**
     * 处理登录逻辑
     */
    @FXML
    public void login(ActionEvent e) {
        String username = usernameTxt.getText().trim();
        String password = passwordTxt.getText().trim();

        if (username.isEmpty() || password.isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "Input Error", "Username and password cannot be empty.");
            return;
        }

        // 验证用户名和密码
        Student student = dataManager.getStudents().stream()
                .filter(s -> s.getUsername().equals(username) && s.getPassword().equals(password))
                .findFirst()
                .orElse(null);

        if (student != null) {
            // 登录成功弹窗
            showAlert(Alert.AlertType.INFORMATION, "Login Successful", "Welcome, " + student.getName() + "!");

            try {
                // 跳转到主界面
                FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("StudentMainUI.fxml"));
                Stage stage = new Stage();
                stage.setTitle("Hi " + student.getName() + ", Welcome to HKUST Examination System");
                stage.setScene(new Scene(fxmlLoader.load()));
                stage.show();

                // 关闭当前窗口
                ((Stage) ((Button) e.getSource()).getScene().getWindow()).close();
            } catch (IOException ex) {
                ex.printStackTrace();
                showAlert(Alert.AlertType.ERROR, "Error", "An error occurred while loading the next screen.");
            }
        } else {
            // 登录失败
            showAlert(Alert.AlertType.ERROR, "Login Failed", "Invalid username or password.");
        }
    }

    /**
     * 处理注册逻辑
     */
    @FXML
    public void register(ActionEvent e) {
        try {
            // 跳转到注册界面
            FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("StudentRegisterUI.fxml"));
            Stage stage = new Stage();
            stage.setTitle("Student Registration");
            stage.setScene(new Scene(fxmlLoader.load()));
            stage.show();

            // 关闭当前窗口
            ((Stage) ((Button) e.getSource()).getScene().getWindow()).close();
        } catch (IOException ex) {
            ex.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Error", "An error occurred while loading the registration screen.");
        }
    }

    /**
     * 显示提示信息
     */
    private void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
