package comp3111.examsystem.controller;

import comp3111.examsystem.data.DataManager;
import comp3111.examsystem.service.StudentRegisterService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;

public class StudentRegisterController {

    @FXML
    private TextField usernameTxt;
    @FXML
    private TextField nameTxt;
    @FXML
    private ComboBox<String> genderBox;
    @FXML
    private TextField departmentTxt;
    @FXML
    private PasswordField passwordTxt;
    @FXML
    private PasswordField confirmPasswordTxt;

    private StudentRegisterService registerService;

    @FXML
    public void initialize() {
        // 初始化性别选项
        genderBox.getItems().addAll("Male", "Female", "Other");
        // 初始化 DataManager 和服务类
        DataManager dataManager = new DataManager(); // 如果 DataManager 是单例，可以修改为 DataManager.getInstance()
        this.registerService = new StudentRegisterService(dataManager);
    }

    @FXML
    private void register(ActionEvent event) {
        String username = usernameTxt.getText().trim();
        String name = nameTxt.getText().trim();
        String gender = genderBox.getValue();
        String department = departmentTxt.getText().trim();
        String password = passwordTxt.getText().trim();
        String confirmPassword = confirmPasswordTxt.getText().trim();

        // 检查输入是否为空
        if (username.isEmpty() || name.isEmpty() || gender == null || gender.isEmpty()
                || department.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Input Error", "All fields are required.");
            return;
        }

        // 检查密码是否匹配
        if (!password.equals(confirmPassword)) {
            showAlert(Alert.AlertType.ERROR, "Password Mismatch", "Passwords do not match. Please try again.");
            return;
        }

        try {
            // 检查用户名是否已存在
            if (registerService.isUsernameTaken(username)) {
                showAlert(Alert.AlertType.ERROR, "Username Taken", "The username is already taken. Please choose another.");
                return;
            }

            // 注册学生
            registerService.registerStudent(username, password, name, gender, department);
            showAlert(Alert.AlertType.INFORMATION, "Success", "Registration successful!");

            // 清空输入字段
            clearFields();

        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Error", "An error occurred: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    private void cancel(ActionEvent event) {
        try {
            // 加载学生登录界面的 FXML 文件
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/comp3111/examsystem/StudentLoginUI.fxml"));
            Parent loginRoot = loader.load();

            // 获取当前窗口并设置新的场景
            Stage currentStage = (Stage) usernameTxt.getScene().getWindow();
            currentStage.setScene(new Scene(loginRoot));
            currentStage.setTitle("Student Login");
        } catch (IOException e) {
            // 捕获并显示错误信息
            showAlert(Alert.AlertType.ERROR, "Error", "Unable to load login screen: " + e.getMessage());
            e.printStackTrace();
        }
    }


    private void clearFields() {
        usernameTxt.clear();
        nameTxt.clear();
        genderBox.setValue(null);
        departmentTxt.clear();
        passwordTxt.clear();
        confirmPasswordTxt.clear();
    }

    private void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
