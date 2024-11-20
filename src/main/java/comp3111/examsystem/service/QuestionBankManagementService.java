package comp3111.examsystem.service;

import comp3111.examsystem.entity.Question;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.io.*;
import java.util.List;
import java.util.stream.Collectors;

public class QuestionBankManagementService {
    private String questionFilePath;
    private ObservableList<Question> questionList;

    public QuestionBankManagementService(String questionFilePath) {
        this.questionFilePath = questionFilePath;
        this.questionList = FXCollections.observableArrayList();
        loadQuestionsFromFile();
    }

    public ObservableList<Question> getQuestionList() {
        return questionList;
    }

    public void loadQuestionsFromFile() {
        try (BufferedReader br = new BufferedReader(new FileReader(questionFilePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                // questionText,optionA,optionB,optionC,optionD,answer,type,score
                String[] parts = line.split(",");
                if (parts.length >= 8) {
                    String questionText = parts[0];
                    String optionA = parts[1];
                    String optionB = parts[2];
                    String optionC = parts[3];
                    String optionD = parts[4];
                    String answer = parts[5];
                    String type = parts[6];
                    int score = Integer.parseInt(parts[7]);

                    Question question = new Question(
                            questionText,
                            optionA,
                            optionB,
                            optionC,
                            optionD,
                            answer,
                            type,
                            score
                    );
                    questionList.add(question);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            // Handle exception appropriately
        }
    }

    public void saveQuestionsToFile() throws IOException {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(questionFilePath))) {
            for (Question question : questionList) {
                StringBuilder sb = new StringBuilder();
                sb.append(question.getQuestion()).append(",");
                sb.append(question.getOptionA()).append(",");
                sb.append(question.getOptionB()).append(",");
                sb.append(question.getOptionC()).append(",");
                sb.append(question.getOptionD()).append(",");
                sb.append(question.getAnswer()).append(",");
                sb.append(question.getType()).append(",");
                sb.append(question.getScore());
                bw.write(sb.toString());
                bw.newLine();
            }
        }
    }

    public void addQuestion(Question newQuestion) throws IOException {
        questionList.add(newQuestion);
        saveQuestionsToFile();
    }

    public void updateQuestion(Question updatedQuestion, String originalQuestionText) throws IOException {
        boolean questionFound = false;
        for (int i = 0; i < questionList.size(); i++) {
            if (questionList.get(i).getQuestion().equals(originalQuestionText)) {
                questionList.set(i, updatedQuestion);
                questionFound = true;
                break;
            }
        }
        if (!questionFound) {
            throw new IOException("Question not found: " + originalQuestionText);
        }
        saveQuestionsToFile();
    }

    public void deleteQuestion(String questionText) throws IOException {
        boolean questionRemoved = questionList.removeIf(q -> q.getQuestion().equals(questionText));
        if (!questionRemoved) {
            throw new IOException("Question not found: " + questionText);
        }
        saveQuestionsToFile();
    }

    public List<Question> filterQuestions(String questionFilter, String typeFilter, String scoreFilter) {
        return questionList.stream()
                .filter(q -> (questionFilter.isEmpty() || q.getQuestion().toLowerCase().contains(questionFilter.toLowerCase())) &&
                        (typeFilter.equals("All") || q.getType().equals(typeFilter)) &&
                        (scoreFilter.isEmpty() || String.valueOf(q.getScore()).equals(scoreFilter)))
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