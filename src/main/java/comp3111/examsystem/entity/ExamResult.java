package comp3111.examsystem.entity;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class ExamResult extends Entity {
    private final StringProperty studentID;
    private final StringProperty examID;
    private final StringProperty courseID;   // 新增 courseID 属性
    private final StringProperty examName;   // 新增 examName 属性
    private final IntegerProperty score;
    private final IntegerProperty totalScore;
    private final StringProperty passStatus;

    // 构造函数
    public ExamResult(String id, String studentID, String examID, String courseID, String examName, int score, int totalScore, String passStatus) {
        this.setId(id);
        this.studentID = new SimpleStringProperty(studentID);
        this.examID = new SimpleStringProperty(examID);
        this.courseID = new SimpleStringProperty(courseID); // 初始化 courseID
        this.examName = new SimpleStringProperty(examName); // 初始化 examName
        this.score = new SimpleIntegerProperty(score);
        this.totalScore = new SimpleIntegerProperty(totalScore);
        this.passStatus = new SimpleStringProperty(passStatus);
    }

    // Getters 和 Setters（JavaFX 风格，返回 Property）
    public StringProperty studentIDProperty() {
        return studentID;
    }

    public String getStudentID() {
        return studentID.get();
    }

    public void setStudentID(String studentID) {
        this.studentID.set(studentID);
    }

    public StringProperty examIDProperty() {
        return examID;
    }

    public String getExamID() {
        return examID.get();
    }

    public void setExamID(String examID) {
        this.examID.set(examID);
    }

    public StringProperty courseIDProperty() { // 新增 courseIDProperty 方法
        return courseID;
    }

    public String getCourseID() { // 新增 getCourseID 方法
        return courseID.get();
    }

    public void setCourseID(String courseID) { // 新增 setCourseID 方法
        this.courseID.set(courseID);
    }

    public StringProperty examNameProperty() { // 新增 examNameProperty 方法
        return examName;
    }

    public String getExamName() { // 新增 getExamName 方法
        return examName.get();
    }

    public void setExamName(String examName) { // 新增 setExamName 方法
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
