package comp3111.examsystem.service;

import comp3111.examsystem.entity.Exam;
import comp3111.examsystem.entity.Question;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class ExamService {
    private Exam currentExam;
    private List<Question> questions;
    private Map<Integer, String> userAnswers;
    private int currentQuestionIndex;
    private int remainingTime;

    private final String examFilePath;
    private final String questionFilePath;

    // Default constructor
    public ExamService() {
        this("data/StudentExam.txt", "data/StudentExamQuestion.txt");
    }

    // Constructor with custom file paths
    public ExamService(String examFilePath, String questionFilePath) {
        this.examFilePath = examFilePath;
        this.questionFilePath = questionFilePath;
        this.userAnswers = new HashMap<>();
        this.currentQuestionIndex = 0;
    }

    /**
     * Load exams from the exam file.
     */
    public ObservableList<Exam> loadExams() {
        ObservableList<Exam> exams = FXCollections.observableArrayList();
        try (BufferedReader br = new BufferedReader(new FileReader(examFilePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] fields = line.split(",");
                if (fields.length >= 7) {
                    exams.add(parseExam(fields));
                }
            }
        } catch (IOException e) {
            System.err.println("Error reading exam file: " + e.getMessage());
        }
        return exams;
    }

    private Exam parseExam(String[] fields) {
        String examName = fields[0].trim();
        String examDate = fields[1].trim();
        String courseID = fields[2].trim();
        String courseName = fields[3].trim();
        String publish = fields[4].trim();
        String[] questionIDs = fields[5].split("\\|");
        int duration = Integer.parseInt(fields[6].trim());

        Exam exam = new Exam(examName, courseID, examDate, publish);
        exam.setCourseName(courseName);
        exam.setDuration(duration);
        exam.setQuestions(loadQuestions(questionIDs));
        return exam;
    }

    private ObservableList<Question> loadQuestions(String[] questionIDs) {
        ObservableList<Question> questionList = FXCollections.observableArrayList();
        Set<String> questionIDSet = new HashSet<>(Arrays.asList(questionIDs));
        try (BufferedReader br = new BufferedReader(new FileReader(questionFilePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] fields = line.split(",");
                if (fields.length >= 9 && questionIDSet.contains(fields[0].trim())) {
                    questionList.add(parseQuestion(fields));
                }
            }
        } catch (IOException e) {
            System.err.println("Error reading question file: " + e.getMessage());
        }
        return questionList;
    }

    private Question parseQuestion(String[] fields) {
        return new Question(
                fields[1].trim(), fields[2].trim(), fields[3].trim(),
                fields[4].trim(), fields[5].trim(), fields[6].trim(),
                fields[7].trim(), Integer.parseInt(fields[8].trim())
        );
    }

    /**
     * Initialize the exam.
     */
    public void initializeExam(Exam exam) {
        this.currentExam = exam;
        this.questions = exam.getQuestions();
        this.userAnswers.clear();
        this.currentQuestionIndex = 0;
        this.remainingTime = exam.getDuration();
    }

    public String getQuizName() {
        return currentExam != null ? currentExam.getCourseID() + " - " + currentExam.getExamName() : "No Exam Selected";
    }

    public int getTotalQuestions() {
        return questions != null ? questions.size() : 0;
    }

    public List<String> getQuestionList() {
        if (questions == null) return List.of();
        return questions.stream()
                .map(q -> "Question: " + q.getQuestion())
                .toList();
    }

    public int getMaxScore() {
        return questions.stream().mapToInt(Question::getScore).sum();
    }

    public double getPrecision() {
        if (questions == null || questions.isEmpty()) return 0.0;
        int correctAnswers = calculateResults()[0];
        return (correctAnswers * 100.0) / questions.size();
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

    public Question getCurrentQuestion() {
        return questions.get(currentQuestionIndex);
    }

    /**
     * Save the user's answer for the current question.
     *
     * @param answer User's answer
     */
    public void saveAnswer(String answer) {
        userAnswers.put(currentQuestionIndex, answer);
    }

    /**
     * Get the user's answer for the current question.
     *
     * @return User's answer or an empty string if no answer is saved
     */
    public String getUserAnswer() {
        return userAnswers.getOrDefault(currentQuestionIndex, "");
    }

    /**
     * Set the current question index.
     *
     * @param index Index of the question to set
     */
    public void setCurrentQuestionIndex(int index) {
        if (index < 0 || index >= questions.size()) {
            throw new IllegalArgumentException("Invalid question index: " + index);
        }
        this.currentQuestionIndex = index;
    }

    /**
     * Calculate the results of the exam.
     *
     * @return An array where [0] = correct answers, [1] = total score
     */
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
