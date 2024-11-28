package comp3111.examsystem.controller;

import comp3111.examsystem.Main;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import comp3111.examsystem.entity.Teacher;

import java.net.URL;
import java.util.ResourceBundle;

public class TeacherMainController implements Initializable {
    @FXML
    private VBox mainbox;

    private Teacher loggedInTeacher;
    public void setLoggedInTeacher(Teacher teacher) {
        this.loggedInTeacher = teacher;
    }

    public void initialize(URL location, ResourceBundle resources) {
    }

    @FXML
    public void openQuestionManageUI() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("QuestionBankManagementUI.fxml"));
            Stage stage = new Stage();
            stage.setTitle("Question Bank Management");
            stage.setScene(new Scene(fxmlLoader.load()));
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void openExamManageUI() {
        try {
            // 确保路径正确，根据项目结构调整
            FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("/comp3111/examsystem/ExamManagementUI.fxml"));

            // 加载 FXML 并获取根节点
            Parent root = fxmlLoader.load();

            // 获取控制器实例
            ExamManagementController controller = fxmlLoader.getController();
            // 将当前登录的教师对象传递给控制器
            controller.setLoggedInTeacher(loggedInTeacher);

            // 创建新 Stage 和 Scene
            Stage stage = new Stage();
            stage.setTitle("Exam Management");
            stage.setScene(new Scene(root));
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void openGradeStatistic() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("TeacherGradeStatistic.fxml"));
            Stage stage = new Stage();
            stage.setTitle("Grade Statistics");
            stage.setScene(new Scene(fxmlLoader.load()));
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void exit() {
        System.exit(0);
    }
}