package comp3111.examsystem.entity;

import java.util.List;

public class Quiz {
    private String courseId;
    private String courseName;
    private String examType;
    private int duration; // 考试总时间
    private List<StudentQuestion> questions;

    public Quiz(String courseId, String courseName, String examType, int duration, List<StudentQuestion> questions) {
        this.courseId = courseId;
        this.courseName = courseName;
        this.examType = examType;
        this.duration = duration;
        this.questions = questions;
    }

    public String getCourseId() { return courseId; }
    public String getCourseName() { return courseName; }
    public String getExamType() { return examType; }
    public int getDuration() { return duration; }
    public List<StudentQuestion> getQuestions() { return questions; }
}
