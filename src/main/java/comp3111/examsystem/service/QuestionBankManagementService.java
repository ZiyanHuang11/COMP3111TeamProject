package comp3111.examsystem.service;

import comp3111.examsystem.entity.Question;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.io.*;
import java.util.List;
import java.util.stream.Collectors;

/**
 * The QuestionBankManagementService class provides functionalities to manage a question bank
 * in the exam system. It allows loading, saving, adding, updating, deleting, and filtering questions.
 */
public class QuestionBankManagementService {
    private String questionFilePath;
    private ObservableList<Question> questionList;

    /**
     * Constructs a QuestionBankManagementService instance with the specified file path
     * for questions and loads questions from that file.
     *
     * @param questionFilePath The path to the file containing questions.
     */
    public QuestionBankManagementService(String questionFilePath) {
        this.questionFilePath = questionFilePath;
        this.questionList = FXCollections.observableArrayList();
        loadQuestionsFromFile();
    }

    /**
     * Returns the list of questions.
     *
     * @return An ObservableList of Question objects.
     */
    public ObservableList<Question> getQuestionList() {
        return questionList;
    }

    /**
     * Loads questions from the specified file into the questionList.
     */
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

    /**
     * Saves all questions in the questionList to the specified file.
     *
     * @throws IOException If an I/O error occurs while writing to the file.
     */
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

    /**
     * Adds a new question to the questionList and saves it to the file.
     *
     * @param newQuestion The Question object to be added.
     * @throws IOException If an I/O error occurs while saving to the file.
     */
    public void addQuestion(Question newQuestion) throws IOException {
        questionList.add(newQuestion);
        saveQuestionsToFile();
    }

    /**
     * Updates an existing question in the questionList.
     *
     * @param updatedQuestion The updated Question object.
     * @param originalQuestionText The original text of the question to be updated.
     * @throws IOException If the question is not found or an I/O error occurs.
     */
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

    /**
     * Deletes a question from the questionList.
     *
     * @param questionText The text of the question to be deleted.
     * @throws IOException If the question is not found or an I/O error occurs.
     */
    public void deleteQuestion(String questionText) throws IOException {
        boolean questionRemoved = questionList.removeIf(q -> q.getQuestion().equals(questionText));
        if (!questionRemoved) {
            throw new IOException("Question not found: " + questionText);
        }
        saveQuestionsToFile();
    }

    /**
     * Filters questions based on the provided criteria.
     *
     * @param questionFilter The question text filter.
     * @param typeFilter The type filter.
     * @param scoreFilter The score filter.
     * @return A list of questions matching the criteria.
     */
    public List<Question> filterQuestions(String questionFilter, String typeFilter, String scoreFilter) {
        return questionList.stream()
                .filter(q -> (questionFilter.isEmpty() || q.getQuestion().toLowerCase().contains(questionFilter.toLowerCase())) &&
                        (typeFilter.equals("All") || q.getType().equals(typeFilter)) &&
                        (scoreFilter.isEmpty() || String.valueOf(q.getScore()).equals(scoreFilter)))
                .collect(Collectors.toList());
    }

    /**
     * Validates the input data for adding or updating a question.
     *
     * @param question The question text to validate.
     * @param optionA The first option to validate.
     * @param optionB The second option to validate.
     * @param optionC The third option to validate.
     * @param optionD The fourth option to validate.
     * @param answer The correct answer to validate.
     * @param type The type of the question to validate.
     * @param scoreText The score to validate.
     * @return An error message if validation fails, otherwise null.
     */

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