package comp3111.examsystem.service;

import comp3111.examsystem.entity.Exam;
import comp3111.examsystem.entity.Question;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;

public class ExamService {
    private String examFilePath;
    private String questionFilePath;

    private Exam currentExam;
    private List<Question> questions;
    private Map<Integer, String> userAnswers;
    private int currentQuestionIndex;
    private int remainingTime;

    public ExamService() {
        this.examFilePath = "data/exam.txt";
        this.questionFilePath = "data/question.txt";
        this.userAnswers = new HashMap<>();
    }

    /**
     * 加载所有考试
     */
    public ObservableList<Exam> loadExams() {
        ObservableList<Exam> exams = FXCollections.observableArrayList();
        try (BufferedReader br = new BufferedReader(new FileReader(examFilePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] fields = line.split(",");
                if (fields.length >= 7) {
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

                    ObservableList<Question> questions = loadQuestions(questionIDs);
                    exam.setQuestions(questions);

                    exams.add(exam);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return exams;
    }

    /**
     * 根据问题 ID 加载问题
     */
    private ObservableList<Question> loadQuestions(String[] questionIDs) {
        List<Question> questionList = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(questionFilePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] fields = line.split(",");
                if (fields.length >= 9) {
                    String questionID = fields[0].trim();
                    for (String id : questionIDs) {
                        if (questionID.equals(id.trim())) {
                            Question question = new Question(
                                    fields[1], fields[2], fields[3], fields[4], fields[5],
                                    fields[6], fields[7], Integer.parseInt(fields[8])
                            );
                            questionList.add(question);
                            break;
                        }
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return FXCollections.observableArrayList(questionList);
    }

    /**
     * 初始化考试
     */
    public void initializeExam(Exam exam) {
        this.currentExam = exam;
        this.questions = exam.getQuestions();
        this.userAnswers.clear();
        this.currentQuestionIndex = 0;
        this.remainingTime = exam.getDuration();
    }

    public String getQuizName() {
        return currentExam.getCourseID() + " - " + currentExam.getExamName();
    }

    public int getTotalQuestions() {
        return questions.size();
    }

    public List<String> getQuestionList() {
        List<String> questionTitles = new ArrayList<>();
        for (int i = 0; i < questions.size(); i++) {
            questionTitles.add("Question " + (i + 1));
        }
        return questionTitles;
    }

    public Question getCurrentQuestion() {
        return questions.get(currentQuestionIndex);
    }

    public boolean hasPreviousQuestion() {
        return currentQuestionIndex > 0;
    }

    public boolean hasNextQuestion() {
        return currentQuestionIndex < questions.size() - 1;
    }

    public void goToPreviousQuestion() {
        if (hasPreviousQuestion()) {
            currentQuestionIndex--;
        }
    }

    public void setCurrentQuestionIndex(int index) {
        if (index >= 0 && index < questions.size()) {
            this.currentQuestionIndex = index;
        } else {
            throw new IllegalArgumentException("Invalid question index: " + index);
        }
    }

    public void goToNextQuestion() {
        if (hasNextQuestion()) {
            currentQuestionIndex++;
        }
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

    public int getMaxScore() {
        return questions.stream().mapToInt(Question::getScore).sum();
    }

    public double getPrecision() {
        int correctAnswers = calculateResults()[0];
        return correctAnswers * 100.0 / getTotalQuestions();
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
}




