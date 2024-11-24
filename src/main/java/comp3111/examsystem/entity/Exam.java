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
    private final List<String> questionIds; // 保存问题 ID 的列表
    private final StringProperty courseName; // 新增的 courseName 属性
    private int duration;

    // 原有构造函数
    public Exam(String examName, String courseID, String examTime, String publish) {
        this.examName = new SimpleStringProperty(examName);
        this.courseID = new SimpleStringProperty(courseID);
        this.examTime = new SimpleStringProperty(examTime);
        this.publish = new SimpleStringProperty(publish);
        this.questionIds = new ArrayList<>();
        this.courseName = new SimpleStringProperty(""); // 初始化 courseName
    }

    // 新增带问题 ID 列表的构造函数
    public Exam(String examName, String courseID, String examTime, String publish, List<String> questionIds) {
        this.examName = new SimpleStringProperty(examName);
        this.courseID = new SimpleStringProperty(courseID);
        this.examTime = new SimpleStringProperty(examTime);
        this.publish = new SimpleStringProperty(publish);
        this.questionIds = new ArrayList<>(questionIds); // 复制问题 ID 列表
        this.courseName = new SimpleStringProperty(""); // 初始化 courseName
    }

    // Getter 和 Setter 方法

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
        return questionIds;
    }

    public void addQuestionId(String questionId) {
        this.questionIds.add(questionId);
    }

    public void removeQuestionId(String questionId) {
        this.questionIds.remove(questionId);
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
                ", questionIds=" + questionIds +
                ", courseName=" + courseName.get() +
                ", duration=" + duration +
                '}';
    }
}
