package comp3111.examsystem.entity;

import javafx.beans.property.*;

public class ExamResult extends Entity {
    private String studentID;
    private String examID;
    private String courseID;
    private String examName;
    private String courseName; // 新增字段
    private int time; // 新增字段
    private int score;
    private int totalScore;

    // 无参数构造函数
    public ExamResult() {
    }

    // 全参数构造函数
    public ExamResult(String id, String studentID, String examName, String courseID, String courseName, int time, int score, int totalScore) {
        this.setId(id); // 设置 ID
        this.studentID = studentID;
        this.examName = examName;
        this.courseID = courseID;
        this.courseName = courseName; // 默认值为传入的 courseName
        this.time = time;
        this.score = score;
        this.totalScore = totalScore;
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

    public String getCourseName() { // 新增 Getter
        return courseName;
    }

    public void setCourseName(String courseName) { // 新增 Setter
        this.courseName = courseName;
    }

    public int getTime() { // 新增 Getter
        return time;
    }

    public void setTime(int time) { // 新增 Setter
        this.time = time;
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

    // JavaFX Properties

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

    public StringProperty courseNameProperty() { // 新增 Property
        return new SimpleStringProperty(courseName);
    }

    public IntegerProperty timeProperty() { // 新增 Property
        return new SimpleIntegerProperty(time);
    }

    public IntegerProperty scoreProperty() {
        return new SimpleIntegerProperty(score);
    }

    public IntegerProperty totalScoreProperty() {
        return new SimpleIntegerProperty(totalScore);
    }

    @Override
    public String toString() {
        return "ExamResult{" +
                "id=" + getId() +
                ", studentID=" + studentID +
                ", examID=" + examID +
                ", courseID=" + courseID +
                ", examName=" + examName +
                ", courseName=" + courseName +
                ", time=" + time +
                ", score=" + score +
                ", totalScore=" + totalScore +
                '}';
    }
}
