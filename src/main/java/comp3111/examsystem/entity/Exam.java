package comp3111.examsystem.entity;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import java.util.ArrayList;
import java.util.List;

public class Exam extends Entity {
    private final StringProperty examName;
    private final StringProperty courseID;
    private final StringProperty examTime;
    private final StringProperty publish;
    private final List<String> questionIds;
    private final StringProperty courseName;
    private int duration;

    // Constructor
    public Exam(String examName, String courseID, String examTime, String publish, List<String> questionIds, int duration) {
        this.examName = new SimpleStringProperty(examName);
        this.courseID = new SimpleStringProperty(courseID);
        this.examTime = new SimpleStringProperty(examTime);
        this.publish = new SimpleStringProperty(publish);
        this.questionIds = new ArrayList<>(questionIds);
        this.courseName = new SimpleStringProperty("");
        this.duration = duration;
    }

    public Exam() {
        this.examName = new SimpleStringProperty("");
        this.courseID = new SimpleStringProperty("");
        this.examTime = new SimpleStringProperty("");
        this.publish = new SimpleStringProperty("");
        this.questionIds = new ArrayList<>();
        this.courseName = new SimpleStringProperty("");
        this.duration = 0;
    }

    public Exam(String examName, String courseID, String examTime, String publish, List<String> questionIds) {
        this(examName, courseID, examTime, publish, questionIds, 0);
    }

    // Method to get question IDs as a single string
    public String getQuestionIdsAsString() {
        return String.join("|", questionIds); // Joins IDs with "|" as the delimiter
    }

    // Retrieve associated Question objects by accepting DataManager as a parameter
    public List<Question> getQuestions(List<Question> allQuestions) {
        List<Question> associatedQuestions = new ArrayList<>();
        for (String questionId : questionIds) {
            for (Question question : allQuestions) {
                if (question.getId().equals(questionId)) {
                    associatedQuestions.add(question);
                }
            }
        }
        return associatedQuestions;
    }

    // Getter and Setter for duration
    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public String getCourseName() {
        return courseName.get();
    }

    public void setCourseName(String courseName) {
        this.courseName.set(courseName);
    }

    public StringProperty courseNameProperty() {
        return courseName;
    }

    public String getExamName() {
        return examName.get();
    }

    public void setExamName(String examName) {
        this.examName.set(examName);
    }

    public StringProperty examNameProperty() {
        return examName;
    }

    public String getCourseID() {
        return courseID.get();
    }

    public void setCourseID(String courseID) {
        this.courseID.set(courseID);
    }

    public StringProperty courseIDProperty() {
        return courseID;
    }

    public String getExamTime() {
        return examTime.get();
    }

    public void setExamTime(String examTime) {
        this.examTime.set(examTime);
    }

    public StringProperty examTimeProperty() {
        return examTime;
    }

    public String getPublish() {
        return publish.get();
    }

    public void setPublish(String publish) {
        this.publish.set(publish);
    }

    public StringProperty publishProperty() {
        return publish;
    }

    public List<String> getQuestionIds() {
        return new ArrayList<>(questionIds);
    }

    public void addQuestionId(String questionId) {
        this.questionIds.add(questionId);
    }

    public boolean removeQuestionId(String questionId) {
        return this.questionIds.remove(questionId); // 返回操作结果，便于测试校验
    }

    public void setQuestionIds(List<String> questionIds) {
        this.questionIds.clear();
        this.questionIds.addAll(questionIds);
    }

    @Override
    public String toString() {
        return "Exam{" +
                "examName=" + examName.get() +
                ", courseID=" + courseID.get() +
                ", examTime=" + examTime.get() +
                ", publish=" + publish.get() +
                ", questionIds=" + getQuestionIdsAsString() + // Use new method
                ", courseName=" + courseName.get() +
                ", duration=" + duration +
                '}';
    }
}
