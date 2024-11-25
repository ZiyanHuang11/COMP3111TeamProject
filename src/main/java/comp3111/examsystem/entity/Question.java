package comp3111.examsystem.entity;

import javafx.beans.property.*;

/**
 * Represents a question in the examination system.
 */
public class Question {
    private final StringProperty question;
    private final StringProperty optionA;
    private final StringProperty optionB;
    private final StringProperty optionC;
    private final StringProperty optionD;
    private final StringProperty answer;
    private final StringProperty type;
    private final IntegerProperty score;

    /**
     * Constructs a Question object with the specified details.
     *
     * @param question the text of the question
     * @param optionA  the text of option A
     * @param optionB  the text of option B
     * @param optionC  the text of option C
     * @param optionD  the text of option D
     * @param answer   the correct answer
     * @param type     the type of question (e.g., single choice, multiple choice)
     * @param score    the score assigned to the question
     */
    public Question(String question, String optionA, String optionB, String optionC, String optionD,
                    String answer, String type, int score) {
        this.question = new SimpleStringProperty(question);
        this.optionA = new SimpleStringProperty(optionA);
        this.optionB = new SimpleStringProperty(optionB);
        this.optionC = new SimpleStringProperty(optionC);
        this.optionD = new SimpleStringProperty(optionD);
        this.answer = new SimpleStringProperty(answer);
        this.type = new SimpleStringProperty(type);
        this.score = new SimpleIntegerProperty(score);
    }

    // Getter and Setter methods

    /**
     * Returns the text of the question.
     *
     * @return the question text
     */
    public String getQuestion() {
        return question.get();
    }

    /**
     * Sets the text of the question.
     *
     * @param question the new question text
     */
    public void setQuestion(String question) {
        this.question.set(question);
    }

    /**
     * Returns the property for the question text.
     *
     * @return the question property
     */
    public StringProperty questionProperty() {
        return question;
    }

    /**
     * Returns the text of option A.
     *
     * @return the option A text
     */
    public String getOptionA() {
        return optionA.get();
    }

    /**
     * Sets the text of option A.
     *
     * @param optionA the new option A text
     */
    public void setOptionA(String optionA) {
        this.optionA.set(optionA);
    }

    /**
     * Returns the property for option A text.
     *
     * @return the option A property
     */
    public StringProperty optionAProperty() {
        return optionA;
    }

    /**
     * Returns the text of option B.
     *
     * @return the option B text
     */
    public String getOptionB() {
        return optionB.get();
    }

    /**
     * Sets the text of option B.
     *
     * @param optionB the new option B text
     */
    public void setOptionB(String optionB) {
        this.optionB.set(optionB);
    }

    /**
     * Returns the property for option B text.
     *
     * @return the option B property
     */
    public StringProperty optionBProperty() {
        return optionB;
    }

    /**
     * Returns the text of option C.
     *
     * @return the option C text
     */
    public String getOptionC() {
        return optionC.get();
    }

    /**
     * Sets the text of option C.
     *
     * @param optionC the new option C text
     */
    public void setOptionC(String optionC) {
        this.optionC.set(optionC);
    }

    /**
     * Returns the property for option C text.
     *
     * @return the option C property
     */
    public StringProperty optionCProperty() {
        return optionC;
    }

    /**
     * Returns the text of option D.
     *
     * @return the option D text
     */
    public String getOptionD() {
        return optionD.get();
    }

    /**
     * Sets the text of option D.
     *
     * @param optionD the new option D text
     */
    public void setOptionD(String optionD) {
        this.optionD.set(optionD);
    }

    /**
     * Returns the property for option D text.
     *
     * @return the option D property
     */
    public StringProperty optionDProperty() {
        return optionD;
    }

    /**
     * Returns the correct answer for the question.
     *
     * @return the answer text
     */
    public String getAnswer() {
        return answer.get();
    }

    /**
     * Sets the correct answer for the question.
     *
     * @param answer the new answer text
     */
    public void setAnswer(String answer) {
        this.answer.set(answer);
    }

    /**
     * Returns the property for the answer text.
     *
     * @return the answer property
     */
    public StringProperty answerProperty() {
        return answer;
    }

    /**
     * Returns the type of the question.
     *
     * @return the question type
     */
    public String getType() {
        return type.get();
    }

    /**
     * Sets the type of the question.
     *
     * @param type the new question type
     */
    public void setType(String type) {
        this.type.set(type);
    }

    /**
     * Returns the property for the question type.
     *
     * @return the question type property
     */
    public StringProperty typeProperty() {
        return type;
    }

    /**
     * Returns the score assigned to the question.
     *
     * @return the score
     */
    public int getScore() {
        return score.get();
    }

    /**
     * Sets the score assigned to the question.
     *
     * @param score the new score
     */
    public void setScore(int score) {
        this.score.set(score);
    }

    /**
     * Returns the property for the score.
     *
     * @return the score property
     */
    public IntegerProperty scoreProperty() {
        return score;
    }
}