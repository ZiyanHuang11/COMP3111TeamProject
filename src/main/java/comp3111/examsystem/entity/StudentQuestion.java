package comp3111.examsystem.entity;

import java.util.List;

public class StudentQuestion {
    private final String question;
    private final String optionA;
    private final String optionB;
    private final String optionC;
    private final String optionD;
    private final List<String> answers; // 支持多个答案
    private final String type;
    private final int score;

    public StudentQuestion(String question, String optionA, String optionB, String optionC, String optionD,
                           List<String> answers, String type, int score) {
        this.question = question;
        this.optionA = optionA;
        this.optionB = optionB;
        this.optionC = optionC;
        this.optionD = optionD;
        this.answers = answers;
        this.type = type;
        this.score = score;
    }

    public String getQuestion() { return question; }
    public String getOptionA() { return optionA; }
    public String getOptionB() { return optionB; }
    public String getOptionC() { return optionC; }
    public String getOptionD() { return optionD; }
    public List<String> getAnswers() { return answers; }
    public String getType() { return type; }
    public int getScore() { return score; }
}
