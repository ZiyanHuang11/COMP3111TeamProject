package comp3111.examsystem.controller;

import comp3111.examsystem.Main;
import comp3111.examsystem.entity.Teacher;
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
        String teacherFilePath = "data/teachers.txt";
        loginService = new TeacherLoginService(teacherFilePath);
    }

    @FXML
    public void login(ActionEvent e) {
        String username = usernameTxt.getText();
        String password = passwordTxt.getText();

        Teacher loggedInTeacher = loginService.validate(username, password);
        if (loggedInTeacher != null) {
            // 登录成功
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Hint");
            alert.setHeaderText(null);
            alert.setContentText("Login Successful");
            alert.showAndWait();

            try {
                FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("TeacherMainUI.fxml"));
                Stage stage = new Stage();
                stage.setTitle("Hi " + username + ", Welcome to HKUST Examination System");
                stage.setScene(new Scene(fxmlLoader.load()));

                // 获取控制器实例
                TeacherMainController controller = fxmlLoader.getController();
                // 将当前登录的教师对象传递给控制器
                controller.setLoggedInTeacher(loggedInTeacher);

                stage.show();

                // 关闭登录窗口
                ((Stage) ((Button) e.getSource()).getScene().getWindow()).close();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        } else {
            // 登录失败
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

            // Close the login window
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