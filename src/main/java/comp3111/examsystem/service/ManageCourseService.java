package comp3111.examsystem.service;

import comp3111.examsystem.data.DataManager;
import comp3111.examsystem.entity.Course;

import java.util.List;
import java.util.stream.Collectors;

public class ManageCourseService {

    private final DataManager dataManager;

    public ManageCourseService(DataManager dataManager) {
        this.dataManager = dataManager;
    }

    public List<Course> getCourses() {
        return dataManager.getCourses();
    }

    public void addCourse(Course course) {
        // 如果 ID 未设置，自动生成唯一 ID
        if (course.getId() == null) {
            course.setId(String.valueOf(System.currentTimeMillis()));
        }
        dataManager.getCourses().add(course);
    }


    public void updateCourse(Course updatedCourse, String originalCourseID) {
        List<Course> courses = dataManager.getCourses();
        for (int i = 0; i < courses.size(); i++) {
            if (courses.get(i).getCourseID().equals(originalCourseID)) {
                courses.set(i, updatedCourse);
                return;
            }
        }
    }

    public void deleteCourse(String courseID) {
        dataManager.getCourses().removeIf(course -> course.getCourseID().equals(courseID));
    }

    public List<Course> filterCourses(String courseID, String courseName, String department) {
        return dataManager.getCourses().stream()
                .filter(course -> course.getCourseID().toLowerCase().contains(courseID.toLowerCase()) &&
                        course.getCourseName().toLowerCase().contains(courseName.toLowerCase()) &&
                        course.getDepartment().toLowerCase().contains(department.toLowerCase()))
                .collect(Collectors.toList());
    }

    public String validateInputs(String courseID, String courseName, String department) {
        if (courseID.isEmpty() || courseName.isEmpty() || department.isEmpty()) {
            return "All fields must be filled.";
        }
        if (dataManager.getCourses().stream().anyMatch(course -> course.getCourseID().equals(courseID))) {
            return "Course ID already exists.";
        }
        return null;
    }

    public String validateUpdateInputs(String courseID, String courseName, String department) {
        if (courseID.isEmpty() || courseName.isEmpty() || department.isEmpty()) {
            return "All fields must be filled.";
        }
        return null;
    }
}
