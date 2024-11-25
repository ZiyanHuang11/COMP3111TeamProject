package comp3111.examsystem.entity;

public class Teacher extends Entity {
    private String username;
    private String password;
    private String name;
    private String gender;
    private int age;
    private String title; // Renamed position to title for consistency
    private String department;

    // No-arg constructor for reflection or default instantiation
    public Teacher() {
        // Initialize fields with default values
        this.username = "";
        this.password = "";
        this.name = "";
        this.gender = "";
        this.age = 0;
        this.title = "";
        this.department = "";
    }

    // Full constructor with title field
    public Teacher(String id, String username, String password, String name, String gender, int age, String title, String department) {
        setId(id); // Set the ID using the parent Entity class
        this.username = username;
        this.password = password;
        this.name = name;
        this.gender = gender;
        this.age = age;
        this.title = title; // Initialize title
        this.department = department;
    }

    // Getters and Setters
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getTitle() { // Getter for title
        return title;
    }

    public void setTitle(String title) { // Setter for title
        this.title = title;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    @Override
    public String toString() {
        return "Teacher{" +
                "id='" + getId() + '\'' +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", name='" + name + '\'' +
                ", gender='" + gender + '\'' +
                ", age=" + age +
                ", title='" + title + '\'' +
                ", department='" + department + '\'' +
                '}';
    }
}
