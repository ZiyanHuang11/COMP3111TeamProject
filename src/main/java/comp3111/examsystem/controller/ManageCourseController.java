package comp3111.examsystem.controller;

import comp3111.examsystem.data.DataManager;
import comp3111.examsystem.entity.Course;
import comp3111.examsystem.service.ManageCourseService;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.util.List;

public class ManageCourseController {

    @FXML
    private TextField courseIDFilter;

    @FXML
    private TextField courseNameFilter;

    @FXML
    private TextField departmentFilter;

    @FXML
    private TableView<Course> courseTable;

    @FXML
    private TableColumn<Course, String> courseIDColumn;

    @FXML
    private TableColumn<Course, String> courseNameColumn;

    @FXML
    private TableColumn<Course, String> departmentColumn;

    @FXML
    private TextField courseIDField;

    @FXML
    private TextField courseNameField;

    @FXML
    private TextField departmentField;

    private ManageCourseService courseService;

    // Constructor injection
    public ManageCourseController(DataManager dataManager) {
        this.courseService = new ManageCourseService(dataManager);
    }

    public void setDataManager(DataManager dataManager) {
        this.courseService = new ManageCourseService(dataManager);
        initialize();
    }

    @FXML
    public void initialize() {
        courseIDColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getCourseID()));
        courseNameColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getCourseName()));
        departmentColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getDepartment()));

        displayCourses(FXCollections.observableArrayList(courseService.getCourses()));
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
        displayCourses(FXCollections.observableArrayList(courseService.getCourses()));
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
        courseService.addCourse(newCourse);
        refreshCourseList();
        showAlert("Course added successfully.");
        clearFields();
    }

    @FXML
    public void updateCourse() {
        Course selectedCourse = courseTable.getSelectionModel().getSelectedItem();
        if (selectedCourse != null) {
            String originalCourseID = selectedCourse.getCourseID();
            String newCourseID = courseIDField.getText();
            String courseName = courseNameField.getText();
            String department = departmentField.getText();

            String validationMessage = courseService.validateUpdateInputs(newCourseID, courseName, department);
            if (validationMessage != null) {
                showAlert(validationMessage);
                return;
            }

            Course updatedCourse = new Course(newCourseID, courseName, department);
            courseService.updateCourse(updatedCourse, originalCourseID);
            refreshCourseList();
            showAlert("Course updated successfully.");
            clearFields();
        } else {
            showAlert("Please select a course to update.");
        }
    }

    @FXML
    public void deleteCourse() {
        Course selectedCourse = courseTable.getSelectionModel().getSelectedItem();
        if (selectedCourse != null) {
            courseService.deleteCourse(selectedCourse.getCourseID());
            refreshCourseList();
            showAlert("Course deleted successfully.");
        } else {
            showAlert("Please select a course to delete.");
        }
    }

    public void refreshCourseList() {
        displayCourses(FXCollections.observableArrayList(courseService.getCourses()));
    }

    private void clearFields() {
        courseIDField.clear();
        courseNameField.clear();
        departmentField.clear();
    }

    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Information");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void updateFields(Course course) {
        courseIDField.setText(course.getCourseID());
        courseNameField.setText(course.getCourseName());
        departmentField.setText(course.getDepartment());
    }
}
