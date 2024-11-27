package comp3111.examsystem.entity;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.List;

public class Exam {
    // 原有字段
    private final StringProperty examName;
    private final StringProperty courseID;
    private final StringProperty examTime;
    private final StringProperty publish;

    // 新增字段
    private final StringProperty courseName;
    private final SimpleIntegerProperty duration; // 持续时间，以分钟为单位

    // 问题列表
    private final ObservableList<Question> questions;

    /**
     * 构造函数
     *
     * @param examName    考试名称
     * @param courseID    课程ID
     * @param examTime    考试时间（格式如 2023-11-20）
     * @param publish     是否发布（Yes/No）
     * @param courseName  课程名称
     * @param duration    考试持续时间，以分钟为单位
     * @param questions   考试包含的题目列表
     */
    public Exam(String examName, String courseID, String examTime, String publish, String courseName, int duration, List<Question> questions) {
        this.examName = new SimpleStringProperty(examName);
        this.courseID = new SimpleStringProperty(courseID);
        this.examTime = new SimpleStringProperty(examTime);
        this.publish = new SimpleStringProperty(publish);
        this.courseName = new SimpleStringProperty(courseName);
        this.duration = new SimpleIntegerProperty(duration);
        this.questions = FXCollections.observableArrayList(questions);
    }

    // 原有 Getter 和 Setter 方法

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

    // 新增 Getter 和 Setter 方法

    public String getCourseName() {
        return courseName.get();
    }

    public void setCourseName(String courseName) {
        this.courseName.set(courseName);
    }

    public StringProperty courseNameProperty() {
        return courseName;
    }

    public int getDuration() {
        return duration.get();
    }

    public void setDuration(int duration) {
        this.duration.set(duration);
    }

    public SimpleIntegerProperty durationProperty() {
        return duration;
    }

    // 问题列表的 Getter 和 Setter 方法

    public ObservableList<Question> getQuestions() {
        return questions;
    }

    public void setQuestions(List<Question> questions) {
        this.questions.setAll(questions);
    }

    /**
     * 添加一个问题到考试中
     *
     * @param question 要添加的问题
     */
    public void addQuestion(Question question) {
        this.questions.add(question);
    }

    /**
     * 从考试中移除一个问题
     *
     * @param question 要移除的问题
     */
    public void removeQuestion(Question question) {
        this.questions.remove(question);
    }

    @Override
    public String toString() {
        return "Exam{" +
                "examName='" + examName.get() + '\'' +
                ", courseID='" + courseID.get() + '\'' +
                ", examTime='" + examTime.get() + '\'' +
                ", publish='" + publish.get() + '\'' +
                ", courseName='" + courseName.get() + '\'' +
                ", duration=" + duration.get() +
                ", questions=" + questions +
                '}';
    }
}

