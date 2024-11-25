package comp3111.examsystem.data;

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
                .map(student -> String.format("id:%s,username:%s,name:%s,age:%d,gender:%s,department:%s,password:%s",
                        student.getId(), student.getUsername(), student.getName(),
                        student.getAge(), student.getGender(), student.getDepartment(), student.getPassword()))
                .collect(Collectors.toList());

        // 读取现有文件内容
        List<String> existingLines = FileUtil.readFile("data/students.txt");

        // 将现有内容和新内容合并
        existingLines.addAll(lines);

        // 写入文件，保持原内容，并追加新数据
        FileUtil.writeFile("data/students.txt", existingLines);
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

        // 读取现有内容
        List<String> existingLines = FileUtil.readFile("data/courses.txt");

        // 合并新内容
        existingLines.addAll(lines);

        // 写入文件，保持原内容，并追加新数据
        FileUtil.writeFile("data/courses.txt", existingLines);
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

        // 读取现有内容
        List<String> existingLines = FileUtil.readFile("data/exam.txt");

        // 合并新内容
        existingLines.addAll(lines);

        // 写入文件，保持原内容，并追加新数据
        FileUtil.writeFile("data/exam.txt", existingLines);
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

    public List<Question> filterQuestions(String questionFilter, String typeFilter, String scoreFilter) {
        return questionDatabase.getAll().stream()
                .filter(q -> (questionFilter.isEmpty() || q.getQuestion().toLowerCase().contains(questionFilter.toLowerCase())) &&
                        (typeFilter.equals("All") || q.getType().equals(typeFilter)) &&
                        (scoreFilter.isEmpty() || String.valueOf(q.getScore()).equals(scoreFilter)))
                .collect(Collectors.toList());
    }

    public void saveQuestions() {
        List<String> lines = getQuestions().stream()
                .map(question -> String.format("id:%s,question:%s,optionA:%s,optionB:%s,optionC:%s,optionD:%s,answer:%s,type:%s,score:%d",
                        question.getId(), question.getQuestion(), question.getOptionA(),
                        question.getOptionB(), question.getOptionC(), question.getOptionD(),
                        question.getAnswer(), question.getType(), question.getScore()))
                .collect(Collectors.toList());

        // 读取现有内容
        List<String> existingLines = FileUtil.readFile("data/questions.txt");

        // 合并新内容
        existingLines.addAll(lines);

        // 写入文件，保持原内容，并追加新数据
        FileUtil.writeFile("data/questions.txt", existingLines);
    }


    public void saveExamResults() {
        List<String> lines = getExamResults().stream()
                .map(result -> String.format("id:%s,studentID:%s,examID:%s,score:%d,totalScore:%d,passStatus:%s",
                        result.getId(), result.getStudentID(), result.getExamID(),
                        result.getScore(), result.getTotalScore(), result.getPassStatus()))
                .collect(Collectors.toList());

        // 读取现有内容
        List<String> existingLines = FileUtil.readFile("data/exam_results.txt");

        // 合并新内容
        existingLines.addAll(lines);

        // 写入文件，保持原内容，并追加新数据
        FileUtil.writeFile("data/exam_results.txt", existingLines);
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

        // 读取现有内容
        List<String> existingLines = FileUtil.readFile("data/teachers.txt");

        // 合并新内容
        existingLines.addAll(lines);

        // 写入文件，保持原内容，并追加新数据
        FileUtil.writeFile("data/teachers.txt", existingLines);
    }


    public void saveManagers() {
        List<String> lines = getManagers().stream()
                .map(manager -> String.format("id:%s,username:%s,password:%s",
                        manager.getId(), manager.getUsername(), manager.getPassword()))
                .collect(Collectors.toList());

        // 读取现有内容
        List<String> existingLines = FileUtil.readFile("data/managers.txt");

        // 合并新内容
        existingLines.addAll(lines);

        // 写入文件，保持原内容，并追加新数据
        FileUtil.writeFile("data/managers.txt", existingLines);
    }

}
