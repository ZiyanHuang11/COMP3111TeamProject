package comp3111.examsystem.entity;

import javafx.beans.property.*;

import java.util.List;

public class Exam {
    private StringProperty examName;
    private StringProperty courseID;
    private StringProperty examTime;
    private StringProperty publish;
    private IntegerProperty duration;
    private List<Question> questions;

    public Exam(String examName, String courseID, String examTime, String publish, int duration, List<Question> questions) {
        this.examName = new SimpleStringProperty(examName);
        this.courseID = new SimpleStringProperty(courseID);
        this.examTime = new SimpleStringProperty(examTime);
        this.publish = new SimpleStringProperty(publish);
        this.duration = new SimpleIntegerProperty(duration);
        this.questions = questions;
    }

    // Getter 和 Setter 方法

    public StringProperty examNameProperty() {
        return examName;
    }

    public String getExamName() {
        return examName.get();
    }

    public void setExamName(String examName) {
        this.examName.set(examName);
    }

    public StringProperty courseIDProperty() {
        return courseID;
    }

    public String getCourseID() {
        return courseID.get();
    }

    public void setCourseID(String courseID) {
        this.courseID.set(courseID);
    }

    public StringProperty examTimeProperty() {
        return examTime;
    }

    public String getExamTime() {
        return examTime.get();
    }

    public void setExamTime(String examTime) {
        this.examTime.set(examTime);
    }

    public StringProperty publishProperty() {
        return publish;
    }

    public String getPublish() {
        return publish.get();
    }

    public void setPublish(String publish) {
        this.publish.set(publish);
    }

    public IntegerProperty durationProperty() {
        return duration;
    }

    public int getDuration() {
        return duration.get();
    }

    public void setDuration(int duration) {
        this.duration.set(duration);
    }

    public List<Question> getQuestions() {
        return questions;
    }

    public void setQuestions(List<Question> questions) {
        this.questions = questions;
    }

    public void addQuestion(Question question) {
        this.questions.add(question);
    }
}
