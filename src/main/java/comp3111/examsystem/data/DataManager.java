package comp3111.examsystem.data;

import java.util.Objects;
import comp3111.examsystem.entity.*;
import comp3111.examsystem.tools.Database;
import comp3111.examsystem.tools.FileUtil;

import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.stream.Collectors;

public class DataManager {
    private final Database<Student> studentDatabase = new Database<>(Student.class);
    private final Database<Course> courseDatabase = new Database<>(Course.class);
    private final Database<Exam> examDatabase = new Database<>(Exam.class);
    private final Database<Question> questionDatabase = new Database<>(Question.class);
    private final Database<ExamResult> examResultDatabase = new Database<>(ExamResult.class);
    private final Database<Teacher> teacherDatabase = new Database<>(Teacher.class);
    private final Database<Manager> managerDatabase = new Database<>(Manager.class);

    // --- GETTERS FOR DATA ---
    public List<Student> getStudents() {
        return studentDatabase.getAll();
    }

    public List<Course> getCourses() {
        return courseDatabase.getAll();
    }

    public List<Exam> getExams() {
        return examDatabase.getAll();
    }

    public List<Question> getQuestions() {
        return questionDatabase.getAll();
    }

    public List<ExamResult> getExamResults() {
        return examResultDatabase.getAll();
    }

    public List<Teacher> getTeachers() {
        return teacherDatabase.getAll();
    }

    public List<Manager> getManagers() {
        return managerDatabase.getAll();
    }

    // --- COURSE DATA LOADING ---
    public List<Map<String, String>> loadCourseData() {
        List<String> lines = FileUtil.readFile("data/course.txt");

        return lines.stream()
                .map(line -> {
                    Map<String, String> courseMap = new HashMap<>();
                    String[] pairs = line.split(",");
                    for (String pair : pairs) {
                        String[] keyValue = pair.split(":");
                        if (keyValue.length == 2) {
                            courseMap.put(keyValue[0].trim(), keyValue[1].trim());
                        }
                    }
                    return courseMap;
                })
                .collect(Collectors.toList());
    }

    // --- EXAM SUMMARY DATA LOADING ---
    public List<Map<String, String>> loadExamSummaryData() {
        List<String> lines = FileUtil.readFile("data/exam_summary.txt");

        return lines.stream()
                .map(line -> {
                    Map<String, String> examMap = new HashMap<>();
                    String[] pairs = line.split(",");
                    for (String pair : pairs) {
                        String[] keyValue = pair.split(":");
                        if (keyValue.length == 2) {
                            examMap.put(keyValue[0].trim(), keyValue[1].trim());
                        }
                    }
                    return examMap;
                })
                .collect(Collectors.toList());
    }

    // --- STUDENT OPERATIONS ---
    public void addStudent(Student student) {
        studentDatabase.add(student);
    }

    public void updateStudent(String id, Student updatedStudent) {
        studentDatabase.update(updatedStudent);
    }

    public void deleteStudent(String id) {
        studentDatabase.delByKey(id);
    }

    // --- COURSE OPERATIONS ---
    public void addCourse(Course course) {
        courseDatabase.add(course);
    }

    public void updateCourse(String id, Course updatedCourse) {
        courseDatabase.update(updatedCourse);
    }

    public void deleteCourse(String id) {
        courseDatabase.delByKey(id);
    }

    // --- EXAM OPERATIONS ---
    public void addExam(Exam exam) {
        examDatabase.add(exam);
    }

    public void updateExam(String id, Exam updatedExam) {
        examDatabase.update(updatedExam);
    }

    public void deleteExam(String id) {
        examDatabase.delByKey(id);
    }

    // --- QUESTION OPERATIONS ---
    public void addQuestion(Question question) {
        questionDatabase.add(question);
    }

    public void updateQuestion(String id, Question updatedQuestion) {
        questionDatabase.update(updatedQuestion);
    }

    public void deleteQuestion(String id) {
        questionDatabase.delByKey(id);
    }

    // --- EXAM RESULT OPERATIONS ---
    public void addExamResult(ExamResult result) {
        examResultDatabase.add(result);
    }

    public void updateExamResult(String id, ExamResult updatedResult) {
        examResultDatabase.update(updatedResult);
    }

    public void deleteExamResult(String id) {
        examResultDatabase.delByKey(id);
    }

    public void saveExamResults() {
        List<String> lines = getExamResults().stream()
                .map(result -> String.format("id:%s,studentID:%s,examID:%s,score:%d,totalScore:%d,time:%d",
                        result.getId(), result.getStudentID(), result.getExamID(),
                        result.getScore(), result.getTotalScore(), result.getTime()))
                .collect(Collectors.toList());

        if (lines.isEmpty()) {
            System.out.println("No exam results to save!");
            return;
        }

        FileUtil.writeFile("data/examresult.txt", lines);
    }

    // --- TEACHER OPERATIONS ---
    public void addTeacher(Teacher teacher) {
        teacherDatabase.add(teacher);
    }

    public void updateTeacher(String id, Teacher updatedTeacher) {
        teacherDatabase.update(updatedTeacher);
    }

    public void deleteTeacher(String id) {
        teacherDatabase.delByKey(id);
    }

    public void saveTeachers() {
        List<String> lines = getTeachers().stream()
                .map(teacher -> String.format("id:%s,username:%s,password:%s,name:%s,gender:%s,age:%d,title:%s,department:%s",
                        teacher.getId(), teacher.getUsername(), teacher.getPassword(),
                        teacher.getName(), teacher.getGender(), teacher.getAge(),
                        teacher.getTitle(), teacher.getDepartment()))
                .collect(Collectors.toList());

        if (lines.isEmpty()) {
            System.out.println("No teachers to save!");
            return;
        }

        FileUtil.writeFile("data/teacher.txt", lines);
    }
    public Teacher getTeacherByUsername(String username) {
        return getTeachers().stream()
                .filter(t -> t != null && t.getUsername().equals(username))
                .findFirst()
                .orElse(null);
    }

    // --- MANAGER OPERATIONS ---
    public void addManager(Manager manager) {
        managerDatabase.add(manager);
    }

    public void updateManager(String id, Manager updatedManager) {
        managerDatabase.update(updatedManager);
    }

    public void deleteManager(String id) {
        managerDatabase.delByKey(id);
    }

    public void saveManagers() {
        List<String> lines = getManagers().stream()
                .map(manager -> String.format("id:%s,username:%s,password:%s",
                        manager.getId(), manager.getUsername(), manager.getPassword()))
                .collect(Collectors.toList());

        if (lines.isEmpty()) {
            System.out.println("No managers to save!");
            return;
        }

        FileUtil.writeFile("data/manager.txt", lines);
    }
    public void saveStudents() {
        List<String> lines = getStudents().stream()
                .filter(Objects::nonNull) // 确保没有空的学生记录
                .map(student -> String.format("id:%s,username:%s,name:%s,age:%d,gender:%s,department:%s,password:%s",
                        student.getId(), student.getUsername(), student.getName(),
                        student.getAge(), student.getGender(), student.getDepartment(), student.getPassword()))
                .collect(Collectors.toList());

        if (lines.isEmpty()) {
            System.out.println("No students to save!");
            return;
        }

        // 将学生数据写入文件
        FileUtil.writeFile("data/student.txt", lines);
    }

}
