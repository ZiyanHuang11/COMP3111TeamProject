package comp3111.examsystem.controller;

import comp3111.examsystem.data.DataManager;
import comp3111.examsystem.entity.Exam;
import comp3111.examsystem.service.StudentMainService;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class StudentMainController implements Initializable {
    @FXML
    private ComboBox<String> examCombox;

    private StudentMainService studentMainService;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // 创建 DataManager 并注入到 Service 中
        DataManager dataManager = new DataManager();
        studentMainService = new StudentMainService(dataManager);
        loadExams();
    }

    /**
     * 加载考试列表到 ComboBox
     */
    private void loadExams() {
        for (String examDisplayText : studentMainService.getExamDisplayTexts()) {
            examCombox.getItems().add(examDisplayText);
        }
    }

    @FXML
    public void openExamUI() {
        String selectedExamText = examCombox.getSelectionModel().getSelectedItem();
        if (selectedExamText == null) {
            showAlert(Alert.AlertType.WARNING, "No Exam Selected", "Please select an exam to start.");
            return;
        }

        Exam selectedExam = studentMainService.getExamByDisplayText(selectedExamText);
        if (selectedExam == null) {
            showAlert(Alert.AlertType.ERROR, "Error", "Failed to find the selected exam.");
            return;
        }

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/comp3111/examsystem/ExamUI.fxml"));
            Scene scene = new Scene(loader.load());
            Stage stage = new Stage();
            stage.setTitle("Exam: " + selectedExam.getExamName());
            stage.setScene(scene);

            // 传递考试数据到 ExamUI
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



