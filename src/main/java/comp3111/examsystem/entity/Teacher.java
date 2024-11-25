package comp3111.examsystem.entity;

/**
 * Represents a teacher in the examination system.
 */
public class Teacher {
    private String username;
    private String name;
    private String gender;
    private int age;
    private String position;
    private String department;
    private String password;

    /**
     * Constructs a Teacher object with the specified details.
     *
     * @param username   the unique username of the teacher
     * @param password   the password for the teacher's account
     * @param name       the name of the teacher
     * @param gender     the gender of the teacher
     * @param age        the age of the teacher
     * @param position    the position of the teacher (e.g., Professor, Lecturer)
     * @param department  the department the teacher belongs to
     */
    public Teacher(String username, String password, String name, String gender, int age, String position, String department) {
        this.username = username;
        this.name = name;
        this.gender = gender;
        this.age = age;
        this.position = position;
        this.department = department;
        this.password = password;
    }

    /**
     * Returns the unique username of the teacher.
     *
     * @return the username
     */
    public String getUsername() {
        return username;
    }

    /**
     * Returns the name of the teacher.
     *
     * @return the teacher's name
     */
    public String getName() {
        return name;
    }

    /**
     * Returns the department the teacher belongs to.
     *
     * @return the department
     */
    public String getDepartment() {
        return department;
    }

    /**
     * Returns the age of the teacher.
     *
     * @return the age
     */
    public int getAge() {
        return age;
    }

    /**
     * Returns the position of the teacher.
     *
     * @return the position
     */
    public String getPosition() {
        return position;
    }

    /**
     * Returns the password for the teacher's account.
     *
     * @return the password
     */
    public String getPassword() {
        return password;
    }

    /**
     * Returns the gender of the teacher.
     *
     * @return the gender
     */
    public String getGender() {
        return gender;
    }

    /**
     * Sets the unique username of the teacher.
     *
     * @param username the new username
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * Sets the name of the teacher.
     *
     * @param name the new name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Sets the age of the teacher.
     *
     * @param age the new age
     */
    public void setAge(int age) {
        this.age = age;
    }

    /**
     * Sets the gender of the teacher.
     *
     * @param gender the new gender
     */
    public void setGender(String gender) {
        this.gender = gender;
    }

    /**
     * Sets the department the teacher belongs to.
     *
     * @param department the new department
     */
    public void setDepartment(String department) {
        this.department = department;
    }

    /**
     * Sets the password for the teacher's account.
     *
     * @param password the new password
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * Sets the position of the teacher.
     *
     * @param position the new position
     */
    public void setPosition(String position) {
        this.position = position;
    }
}