package comp3111.examsystem.controller;

import comp3111.examsystem.service.StudentMainService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.List;

public class StudentMainController {
    @FXML
    private ComboBox<String> examCombox;

    private String loggedInUsername;
    private StudentMainService studentMainService;

    /**
     * Set the logged-in username and initialize the service.
     *
     * @param username Logged-in username.
     */
    public void setLoggedInUsername(String username) {
        this.loggedInUsername = username;

        String studentExamsFilePath = "data/studentsexams.txt";
        String completedQuizzesFilePath = "data/completed_quizzes.txt";
        studentMainService = new StudentMainService(studentExamsFilePath, completedQuizzesFilePath);

        loadExamsForUser();
    }

    /**
     * Load exams for the logged-in user and populate the ComboBox.
     */
    private void loadExamsForUser() {
        List<String> exams = studentMainService.getExamsForStudent(loggedInUsername);
        if (exams.isEmpty()) {
            showAlert("No Exams Found", "No exams or grades available for the user: " + loggedInUsername, Alert.AlertType.WARNING);
        } else {
            examCombox.getItems().setAll(exams);
        }
    }

    @FXML
    private void openExamUI(ActionEvent event) {
        String selectedExam = examCombox.getValue();
        if (selectedExam == null || selectedExam.isEmpty()) {
            showAlert("Error", "Please select an exam before proceeding.", Alert.AlertType.ERROR);
            return;
        }

        if (selectedExam.endsWith("(Completed)")) {
            showAlert("Info", "You have already completed this exam.", Alert.AlertType.INFORMATION);
            return;
        }

        try {
            String[] parts = selectedExam.split(" \\| ");
            String[] courseInfo = parts[0].split(" ");
            String courseId = courseInfo[0];
            String examType = parts[1];

            // Simulate completing an exam and saving the grade (example scores)
            studentMainService.addCompletedExam(loggedInUsername, courseId, examType, 40, 50, 30);

            // Refresh the exam list
            loadExamsForUser();
        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Error", "Failed to open the exam UI.", Alert.AlertType.ERROR);
        }
    }

    @FXML
    private void openGradeStatistic(ActionEvent event) {
        // Check if the logged-in username exists in the completed quizzes file
        boolean hasCompletedExams = studentMainService.hasCompletedExams(loggedInUsername);

        if (!hasCompletedExams) {
            showAlert("No Completed Exams", "No completed exams or grades found for the user: " + loggedInUsername, Alert.AlertType.INFORMATION);
            return;
        }

        try {
            // Load GradeStatistics UI
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/comp3111/examsystem/GradeStatistics.fxml"));
            Parent gradeStatisticsUI = loader.load();

            // Pass logged-in username to GradeStatisticsController
            GradeStatisticsController controller = loader.getController();
            controller.setLoggedInUsername(this.loggedInUsername);

            // Get the current stage and set the new scene
            Stage stage = (Stage) examCombox.getScene().getWindow();
            stage.getScene().setRoot(gradeStatisticsUI);
            stage.sizeToScene();
        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Error", "Failed to open the Grade Statistics UI.", Alert.AlertType.ERROR);
        }
    }

    /**
     * Show an alert dialog with the given title, message, and type.
     *
     * @param title   The title of the alert dialog.
     * @param message The message to display in the alert.
     * @param type    The type of the alert.
     */
    private void showAlert(String title, String message, Alert.AlertType type) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    @FXML
    private void exit(ActionEvent event) {
        System.exit(0);
    }
}
