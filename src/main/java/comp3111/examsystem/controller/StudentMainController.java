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
        String selectedExam = examCombox.getSelectionModel().getSelectedItem();
        if (selectedExam == null) {
            showAlert(Alert.AlertType.WARNING, "No Exam Selected", "Please select an exam to start.");
            return;
        }

        // TODO: 打开考试界面，加载对应考试数据
    }

    private void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

}

