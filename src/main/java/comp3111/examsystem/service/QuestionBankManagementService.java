package comp3111.examsystem.service;

import comp3111.examsystem.data.DataManager;
import comp3111.examsystem.entity.Question;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.List;

public class QuestionBankManagementService {
    private final DataManager dataManager;
    private final ObservableList<Question> questionList;

    public QuestionBankManagementService(DataManager dataManager) {
        this.dataManager = dataManager;
        this.questionList = FXCollections.observableArrayList(dataManager.getQuestions());
    }

    public ObservableList<Question> getQuestionList() {
        return questionList;
    }

    public void addQuestion(Question question) {
        dataManager.addQuestion(question);
        questionList.add(question);
    }

    public void updateQuestion(String questionId, Question updatedQuestion) {
        dataManager.updateQuestion(questionId, updatedQuestion);
        refreshQuestionList();
    }

    public void deleteQuestion(String questionId) {
        dataManager.deleteQuestion(questionId);
        refreshQuestionList();
    }

    public List<Question> filterQuestions(String questionFilter, String typeFilter, String scoreFilter) {
        return dataManager.filterQuestions(questionFilter, typeFilter, scoreFilter);
    }

    private void refreshQuestionList() {
        questionList.setAll(dataManager.getQuestions());
    }

    public String validateInputs(String question, String optionA, String optionB, String optionC, String optionD, String answer, String type, String scoreText) {
        if (question.isEmpty() || optionA.isEmpty() || optionB.isEmpty() ||
                optionC.isEmpty() || optionD.isEmpty() || answer.isEmpty() || type == null || scoreText.isEmpty()) {
            return "Please fill in all fields.";
        }
        try {
            Integer.parseInt(scoreText);
        } catch (NumberFormatException e) {
            return "Score must be a number.";
        }
        return null;
    }
}
