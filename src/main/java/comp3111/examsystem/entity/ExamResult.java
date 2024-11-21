package comp3111.examsystem.entity;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class ExamResult {
    private final StringProperty courseID;   // 课程代码
    private final StringProperty examName;   // 考试名称
    private final IntegerProperty totalScore; // 总分
    private final IntegerProperty score;     // 得分
    private final StringProperty passStatus; // 是否通过

    // 构造函数
    public ExamResult(String courseID, String examName, int totalScore, int score, String passStatus) {
        this.courseID = new SimpleStringProperty(courseID);
        this.examName = new SimpleStringProperty(examName);
        this.totalScore = new SimpleIntegerProperty(totalScore);
        this.score = new SimpleIntegerProperty(score);
        this.passStatus = new SimpleStringProperty(passStatus);
    }

    // Getters 和 Setters（JavaFX 风格，返回 Property）
    public StringProperty courseIDProperty() {
        return courseID;
    }

    public String getCourseID() {
        return courseID.get();
    }

    public void setCourseID(String courseID) {
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

    public IntegerProperty totalScoreProperty() {
        return totalScore;
    }

    public int getTotalScore() {
        return totalScore.get();
    }

    public void setTotalScore(int totalScore) {
        this.totalScore.set(totalScore);
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

    public StringProperty passStatusProperty() {
        return passStatus;
    }

    public String getPassStatus() {
        return passStatus.get();
    }

    public void setPassStatus(String passStatus) {
        this.passStatus.set(passStatus);
    }
}
