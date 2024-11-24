package comp3111.examsystem.entity;

public class Student extends Entity {
    private String id; // 从父类 Entity 继承的 id
    private String username;
    private String name;
    private int age;
    private String gender;
    private String department;
    private String password;

    // 默认构造函数（无参数）
    public Student() {}

    // 全参数构造函数
    public Student(String id, String username, String name, int age, String gender, String department, String password) {
        this.setId(id); // 调用 Entity 类的 setId 方法
        this.username = username;
        this.name = name;
        this.age = age;
        this.gender = gender;
        this.department = department;
        this.password = password;
    }

    // 构造函数（不包含 ID，ID 会在其他地方设置）
    public Student(String username, String name, int age, String gender, String department, String password) {
        this.username = username;
        this.name = name;
        this.age = age;
        this.gender = gender;
        this.department = department;
        this.password = password;
    }

    // Getters 和 Setters
    public String getId() {
        return id;
    }

    @Override
    public void setId(String id) {
        this.id = id;
    }

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
                "id='" + id + '\'' +
                ", username='" + username + '\'' +
                ", name='" + name + '\'' +
                ", age=" + age +
                ", gender='" + gender + '\'' +
                ", department='" + department + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}
