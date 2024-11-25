package comp3111.examsystem.entity;

import javafx.beans.property.*;

public class Question {
    private final StringProperty question;
    private final StringProperty optionA;
    private final StringProperty optionB;
    private final StringProperty optionC;
    private final StringProperty optionD;
    private final StringProperty answer;
    private final StringProperty type;
    private final IntegerProperty score;

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

    // Getter 和 Setter 方法

    public String getQuestion() {
        return question.get();
    }

    public void setQuestion(String question) {
        this.question.set(question);
    }

    public StringProperty questionProperty() {
        return question;
    }

    public String getOptionA() {
        return optionA.get();
    }

    public void setOptionA(String optionA) {
        this.optionA.set(optionA);
    }

    public StringProperty optionAProperty() {
        return optionA;
    }

    public String getOptionB() {
        return optionB.get();
    }

    public void setOptionB(String optionB) {
        this.optionB.set(optionB);
    }

    public StringProperty optionBProperty() {
        return optionB;
    }

    public String getOptionC() {
        return optionC.get();
    }

    public void setOptionC(String optionC) {
        this.optionC.set(optionC);
    }

    public StringProperty optionCProperty() {
        return optionC;
    }

    public String getOptionD() {
        return optionD.get();
    }

    public void setOptionD(String optionD) {
        this.optionD.set(optionD);
    }

    public StringProperty optionDProperty() {
        return optionD;
    }

    public String getAnswer() {
        return answer.get();
    }

    public void setAnswer(String answer) {
        this.answer.set(answer);
    }

    public StringProperty answerProperty() {
        return answer;
    }

    public String getType() {
        return type.get();
    }

    public void setType(String type) {
        this.type.set(type);
    }

    public StringProperty typeProperty() {
        return type;
    }

    public int getScore() {
        return score.get();
    }

    public void setScore(int score) {
        this.score.set(score);
    }

    public IntegerProperty scoreProperty() {
        return score;
    }
}