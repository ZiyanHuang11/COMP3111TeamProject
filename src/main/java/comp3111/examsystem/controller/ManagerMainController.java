package comp3111.examsystem.controller;

import comp3111.examsystem.Main;
import comp3111.examsystem.data.DataManager;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class ManagerMainController {

    private final DataManager dataManager;

    public ManagerMainController() {
        this.dataManager = new DataManager();
    }

    @FXML
    public void openStudentManageUI() {
        try {
            FXMLLoader loader = new FXMLLoader(Main.class.getResource("StudentManageUI.fxml"));
            Stage stage = new Stage();
            stage.setTitle("Student Management");
            stage.setScene(new Scene(loader.load()));

            ManageStudentController controller = loader.getController();
            controller.setDataManager(dataManager); // Pass DataManager to controller
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void openTeacherManageUI() {
        try {
            FXMLLoader loader = new FXMLLoader(Main.class.getResource("TeacherManageUI.fxml"));
            Stage stage = new Stage();
            stage.setTitle("Teacher Management");
            stage.setScene(new Scene(loader.load()));

            ManageTeacherController controller = loader.getController();
            controller.setDataManager(dataManager); // Pass DataManager to controller
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void openCourseManageUI() {
        try {
            FXMLLoader loader = new FXMLLoader(Main.class.getResource("CourseManageUI.fxml"));
            Stage stage = new Stage();
            stage.setTitle("Course Management");
            stage.setScene(new Scene(loader.load()));

            ManageCourseController controller = loader.getController();
            controller.setDataManager(dataManager); // Pass DataManager to controller
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void exit() {
        System.exit(0);
    }
}
