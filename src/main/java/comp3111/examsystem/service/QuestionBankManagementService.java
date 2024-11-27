package comp3111.examsystem.service;

import comp3111.examsystem.entity.Question;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.io.*;
import java.nio.file.*;
import java.util.ArrayList;
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
                // questionText,optionA,optionB,optionC,optionD,answers,type,score
                String[] parts = line.split(",");
                if (parts.length >= 8) {
                    String questionText = parts[0].trim();
                    String optionA = parts[1].trim();
                    String optionB = parts[2].trim();
                    String optionC = parts[3].trim();
                    String optionD = parts[4].trim();
                    String answersRaw = parts[5].trim(); // Now using labels like A, B, etc.
                    String type = parts[6].trim();
                    int score = Integer.parseInt(parts[7].trim());

                    List<String> answers = new ArrayList<>();
                    if (type.equalsIgnoreCase("Multiple")) {
                        // Multiple answers separated by '|'
                        String[] answerParts = answersRaw.split("\\|");
                        for (String ans : answerParts) {
                            ans = ans.trim().toUpperCase();
                            if (ans.matches("[A-D]")) { // Validate answer label
                                answers.add(ans);
                            } else {
                                System.err.println("Invalid answer label in questions.txt: " + ans);
                            }
                        }
                    } else { // Single answer
                        String ans = answersRaw.trim().toUpperCase();
                        if (ans.matches("[A-D]")) { // Validate answer label
                            answers.add(ans);
                        } else {
                            System.err.println("Invalid answer label in questions.txt: " + ans);
                        }
                    }

                    Question question = new Question(
                            questionText,
                            optionA,
                            optionB,
                            optionC,
                            optionD,
                            String.join("|", answers), // Store as labels separated by '|'
                            type,
                            score
                    );
                    questionList.add(question);
                } else {
                    System.err.println("questions.txt line does not have enough fields: " + line);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            // Handle exception appropriately
        } catch (NumberFormatException e) {
            e.printStackTrace();
            // Handle invalid number format
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
        // Check if question already exists
        for (Question q : questionList) {
            if (q.getQuestion().equalsIgnoreCase(newQuestion.getQuestion())) {
                throw new IOException("Question already exists.");
            }
        }

        // Validate answer labels
        String[] answerParts = newQuestion.getAnswer().split("\\|");
        for (String ans : answerParts) {
            ans = ans.trim().toUpperCase();
            if (!ans.matches("[A-D]")) {
                throw new IOException("Invalid answer label: " + ans);
            }
        }

        // Add to list
        questionList.add(newQuestion);
        // Save to file
        saveQuestionsToFile();
    }

    public void updateQuestion(Question updatedQuestion, String originalQuestionText) throws IOException {
        boolean questionFound = false;
        for (int i = 0; i < questionList.size(); i++) {
            Question q = questionList.get(i);
            if (q.getQuestion().equalsIgnoreCase(originalQuestionText)) {
                // Check for duplicate question text
                for (Question otherQ : questionList) {
                    if (!otherQ.getQuestion().equalsIgnoreCase(originalQuestionText)
                            && otherQ.getQuestion().equalsIgnoreCase(updatedQuestion.getQuestion())) {
                        throw new IOException("Another question with the same text already exists.");
                    }
                }

                // Validate answer labels
                String[] answerParts = updatedQuestion.getAnswer().split("\\|");
                for (String ans : answerParts) {
                    ans = ans.trim().toUpperCase();
                    if (!ans.matches("[A-D]")) {
                        throw new IOException("Invalid answer label: " + ans);
                    }
                }

                // Update question
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
        boolean questionRemoved = questionList.removeIf(q -> q.getQuestion().equalsIgnoreCase(questionText));
        if (!questionRemoved) {
            throw new IOException("Question not found: " + questionText);
        }
        saveQuestionsToFile();
    }

    public List<Question> filterQuestions(String questionFilter, String typeFilter, String scoreFilter) {
        return questionList.stream()
                .filter(q -> (questionFilter.isEmpty() || q.getQuestion().toLowerCase().contains(questionFilter.toLowerCase()))
                        && (typeFilter.equalsIgnoreCase("All") || q.getType().equalsIgnoreCase(typeFilter))
                        && (scoreFilter.isEmpty() || String.valueOf(q.getScore()).equals(scoreFilter)))
                .collect(Collectors.toList());
    }

    public String validateInputs(String question, String optionA, String optionB, String optionC, String optionD, String answer, String type, String scoreText) {
        if (question.isEmpty() || optionA.isEmpty() || optionB.isEmpty() ||
                optionC.isEmpty() || optionD.isEmpty() || answer.isEmpty() || type == null || scoreText.isEmpty()) {
            return "Please fill in all fields.";
        }
        if (!type.equalsIgnoreCase("Single") && !type.equalsIgnoreCase("Multiple")) {
            return "Question type must be 'Single' or 'Multiple'.";
        }
        try {
            int score = Integer.parseInt(scoreText);
            if (score <= 0) {
                return "Score must be a positive integer.";
            }
        } catch (NumberFormatException e) {
            return "Score must be a number.";
        }
        // Validate answer labels
        String[] answerParts = answer.split("\\|");
        for (String ans : answerParts) {
            ans = ans.trim().toUpperCase();
            if (!ans.matches("[A-D]")) {
                return "Answers must be A, B, C, or D.";
            }
        }
        return null; // All inputs are valid
    }
}
