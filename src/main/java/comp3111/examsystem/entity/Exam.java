package comp3111.examsystem.entity;
import comp3111.examsystem.data.DataManager;

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

    // 默认构造函数
    public Exam(String examName, String courseID, String examTime, String publish, List<String> questionIds, int duration) {
        this.examName = new SimpleStringProperty(examName);
        this.courseID = new SimpleStringProperty(courseID);
        this.examTime = new SimpleStringProperty(examTime);
        this.publish = new SimpleStringProperty(publish);
        this.questionIds = new ArrayList<>(questionIds); // 初始化问题 ID 列表
        this.duration = duration; // 设置考试时长
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

    /**
     * 提供无参 `getQuestions` 方法，通过静态方法从全局 `DataManager` 获取所有问题。
     * 确保 DataManager 已经被初始化，并且包含所有问题的列表。
     *
     * @return 与考试相关的问题对象列表
     */
    public List<Question> getQuestions() {
        return getQuestions(DataManager.getInstance().getQuestions());
    }

    /**
     * 根据问题 ID 获取完整的问题对象列表
     *
     * @param allQuestions 所有可用的问题列表
     * @return 与考试相关的问题对象列表
     */
    public List<Question> getQuestions(List<Question> allQuestions) {
        List<Question> associatedQuestions = new ArrayList<>();
        for (String questionId : questionIds) {
            for (Question question : allQuestions) {
                if (question.getId().equals(questionId)) { // 根据 ID 匹配问题
                    associatedQuestions.add(question);
                }
            }
        }
        return associatedQuestions;
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
