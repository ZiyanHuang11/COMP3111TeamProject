package comp3111.examsystem.entity;

public class Teacher extends Entity {
    private String username;
    private String password;
    private String name;
    private String gender;
    private int age;
    private String position; // Position field
    private String department;

    // Constructor including position
    public Teacher(String id, String username, String password, String name, String gender, int age, String position, String department) {
        setId(id);
        this.username = username;
        this.password = password;
        this.name = name;
        this.gender = gender;
        this.age = age;
        this.position = position; // Initialize position
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

    public String getPosition() { // Getter for position
        return position;
    }

    public void setPosition(String position) { // Setter for position
        this.position = position;
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
                ", position='" + position + '\'' +
                ", department='" + department + '\'' +
                '}';
    }
}

