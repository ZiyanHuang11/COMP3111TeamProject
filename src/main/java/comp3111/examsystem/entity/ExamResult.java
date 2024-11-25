package comp3111.examsystem.entity;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class ExamResult extends Entity {
    private final StringProperty studentID; // Changed field name to studentID
    private final StringProperty examID;    // Changed field name to examID
    private final StringProperty courseID;  // Changed field name to courseID
    private final StringProperty examName;
    private final IntegerProperty score;
    private final IntegerProperty totalScore;
    private final StringProperty passStatus;

    // Constructor
    public ExamResult(String id, String studentID, String examID, String courseID, String examName, int score, int totalScore, String passStatus) {
        this.setId(id); // Call the setId method from Entity
        this.studentID = new SimpleStringProperty(studentID); // Updated field name
        this.examID = new SimpleStringProperty(examID);       // Updated field name
        this.courseID = new SimpleStringProperty(courseID);   // Updated field name
        this.examName = new SimpleStringProperty(examName);
        this.score = new SimpleIntegerProperty(score);
        this.totalScore = new SimpleIntegerProperty(totalScore);
        this.passStatus = new SimpleStringProperty(passStatus);
    }

    // Getters and Setters

    public StringProperty studentIDProperty() {
        return studentID;
    }

    public String getStudentID() { // Renamed method
        return studentID.get();
    }

    public void setStudentID(String studentID) { // Renamed method
        this.studentID.set(studentID);
    }

    public StringProperty examIDProperty() {
        return examID;
    }

    public String getExamID() { // Renamed method
        return examID.get();
    }

    public void setExamID(String examID) { // Renamed method
        this.examID.set(examID);
    }

    public StringProperty courseIDProperty() {
        return courseID;
    }

    public String getCourseID() { // Renamed method
        return courseID.get();
    }

    public void setCourseID(String courseID) { // Renamed method
        this.courseID.set(courseID);
    }

    public StringProperty examNameProperty() {
        return examName;
    }

    public String getExamName() {
        return examName.get();
    }

    public void setExamName(String examName) {
        this.examName.set(examName);
    }

    public IntegerProperty scoreProperty() {
        return score;
    }

    public int getScore() {
        return score.get();
    }

    public void setScore(int score) {
        this.score.set(score);
    }

    public IntegerProperty totalScoreProperty() {
        return totalScore;
    }

    public int getTotalScore() {
        return totalScore.get();
    }

    public void setTotalScore(int totalScore) {
        this.totalScore.set(totalScore);
    }

    public StringProperty passStatusProperty() {
        return passStatus;
    }

    public String getPassStatus() {
        return passStatus.get();
    }

    public void setPassStatus(String passStatus) {
        this.passStatus.set(passStatus);
    }

    @Override
    public String toString() {
        return "ExamResult{" +
                "id=" + getId() +
                ", studentID=" + studentID.get() +
                ", examID=" + examID.get() +
                ", courseID=" + courseID.get() +
                ", examName=" + examName.get() +
                ", score=" + score.get() +
                ", totalScore=" + totalScore.get() +
                ", passStatus=" + passStatus.get() +
                '}';
    }
}
