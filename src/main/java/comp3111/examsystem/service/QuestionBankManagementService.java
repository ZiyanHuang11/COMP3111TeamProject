package comp3111.examsystem.service;

import comp3111.examsystem.data.DataManager;
import comp3111.examsystem.entity.Question;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.List;
import java.util.stream.Collectors;

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
        dataManager.getQuestions().add(question);
        questionList.add(question);
    }

    public void updateQuestion(Question updatedQuestion) {
        Question existingQuestion = questionList.stream()
                .filter(q -> q.getId().equals(updatedQuestion.getId()))
                .findFirst()
                .orElse(null);

        if (existingQuestion != null) {
            questionList.remove(existingQuestion);
            questionList.add(updatedQuestion);
        }
    }

    public void deleteQuestion(String questionId) {
        Question questionToDelete = questionList.stream()
                .filter(q -> q.getId().equals(questionId))
                .findFirst()
                .orElse(null);

        if (questionToDelete != null) {
            questionList.remove(questionToDelete);
            dataManager.getQuestions().remove(questionToDelete);
        }
    }

    public List<Question> filterQuestions(String questionFilter, String typeFilter, String scoreFilter) {
        return questionList.stream()
                .filter(q -> (questionFilter.isEmpty() || q.getQuestion().toLowerCase().contains(questionFilter.toLowerCase()))
                        && (typeFilter.equals("All") || q.getType().equals(typeFilter))
                        && (scoreFilter.isEmpty() || Integer.toString(q.getScore()).equals(scoreFilter)))
                .collect(Collectors.toList());
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
