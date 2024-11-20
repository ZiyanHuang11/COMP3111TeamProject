package comp3111.examsystem.controller;

import comp3111.examsystem.Main;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

public class StudentMainController implements Initializable {
    @FXML
    private ComboBox<String> examCombox; // 考试选择框

    private static final Logger logger = Logger.getLogger(StudentMainController.class.getName());

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // 初始化考试列表
        examCombox.getItems().addAll(
                "COMP3111 Software Engineering I | quiz1",
                "COMP3111 Software Engineering I | quiz2",
                "COMP5111 Software Engineering II | quiz1",
                "COMP5111 Software Engineering II | quiz2"
        );
        examCombox.setPromptText("Select an exam");
    }


    @FXML
    public void openExamUI() {
        String selectedExam = examCombox.getValue(); // 获取选中的考试
        if (selectedExam == null) {
            // 如果没有选择考试，显示警告
            showAlert(Alert.AlertType.WARNING, "No Exam Selected", "Please select an exam before starting.");
            return;
        }

        try {
            // 加载测验界面并传递测验名称
            FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("QuizScreenUI.fxml"));
            Stage stage = new Stage();
            stage.setTitle("Start Exam");
            Scene scene = new Scene(fxmlLoader.load());
            QuizScreenController controller = fxmlLoader.getController();
            controller.initializeQuiz(selectedExam); // 初始化测验名称和相关信息
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            logger.log(Level.SEVERE, "Failed to load the quiz UI.", e);
            showAlert(Alert.AlertType.ERROR, "Error", "Failed to load the quiz UI.");
        }
    }

    @FXML
    public void openGradeStatistic() {
        try {
            // 加载成绩统计界面
            FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("GradeStatisticUI.fxml"));
            Stage stage = new Stage();
            stage.setTitle("Grade Statistics");
            stage.setScene(new Scene(fxmlLoader.load()));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Error", "Failed to load the grade statistics UI.");
        }
    }

    @FXML
    public void exit() {
        // 退出应用程序
        System.exit(0);
    }

    private void showAlert(Alert.AlertType alertType, String title, String message) {
        // 通用的弹窗提示方法
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}

