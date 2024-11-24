package comp3111.examsystem.entity;

public class Student extends Entity {
    private String username;
    private String name;
    private int age;
    private String gender;
    private String department;
    private String password;

    public Student(String id, String username, String name, int age, String gender, String department, String password) {
        this.setId(id);
        this.username = username;
        this.name = name;
        this.age = age;
        this.gender = gender;
        this.department = department;
        this.password = password;
    }

    public String getUsername() { return username; }
    public String getName() { return name; }
    public String getDepartment() { return department; }
    public int getAge() { return age; }
    public String getPassword() { return password; }
    public String getGender() { return gender; }

    public void setUsername(String username) { this.username = username; }
    public void setName(String name) { this.name = name; }
    public void setAge(int age) { this.age = age; }
    public void setGender(String gender) { this.gender = gender; }
    public void setDepartment(String department) { this.department = department; }
    public void setPassword(String password) { this.password = password; }

    @Override
    public String toString() {
        return "Student{" +
                "id=" + getId() +
                ", username='" + username + '\'' +
                ", name='" + name + '\'' +
                ", age=" + age +
                ", gender='" + gender + '\'' +
                ", department='" + department + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}
