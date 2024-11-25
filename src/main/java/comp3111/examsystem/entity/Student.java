package comp3111.examsystem.entity;

/**
 * Represents a student in the examination system.
 */
public class Student {
    private String username;
    private String name;
    private int age;
    private String gender;
    private String department;
    private String password;

    /**
     * Constructs a Student object with the specified details.
     *
     * @param username   the unique username of the student
     * @param name       the name of the student
     * @param age        the age of the student
     * @param gender     the gender of the student
     * @param department  the department the student belongs to
     * @param password   the password for the student's account
     */
    public Student(String username, String name, int age, String gender, String department, String password) {
        this.username = username;
        this.name = name;
        this.age = age;
        this.gender = gender;
        this.department = department;
        this.password = password;
    }

    /**
     * Returns the unique username of the student.
     *
     * @return the username
     */
    public String getUsername() {
        return username;
    }

    /**
     * Returns the name of the student.
     *
     * @return the student's name
     */
    public String getName() {
        return name;
    }

    /**
     * Returns the department the student belongs to.
     *
     * @return the department
     */
    public String getDepartment() {
        return department;
    }

    /**
     * Returns the age of the student.
     *
     * @return the age
     */
    public int getAge() {
        return age;
    }

    /**
     * Returns the password for the student's account.
     *
     * @return the password
     */
    public String getPassword() {
        return password;
    }

    /**
     * Returns the gender of the student.
     *
     * @return the gender
     */
    public String getGender() {
        return gender;
    }

    /**
     * Sets the unique username of the student.
     *
     * @param username the new username
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * Sets the name of the student.
     *
     * @param name the new name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Sets the age of the student.
     *
     * @param age the new age
     */
    public void setAge(int age) {
        this.age = age;
    }

    /**
     * Sets the gender of the student.
     *
     * @param gender the new gender
     */
    public void setGender(String gender) {
        this.gender = gender;
    }

    /**
     * Sets the department the student belongs to.
     *
     * @param department the new department
     */
    public void setDepartment(String department) {
        this.department = department;
    }

    /**
     * Sets the password for the student's account.
     *
     * @param password the new password
     */
    public void setPassword(String password) {
        this.password = password;
    }
}