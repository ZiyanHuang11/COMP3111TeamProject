package comp3111.examsystem.data;

import java.util.Objects;
import comp3111.examsystem.entity.*;
import comp3111.examsystem.tools.Database;
import comp3111.examsystem.tools.FileUtil;

import java.util.List;
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

    // --- SAVE ALL DATA ---
    public void save() {
        saveStudents();
        saveCourses();
        saveExams();
        saveQuestions();
        saveExamResults();
        saveTeachers();
        saveManagers();
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

    public void saveStudents() {
        List<String> lines = getStudents().stream()
                .filter(Objects::nonNull)
                .map(student -> String.format("id:%s,username:%s,name:%s,age:%d,gender:%s,department:%s,password:%s",
                        student.getId(), student.getUsername(), student.getName(),
                        student.getAge(), student.getGender(), student.getDepartment(), student.getPassword()))
                .collect(Collectors.toList());

        // Only proceed if the lines are not empty
        if (lines.isEmpty()) {
            System.out.println("No students to save!");
            return;
        }

        // Write the data to the file
        FileUtil.writeFile("data/student.txt", lines);
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

    public void saveCourses() {
        List<String> lines = getCourses().stream()
                .map(course -> String.format("id:%s,courseID:%s,courseName:%s,department:%s",
                        course.getId(), course.getCourseID(), course.getCourseName(), course.getDepartment()))
                .collect(Collectors.toList());

        // Only proceed if the lines are not empty
        if (lines.isEmpty()) {
            System.out.println("No courses to save!");
            return;
        }

        // Write the data to the file
        FileUtil.writeFile("data/course.txt", lines);
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

    public void saveExams() {
        List<String> lines = getExams().stream()
                .map(exam -> String.format("id:%s,examName:%s,examTime:%s,courseID:%s,questionIDs:%s,duration:%d",
                        exam.getId(), exam.getExamName(), exam.getExamTime(),
                        exam.getCourseID(), String.join("|", exam.getQuestionIds()), exam.getDuration()))
                .collect(Collectors.toList());

        // Only proceed if the lines are not empty
        if (lines.isEmpty()) {
            System.out.println("No exams to save!");
            return;
        }

        // Write the data to the file
        FileUtil.writeFile("data/exam.txt", lines);
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

    public void saveQuestions() {
        List<String> lines = getQuestions().stream()
                .map(question -> String.format("id:%s,question:%s,option1:%s,option2:%s,option3:%s,option4:%s,answer:%s,type:%s,score:%d",
                        question.getId(), question.getQuestion(), question.getOption1(),
                        question.getOption2(), question.getOption3(), question.getOption4(),
                        question.getAnswer(), question.getType(), question.getScore()))
                .collect(Collectors.toList());

        // Only proceed if the lines are not empty
        if (lines.isEmpty()) {
            System.out.println("No questions to save!");
            return;
        }

        // Write the data to the file
        FileUtil.writeFile("data/question.txt", lines);
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
                .map(result -> String.format("id:%s,studentID:%s,examID:%s,score:%d,totalScore:%d,passStatus:%s",
                        result.getId(), result.getStudentID(), result.getExamID(),
                        result.getScore(), result.getTotalScore(), result.getPassStatus()))
                .collect(Collectors.toList());

        // Only proceed if the lines are not empty
        if (lines.isEmpty()) {
            System.out.println("No exam results to save!");
            return;
        }

        // Write the data to the file
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

        // Only proceed if the lines are not empty
        if (lines.isEmpty()) {
            System.out.println("No teachers to save!");
            return;
        }

        // Write the data to the file
        FileUtil.writeFile("data/teacher.txt", lines);
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

        // Only proceed if the lines are not empty
        if (lines.isEmpty()) {
            System.out.println("No managers to save!");
            return;
        }

        // Write the data to the file
        FileUtil.writeFile("data/manager.txt", lines);
    }

    // --- QUESTION FILTERING ---
    public List<Question> filterQuestions(String questionFilter, String typeFilter, String scoreFilter) {
        // Stream through the list of questions and filter based on the provided filters
        return questionDatabase.getAll().stream()
                .filter(q -> (questionFilter.isEmpty() || q.getQuestion().toLowerCase().contains(questionFilter.toLowerCase())) &&
                        (typeFilter.equals("All") || q.getType().equals(typeFilter)) &&
                        (scoreFilter.isEmpty() || String.valueOf(q.getScore()).equals(scoreFilter)))
                .collect(Collectors.toList());
    }

    public Student getStudentByUsername(String username) {
        return getStudents().stream()
                .filter(s -> s != null && s.getUsername().equals(username))
                .findFirst()
                .orElse(null);
    }

    public Teacher getTeacherByUsername(String username) {
        return getTeachers().stream()
                .filter(t -> t != null && t.getUsername().equals(username))
                .findFirst()
                .orElse(null);
    }
}
