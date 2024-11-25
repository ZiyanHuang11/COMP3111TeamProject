package comp3111.examsystem.service;

import comp3111.examsystem.entity.Course;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.io.*;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Service class for managing courses in the examination system.
 */
public class ManageCourseService {
    private String courseFilePath;
    private ObservableList<Course> courseList;

    /**
     * Constructs a ManageCourseService with the specified file path for courses.
     *
     * @param courseFilePath the file path for storing course data
     */
    public ManageCourseService(String courseFilePath) {
        this.courseFilePath = courseFilePath;
        this.courseList = FXCollections.observableArrayList();
        loadCoursesFromFile();
    }

    /**
     * Returns the list of courses.
     *
     * @return an observable list of courses
     */
    public ObservableList<Course> getCourseList() {
        return courseList;
    }

    /**
     * Loads courses from a file into the course list.
     */
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

    /**
     * Adds a new course to the course list and saves it to the file.
     *
     * @param newCourse the new course to be added
     * @throws IOException if an error occurs while saving to the file
     */
    public void addCourse(Course newCourse) throws IOException {
        courseList.add(newCourse);
        String courseInput = newCourse.getCourseID() + ',' + newCourse.getCourseName() + ',' + newCourse.getDepartment();
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(courseFilePath, true))) {
            bw.write(courseInput);
            bw.newLine();
        }
    }

    /**
     * Updates an existing course in the course list and saves the changes to the file.
     *
     * @param updatedCourse   the course with updated details
     * @param originalCourseID the original ID of the course to be updated
     * @throws IOException if an error occurs while saving to the file
     */
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

    /**
     * Deletes a course from the course list and saves the changes to the file.
     *
     * @param courseID the ID of the course to be deleted
     * @throws IOException if an error occurs while saving to the file
     */
    public void deleteCourse(String courseID) throws IOException {
        boolean courseRemoved = courseList.removeIf(course -> course.getCourseID().equals(courseID));
        if (!courseRemoved) {
            throw new IOException("Course with ID " + courseID + " does not exist.");
        }
        saveCoursesToFile();
    }

    /**
     * Saves the current list of courses to the course file.
     *
     * @throws IOException if an error occurs while writing to the file
     */
    private void saveCoursesToFile() throws IOException {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(courseFilePath))) {
            for (Course course : courseList) {
                String courseInput = String.join(",", course.getCourseID(), course.getCourseName(), course.getDepartment());
                bw.write(courseInput);
                bw.newLine();
            }
        }
    }

    /**
     * Filters courses based on the specified criteria.
     *
     * @param courseID    the ID of the course to filter by
     * @param courseName  the name of the course to filter by
     * @param department  the department to filter by
     * @return a list of courses that match the specified criteria
     */
    public List<Course> filterCourses(String courseID, String courseName, String department) {
        return courseList.stream()
                .filter(course -> course.getCourseID().toLowerCase().contains(courseID.toLowerCase()) &&
                        course.getCourseName().toLowerCase().contains(courseName.toLowerCase()) &&
                        course.getDepartment().toLowerCase().contains(department.toLowerCase()))
                .collect(Collectors.toList());
    }

    /**
     * Validates the course ID to check if it already exists in the course list.
     *
     * @param courseID the course ID to validate
     * @return an error message if the course ID exists, or null if it does not
     */
    public String validateCourseID(String courseID) {
        for (Course course : courseList) {
            if (course.getCourseID().equals(courseID)) {
                return "The course ID already exists";
            }
        }
        return null;
    }

    /**
     * Validates input fields for adding a new course.
     *
     * @param courseID   the course ID to validate
     * @param courseName the course name to validate
     * @param department the department to validate
     * @return an error message if any field is invalid, or null if all fields are valid
     */
    public String validateInputs(String courseID, String courseName, String department) {
        if (courseID.isEmpty() || courseName.isEmpty() || department.isEmpty()) {
            return "Each field should be filled in";
        }
        return validateCourseID(courseID);
    }

    /**
     * Validates input fields for updating an existing course.
     *
     * @param courseID   the course ID to validate
     * @param courseName the course name to validate
     * @param department the department to validate
     * @return an error message if any field is invalid, or null if all fields are valid
     */
    public String validateUpdateInputs(String courseID, String courseName, String department) {
        if (courseID.isEmpty() || department.isEmpty() || courseName.isEmpty()) {
            return "Each field should be filled in";
        }
        return null;
    }
}