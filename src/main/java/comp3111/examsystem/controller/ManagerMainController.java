package comp3111.examsystem.controller;

import comp3111.examsystem.Main;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * Controller for the manager to choose three main functions
 */

public class ManagerMainController implements Initializable {

    /**
     * Initializes the controller after its root element has been processed.
     *
     * @param location  The location used to resolve relative paths for the root object,
     *                  or null if the location is not known.
     * @param resources The resources used to localize the root object,
     *                  or null if the root object was not localized.
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Initialization logic can be added here if needed
    }

    /**
     * Opens the Student Management User Interface.
     * Creates a new stage and loads the StudentManageUI FXML file.
     */
    @FXML
    public void openStudentManageUI() {
        try {
            Stage stage = new Stage();
            FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("StudentManageUI.fxml"));
            Scene scene = new Scene(fxmlLoader.load());
            stage.setTitle("Student Management");
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Opens the Teacher Management User Interface.
     * Creates a new stage and loads the TeacherManageUI FXML file.
     */
    @FXML
    public void openTeacherManageUI() {
        try {
            Stage stage = new Stage();
            FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("TeacherManageUI.fxml"));
            Scene scene = new Scene(fxmlLoader.load());
            stage.setTitle("Teacher Management");
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Opens the Course Management User Interface.
     * Creates a new stage and loads the CourseManageUI FXML file.
     */
    @FXML
    public void openCourseManageUI() {
        try {
            Stage stage = new Stage();
            FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("CourseManageUI.fxml"));
            Scene scene = new Scene(fxmlLoader.load());
            stage.setTitle("Course Management");
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Exits the application.
     * Closes the application by calling System.exit(0).
     */
    @FXML
    public void exit() {
        System.exit(0);
    }
}