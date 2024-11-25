package comp3111.examsystem.controller;

import comp3111.examsystem.data.DataManager;
import comp3111.examsystem.entity.Question;
import comp3111.examsystem.service.QuestionBankManagementService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.util.List;

public class QuestionBankManagementController {
    @FXML
    private TextField questionFilterTxt;
    @FXML
    private ComboBox<String> typeFilterComboBox;
    @FXML
    private TextField scoreFilterTxt;

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

    public void setDataManager(DataManager dataManager) {
        this.questionService = new QuestionBankManagementService(dataManager);
        initialize();
    }

    @FXML
    public void initialize() {
        // Initialize type ComboBoxes
        typeFilterComboBox.getItems().addAll("All", "Single", "Multiple");
        typeFilterComboBox.setValue("All");

        typeComboBox.getItems().addAll("Single", "Multiple");
        typeComboBox.setValue(null);

        // Set table columns
        questionColumn.setCellValueFactory(cellData -> cellData.getValue().questionProperty());
        optionAColumn.setCellValueFactory(cellData -> cellData.getValue().optionAProperty());
        optionBColumn.setCellValueFactory(cellData -> cellData.getValue().optionBProperty());
        optionCColumn.setCellValueFactory(cellData -> cellData.getValue().optionCProperty());
        optionDColumn.setCellValueFactory(cellData -> cellData.getValue().optionDProperty());
        answerColumn.setCellValueFactory(cellData -> cellData.getValue().answerProperty());
        typeColumn.setCellValueFactory(cellData -> cellData.getValue().typeProperty());
        scoreColumn.setCellValueFactory(cellData -> cellData.getValue().scoreProperty().asObject());

        // Bind question list to table
        if (questionService != null) {
            questionTable.setItems(questionService.getQuestionList());
        }

        // Set table selection listener
        questionTable.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> showQuestionDetails(newValue));
    }

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
            clearInputFields();
        }
    }

    @FXML
    private void handleAdd() {
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
        String id = String.valueOf(System.currentTimeMillis()); // Generate unique ID

        Question newQuestion = new Question(id, questionText, optionA, optionB, optionC, optionD, answer, type, score);

        questionService.addQuestion(newQuestion);
        questionTable.setItems(questionService.getQuestionList());
        clearInputFields();
    }

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
            Question updatedQuestion = new Question(selectedQuestion.getId(), questionText, optionA, optionB, optionC, optionD, answer, type, score);

            questionService.updateQuestion(selectedQuestion.getId(), updatedQuestion);
            questionTable.refresh();
            clearInputFields();
        } else {
            showAlert("No Selection", "Please select a question to update.", Alert.AlertType.WARNING);
        }
    }

    @FXML
    private void handleDelete() {
        Question selectedQuestion = questionTable.getSelectionModel().getSelectedItem();
        if (selectedQuestion != null) {
            questionService.deleteQuestion(selectedQuestion.getId());
            questionTable.setItems(questionService.getQuestionList());
            clearInputFields();
        } else {
            showAlert("No Selection", "Please select a question to delete.", Alert.AlertType.WARNING);
        }
    }

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

    private void showAlert(String title, String message, Alert.AlertType type) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
