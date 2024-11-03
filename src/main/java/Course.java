public class Course {
    private String courseID;
    private String courseName;

    public Course(String courseID, String courseName) {
        this.courseID = courseID;
        this.courseName = courseName;
    }

    // Getter methods if needed for testing or further functionality
    public String getCourseID() {
        return courseID;
    }

    public String getCourseName() {
        return courseName;
    }
}
