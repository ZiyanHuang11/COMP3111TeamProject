package comp3111.examsystem.service;

import comp3111.examsystem.entity.Teacher;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.io.*;
import java.util.List;
import java.util.stream.Collectors;

public class ManageTeacherService {

    private String teacherFilePath;
    private String courseFilePath; // 课程文件路径
    private ObservableList<Teacher> teacherList;
    private ObservableList<String> allCourses; // 所有课程列表

    public ManageTeacherService() {
        teacherFilePath = "data/teachers.txt";
        courseFilePath = "data/courses.txt"; // 初始化课程文件路径
        teacherList = FXCollections.observableArrayList();
        allCourses = FXCollections.observableArrayList(); // 初始化课程列表
        loadTeachersFromFile();
        loadCoursesFromFile(); // 加载所有课程
    }

    public ObservableList<Teacher> getTeacherList() {
        return teacherList;
    }

    public ObservableList<String> getAllCourses() {
        return allCourses; // 返回所有课程
    }

    private void loadTeachersFromFile() {
        File file = new File(teacherFilePath);
        if (!file.exists()) {
            return;
        }

        try (BufferedReader br = new BufferedReader(new FileReader(teacherFilePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] data = line.split(",");
                if (data.length >= 7) { // 确保读取的行有足够的数据
                    String username = data[0];
                    String password = data[1];
                    String name = data[2];
                    String gender = data[3];
                    int age = Integer.parseInt(data[4]);
                    String position = data[5];
                    String department = data[6];
                    String courseId1 = data.length > 7 ? data[7] : null; // 第一门课程ID
                    String courseId2 = data.length > 8 ? data[8] : null; // 第二门课程ID

                    Teacher teacher = new Teacher(username, password, name, gender, age, position, department, courseId1, courseId2);
                    teacherList.add(teacher);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void loadCoursesFromFile() {
        File file = new File(courseFilePath);
        if (!file.exists()) {
            return;
        }

        try (BufferedReader br = new BufferedReader(new FileReader(courseFilePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] data = line.split(","); // 假设课程文件以逗号分隔
                if (data.length > 0) {
                    allCourses.add(data[0]); // 只取课程ID
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void addTeacher(Teacher newTeacher) throws IOException {
        if (isUsernameExists(newTeacher.getUsername())) {
            throw new IOException("The user name already exists");
        }
        teacherList.add(newTeacher);
        writeTeacherToFile(newTeacher);
    }

    private void writeTeacherToFile(Teacher teacher) throws IOException {
        String teacherInput = String.join(",", teacher.getUsername(), teacher.getPassword(), teacher.getName(),
                teacher.getGender(), String.valueOf(teacher.getAge()), teacher.getPosition(), teacher.getDepartment(),
                teacher.getCourseid1() != null ? teacher.getCourseid1() : "",
                teacher.getCourseid2() != null ? teacher.getCourseid2() : "");
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(teacherFilePath, true))) {
            bw.write(teacherInput);
            bw.newLine();
        }
    }

    public void updateTeacher(Teacher updatedTeacher, String originalUsername) throws IOException {
        boolean found = false;
        for (int i = 0; i < teacherList.size(); i++) {
            if (teacherList.get(i).getUsername().equals(originalUsername)) {
                teacherList.set(i, updatedTeacher);
                found = true;
                break;
            }
        }
        if (!found) {
            throw new IOException("Teacher not found");
        }
        writeAllTeachersToFile();
    }

    private void writeAllTeachersToFile() throws IOException {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(teacherFilePath))) {
            for (Teacher teacher : teacherList) {
                StringBuilder teacherInput = new StringBuilder();
                teacherInput.append(teacher.getUsername()).append(",")
                        .append(teacher.getPassword()).append(",")
                        .append(teacher.getName()).append(",")
                        .append(teacher.getGender()).append(",")
                        .append(teacher.getAge()).append(",")
                        .append(teacher.getPosition()).append(",")
                        .append(teacher.getDepartment());

                // 只添加非空的课程ID
                if (teacher.getCourseid1() != null && !teacher.getCourseid1().isEmpty()) {
                    teacherInput.append(",").append(teacher.getCourseid1());
                }
                if (teacher.getCourseid2() != null && !teacher.getCourseid2().isEmpty()) {
                    teacherInput.append(",").append(teacher.getCourseid2());
                }

                bw.write(teacherInput.toString());
                bw.newLine();
            }
        }
    }

    public void deleteTeacher(Teacher teacherToDelete) throws IOException {
        if (!teacherList.remove(teacherToDelete)) {
            throw new IOException("Teacher not found");
        }
        writeAllTeachersToFile();
    }

    public List<Teacher> filterTeachers(String username, String name, String department) {
        return teacherList.stream()
                .filter(teacher -> teacher.getUsername().toLowerCase().contains(username) &&
                        teacher.getName().toLowerCase().contains(name) &&
                        teacher.getDepartment().toLowerCase().contains(department))
                .collect(Collectors.toList());
    }

    public String validateInputs(String username, String name, String gender, String ageText, String position, String department, String password) {
        if (username.isEmpty() || name.isEmpty() || ageText.isEmpty() || gender == null || position == null || department.isEmpty() || password.isEmpty()) {
            return "Each field should be filled in";
        }
        try {
            Integer.parseInt(ageText);
        } catch (NumberFormatException e) {
            return "Age must be a valid number";
        }
        if(Integer.parseInt(ageText) <= 0) {
            return "Age must be a positive number";
        }
        if(Integer.parseInt(ageText) > 150) {
            return "Age can't larger than 150";
        }
        if (isUsernameExists(username)) {
            return "The user name already exists";
        }
        if (isValidPassword(password)) {
            return "The password must contain both letters and numbers and be at least eight characters long";
        }
        return null;
    }

    private boolean isUsernameExists(String username) {
        return teacherList.stream().anyMatch(teacher -> teacher.getUsername().equals(username));
    }

    private boolean isValidPassword(String password) {
        return password.length() < 8 || !password.matches("^(?=.*[a-zA-Z])(?=.*[0-9]).+$");
    }

    public void assignCourseToTeacher(Teacher teacher, String courseId) throws IOException {
        if (courseId.equals(teacher.getCourseid1()) || courseId.equals(teacher.getCourseid2())) {
            throw new IOException("This course is already assigned to the teacher.");
        }

        if (teacher.getCourseid1() == null) {
            teacher.setCourseId1(courseId);
        } else if (teacher.getCourseid2() == null) {
            teacher.setCourseId2(courseId);
        } else {
            throw new IOException("Teacher has reached maximum course limit.");
        }

        writeAllTeachersToFile();
    }

    public void removeCourseFromTeacher(Teacher teacher, String courseId) throws IOException {
        if (courseId.equals(teacher.getCourseid1())) {
            teacher.setCourseId1(null);
        } else if (courseId.equals(teacher.getCourseid2())) {
            teacher.setCourseId2(null);
        } else {
            throw new IOException("The selected course is not assigned to this teacher.");
        }
        writeAllTeachersToFile();
    }

    public String validateUpdateInputs(String name, String gender, String ageText, String position, String department, String password) {
        if (name.isEmpty() || ageText.isEmpty() || gender == null || position == null || department.isEmpty() || password.isEmpty()) {
            return "Each field should be filled in";
        }
        try {
            Integer.parseInt(ageText);
        } catch (NumberFormatException e) {
            return "Age must be a valid number";
        }
        if(Integer.parseInt(ageText) <= 0) {
            return "Age must be a positive number";
        }
        if(Integer.parseInt(ageText) > 150) {
            return "Age can't larger than 150";
        }
        if (isValidPassword(password)) {
            return "The password must contain both letters and numbers and be at least eight characters long";
        }
        return null;
    }
}