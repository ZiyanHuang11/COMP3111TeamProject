package comp3111.examsystem.entity;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 * Represents an exam in the examination system.
 */
public class Exam {
    private final StringProperty examName;
    private final StringProperty courseID;
    private final StringProperty examTime;
    private final StringProperty publish;
    private final ObservableList<Question> questions;

    /**
     * Constructs an Exam object with the specified details.
     *
     * @param examName the name of the exam
     * @param courseID  the unique identifier of the course associated with the exam
     * @param examTime  the time allocated for the exam
     * @param publish    the publication status of the exam
     */
    public Exam(String examName, String courseID, String examTime, String publish) {
        this.examName = new SimpleStringProperty(examName);
        this.courseID = new SimpleStringProperty(courseID);
        this.examTime = new SimpleStringProperty(examTime);
        this.publish = new SimpleStringProperty(publish);
        this.questions = FXCollections.observableArrayList();
    }

    /**
     * Returns the name of the exam.
     *
     * @return the exam name
     */
    public String getExamName() {
        return examName.get();
    }

    /**
     * Sets the name of the exam.
     *
     * @param examName the new exam name
     */
    public void setExamName(String examName) {
        this.examName.set(examName);
    }

    /**
     * Returns the property for the exam name.
     *
     * @return the exam name property
     */
    public StringProperty examNameProperty() {
        return examName;
    }

    /**
     * Returns the unique identifier of the course associated with the exam.
     *
     * @return the course ID
     */
    public String getCourseID() {
        return courseID.get();
    }

    /**
     * Sets the unique identifier of the course associated with the exam.
     *
     * @param courseID the new course ID
     */
    public void setCourseID(String courseID) {
        this.courseID.set(courseID);
    }

    /**
     * Returns the property for the course ID.
     *
     * @return the course ID property
     */
    public StringProperty courseIDProperty() {
        return courseID;
    }

    /**
     * Returns the time allocated for the exam.
     *
     * @return the exam time
     */
    public String getExamTime() {
        return examTime.get();
    }

    /**
     * Sets the time allocated for the exam.
     *
     * @param examTime the new exam time
     */
    public void setExamTime(String examTime) {
        this.examTime.set(examTime);
    }

    /**
     * Returns the property for the exam time.
     *
     * @return the exam time property
     */
    public StringProperty examTimeProperty() {
        return examTime;
    }

    /**
     * Returns the publication status of the exam.
     *
     * @return the publish status
     */
    public String getPublish() {
        return publish.get();
    }

    /**
     * Sets the publication status of the exam.
     *
     * @param publish the new publish status
     */
    public void setPublish(String publish) {
        this.publish.set(publish);
    }

    /**
     * Returns the property for the publish status.
     *
     * @return the publish property
     */
    public StringProperty publishProperty() {
        return publish;
    }

    /**
     * Returns the list of questions associated with the exam.
     *
     * @return the observable list of questions
     */
    public ObservableList<Question> getQuestions() {
        return questions;
    }

    /**
     * Sets the list of questions associated with the exam.
     *
     * @param questions the new list of questions
     */
    public void setQuestions(ObservableList<Question> questions) {
        this.questions.setAll(questions);
    }
}