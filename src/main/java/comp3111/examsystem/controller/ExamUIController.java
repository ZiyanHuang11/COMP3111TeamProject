package comp3111.examsystem.controller;

import comp3111.examsystem.entity.Exam;
import comp3111.examsystem.entity.Question;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.util.Duration;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ExamUIController {
    @FXML
    private Label quizNameLabel;
    @FXML
    private Label totalQuestionsLabel;
    @FXML
    private Label timerLabel;
    @FXML
    private Label questionLabel;
    @FXML
    private ListView<String> questionListView;
    @FXML
    private RadioButton optionA;
    @FXML
    private RadioButton optionB;
    @FXML
    private RadioButton optionC;
    @FXML
    private RadioButton optionD;

    private Exam selectedExam;
    private List<Question> questions;
    private int currentQuestionIndex = 0;
    private Map<Integer, String> userAnswers = new HashMap<>();
    private int remainingTime;
    private Timeline timer;

    public void setExam(Exam exam) {
        this.selectedExam = exam;
        this.questions = exam.getQuestions();
        this.remainingTime = exam.getDuration(); // 从 Exam 中获取总时间
        initializeExam();
    }

    private void initializeExam() {
        // 设置考试信息
        quizNameLabel.setText(selectedExam.getCourseID() + " - " + selectedExam.getExamName());
        totalQuestionsLabel.setText("Total Questions: " + questions.size());
        startTimer();

        // 加载问题列表
        for (int i = 0; i < questions.size(); i++) {
            questionListView.getItems().add("Question " + (i + 1));
        }

        questionListView.getSelectionModel().selectedIndexProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                currentQuestionIndex = newValue.intValue();
                loadQuestion();
            }
        });

        loadQuestion();
    }

    private void loadQuestion() {
        Question question = questions.get(currentQuestionIndex);
        questionLabel.setText(question.getQuestion());
        optionA.setText("A. " + question.getOptionA());
        optionB.setText("B. " + question.getOptionB());
        optionC.setText("C. " + question.getOptionC());
        optionD.setText("D. " + question.getOptionD());

        // 加载用户选择的答案
        String userAnswer = userAnswers.getOrDefault(currentQuestionIndex, "");
        if (userAnswer.equals("A")) optionA.setSelected(true);
        else if (userAnswer.equals("B")) optionB.setSelected(true);
        else if (userAnswer.equals("C")) optionC.setSelected(true);
        else if (userAnswer.equals("D")) optionD.setSelected(true);
        else {
            optionA.setSelected(false);
            optionB.setSelected(false);
            optionC.setSelected(false);
            optionD.setSelected(false);
        }
    }

    private void saveAnswer() {
        if (optionA.isSelected()) userAnswers.put(currentQuestionIndex, "A");
        else if (optionB.isSelected()) userAnswers.put(currentQuestionIndex, "B");
        else if (optionC.isSelected()) userAnswers.put(currentQuestionIndex, "C");
        else if (optionD.isSelected()) userAnswers.put(currentQuestionIndex, "D");
    }

    @FXML
    private void submitExam() {
        saveAnswer();
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

        // 弹窗显示结果
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Quiz Result");
        alert.setHeaderText(null);
        alert.setContentText(correctAnswers + "/" + questions.size() + " Correct, the precision is "
                + (correctAnswers * 100.0 / questions.size()) + "%, the score is " + totalScore + "/" + getTotalScore());
        alert.showAndWait();

        // 停止计时器
        if (timer != null) timer.stop();
    }

    private int getTotalScore() {
        return questions.stream().mapToInt(Question::getScore).sum();
    }

    private void startTimer() {
        timer = new Timeline(new KeyFrame(Duration.seconds(1), event -> {
            remainingTime--;
            timerLabel.setText("Remaining Time: " + remainingTime + "s");
            if (remainingTime <= 0) {
                timer.stop();
                submitExam();
            }
        }));
        timer.setCycleCount(Timeline.INDEFINITE);
        timer.play();
    }
}
