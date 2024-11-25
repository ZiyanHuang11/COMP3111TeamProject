package comp3111.examsystem.controller;

import comp3111.examsystem.Main;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.stage.Stage;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

/**
 * Controller for managing the main interface for students in the examination system.
 */
public class StudentMainController implements Initializable {

    @FXML
    ComboBox<String> examCombox;

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
     * Opens the exam user interface for the selected exam.
     */
    @FXML
    public void openExamUI() {
        // Implementation to open exam UI will go here
    }

    /**
     * Opens the grade statistics interface for the student.
     */
    @FXML
    public void openGradeStatistic() {
        // Implementation to open grade statistics will go here
    }

    /**
     * Exits the application.
     */
    @FXML
    public void exit() {
        System.exit(0);
    }
}
