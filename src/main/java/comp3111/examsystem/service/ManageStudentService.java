package comp3111.examsystem.service;

import comp3111.examsystem.entity.Student;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.io.*;
import java.util.*;
import java.util.stream.Collectors;

public class ManageStudentService {
    private String studentFilePath;
    private String studentExamsFilePath;
    private ObservableList<Student> studentList;
    private List<String> courseList; // To hold course IDs
    private HashMap<String, List<String>> studentCourses; // To hold student course assignments

    public ManageStudentService(String studentFilePath, String studentExamsFilePath) {
        this.studentFilePath = studentFilePath;
        this.studentExamsFilePath = studentExamsFilePath;
        this.studentList = FXCollections.observableArrayList();
        this.courseList = new ArrayList<>();
        this.studentCourses = new HashMap<>();
        loadStudentsFromFile();
        loadCoursesFromFile();
        loadStudentCoursesFromFile();
    }

    public ObservableList<Student> getStudentList() {
        return studentList;
    }

    public List<String> getCourseList() {
        return courseList;
    }

    public List<String> getCoursesForStudent(String username) {
        return studentCourses.getOrDefault(username, new ArrayList<>());
    }

    public void loadStudentsFromFile() {
        try (BufferedReader br = new BufferedReader(new FileReader(studentFilePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] data = line.split(",");
                if (data.length == 6 && !data[0].trim().isEmpty() && !data[1].trim().isEmpty() &&
                        !data[2].trim().isEmpty() && !data[3].trim().isEmpty() &&
                        !data[4].trim().isEmpty() && !data[5].trim().isEmpty()) {
                    Student student = new Student(data[0].trim(), data[1].trim(),
                            Integer.parseInt(data[2].trim()), data[3].trim(),
                            data[4].trim(), data[5].trim());
                    studentList.add(student);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void loadCoursesFromFile() {
        try (BufferedReader br = new BufferedReader(new FileReader("data/courses.txt"))) {
            String line;
            while ((line = br.readLine()) != null) {
                String courseId = line.split(",")[0].trim();
                if (!courseId.isEmpty()) {
                    courseList.add(courseId);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void loadStudentCoursesFromFile() {
        try (BufferedReader br = new BufferedReader(new FileReader("data/studentsexams.txt"))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] data = line.split(",");
                String username = data[0].trim();
                String courseId = data[1].trim();
                if (!username.isEmpty() && !courseId.isEmpty()) {
                    studentCourses.computeIfAbsent(username, k -> new ArrayList<>()).add(courseId);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void assignCourseToStudent(String username, String courseId) throws IOException {
        Set<String> courses = new HashSet<>(studentCourses.getOrDefault(username, new ArrayList<>()));

        // 检查课程是否已经分配
        if (courses.contains(courseId)) {
            throw new IOException("The student is already assigned this course.");
        }

        if (courses.size() >= 6) {
            throw new IOException("A student can only have up to 6 courses.");
        }

        courses.add(courseId);
        studentCourses.put(username, new ArrayList<>(courses));

        saveStudentCoursesToFile(username, courseId);
    }

    public void removeCourseFromStudent(String username, String courseId) throws IOException {
        if (studentCourses.containsKey(username)) {
            studentCourses.get(username).remove(courseId);
            saveStudentCoursesToFile(username, courseId, true);
        }
    }

    private void saveStudentCoursesToFile(String username, String courseId) throws IOException {
        String courseName = getCourseNameByCourseId(courseId);
        List<String> examNames = getExamNamesByCourseId(courseId);

        try (BufferedWriter bw = new BufferedWriter(new FileWriter("data/studentsexams.txt", true))) {
            for (String examName : examNames) {
                bw.write(String.join(",", username, courseId, examName, courseName));
                bw.newLine();
            }
        }
    }

    private List<String> getExamNamesByCourseId(String courseId) {
        List<String> examNames = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader("data/exam.txt"))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] data = line.split(",");
                if (data.length > 2 && data[2].trim().equals(courseId)) {
                    examNames.add(data[0].trim()); // 收集所有相关的考试名称
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return examNames; // 返回所有考试名称的列表
    }

    private String getCourseNameByCourseId(String courseId) {
        try (BufferedReader br = new BufferedReader(new FileReader("data/courses.txt"))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] data = line.split(",");
                if (data.length > 1 && data[0].trim().equals(courseId)) {
                    return data[1].trim();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "Unknown Course";
    }

    private void saveStudentCoursesToFile(String username, String courseId, boolean isRemoval) throws IOException {
        List<String> lines = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader("data/studentsexams.txt"))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] data = line.split(",");
                if (!(data[0].equals(username) && data[1].equals(courseId))) {
                    lines.add(line);
                }
            }
        }
        try (BufferedWriter bw = new BufferedWriter(new FileWriter("data/studentsexams.txt"))) {
            for (String line : lines) {
                bw.write(line);
                bw.newLine();
            }
        }
    }

    public void addStudent(Student newStudent) throws IOException {
        studentList.add(newStudent);
        String studentInput = String.join(",", newStudent.getUsername(), newStudent.getName(),
                String.valueOf(newStudent.getAge()), newStudent.getGender(),
                newStudent.getDepartment(), newStudent.getPassword());
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(studentFilePath, true))) {
            bw.write(studentInput);
            bw.newLine();
        }
    }

    public void updateStudent(Student updatedStudent, String originalUsername) throws IOException {
        boolean found = false;
        for (int i = 0; i < studentList.size(); i++) {
            if (studentList.get(i).getUsername().equals(originalUsername)) {
                studentList.set(i, updatedStudent);
                found = true;
                break;
            }
        }
        if (!found) {
            throw new IOException("Student with username " + originalUsername + " does not exist.");
        }
        saveStudentsToFile();
        updateStudentExams(originalUsername, updatedStudent.getUsername());
    }

    public void deleteStudent(String username) throws IOException {
        boolean removed = studentList.removeIf(student -> student.getUsername().equals(username));
        if (!removed) {
            throw new IOException("Student with username " + username + " does not exist.");
        }
        saveStudentsToFile();
        deleteStudentFromExams(username);
    }

    void saveStudentsToFile() throws IOException {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(studentFilePath))) {
            for (Student student : studentList) {
                String studentInput = String.join(",", student.getUsername(), student.getName(),
                        String.valueOf(student.getAge()), student.getGender(),
                        student.getDepartment(), student.getPassword());
                bw.write(studentInput);
                bw.newLine();
            }
        }
    }

    void updateStudentExams(String originalUsername, String newUsername) throws IOException {
        List<String> lines = new BufferedReader(new FileReader(studentExamsFilePath)).lines().collect(Collectors.toList());
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(studentExamsFilePath))) {
            for (String line : lines) {
                String[] data = line.split(",");
                if (data[0].equals(originalUsername)) {
                    data[0] = newUsername;
                }
                bw.write(String.join(",", data));
                bw.newLine();
            }
        }
    }

    void deleteStudentFromExams(String username) throws IOException {
        List<String> lines = new BufferedReader(new FileReader(studentExamsFilePath)).lines().collect(Collectors.toList());
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(studentExamsFilePath))) {
            for (String line : lines) {
                String[] data = line.split(",");
                if (!data[0].equals(username)) {
                    bw.write(line);
                    bw.newLine();
                }
            }
        }
    }
    public List<Student> filterStudents(String username, String name, String department) {
        return studentList.stream()
                .filter(student -> student.getUsername().toLowerCase().contains(username.toLowerCase()) &&
                        student.getName().toLowerCase().contains(name.toLowerCase()) &&
                        student.getDepartment().toLowerCase().contains(department.toLowerCase()))
                .collect(Collectors.toList());
    }

    public String validateUsername(String username) {
        for (Student student : studentList) {
            if (student.getUsername().equals(username)) {
                return "The user name already exists";
            }
        }
        return null;
    }

    public String validateInputs(String username, String name, String ageText, String gender, String department, String password) {
        if (username.isEmpty() || name.isEmpty() || ageText.isEmpty() || gender == null || department.isEmpty() || password.isEmpty()) {
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
        for (Student student : studentList) {
            if (student.getUsername().equals(username)) {
                return "The user name already exists";
            }
        }
        if (isValidPassword(password)) {
            return "The password must contain both letters and numbers and be at least eight characters long";
        }
        return null;
    }

    public boolean isValidPassword(String password) {
        return password.length() < 8 || !password.matches("^(?=.*[a-zA-Z])(?=.*[0-9]).+$");
    }

    public String validateUpdateInputs(String name, String ageText, String gender, String department, String password) {
        if (name.isEmpty() || ageText.isEmpty() || gender == null || department.isEmpty() || password.isEmpty()) {
            return "Each field should be filled in";
        }
        try {
            Integer.parseInt(ageText);
        } catch (NumberFormatException e) {
            return "Age must be a valid number";
        }
        if (isValidPassword(password)) {
            return "The password must contain both letters and numbers and be at least eight characters long";
        }
        return null;
    }
}