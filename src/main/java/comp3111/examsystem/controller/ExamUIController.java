package comp3111.examsystem.controller;

import comp3111.examsystem.data.DataManager;
import comp3111.examsystem.entity.Exam;
import comp3111.examsystem.entity.Question;
import comp3111.examsystem.service.ExamService;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.util.Duration;

import java.util.List;

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

    private ToggleGroup optionsGroup;
    private ExamService examService;
    private Timeline timer;

    public ExamUIController() {
        this.examService = new ExamService(new DataManager()); // 使用 DataManager 初始化
    }

    @FXML
    public void initialize() {
        optionsGroup = new ToggleGroup();
        optionA.setToggleGroup(optionsGroup);
        optionB.setToggleGroup(optionsGroup);
        optionC.setToggleGroup(optionsGroup);
        optionD.setToggleGroup(optionsGroup);
    }

    public void setExam(Exam exam) {
        examService.initializeExam(exam);
        updateUI();
        startTimer();
    }

    private void updateUI() {
        quizNameLabel.setText(examService.getQuizName());
        totalQuestionsLabel.setText("Total Questions: " + examService.getTotalQuestions());

        questionListView.getItems().setAll(examService.getQuestionList());
        questionListView.getSelectionModel().selectedIndexProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                examService.setCurrentQuestionIndex(newValue.intValue());
                loadQuestion();
            }
        });

        loadQuestion();
    }

    private void loadQuestion() {
        Question question = examService.getCurrentQuestion();
        questionLabel.setText(question.getQuestion());
        optionA.setText("A. " + question.getOptionA());
        optionB.setText("B. " + question.getOptionB());
        optionC.setText("C. " + question.getOptionC());
        optionD.setText("D. " + question.getOptionD());

        String userAnswer = examService.getUserAnswer();
        optionA.setSelected("A".equals(userAnswer));
        optionB.setSelected("B".equals(userAnswer));
        optionC.setSelected("C".equals(userAnswer));
        optionD.setSelected("D".equals(userAnswer));
    }

    @FXML
    private void goToPreviousQuestion() {
        if (examService.hasPreviousQuestion()) {
            examService.saveAnswer(getSelectedOption());
            examService.goToPreviousQuestion();
            loadQuestion();
        } else {
            showAlert(Alert.AlertType.INFORMATION, "Info", "This is the first question.");
        }
    }

    @FXML
    private void goToNextQuestion() {
        if (examService.hasNextQuestion()) {
            examService.saveAnswer(getSelectedOption());
            examService.goToNextQuestion();
            loadQuestion();
        } else {
            showAlert(Alert.AlertType.INFORMATION, "Info", "This is the last question.");
        }
    }

    @FXML
    private void submitExam() {
        examService.saveAnswer(getSelectedOption());
        int[] results = examService.calculateResults();
        int correctAnswers = results[0];
        int totalScore = results[1];
        showAlert(Alert.AlertType.INFORMATION, "Quiz Result",
                correctAnswers + "/" + examService.getTotalQuestions() +
                        " Correct, the precision is " + examService.getPrecision() +
                        "%, the score is " + totalScore + "/" + examService.getMaxScore());
        if (timer != null) timer.stop();
    }

    private void startTimer() {
        timer = new Timeline(new KeyFrame(Duration.seconds(1), event -> {
            if (examService.decrementTime()) {
                timerLabel.setText("Remaining Time: " + examService.getRemainingTime() + "s");
            } else {
                timer.stop();
                submitExam();
            }
        }));
        timer.setCycleCount(Timeline.INDEFINITE);
        timer.play();
    }

    private String getSelectedOption() {
        if (optionA.isSelected()) return "A";
        if (optionB.isSelected()) return "B";
        if (optionC.isSelected()) return "C";
        if (optionD.isSelected()) return "D";
        return "";
    }

    private void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
