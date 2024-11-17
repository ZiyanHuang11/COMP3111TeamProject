package comp3111.examsystem.controller;

import comp3111.examsystem.entity.Course;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

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

    String courseFilePath;

    ObservableList<Course> courseList = FXCollections.observableArrayList();

    public void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Hint");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    @FXML
    public void initialize() {
        loadCoursesFromFile();
        courseIDColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getCourseID()));
        courseNameColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getCourseName()));
        departmentColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getDepartment()));
        courseTable.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                updateFields(newValue);
            } else {
                clearFields();
            }
        });
    }

    public ManageCourseController() {
        courseFilePath = "data/courses.txt";
        File courseFile = new File(courseFilePath);
        if (courseFile.exists()) {
            System.out.println("Course file found at: " + courseFile.getAbsolutePath());
        } else {
            System.out.println("Course file not found!");
        }
    }

    public void loadCoursesFromFile() {
        try (BufferedReader br = new BufferedReader(new FileReader(courseFilePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] data = line.split(",");
                if (data.length == 3) {
                    Course course = new Course(data[0], data[1], data[2]);
                    courseList.add(course);
                }
            }
            displayCourses(courseList);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // display courses info in table
    private void displayCourses(ObservableList<Course> courses) {
        courseTable.setItems(courses);
    }

    @FXML
    public void resetFilters() {
        courseIDFilter.clear();
        courseNameFilter.clear();
        departmentFilter.clear();
        displayCourses(courseList);
    }

    @FXML
    public void filterCourses() {
        String courseID = courseIDFilter.getText().toLowerCase();
        String courseName = courseNameFilter.getText().toLowerCase();
        String department = departmentFilter.getText().toLowerCase();

        List<Course> filteredList = courseList.stream()
                .filter(course -> course.getCourseID().toLowerCase().contains(courseID) &&
                        course.getCourseName().toLowerCase().contains(courseName) &&
                        course.getDepartment().toLowerCase().contains(department))
                .collect(Collectors.toList());
        displayCourses(FXCollections.observableArrayList(filteredList));
    }

    @FXML
    public void addCourse() {
        String courseID = courseIDField.getText();
        String courseName = courseNameField.getText();
        String department = departmentField.getText();

        // validate username
        String courseIDValidationMessage = validateCourseID(courseID);
        if (courseIDValidationMessage != null) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Hint");
            alert.setHeaderText(null);
            alert.setContentText(courseIDValidationMessage);
            alert.showAndWait();
            return;
        }

        // validate other inputs
        String validationMessage = validateInputs(courseID, courseName, department);
        if (validationMessage != null) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Hint");
            alert.setHeaderText(null);
            alert.setContentText(validationMessage);
            alert.showAndWait();
            return;
        }

        Course newCourse = new Course(courseID, courseName, department);
        courseList.add(newCourse);
        String courseInput = courseID + ',' + courseName + ',' + department;
        // write to txt
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(courseFilePath, true))) {
            bw.write(courseInput);
            bw.newLine();
            displayCourses(courseList);
        } catch (IOException e) {
            e.printStackTrace();
        }
        showAlert("Add course success");
        clearFields();
    }

    String validateInputs(String courseID, String courseName, String department) {
        if (courseID.isEmpty() || courseName.isEmpty() || department.isEmpty()) {
            return "Each field should be filled in";
        }
        for (Course course : courseList) {
            if (course.getCourseID().equals(courseID)) {
                return "The course ID already exists";
            }
        }
        return null;
    }

    private String validateCourseID(String courseID) {
        for (Course course : courseList) {
            if (course.getCourseID().equals(courseID)) {
                return "The course ID already exists";
            }
        }
        return null;
    }

    public void updateFields(Course course) {
        courseIDField.setText(course.getCourseID());
        courseNameField.setText(course.getCourseName());
        departmentField.setText(course.getDepartment());
    }

    @FXML
    public void updateCourse() {
        Course selectedCourse = courseTable.getSelectionModel().getSelectedItem();
        if (selectedCourse != null) {

            String originalCourseID = selectedCourse.getCourseID();
            String newCourseID = courseIDField.getText();
            String courseName = courseNameField.getText();
            String department = departmentField.getText();
            String validationMessage = validateUpdateInputs(courseName, department);

            if (validationMessage != null) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Hint");
                alert.setHeaderText(null);
                alert.setContentText(validationMessage);
                alert.showAndWait();
                return;
            }

            // validate new courseID
            if (!newCourseID.equals(originalCourseID)) {
                String courseIDValidationMessage = validateCourseID(newCourseID);
                if (courseIDValidationMessage != null) {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Hint");
                    alert.setHeaderText(null);
                    alert.setContentText(courseIDValidationMessage);
                    alert.showAndWait();
                    return;
                }
            }

            selectedCourse.setCourseID(newCourseID);
            selectedCourse.setCourseName(courseName);
            selectedCourse.setDepartment(department);
            try {
                List<Course> courses = new ArrayList<>();
                try (BufferedReader br = new BufferedReader(new FileReader(courseFilePath))) {
                    String line;
                    while ((line = br.readLine()) != null) {
                        String[] parts = line.split(",");
                        if (parts.length == 3) {
                            Course course = new Course(parts[0], parts[1], parts[2]);
                            courses.add(course);
                        }
                    }
                }
                for (int i = 0; i < courses.size(); i++) {
                    if (courses.get(i).getCourseID().equals(originalCourseID)) {
                        courses.set(i, selectedCourse);
                        break;
                    }
                }
                try (BufferedWriter bw = new BufferedWriter(new FileWriter(courseFilePath))) {
                    for (Course course : courses) {
                        String courseInput = String.join(",", course.getCourseID(), course.getCourseName(), course.getDepartment());
                        bw.write(courseInput);
                        bw.newLine();
                    }
                }
                System.out.println("success");
                courseList.clear();
                courseList.addAll(courses);
                displayCourses(courseList);

            } catch (IOException e) {
                e.printStackTrace();
            }
            showAlert("Update course success");
            clearFields();
            courseTable.getSelectionModel().clearSelection();
        } else {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Warning");
            alert.setHeaderText(null);
            alert.setContentText("Please select a course to update.");
            alert.showAndWait();
        }
    }

    String validateUpdateInputs(String courseName, String department) {
        if (courseName.isEmpty() || department.isEmpty()) {
            return "Each field should be filled in";
        }
        return null;
    }

    @FXML
    public void deleteCourse() {
        Course selectedCourse = courseTable.getSelectionModel().getSelectedItem();
        if (selectedCourse != null) {
            // Show confirmation dialog
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Confirm Deletion");
            alert.setHeaderText("Are you sure you want to delete course " + selectedCourse.getCourseName() + "?");
            alert.setContentText("This action cannot be undone.");

            // Get the user's choice
            ButtonType result = alert.showAndWait().orElse(ButtonType.CANCEL);
            if (result == ButtonType.OK) {
                courseList.remove(selectedCourse);
                try {
                    List<Course> courses = new ArrayList<>();
                    try (BufferedReader br = new BufferedReader(new FileReader(courseFilePath))) {
                        String line;
                        while ((line = br.readLine()) != null) {
                            String[] parts = line.split(",");
                            if (parts.length == 3) {
                                Course course = new Course(parts[0], parts[1], parts[2]);
                                courses.add(course);
                            }
                        }
                    }

                    courses.removeIf(course -> course.getCourseID().equals(selectedCourse.getCourseID()));

                    try (BufferedWriter bw = new BufferedWriter(new FileWriter(courseFilePath))) {
                        for (Course course : courses) {
                            String courseInput = String.join(",", course.getCourseID(), course.getCourseName(), course.getDepartment());
                            bw.write(courseInput);
                            bw.newLine();
                        }
                    }

                    System.out.println("Course " + selectedCourse.getCourseName() + " has been deleted.");
                    displayCourses(courseList);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        } else {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Warning");
            alert.setHeaderText(null);
            alert.setContentText("Please select a course to delete.");
            alert.showAndWait();
        }
        showAlert("Delete course success");
    }

    @FXML
    public void refreshCourse() {
        clearFields();
        courseTable.getSelectionModel().clearSelection();
        courseList.clear();
        displayCourses(courseList);
    }

    void clearFields() {
        courseIDField.clear();
        courseNameField.clear();
        departmentField.clear();
    }
}