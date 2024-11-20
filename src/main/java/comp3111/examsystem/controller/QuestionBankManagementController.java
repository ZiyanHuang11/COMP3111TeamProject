package comp3111.examsystem.controller;

import comp3111.examsystem.entity.Question;
import comp3111.examsystem.service.QuestionBankManagementService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.io.IOException;
import java.util.List;

public class QuestionBankManagementController {
    // Filter fields
    @FXML
    private TextField questionFilterTxt;
    @FXML
    private ComboBox<String> typeFilterComboBox;
    @FXML
    private TextField scoreFilterTxt;

    // Question table and columns
    @FXML
    private TableView<Question> questionTable;
    @FXML
    private TableColumn<Question, String> questionColumn;
    @FXML
    private TableColumn<Question, String> optionAColumn;
    @FXML
    private TableColumn<Question, String> optionBColumn;
    @FXML
    private TableColumn<Question, String> optionCColumn;
    @FXML
    private TableColumn<Question, String> optionDColumn;
    @FXML
    private TableColumn<Question, String> answerColumn;
    @FXML
    private TableColumn<Question, String> typeColumn;
    @FXML
    private TableColumn<Question, Integer> scoreColumn;

    // Input fields for adding/updating questions
    @FXML
    private TextField questionTxt;
    @FXML
    private TextField optionATxt;
    @FXML
    private TextField optionBTxt;
    @FXML
    private TextField optionCTxt;
    @FXML
    private TextField optionDTxt;
    @FXML
    private TextField answerTxt;
    @FXML
    private ComboBox<String> typeComboBox;
    @FXML
    private TextField scoreTxt;

    private QuestionBankManagementService questionService;

    @FXML
    public void initialize() {
        questionService = new QuestionBankManagementService("data/questions.txt");

        // Initialize type filter ComboBox
        typeFilterComboBox.getItems().clear();
        typeFilterComboBox.getItems().addAll("All", "Single", "Multiple");
        typeFilterComboBox.setValue("All");

        // Initialize type selection ComboBox
        typeComboBox.getItems().clear();
        typeComboBox.getItems().addAll("Single", "Multiple");
        typeComboBox.setValue(null);

        // Initialize table columns
        questionColumn.setCellValueFactory(cellData -> cellData.getValue().questionProperty());
        optionAColumn.setCellValueFactory(cellData -> cellData.getValue().optionAProperty());
        optionBColumn.setCellValueFactory(cellData -> cellData.getValue().optionBProperty());
        optionCColumn.setCellValueFactory(cellData -> cellData.getValue().optionCProperty());
        optionDColumn.setCellValueFactory(cellData -> cellData.getValue().optionDProperty());
        answerColumn.setCellValueFactory(cellData -> cellData.getValue().answerProperty());
        typeColumn.setCellValueFactory(cellData -> cellData.getValue().typeProperty());
        scoreColumn.setCellValueFactory(cellData -> cellData.getValue().scoreProperty().asObject());

        // Bind data to table
        questionTable.setItems(questionService.getQuestionList());

        // Set table click event to show question details
        questionTable.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> showQuestionDetails(newValue));
    }

    // Show question details
    private void showQuestionDetails(Question question) {
        if (question != null) {
            questionTxt.setText(question.getQuestion());
            optionATxt.setText(question.getOptionA());
            optionBTxt.setText(question.getOptionB());
            optionCTxt.setText(question.getOptionC());
            optionDTxt.setText(question.getOptionD());
            answerTxt.setText(question.getAnswer());
            typeComboBox.setValue(question.getType());
            scoreTxt.setText(String.valueOf(question.getScore()));
        } else {
            // Clear fields
            clearInputFields();
        }
    }

    // Handle add button
    @FXML
    private void handleAdd() {
        // Get input data
        String questionText = questionTxt.getText().trim();
        String optionA = optionATxt.getText().trim();
        String optionB = optionBTxt.getText().trim();
        String optionC = optionCTxt.getText().trim();
        String optionD = optionDTxt.getText().trim();
        String answer = answerTxt.getText().trim();
        String type = typeComboBox.getValue();
        String scoreText = scoreTxt.getText().trim();

        String validationMessage = questionService.validateInputs(questionText, optionA, optionB, optionC, optionD, answer, type, scoreText);
        if (validationMessage != null) {
            showAlert("Invalid Input", validationMessage, Alert.AlertType.ERROR);
            return;
        }

        int score = Integer.parseInt(scoreText);

        // Add new question
        Question newQuestion = new Question(questionText, optionA, optionB, optionC, optionD, answer, type, score);
        try {
            questionService.addQuestion(newQuestion);
            // Update table
            questionTable.setItems(questionService.getQuestionList());
            // Clear input fields
            clearInputFields();
        } catch (IOException e) {
            showAlert("Error", "Failed to save question to file.", Alert.AlertType.ERROR);
        }
    }

    // Handle update button
    @FXML
    private void handleUpdate() {
        Question selectedQuestion = questionTable.getSelectionModel().getSelectedItem();
        if (selectedQuestion != null) {
            String questionText = questionTxt.getText().trim();
            String optionA = optionATxt.getText().trim();
            String optionB = optionBTxt.getText().trim();
            String optionC = optionCTxt.getText().trim();
            String optionD = optionDTxt.getText().trim();
            String answer = answerTxt.getText().trim();
            String type = typeComboBox.getValue();
            String scoreText = scoreTxt.getText().trim();

            String validationMessage = questionService.validateInputs(questionText, optionA, optionB, optionC, optionD, answer, type, scoreText);
            if (validationMessage != null) {
                showAlert("Invalid Input", validationMessage, Alert.AlertType.ERROR);
                return;
            }

            int score = Integer.parseInt(scoreText);

            // Update selected question
            Question updatedQuestion = new Question(questionText, optionA, optionB, optionC, optionD, answer, type, score);

            try {
                questionService.updateQuestion(updatedQuestion, selectedQuestion.getQuestion());
                // Update table
                questionTable.refresh();
                // Clear input fields
                clearInputFields();
                // Clear selection
                questionTable.getSelectionModel().clearSelection();
            } catch (IOException e) {
                showAlert("Error", "Failed to update question.", Alert.AlertType.ERROR);
            }
        } else {
            showAlert("No Selection", "Please select a question to update", Alert.AlertType.WARNING);
        }
    }

    // Handle delete button
    @FXML
    private void handleDelete() {
        Question selectedQuestion = questionTable.getSelectionModel().getSelectedItem();
        if (selectedQuestion != null) {
            try {
                questionService.deleteQuestion(selectedQuestion.getQuestion());
                // Update table
                questionTable.setItems(questionService.getQuestionList());
                // Clear input fields
                clearInputFields();
            } catch (IOException e) {
                showAlert("Error", "Failed to delete question.", Alert.AlertType.ERROR);
            }
        } else {
            showAlert("No Selection", "Please select a question to delete", Alert.AlertType.WARNING);
        }
    }

    @FXML
    private void handleRefresh() {
        questionTable.refresh();
    }

    // Handle reset button
    @FXML
    private void handleReset() {
        questionFilterTxt.clear();
        typeFilterComboBox.setValue("All");
        scoreFilterTxt.clear();
        questionTable.setItems(questionService.getQuestionList());
    }

    // Handle filter button
    @FXML
    private void handleFilter() {
        String questionFilter = questionFilterTxt.getText().trim();
        String typeFilter = typeFilterComboBox.getValue();
        String scoreFilter = scoreFilterTxt.getText().trim();

        List<Question> filteredQuestions = questionService.filterQuestions(questionFilter, typeFilter, scoreFilter);
        questionTable.setItems(FXCollections.observableArrayList(filteredQuestions));
    }

    // Clear input fields
    private void clearInputFields() {
        questionTxt.clear();
        optionATxt.clear();
        optionBTxt.clear();
        optionCTxt.clear();
        optionDTxt.clear();
        answerTxt.clear();
        typeComboBox.setValue(null);
        scoreTxt.clear();
    }

    // Show alert
    private void showAlert(String title, String message, Alert.AlertType type) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }
}