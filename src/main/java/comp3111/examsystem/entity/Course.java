package comp3111.examsystem.entity;

/**
 * Represents a course in the examination system.
 */
public class Course {
    private String courseID;
    private String courseName;
    private String department;

    /**
     * Constructs a Course object with the specified details.
     *
     * @param courseID   the unique identifier for the course
     * @param courseName the name of the course
     * @param department  the department offering the course
     */
    public Course(String courseID, String courseName, String department) {
        this.courseID = courseID;
        this.courseName = courseName;
        this.department = department;
    }

    /**
     * Returns the unique identifier for the course.
     *
     * @return the course ID
     */
    public String getCourseID() {
        return courseID;
    }

    /**
     * Returns the name of the course.
     *
     * @return the course name
     */
    public String getCourseName() {
        return courseName;
    }

    /**
     * Returns the department offering the course.
     *
     * @return the department name
     */
    public String getDepartment() {
        return department;
    }

    /**
     * Sets the unique identifier for the course.
     *
     * @param courseID the new course ID
     */
    public void setCourseID(String courseID) {
        this.courseID = courseID;
    }

    /**
     * Sets the name of the course.
     *
     * @param courseName the new course name
     */
    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    /**
     * Sets the department offering the course.
     *
     * @param department the new department name
     */
    public void setDepartment(String department) {
        this.department = department;
    }
}