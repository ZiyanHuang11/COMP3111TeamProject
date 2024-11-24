package comp3111.examsystem.service;

import comp3111.examsystem.data.DataManager;
import comp3111.examsystem.entity.Exam;
import comp3111.examsystem.entity.Question;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.*;

public class ExamService {
    private final DataManager dataManager;
    private Exam currentExam;
    private List<Question> questions;
    private Map<Integer, String> userAnswers;
    private int currentQuestionIndex;
    private int remainingTime;

    public ExamService(DataManager dataManager) {
        this.dataManager = dataManager;
        this.userAnswers = new HashMap<>();
        this.currentQuestionIndex = 0;
    }

    public void initializeExam(Exam exam) {
        this.currentExam = exam;
        this.questions = dataManager.getQuestions().stream()
                .filter(q -> exam.getQuestionIds().contains(q.getId()))
                .toList();
        this.userAnswers.clear();
        this.currentQuestionIndex = 0;
        this.remainingTime = exam.getDuration();
    }

    public String getQuizName() {
        return currentExam != null ? currentExam.getCourseName() + " - " + currentExam.getExamName() : "No Exam Selected";
    }

    public int getTotalQuestions() {
        return questions.size();
    }

    public List<String> getQuestionList() {
        return questions.stream().map(Question::getQuestion).toList();
    }

    public Question getCurrentQuestion() {
        return questions.get(currentQuestionIndex);
    }

    public void saveAnswer(String answer) {
        userAnswers.put(currentQuestionIndex, answer);
    }

    public String getUserAnswer() {
        return userAnswers.getOrDefault(currentQuestionIndex, "");
    }

    public int[] calculateResults() {
        int correctAnswers = 0;
        int totalScore = 0;

        for (int i = 0; i < questions.size(); i++) {
            Question question = questions.get(i);
            String correctAnswer = question.getAnswer();
            String userAnswer = userAnswers.getOrDefault(i, "");
            if (correctAnswer.equals(userAnswer)) {
                correctAnswers++;
                totalScore += question.getScore();
            }
        }

        return new int[]{correctAnswers, totalScore};
    }

    public double getPrecision() {
        return questions.isEmpty() ? 0 : (calculateResults()[0] * 100.0 / questions.size());
    }

    public boolean decrementTime() {
        if (remainingTime > 0) {
            remainingTime--;
            return true;
        }
        return false;
    }

    public int getRemainingTime() {
        return remainingTime;
    }

    public boolean hasNextQuestion() {
        return currentQuestionIndex < questions.size() - 1;
    }

    public boolean hasPreviousQuestion() {
        return currentQuestionIndex > 0;
    }

    public void goToNextQuestion() {
        if (hasNextQuestion()) {
            currentQuestionIndex++;
        }
    }

    public void goToPreviousQuestion() {
        if (hasPreviousQuestion()) {
            currentQuestionIndex--;
        }
    }
}
