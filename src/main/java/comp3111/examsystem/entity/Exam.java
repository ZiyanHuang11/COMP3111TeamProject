package comp3111.examsystem.entity;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class Exam {
    private final StringProperty examName;
    private final StringProperty courseID;
    private final StringProperty examTime;
    private final StringProperty publish;
    private final ObservableList<Question> questions;
    private final StringProperty courseName;
    private int duration;

    public Exam(String examName, String courseID, String examTime, String publish) {
        this.examName = new SimpleStringProperty(examName);
        this.courseID = new SimpleStringProperty(courseID);
        this.examTime = new SimpleStringProperty(examTime);
        this.publish = new SimpleStringProperty(publish);
        this.questions = FXCollections.observableArrayList();
        this.courseName = new SimpleStringProperty("");
    }

    // Getter 和 Setter 方法

    public int getDuration() {return duration;}

    public void setDuration(int duration) {this.duration = duration;}

    public String getCourseName() {return courseName.get();}

    public void setCourseName(String courseName) {this.courseName.set(courseName);}

    public StringProperty courseNameProperty() {return courseName;}

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

    public ObservableList<Question> getQuestions() {
        return questions;
    }

    public void setQuestions(ObservableList<Question> questions) {
        this.questions.setAll(questions);
    }
}
