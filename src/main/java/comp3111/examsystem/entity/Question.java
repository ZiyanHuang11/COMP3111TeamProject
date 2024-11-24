package comp3111.examsystem.entity;

import javafx.beans.property.*;

public class Question extends Entity {
    private final StringProperty question;
    private final StringProperty optionA;
    private final StringProperty optionB;
    private final StringProperty optionC;
    private final StringProperty optionD;
    private final StringProperty answer;
    private final StringProperty type;
    private final IntegerProperty score;

    // 无参构造函数（便于反射或序列化）
    public Question() {
        this.question = new SimpleStringProperty("");
        this.optionA = new SimpleStringProperty("");
        this.optionB = new SimpleStringProperty("");
        this.optionC = new SimpleStringProperty("");
        this.optionD = new SimpleStringProperty("");
        this.answer = new SimpleStringProperty("");
        this.type = new SimpleStringProperty("");
        this.score = new SimpleIntegerProperty(0);
    }

    // 全参构造函数，包含 id
    public Question(String id, String question, String optionA, String optionB, String optionC, String optionD,
                    String answer, String type, int score) {
        this.setId(id); // 设置 id
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

    @Override
    public String toString() {
        return "Question{" +
                "id=" + getId() +
                ", question=" + getQuestion() +
                ", optionA=" + getOptionA() +
                ", optionB=" + getOptionB() +
                ", optionC=" + getOptionC() +
                ", optionD=" + getOptionD() +
                ", answer=" + getAnswer() +
                ", type=" + getType() +
                ", score=" + getScore() +
                '}';
    }
}
