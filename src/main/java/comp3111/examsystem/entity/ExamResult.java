package comp3111.examsystem.entity;

import javafx.beans.property.*;

public class ExamResult extends Entity {
    private String studentID;
    private String examID;
    private String courseID;
    private String examName;
    private int score;
    private int totalScore;
    private String passStatus;

    // 无参数构造函数
    public ExamResult() {
    }

    // 全参数构造函数
    public ExamResult(String id, String studentID, String examID, String courseID, String examName, int score, int totalScore, String passStatus) {
        this.setId(id); // Call the setId method from Entity
        this.studentID = studentID;
        this.examID = examID;
        this.courseID = courseID;
        this.examName = examName;
        this.score = score;
        this.totalScore = totalScore;
        this.passStatus = passStatus;
    }

    // Getters and Setters

    public String getStudentID() {
        return studentID;
    }

    public void setStudentID(String studentID) {
        this.studentID = studentID;
    }

    public String getExamID() {
        return examID;
    }

    public void setExamID(String examID) {
        this.examID = examID;
    }

    public String getCourseID() {
        return courseID;
    }

    public void setCourseID(String courseID) {
        this.courseID = courseID;
    }

    public String getExamName() {
        return examName;
    }

    public void setExamName(String examName) {
        this.examName = examName;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public int getTotalScore() {
        return totalScore;
    }

    public void setTotalScore(int totalScore) {
        this.totalScore = totalScore;
    }

    public String getPassStatus() {
        return passStatus;
    }

    public void setPassStatus(String passStatus) {
        this.passStatus = passStatus;
    }

    // JavaFX Properties (if needed)

    public StringProperty studentIDProperty() {
        return new SimpleStringProperty(studentID);
    }

    public StringProperty examIDProperty() {
        return new SimpleStringProperty(examID);
    }

    public StringProperty courseIDProperty() {
        return new SimpleStringProperty(courseID);
    }

    public StringProperty examNameProperty() {
        return new SimpleStringProperty(examName);
    }

    public IntegerProperty scoreProperty() {
        return new SimpleIntegerProperty(score);
    }

    public IntegerProperty totalScoreProperty() {
        return new SimpleIntegerProperty(totalScore);
    }

    public StringProperty passStatusProperty() {
        return new SimpleStringProperty(passStatus);
    }

    @Override
    public String toString() {
        return "ExamResult{" +
                "id=" + getId() +
                ", studentID=" + studentID +
                ", examID=" + examID +
                ", courseID=" + courseID +
                ", examName=" + examName +
                ", score=" + score +
                ", totalScore=" + totalScore +
                ", passStatus=" + passStatus +
                '}';
    }
}
