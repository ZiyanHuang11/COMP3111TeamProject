package comp3111.examsystem.controller;

import comp3111.examsystem.entity.Exam;
import comp3111.examsystem.service.ExamService;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;


import java.net.URL;
import java.util.ResourceBundle;
import java.io.IOException;


public class StudentMainController implements Initializable {
    @FXML
    private ComboBox<String> examCombox;

    private ExamService examService;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        examService = new ExamService();
        loadExams();
    }

    private void loadExams() {
        for (Exam exam : examService.loadExams()) {
            String displayText = exam.getCourseID() + " " + exam.getCourseName() + " | " + exam.getExamName();
            examCombox.getItems().add(displayText);
        }
    }

    @FXML
    public void openExamUI() {
        // 获取选中的考试
        String selectedExamText = examCombox.getSelectionModel().getSelectedItem();
        if (selectedExamText == null) {
            showAlert(Alert.AlertType.WARNING, "No Exam Selected", "Please select an exam to start.");
            return;
        }

        // 根据选中的考试文本，从 ExamService 中找到对应的 Exam 对象
        Exam selectedExam = examService.loadExams()
                .stream()
                .filter(exam -> (exam.getCourseID() + " " + exam.getCourseName() + " | " + exam.getExamName())
                        .equals(selectedExamText))
                .findFirst()
                .orElse(null);

        if (selectedExam == null) {
            showAlert(Alert.AlertType.ERROR, "Error", "Failed to find the selected exam.");
            return;
        }

        // 打开 ExamUI 界面
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/comp3111/examsystem/ExamUI.fxml")); // 调整路径
            Scene scene = new Scene(loader.load());
            Stage stage = new Stage();
            stage.setTitle("Exam: " + selectedExam.getExamName());
            stage.setScene(scene);

            // 获取控制器并传递考试数据
            ExamUIController controller = loader.getController();
            controller.setExam(selectedExam);

            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Error", "Failed to open the exam interface.");
        }
    }

    @FXML
    public void openGradeStatistic() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/comp3111/examsystem/GradeStatisticsUI.fxml"));
            Scene scene = new Scene(loader.load());
            Stage stage = new Stage();
            stage.setTitle("Grade Statistics");
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Error", "Failed to open the grade statistics interface.");
        }
    }


    private void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

}

