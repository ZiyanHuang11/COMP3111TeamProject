package comp3111.examsystem.controller;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileWriter;
import java.io.FileReader;
import java.io.IOException;

public class TeacherRegisterController {
    @FXML
    private TextField usernameTxt;
    @FXML
    private TextField nameTxt;
    @FXML
    private ComboBox<String> genderComboBox;
    @FXML
    private TextField ageTxt;
    @FXML
    private ComboBox<String> positionComboBox;
    @FXML
    private TextField departmentTxt;
    @FXML
    private PasswordField passwordTxt;
    @FXML
    private PasswordField confirmPasswordTxt;

    private String teacherFilePath = "data/teachers.txt";

    @FXML
    public void initialize() {
        // Initialize gender ComboBox
        genderComboBox.getItems().addAll("Male", "Female", "Other");
        genderComboBox.setValue("Gender");

        // Initialize position ComboBox
        positionComboBox.getItems().addAll("Professor", "Lecturer", "Assistant Professor", "Researcher");
        positionComboBox.setValue("Position");
    }

    @FXML
    private void handleRegister() {
        String username = usernameTxt.getText();
        String name = nameTxt.getText();
        String gender = genderComboBox.getValue();
        String age = ageTxt.getText();
        String position = positionComboBox.getValue();
        String department = departmentTxt.getText();
        String password = passwordTxt.getText();
        String confirmPassword = confirmPasswordTxt.getText();

        // 检查所有必填字段是否为空
        if (username.isEmpty() || name.isEmpty() || gender == null || age.isEmpty() ||
                position == null || department.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
            showAlert("Registration Failed", "All fields are required", Alert.AlertType.ERROR);
            return;
        }

        // 检查是否选择了提示项
        if ("Gender".equals(gender) || "Position".equals(position)) {
            showAlert("Registration Failed", "Please select your gender and position", Alert.AlertType.ERROR);
            return;
        }

        // 检查密码是否匹配
        if (!password.equals(confirmPassword)) {
            showAlert("Registration Failed", "Passwords do not match", Alert.AlertType.ERROR);
            return;
        }

        // 检查用户名是否已存在
        if (isUserExists(username)) {
            showAlert("Registration Failed", "Username already exists", Alert.AlertType.ERROR);
            return;
        }

        // 将新用户信息写入文件
        try {
            File file = new File(teacherFilePath);
            if (!file.exists()) {
                file.createNewFile();
            }

            // 使用 FileWriter 以追加模式写入
            FileWriter writer = new FileWriter(file, true);
            writer.write(username + "," + password + "," + name + "," + gender + "," + age + "," + position + "," + department + "\n");
            writer.close();

            showAlert("Registration Successful", "You have successfully registered", Alert.AlertType.INFORMATION);

            // 关闭注册窗口
            Stage stage = (Stage) usernameTxt.getScene().getWindow();
            stage.close();
        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Registration Failed", "Error occurred while saving user data", Alert.AlertType.ERROR);
        }
    }

    // 检查用户是否已存在
    private boolean isUserExists(String username) {
        try (BufferedReader br = new BufferedReader(new FileReader(teacherFilePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] credentials = line.split(",");
                if (credentials.length > 0) {
                    String storedUsername = credentials[0].trim();
                    if (storedUsername.equals(username)) {
                        return true; // 用户已存在
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false; // 用户不存在
    }

    // 显示提示框
    private void showAlert(String title, String message, Alert.AlertType type) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }

    @FXML
    private void handleClose() {
        // 关闭注册窗口
        Stage stage = (Stage) usernameTxt.getScene().getWindow();
        stage.close();
    }
}
