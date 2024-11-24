package comp3111.examsystem.entity;

import java.util.ArrayList;
import java.util.List;

public class Exam extends Entity {
    private String examName;
    private String courseID;
    private String examDate;
    private List<String> questionIds;
    private int duration;

    public Exam(String id, String examName, String examDate, String courseID, List<String> questionIds, int duration) {
        this.setId(id);
        this.examName = examName;
        this.courseID = courseID;
        this.examDate = examDate;
        this.questionIds = new ArrayList<>(questionIds);
        this.duration = duration;
    }

    public String getExamName() { return examName; }
    public void setExamName(String examName) { this.examName = examName; }

    public String getCourseID() { return courseID; }
    public void setCourseID(String courseID) { this.courseID = courseID; }

    public String getExamDate() { return examDate; }
    public void setExamDate(String examDate) { this.examDate = examDate; }

    public List<String> getQuestionIds() { return questionIds; }
    public void setQuestionIds(List<String> questionIds) { this.questionIds = questionIds; }

    public int getDuration() { return duration; }
    public void setDuration(int duration) { this.duration = duration; }

    @Override
    public String toString() {
        return "Exam{" +
                "id=" + getId() +
                ", examName='" + examName + '\'' +
                ", courseID='" + courseID + '\'' +
                ", examDate='" + examDate + '\'' +
                ", questionIds=" + questionIds +
                ", duration=" + duration +
                '}';
    }
}
