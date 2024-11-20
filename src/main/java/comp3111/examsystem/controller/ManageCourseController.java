package comp3111.examsystem.controller;

import comp3111.examsystem.entity.Course;
import comp3111.examsystem.service.ManageCourseService;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.io.IOException;
import java.util.List;

public class ManageCourseController {
    @FXML
    TextField courseIDFilter;
    @FXML
    TextField courseNameFilter;
    @FXML
    TextField departmentFilter;
    @FXML
    TableView<Course> courseTable;
    @FXML
    private TableColumn<Course, String> courseIDColumn;
    @FXML
    private TableColumn<Course, String> courseNameColumn;
    @FXML
    private TableColumn<Course, String> departmentColumn;
    @FXML
    TextField courseIDField;
    @FXML
    TextField courseNameField;
    @FXML
    TextField departmentField;

    private ManageCourseService courseService;

    public ManageCourseController() {
        courseService = new ManageCourseService("data/courses.txt");
    }

    @FXML
    public void initialize() {
        courseIDColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getCourseID()));
        courseNameColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getCourseName()));
        departmentColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getDepartment()));
        displayCourses(courseService.getCourseList());
        courseTable.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                updateFields(newValue);
            } else {
                clearFields();
            }
        });
    }

    private void displayCourses(ObservableList<Course> courses) {
        courseTable.setItems(courses);
    }

    @FXML
    public void resetFilters() {
        courseIDFilter.clear();
        courseNameFilter.clear();
        departmentFilter.clear();
        displayCourses(courseService.getCourseList());
    }

    @FXML
    public void filterCourses() {
        String courseID = courseIDFilter.getText().toLowerCase();
        String courseName = courseNameFilter.getText().toLowerCase();
        String department = departmentFilter.getText().toLowerCase();

        List<Course> filteredList = courseService.filterCourses(courseID, courseName, department);
        displayCourses(FXCollections.observableArrayList(filteredList));
    }

    @FXML
    public void addCourse() {
        String courseID = courseIDField.getText();
        String courseName = courseNameField.getText();
        String department = departmentField.getText();

        String validationMessage = courseService.validateInputs(courseID, courseName, department);
        if (validationMessage != null) {
            showAlert(validationMessage);
            return;
        }

        Course newCourse = new Course(courseID, courseName, department);
        try {
            courseService.addCourse(newCourse);
            displayCourses(courseService.getCourseList());
            showAlert("Add course success");
            clearFields();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void updateCourse() {
        Course selectedCourse = courseTable.getSelectionModel().getSelectedItem();
        if (selectedCourse != null) {
            String originalCourseID = selectedCourse.getCourseID();
            String newCourseID = courseIDField.getText();
            String courseName = courseNameField.getText();
            String department = departmentField.getText();

            String validationMessage = courseService.validateInputs(newCourseID, courseName, department);
            if (validationMessage != null) {
                showAlert(validationMessage);
                return;
            }

            Course updatedCourse = new Course(newCourseID, courseName, department);
            try {
                courseService.updateCourse(updatedCourse, originalCourseID);
                displayCourses(courseService.getCourseList());
                showAlert("Update course success");
                clearFields();
                courseTable.getSelectionModel().clearSelection();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            showAlert("Please select a course to update.");
        }
    }

    @FXML
    public void deleteCourse() {
        Course selectedCourse = courseTable.getSelectionModel().getSelectedItem();
        if (selectedCourse != null) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Confirm Deletion");
            alert.setHeaderText("Are you sure you want to delete course " + selectedCourse.getCourseName() + "?");
            alert.setContentText("This action cannot be undone.");

            ButtonType result = alert.showAndWait().orElse(ButtonType.CANCEL);
            if (result == ButtonType.OK) {
                try {
                    courseService.deleteCourse(selectedCourse.getCourseID());
                    displayCourses(courseService.getCourseList());
                    showAlert("Delete course success");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        } else {
            showAlert("Please select a course to delete.");
        }
    }

    @FXML
    public void refreshCourse() {
        clearFields();
        courseTable.getSelectionModel().clearSelection();
        courseService.getCourseList().clear();
        courseService.loadCoursesFromFile();
        displayCourses(courseService.getCourseList());
    }

    void clearFields() {
        courseIDField.clear();
        courseNameField.clear();
        departmentField.clear();
    }

    public void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Hint");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public void updateFields(Course course) {
        courseIDField.setText(course.getCourseID());
        courseNameField.setText(course.getCourseName());
        departmentField.setText(course.getDepartment());
    }
}