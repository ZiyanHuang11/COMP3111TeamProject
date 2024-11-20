package comp3111.examsystem.entity;

public class Teacher {
    private String username;
    private String name;
    private String gender;
    private int age;
    private String position;
    private String department;
    private String password;

    public Teacher(String username, String password, String name, String gender, int age, String position, String department) {
        this.username = username;
        this.name = name;
        this.gender = gender;
        this.age = age;
        this.position = position;
        this.department = department;
        this.password = password;
    }

    public String getUsername() { return username; }
    public String getName() { return name; }
    public String getDepartment() { return department; }
    public int getAge() { return age; }
    public String getPosition() { return position; }
    public String getPassword() { return password; }
    public String getGender() { return gender; }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setPosition(String position) {
        this.position = position;
    }
}