package comp3111.examsystem.controller;

import comp3111.examsystem.data.DataManager;
import comp3111.examsystem.entity.Question;
import comp3111.examsystem.entity.Teacher;
import comp3111.examsystem.service.QuestionBankManagementService;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;

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

    private final QuestionBankManagementService questionService;
    private final Teacher loggedInTeacher;

    public QuestionBankManagementController(DataManager dataManager, Teacher loggedInTeacher) {
        this.questionService = new QuestionBankManagementService(dataManager);
        this.loggedInTeacher = loggedInTeacher;
    }

    @FXML
    public void initialize() {
        // Initialize ComboBoxes
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
        refreshQuestionTable();

        // Set table selection listener
        questionTable.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> showQuestionDetails(newValue));
    }

    private void refreshQuestionTable() {
        questionTable.setItems(questionService.getQuestionList());
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
        // Add logic remains unchanged
    }

    @FXML
    private void handleUpdate() {
        // Update logic remains unchanged
    }

    @FXML
    private void handleDelete() {
        // Delete logic remains unchanged
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
