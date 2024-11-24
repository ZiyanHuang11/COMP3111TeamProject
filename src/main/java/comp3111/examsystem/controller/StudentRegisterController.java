package comp3111.examsystem.controller;

import comp3111.examsystem.data.DataManager;
import comp3111.examsystem.entity.Student;
import comp3111.examsystem.service.StudentRegisterService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

public class StudentRegisterController {

    @FXML
    private TextField usernameField;
    @FXML
    private TextField nameField;
    @FXML
    private TextField genderField;
    @FXML
    private TextField departmentField;
    @FXML
    private PasswordField passwordField;

    private final StudentRegisterService registerService;

    // 构造函数
    public StudentRegisterController() {
        DataManager dataManager = new DataManager(); // 初始化 DataManager
        this.registerService = new StudentRegisterService(dataManager); // 将 DataManager 传递给服务类
    }

    @FXML
    private void handleRegister(ActionEvent event) {
        String username = usernameField.getText().trim();
        String name = nameField.getText().trim();
        String gender = genderField.getText().trim();
        String department = departmentField.getText().trim();
        String password = passwordField.getText().trim();

        // 检查输入是否为空
        if (username.isEmpty() || name.isEmpty() || gender.isEmpty() || department.isEmpty() || password.isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Input Error", "All fields are required.");
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

    private void clearFields() {
        usernameField.clear();
        nameField.clear();
        genderField.clear();
        departmentField.clear();
        passwordField.clear();
    }

    private void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
