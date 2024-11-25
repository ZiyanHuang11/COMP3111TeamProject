package comp3111.examsystem.service;

import comp3111.examsystem.data.DataManager;
import comp3111.examsystem.entity.Exam;
import comp3111.examsystem.entity.Question;

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
        Question currentQuestion = getCurrentQuestion();
        String mappedAnswer = mapOptionLabelToText(currentQuestion, answer);
        userAnswers.put(currentQuestionIndex, mappedAnswer);
    }

    private String mapOptionLabelToText(Question question, String labels) {
        String[] labelArray = labels.split(",");
        List<String> mappedOptions = new ArrayList<>();
        for (String label : labelArray) {
            switch (label.trim().toUpperCase()) {
                case "A":
                    mappedOptions.add(question.getOption1());
                    break;
                case "B":
                    mappedOptions.add(question.getOption2());
                    break;
                case "C":
                    mappedOptions.add(question.getOption3());
                    break;
                case "D":
                    mappedOptions.add(question.getOption4());
                    break;
                default:
                    mappedOptions.add(label.trim()); // If not a label, use as is
                    break;
            }
        }
        return String.join(",", mappedOptions);
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

            if ("Multiple".equalsIgnoreCase(question.getType())) {
                // Split answers and compare as sets
                Set<String> correctAnswerSet = new HashSet<>(Arrays.asList(correctAnswer.split(",")));
                Set<String> userAnswerSet = new HashSet<>(Arrays.asList(userAnswer.split(",")));
                if (correctAnswerSet.equals(userAnswerSet)) {
                    correctAnswers++;
                    totalScore += question.getScore();
                }
            } else {
                if (correctAnswer.equals(userAnswer)) {
                    correctAnswers++;
                    totalScore += question.getScore();
                }
            }
        }

        return new int[]{correctAnswers, totalScore};
    }

    public double getPrecision() {
        int[] results = calculateResults();
        int correctAnswers = results[0];
        int totalQuestions = questions.size();
        return totalQuestions == 0 ? 0.0 : (double) correctAnswers * 100 / totalQuestions;
    }

    public int getMaxScore() {
        return questions.stream().mapToInt(Question::getScore).sum();
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

    public void setCurrentQuestionIndex(int index) {
        if (index >= 0 && index < questions.size()) {
            currentQuestionIndex = index;
        }
    }
}
