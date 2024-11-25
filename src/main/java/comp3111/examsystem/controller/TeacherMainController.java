package comp3111.examsystem.controller;

import comp3111.examsystem.Main;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * Controller for managing the main interface for teachers in the examination system.
 */
public class TeacherMainController implements Initializable {

    @FXML
    private VBox mainbox;

    /**
     * Initializes the controller after its root element has been processed.
     *
     * @param location  the location used to resolve relative paths for the root object,
     *                  or null if the location is not known
     * @param resources the resources used to localize the root object,
     *                  or null if the root object was not localized
     */
    public void initialize(URL location, ResourceBundle resources) {
        // No specific initialization required at this moment
    }

    /**
     * Opens the Question Bank Management user interface.
     */
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

    /**
     * Opens the Exam Management user interface.
     */
    @FXML
    public void openExamManageUI() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("ExamManagementUI.fxml"));
            Stage stage = new Stage();
            stage.setTitle("Exam Management");
            stage.setScene(new Scene(fxmlLoader.load()));
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Opens the Grade Statistics user interface.
     */
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

    /**
     * Exits the application.
     */
    @FXML
    public void exit() {
        System.exit(0);
    }
}