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

public class StudentLoginController implements Initializable {
    @FXML
    private TextField usernameTxt;
    @FXML
    private PasswordField passwordTxt;
    // private存学生信息路径
    private String studentFilePath;

    public void initialize(URL location, ResourceBundle resources) {

    }

    public StudentLoginController() {
        studentFilePath = "data/students.txt";
        File studentFile = new File(studentFilePath);
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
            // 登陆成功后先弹窗再显示fxml界面
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
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Hint");
            alert.setHeaderText(null);
            alert.setContentText("Invalid username or password");
            alert.showAndWait();
        }
    }

    // 这里封装一个函数用来从txt读用户信息
    private boolean validate(String username, String password) {
        // bufferedReader，用来读文件内容
        try (BufferedReader br = new BufferedReader(new FileReader(studentFilePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                // 这里遍历database
                // 后续注册内容完成后可能要调整txt数据结构，这里暂时是用','分开，目前共有六个column，0是用户名5是密码
                String[] credentials = line.split(",");
                if (credentials.length == 2) {
                    // 检查username和password是否匹配
                    String storedUsername = credentials[0].trim();
                    String storedPassword = credentials[5].trim();
                    if (storedUsername.equals(username) && storedPassword.equals(password)) {
                        return true;
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false; // 没有找到匹配的用户名和密码
    }

    @FXML
    public void register() {
    }
}
