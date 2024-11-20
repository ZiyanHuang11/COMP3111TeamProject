package comp3111.examsystem.entity;

public class Course {
    private String courseID;
    private String courseName;
    private String department;

    public Course(String courseID, String courseName, String department) {
        this.courseID = courseID;
        this.courseName = courseName;
        this.department = department;
    }

    public String getCourseID() { return courseID; }
    public String getCourseName() { return courseName; }
    public String getDepartment() { return department; }

    public void setCourseID(String courseID) {
        this.courseID = courseID;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public void setDepartment(String department) {
        this.department = department;
    }
}