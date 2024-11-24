package comp3111.examsystem.entity;

public class Student extends Entity {
    private String username;
    private String name;
    private int age;
    private String gender;
    private String department;
    private String password;

    // 无参构造函数（必要，用于反射或默认初始化）
    public Student() {
        this.username = "";
        this.name = "";
        this.age = 0;
        this.gender = "";
        this.department = "";
        this.password = "";
    }

    // 有参构造函数
    public Student(String id, String username, String name, int age, String gender, String department, String password) {
        this.setId(id); // 设置 ID
        this.username = username;
        this.name = name;
        this.age = age;
        this.gender = gender;
        this.department = department;
        this.password = password;
    }

    // Getter 和 Setter 方法
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

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

