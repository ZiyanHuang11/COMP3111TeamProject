package comp3111.examsystem.entity;

public class Teacher {
    private String username;
    private String name;
    private String gender;
    private int age;
    private String position;
    private String department;
    private String password;
    private String courseId1; // 第一门课程ID
    private String courseId2; // 第二门课程ID

    public Teacher(String username, String password, String name, String gender, int age, String position, String department, String courseId1, String courseId2) {
        this.username = username;
        this.password = password;
        this.name = name;
        this.gender = gender;
        this.age = age;
        this.position = position;
        this.department = department;
        this.courseId1 = courseId1;
        this.courseId2 = courseId2;
    }

    public Teacher(String username, String password, String name, String gender, int age, String position, String department) {
        this.username = username;
        this.password = password;
        this.name = name;
        this.gender = gender;
        this.age = age;
        this.position = position;
        this.department = department;
        this.courseId1 = null; // 初始化为空
        this.courseId2 = null; // 初始化为空
    }

    // 现有的 getter 和 setter
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

    public String getCourseid1() {
        return courseId1;
    }

    public String getCourseid2() {
        return courseId2;
    }

    public void setCourseId1(String courseId) {
        this.courseId1 = courseId;
    }

    public void setCourseId2(String courseId) {
        this.courseId2 = courseId;
    }

    public void addCourse(String courseId) {
        if (courseId1 == null) {
            courseId1 = courseId;
        } else if (courseId2 == null) {
            courseId2 = courseId;
        } else {
            System.out.println("该教师已达到最大课程数量");
        }
    }
}