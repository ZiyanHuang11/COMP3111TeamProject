package comp3111.examsystem.service;

import comp3111.examsystem.entity.Course;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.io.*;
import java.util.List;
import java.util.stream.Collectors;

public class ManageCourseService {
    private String courseFilePath;
    private ObservableList<Course> courseList;
    //
    public ManageCourseService(String courseFilePath) {
        this.courseFilePath = courseFilePath;
        this.courseList = FXCollections.observableArrayList();
        loadCoursesFromFile();
    }

    public ObservableList<Course> getCourseList() {
        return courseList;
    }

    public void loadCoursesFromFile() {
        try (BufferedReader br = new BufferedReader(new FileReader(courseFilePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] data = line.split(",");
                if (data.length == 3 && !data[0].trim().isEmpty() && !data[1].trim().isEmpty() && !data[2].trim().isEmpty()) {
                    Course course = new Course(data[0].trim(), data[1].trim(), data[2].trim());
                    courseList.add(course);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void addCourse(Course newCourse) throws IOException {
        courseList.add(newCourse);
        String courseInput = newCourse.getCourseID() + ',' + newCourse.getCourseName() + ',' + newCourse.getDepartment();
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(courseFilePath, true))) {
            bw.write(courseInput);
            bw.newLine();
        }
    }

    public void updateCourse(Course updatedCourse, String originalCourseID) throws IOException {
        boolean courseFound = false;
        for (int i = 0; i < courseList.size(); i++) {
            if (courseList.get(i).getCourseID().equals(originalCourseID)) {
                courseList.set(i, updatedCourse);
                courseFound = true;
                break;
            }
        }
        if (!courseFound) {
            throw new IOException("Course with ID " + originalCourseID + " does not exist.");
        }
        saveCoursesToFile();
    }

    public void deleteCourse(String courseID) throws IOException {
        boolean courseRemoved = courseList.removeIf(course -> course.getCourseID().equals(courseID));
        if (!courseRemoved) {
            throw new IOException("Course with ID " + courseID + " does not exist.");
        }
        saveCoursesToFile();
    }

    private void saveCoursesToFile() throws IOException {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(courseFilePath))) {
            for (Course course : courseList) {
                String courseInput = String.join(",", course.getCourseID(), course.getCourseName(), course.getDepartment());
                bw.write(courseInput);
                bw.newLine();
            }
        }
    }

    public List<Course> filterCourses(String courseID, String courseName, String department) {
        return courseList.stream()
                .filter(course -> course.getCourseID().toLowerCase().contains(courseID.toLowerCase()) &&
                        course.getCourseName().toLowerCase().contains(courseName.toLowerCase()) &&
                        course.getDepartment().toLowerCase().contains(department.toLowerCase()))
                .collect(Collectors.toList());
    }

    public String validateCourseID(String courseID) {
        for (Course course : courseList) {
            if (course.getCourseID().equals(courseID)) {
                return "The course ID already exists";
            }
        }
        return null;
    }

    public String validateInputs(String courseID, String courseName, String department) {
        if (courseID.isEmpty() || courseName.isEmpty() || department.isEmpty()) {
            return "Each field should be filled in";
        }
        return validateCourseID(courseID);
    }

    public String validateUpdateInputs(String courseID, String courseName, String department) {
        if (courseID.isEmpty() || department.isEmpty() || courseName.isEmpty()) {
            return "Each field should be filled in";
        }
        return null;
    }
}