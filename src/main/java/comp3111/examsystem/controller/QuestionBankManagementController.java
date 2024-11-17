package comp3111.examsystem.controller;

import comp3111.examsystem.entity.Question;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;

public class QuestionBankManagementController {
    // 过滤器字段
    @FXML
    private TextField questionFilterTxt;
    @FXML
    private ComboBox<String> typeFilterComboBox;
    @FXML
    private TextField scoreFilterTxt;

    // 问题列表和列
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

    // 添加/更新问题的输入字段
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

    // 数据列表
    private ObservableList<Question> questionList = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        // 初始化类型过滤器 ComboBox
        typeFilterComboBox.getItems().clear();
        typeFilterComboBox.getItems().addAll("All", "Single", "Multiple");
        typeFilterComboBox.setValue("Type");

        // 初始化类型选择 ComboBox
        typeComboBox.getItems().clear();
        typeComboBox.getItems().addAll("Single", "Multiple");
        typeComboBox.setValue("Type");

        // 初始化表格列
        questionColumn.setCellValueFactory(cellData -> cellData.getValue().questionProperty());
        optionAColumn.setCellValueFactory(cellData -> cellData.getValue().optionAProperty());
        optionBColumn.setCellValueFactory(cellData -> cellData.getValue().optionBProperty());
        optionCColumn.setCellValueFactory(cellData -> cellData.getValue().optionCProperty());
        optionDColumn.setCellValueFactory(cellData -> cellData.getValue().optionDProperty());
        answerColumn.setCellValueFactory(cellData -> cellData.getValue().answerProperty());
        typeColumn.setCellValueFactory(cellData -> cellData.getValue().typeProperty());
        scoreColumn.setCellValueFactory(cellData -> cellData.getValue().scoreProperty().asObject());

        // 将数据绑定到表格
        questionTable.setItems(questionList);

        // 添加示例数据（可选）
        loadSampleData();

        // 设置表格点击事件，点击行时在右侧显示问题信息
        questionTable.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> showQuestionDetails(newValue));
    }

    // 显示问题详情
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
            // 清空字段
            questionTxt.clear();
            optionATxt.clear();
            optionBTxt.clear();
            optionCTxt.clear();
            optionDTxt.clear();
            answerTxt.clear();
            typeComboBox.setValue(null);
            scoreTxt.clear();
        }
    }

    // 加载示例数据
    private void loadSampleData() {
        questionList.add(new Question("What is Java?", "A", "B", "C", "D", "A", "Single", 5));
        // 可以添加更多示例数据
    }

    // 处理添加按钮
    @FXML
    private void handleAdd() {
        // 获取输入数据
        String question = questionTxt.getText();
        String optionA = optionATxt.getText();
        String optionB = optionBTxt.getText();
        String optionC = optionCTxt.getText();
        String optionD = optionDTxt.getText();
        String answer = answerTxt.getText();
        String type = typeComboBox.getValue();
        int score;

        try {
            score = Integer.parseInt(scoreTxt.getText());
        } catch (NumberFormatException e) {
            showAlert("Invalid Input", "Score must be a number", Alert.AlertType.ERROR);
            return;
        }

        // 数据校验
        if (question.isEmpty() || optionA.isEmpty() || optionB.isEmpty() ||
                optionC.isEmpty() || optionD.isEmpty() || answer.isEmpty() || type == null) {
            showAlert("Incomplete Data", "Please fill in all fields", Alert.AlertType.ERROR);
            return;
        }

        // 添加新问题
        Question newQuestion = new Question(question, optionA, optionB, optionC, optionD, answer, type, score);
        questionList.add(newQuestion);

        // 清空输入字段
        clearInputFields();
    }

    // 处理更新按钮
    @FXML
    private void handleUpdate() {
        Question selectedQuestion = questionTable.getSelectionModel().getSelectedItem();
        if (selectedQuestion != null) {
            // 更新选定的问题
            selectedQuestion.setQuestion(questionTxt.getText());
            selectedQuestion.setOptionA(optionATxt.getText());
            selectedQuestion.setOptionB(optionBTxt.getText());
            selectedQuestion.setOptionC(optionCTxt.getText());
            selectedQuestion.setOptionD(optionDTxt.getText());
            selectedQuestion.setAnswer(answerTxt.getText());
            selectedQuestion.setType(typeComboBox.getValue());
            try {
                selectedQuestion.setScore(Integer.parseInt(scoreTxt.getText()));
            } catch (NumberFormatException e) {
                showAlert("Invalid Input", "Score must be a number", Alert.AlertType.ERROR);
                return;
            }

            questionTable.refresh();
        } else {
            showAlert("No Selection", "Please select a question to update", Alert.AlertType.WARNING);
        }
    }

    // 处理删除按钮
    @FXML
    private void handleDelete() {
        Question selectedQuestion = questionTable.getSelectionModel().getSelectedItem();
        if (selectedQuestion != null) {
            questionList.remove(selectedQuestion);
        } else {
            showAlert("No Selection", "Please select a question to delete", Alert.AlertType.WARNING);
        }
    }

    @FXML
    private void handleRefresh() {
        // 如果数据在内存中，并且已经更新，那么可以直接设置表格的数据源
        questionTable.setItems(questionList);
    }

    // 处理重置按钮
    @FXML
    private void handleReset() {
        questionFilterTxt.clear();
        typeFilterComboBox.setValue("All");
        scoreFilterTxt.clear();
        questionTable.setItems(questionList);
    }

    // 处理过滤按钮
    @FXML
    private void handleFilter() {
        String questionFilter = questionFilterTxt.getText().toLowerCase();
        String typeFilter = typeFilterComboBox.getValue();
        String scoreFilter = scoreFilterTxt.getText();

        ObservableList<Question> filteredList = FXCollections.observableArrayList();

        for (Question q : questionList) {
            boolean matches = true;

            if (!questionFilter.isEmpty() && !q.getQuestion().toLowerCase().contains(questionFilter)) {
                matches = false;
            }

            if (!typeFilter.equals("All") && !q.getType().equals(typeFilter)) {
                matches = false;
            }

            if (!scoreFilter.isEmpty()) {
                try {
                    int score = Integer.parseInt(scoreFilter);
                    if (q.getScore() != score) {
                        matches = false;
                    }
                } catch (NumberFormatException e) {
                    showAlert("Invalid Input", "Score filter must be a number", Alert.AlertType.ERROR);
                    return;
                }
            }

            if (matches) {
                filteredList.add(q);
            }
        }

        questionTable.setItems(filteredList);
    }

    // 清空输入字段
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

    // 显示提示框
    private void showAlert(String title, String message, Alert.AlertType type) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
