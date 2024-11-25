package comp3111.examsystem.controller;

import comp3111.examsystem.Main;
import comp3111.examsystem.data.DataManager;
import comp3111.examsystem.entity.Teacher;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

public class TeacherMainController implements Initializable {
    @FXML
    private VBox mainbox;

    private final DataManager dataManager;
    private Teacher loggedInTeacher;

    // 构造函数：注入 DataManager 和当前登录的教师信息
    public TeacherMainController(DataManager dataManager, Teacher loggedInTeacher) {
        this.dataManager = dataManager;
        this.loggedInTeacher = loggedInTeacher;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        if (loggedInTeacher != null) {
            showAlert(Alert.AlertType.INFORMATION, "Welcome", "Welcome, " + loggedInTeacher.getName() + "!");
        }
    }

    @FXML
    public void openQuestionManageUI() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("QuestionBankManagementUI.fxml"));
            Stage stage = new Stage();
            stage.setTitle("Question Bank Management");
            stage.setScene(new Scene(fxmlLoader.load()));

            // 注入 DataManager 和教师数据到 QuestionBankManagementController
            QuestionBankManagementController controller = new QuestionBankManagementController(dataManager, loggedInTeacher);
            fxmlLoader.setController(controller);

            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void openExamManageUI() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("ExamManagementUI.fxml"));
            Stage stage = new Stage();
            stage.setTitle("Exam Management");
            stage.setScene(new Scene(fxmlLoader.load()));

            // 注入 DataManager 和教师数据到 ExamManagementController
            ExamManagementController controller = new ExamManagementController(dataManager, loggedInTeacher);
            fxmlLoader.setController(controller);

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

            // 注入 DataManager 和教师数据到 TeacherGradeStatisticController
            TeacherGradeStatisticController controller = new TeacherGradeStatisticController(dataManager, loggedInTeacher);
            fxmlLoader.setController(controller);

            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void exit() {
        System.exit(0);
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
