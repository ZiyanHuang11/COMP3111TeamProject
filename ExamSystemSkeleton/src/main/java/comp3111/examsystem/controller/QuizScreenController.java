package comp3111.examsystem.controller;

import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.util.ArrayList;
import java.util.List;

public class QuizScreenController {
    @FXML
    private Label quizTitleLbl;
    @FXML
    private Label totalQuestionsLbl;
    @FXML
    private Label timerLbl;
    @FXML
    private ListView<String> questionListView;
    @FXML
    private Label questionLbl;
    @FXML
    private RadioButton optionARadio;
    @FXML
    private RadioButton optionBRadio;
    @FXML
    private RadioButton optionCRadio;
    @FXML
    private RadioButton optionDRadio;

    private ToggleGroup answerGroup; // Define ToggleGroup here

    private List<String> questions = new ArrayList<>();
    private List<String> correctAnswers = new ArrayList<>();
    private List<String> userAnswers = new ArrayList<>();
    private int currentQuestionIndex = 0;

    public void initializeQuiz(String quizTitle) {
        quizTitleLbl.setText(quizTitle);

        answerGroup = new ToggleGroup();
        optionARadio.setToggleGroup(answerGroup);
        optionBRadio.setToggleGroup(answerGroup);
        optionCRadio.setToggleGroup(answerGroup);
        optionDRadio.setToggleGroup(answerGroup);

        questions.add("What is Java?");
        correctAnswers.add("A");
        userAnswers.add("");

        questions.add("What is Python?");
        correctAnswers.add("B");
        userAnswers.add("");

        totalQuestionsLbl.setText("Total Questions: " + questions.size());
        questionListView.getItems().addAll("Question 1", "Question 2");
        displayQuestion(0);
    }

    private void displayQuestion(int index) {
        questionLbl.setText(questions.get(index));
        resetOptions();
    }

    private void resetOptions() {
        answerGroup.getToggles().forEach(toggle -> ((RadioButton) toggle).setSelected(false));
    }

    @FXML
    private void goToNextQuestion() {
        if (currentQuestionIndex < questions.size() - 1) {
            currentQuestionIndex++;
            displayQuestion(currentQuestionIndex);
        }
    }

    @FXML
    private void goToPreviousQuestion() {
        if (currentQuestionIndex > 0) {
            currentQuestionIndex--;
            displayQuestion(currentQuestionIndex);
        }
    }

    @FXML
    private void submitQuiz() {
        RadioButton selectedRadioButton = (RadioButton) answerGroup.getSelectedToggle();
        String selectedAnswer = selectedRadioButton == null ? "" : selectedRadioButton.getText();
        userAnswers.set(currentQuestionIndex, selectedAnswer);

        int correctCount = 0;
        for (int i = 0; i < questions.size(); i++) {
            if (userAnswers.get(i).equals(correctAnswers.get(i))) {
                correctCount++;
            }
        }
        double precision = (double) correctCount / questions.size() * 100;
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Result");
        alert.setHeaderText(null);
        alert.setContentText(correctCount + "/" + questions.size() + " Correct, precision: " + precision + "%");
        alert.showAndWait();
    }
}