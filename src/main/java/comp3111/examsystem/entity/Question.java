package comp3111.examsystem.entity;

import javafx.beans.property.*;

public class Question extends Entity {
    private final StringProperty question;
    private final StringProperty option1;
    private final StringProperty option2;
    private final StringProperty option3;
    private final StringProperty option4;
    private final StringProperty answer;
    private final StringProperty type;
    private final IntegerProperty score;

    // 无参构造函数（便于反射或序列化）
    public Question() {
        this.question = new SimpleStringProperty("");
        this.option1 = new SimpleStringProperty("");
        this.option2 = new SimpleStringProperty("");
        this.option3 = new SimpleStringProperty("");
        this.option4 = new SimpleStringProperty("");
        this.answer = new SimpleStringProperty("");
        this.type = new SimpleStringProperty("");
        this.score = new SimpleIntegerProperty(0);
    }

    // 全参构造函数，包含 id
    public Question(String id, String question, String option1, String option2, String option3, String option4,
                    String answer, String type, int score) {
        this.setId(id); // 设置 id
        this.question = new SimpleStringProperty(question);
        this.option1 = new SimpleStringProperty(option1);
        this.option2 = new SimpleStringProperty(option2);
        this.option3 = new SimpleStringProperty(option3);
        this.option4 = new SimpleStringProperty(option4);
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

    public String getOption1() {
        return option1.get();
    }

    public void setOption1(String option1) {
        this.option1.set(option1);
    }

    public StringProperty option1Property() {
        return option1;
    }

    public String getOption2() {
        return option2.get();
    }

    public void setOption2(String option2) {
        this.option2.set(option2);
    }

    public StringProperty option2Property() {
        return option2;
    }

    public String getOption3() {
        return option3.get();
    }

    public void setOption3(String option3) {
        this.option3.set(option3);
    }

    public StringProperty option3Property() {
        return option3;
    }

    public String getOption4() {
        return option4.get();
    }

    public void setOption4(String option4) {
        this.option4.set(option4);
    }

    public StringProperty option4Property() {
        return option4;
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
                ", option1=" + getOption1() +
                ", option2=" + getOption2() +
                ", option3=" + getOption3() +
                ", option4=" + getOption4() +
                ", answer=" + getAnswer() +
                ", type=" + getType() +
                ", score=" + getScore() +
                '}';
    }
}
