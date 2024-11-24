package comp3111.examsystem.data;

import comp3111.examsystem.entity.*;
import comp3111.examsystem.tools.FileUtil;

import java.util.ArrayList;
import java.util.List;

public class DataManager {
    private List<Student> students;
    private List<Course> courses;
    private List<Exam> exams;
    private List<ExamResult> examResults;
    private List<Question> questions;
    private List<Teacher> teachers;
    private List<Manager> managers;

    public DataManager() {
        students = loadStudents();
        courses = loadCourses();
        exams = loadExams();
        examResults = loadExamResults();
        questions = loadQuestions();
        teachers = loadTeachers();
        managers = loadManagers();
    }

    public List<Student> getStudents() {
        return students;
    }

    public List<Course> getCourses() {
        return courses;
    }

    public List<Exam> getExams() {
        return exams;
    }

    public List<ExamResult> getExamResults() {
        return examResults;
    }

    public List<Question> getQuestions() {
        return questions;
    }

    public List<Teacher> getTeachers() {
        return teachers;
    }

    public List<Manager> getManagers() {
        return managers;
    }

    private List<Student> loadStudents() {
        List<Student> studentList = new ArrayList<>();
        List<String> lines = FileUtil.readFileByLines("data/students.txt");
        for (String line : lines) {
            String[] parts = line.split(",");
            String id = parts[0].split(":")[1];
            String username = parts[1].split(":")[1];
            String name = parts[2].split(":")[1];
            int age = Integer.parseInt(parts[3].split(":")[1]);
            String gender = parts[4].split(":")[1];
            String department = parts[5].split(":")[1];
            String password = parts[6].split(":")[1];
            studentList.add(new Student(id, username, name, age, gender, department, password));
        }
        return studentList;
    }

    private List<Course> loadCourses() {
        List<Course> courseList = new ArrayList<>();
        List<String> lines = FileUtil.readFileByLines("data/courses.txt");
        for (String line : lines) {
            String[] parts = line.split(",");
            String id = parts[0].split(":")[1];
            String courseID = parts[1].split(":")[1];
            String courseName = parts[2].split(":")[1];
            String department = parts[3].split(":")[1];
            courseList.add(new Course(id, courseID, courseName, department));
        }
        return courseList;
    }

    private List<Exam> loadExams() {
        List<Exam> examList = new ArrayList<>();
        List<String> lines = FileUtil.readFileByLines("data/exams.txt");
        for (String line : lines) {
            String[] parts = line.split(",");
            String id = parts[0].split(":")[1];
            String examName = parts[1].split(":")[1];
            String examDate = parts[2].split(":")[1];
            String courseID = parts[3].split(":")[1];
            String[] questionIDs = parts[4].split(":")[1].split("\\|");
            List<String> questionList = new ArrayList<>();
            for (String q : questionIDs) {
                questionList.add(q.trim());
            }
            int duration = Integer.parseInt(parts[5].split(":")[1]);
            examList.add(new Exam(id, examName, examDate, courseID, questionList, duration));
        }
        return examList;
    }

    private List<ExamResult> loadExamResults() {
        List<ExamResult> resultList = new ArrayList<>();
        List<String> lines = FileUtil.readFileByLines("data/exam_results.txt");
        for (String line : lines) {
            String[] parts = line.split(",");
            String id = parts[0].split(":")[1];
            String studentID = parts[1].split(":")[1];
            String examID = parts[2].split(":")[1];
            int score = Integer.parseInt(parts[3].split(":")[1]);
            int totalScore = Integer.parseInt(parts[4].split(":")[1]);
            String passStatus = parts[5].split(":")[1];
            resultList.add(new ExamResult(id, studentID, examID, score, totalScore, passStatus));
        }
        return resultList;
    }

    private List<Question> loadQuestions() {
        List<Question> questionList = new ArrayList<>();
        List<String> lines = FileUtil.readFileByLines("data/questions.txt");
        for (String line : lines) {
            String[] parts = line.split(",");
            String id = parts[0].split(":")[1];
            String question = parts[1].split(":")[1];
            String optionA = parts[2].split(":")[1];
            String optionB = parts[3].split(":")[1];
            String optionC = parts[4].split(":")[1];
            String optionD = parts[5].split(":")[1];
            String answer = parts[6].split(":")[1];
            String type = parts[7].split(":")[1];
            int score = Integer.parseInt(parts[8].split(":")[1]);
            questionList.add(new Question(id, question, optionA, optionB, optionC, optionD, answer, type, score));
        }
        return questionList;
    }

    private List<Teacher> loadTeachers() {
        List<Teacher> teacherList = new ArrayList<>();
        List<String> lines = FileUtil.readFileByLines("data/teachers.txt");
        for (String line : lines) {
            String[] parts = line.split(",");
            String id = parts[0].split(":")[1];
            String username = parts[1].split(":")[1];
            String password = parts[2].split(":")[1];
            String name = parts[3].split(":")[1];
            String gender = parts[4].split(":")[1];
            int age = Integer.parseInt(parts[5].split(":")[1]);
            String title = parts[6].split(":")[1];
            String department = parts[7].split(":")[1];
            teacherList.add(new Teacher(id, username, password, name, gender, age, title, department));
        }
        return teacherList;
    }

    private List<Manager> loadManagers() {
        List<Manager> managerList = new ArrayList<>();
        List<String> lines = FileUtil.readFileByLines("data/managers.txt");
        for (String line : lines) {
            String[] parts = line.split(",");
            String id = parts[0].split(":")[1];
            String username = parts[1].split(":")[1];
            String password = parts[2].split(":")[1];
            managerList.add(new Manager(id, username, password));
        }
        return managerList;
    }
}
