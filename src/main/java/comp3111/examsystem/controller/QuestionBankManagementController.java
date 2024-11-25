package comp3111.examsystem.controller;

import comp3111.examsystem.data.DataManager;
import comp3111.examsystem.entity.Question;
import comp3111.examsystem.entity.Teacher;
import comp3111.examsystem.service.QuestionBankManagementService;
import javafx.collections.FXCollections;
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
    private TableColumn<Question, String> option1Column; // 修改名称
    @FXML
    private TableColumn<Question, String> option2Column;
    @FXML
    private TableColumn<Question, String> option3Column;
    @FXML
    private TableColumn<Question, String> option4Column;
    @FXML
    private TableColumn<Question, String> answerColumn;
    @FXML
    private TableColumn<Question, String> typeColumn;
    @FXML
    private TableColumn<Question, Integer> scoreColumn;

    @FXML
    private TextField questionTxt;
    @FXML
    private TextField option1Txt; // 修改名称
    @FXML
    private TextField option2Txt;
    @FXML
    private TextField option3Txt;
    @FXML
    private TextField option4Txt;
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
        option1Column.setCellValueFactory(cellData -> cellData.getValue().option1Property());
        option2Column.setCellValueFactory(cellData -> cellData.getValue().option2Property());
        option3Column.setCellValueFactory(cellData -> cellData.getValue().option3Property());
        option4Column.setCellValueFactory(cellData -> cellData.getValue().option4Property());
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
            option1Txt.setText(question.getOption1());
            option2Txt.setText(question.getOption2());
            option3Txt.setText(question.getOption3());
            option4Txt.setText(question.getOption4());
            answerTxt.setText(question.getAnswer());
            typeComboBox.setValue(question.getType());
            scoreTxt.setText(String.valueOf(question.getScore()));
        } else {
            clearInputFields();
        }
    }

    @FXML
    private void handleAdd() {
        // 获取输入的值
        String questionText = questionTxt.getText();
        String option1 = option1Txt.getText();
        String option2 = option2Txt.getText();
        String option3 = option3Txt.getText();
        String option4 = option4Txt.getText();
        String answer = answerTxt.getText();
        String type = typeComboBox.getValue();
        int score;

        try {
            score = Integer.parseInt(scoreTxt.getText());
        } catch (NumberFormatException e) {
            showAlert("Invalid Input", "Score must be an integer.", Alert.AlertType.ERROR);
            return;
        }

        // 创建新问题
        Question newQuestion = new Question();
        newQuestion.setQuestion(questionText);
        newQuestion.setOption1(option1);
        newQuestion.setOption2(option2);
        newQuestion.setOption3(option3);
        newQuestion.setOption4(option4);
        newQuestion.setAnswer(answer);
        newQuestion.setType(type);
        newQuestion.setScore(score);

        // 添加到数据管理器
        questionService.addQuestion(newQuestion);
        refreshQuestionTable();
        clearInputFields();
    }

    @FXML
    private void handleUpdate() {
        Question selectedQuestion = questionTable.getSelectionModel().getSelectedItem();
        if (selectedQuestion != null) {
            selectedQuestion.setQuestion(questionTxt.getText());
            selectedQuestion.setOption1(option1Txt.getText());
            selectedQuestion.setOption2(option2Txt.getText());
            selectedQuestion.setOption3(option3Txt.getText());
            selectedQuestion.setOption4(option4Txt.getText());
            selectedQuestion.setAnswer(answerTxt.getText());
            selectedQuestion.setType(typeComboBox.getValue());

            try {
                selectedQuestion.setScore(Integer.parseInt(scoreTxt.getText()));
            } catch (NumberFormatException e) {
                showAlert("Invalid Input", "Score must be an integer.", Alert.AlertType.ERROR);
                return;
            }

            questionService.updateQuestion(selectedQuestion);
            refreshQuestionTable();
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
            refreshQuestionTable();
            clearInputFields();
        } else {
            showAlert("No Selection", "Please select a question to delete.", Alert.AlertType.WARNING);
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

    private void clearInputFields() {
        questionTxt.clear();
        option1Txt.clear();
        option2Txt.clear();
        option3Txt.clear();
        option4Txt.clear();
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
