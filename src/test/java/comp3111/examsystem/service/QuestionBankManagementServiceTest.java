package comp3111.examsystem.service;

import comp3111.examsystem.entity.Question;
import javafx.collections.ObservableList;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class QuestionBankManagementServiceTest {
    private QuestionBankManagementService questionService;
    private Path testQuestionFilePath = Paths.get("test_data", "test_questions.txt");

    @BeforeEach
    public void setUp() throws IOException {
        // Create test data directory if it doesn't exist
        Files.createDirectories(testQuestionFilePath.getParent());

        // Write test questions to file
        try (BufferedWriter writer = Files.newBufferedWriter(testQuestionFilePath)) {
            writer.write("What is Java?,A,B,C,D,A,Single,5\n");
            writer.write("Explain OOP concepts.,Encapsulation,Inheritance,Polymorphism,Abstraction,A|B|C|D,Multiple,10\n");
        }

        // Initialize the service with test file paths
        questionService = new QuestionBankManagementService(testQuestionFilePath.toString());
    }

    @Test
    public void testLoadQuestions() {
        ObservableList<Question> questions = questionService.getQuestionList();
        assertEquals(2, questions.size(), "The number of loaded questions should be 2.");
        assertEquals("What is Java?", questions.get(0).getQuestion(), "The first question should be 'What is Java?'");
    }

    @Test
    public void testAddQuestion() throws IOException {
        Question newQuestion = new Question("New Question", "A", "B", "C", "D", "A", "Single", 5);
        questionService.addQuestion(newQuestion);

        ObservableList<Question> questions = questionService.getQuestionList();
        assertEquals(3, questions.size(), "The number of questions should increase to 3.");
        assertTrue(questions.contains(newQuestion), "The new question should be in the list.");
    }

    @Test
    public void testUpdateQuestion() throws IOException {
        // 定义一个有效的更新问题对象
        Question updatedQuestion = new Question("What is Java?", "A", "B", "C", "D", "A", "Single", 5);

        // 调用更新问题的服务
        questionService.updateQuestion(updatedQuestion, "What is Java?");

        // 获取更新后的问题列表
        ObservableList<Question> questions = questionService.getQuestionList();

        // 查找更新后的问题
        Question question = questions.stream()
                .filter(q -> q.getQuestion().equals("What is Java?"))
                .findFirst()
                .orElse(null);

        // 确保找到了该问题
        assertNotNull(question, "The question should be updated.");

        // 验证更新后的选项 A 是否正确
        assertEquals("A", question.getOptionA(), "The option A should be updated.");

        // 如果需要，你还可以验证其他选项是否正确
        assertEquals("B", question.getOptionB(), "The option B should be correct.");
        assertEquals("C", question.getOptionC(), "The option C should be correct.");
        assertEquals("D", question.getOptionD(), "The option D should be correct.");

        // 验证答案
        assertEquals("A", question.getAnswer(), "The answer should be updated to A.");
    }

    @Test
    public void testDeleteQuestion() throws IOException {
        questionService.deleteQuestion("What is Java?");
        ObservableList<Question> questions = questionService.getQuestionList();
        assertEquals(1, questions.size(), "The number of questions should be 1 after deletion.");
        assertFalse(questions.stream().anyMatch(q -> q.getQuestion().equals("What is Java?")), "The deleted question should not exist.");
    }

    @Test
    public void testFilterQuestions() {
        // Filter by question text
        List<Question> filteredQuestions = questionService.filterQuestions("Java", "All", "");
        assertEquals(1, filteredQuestions.size(), "Only one question should match the 'Java' filter.");

        // Filter by type
        filteredQuestions = questionService.filterQuestions("", "Single", "");
        assertEquals(1, filteredQuestions.size(), "Only one question should be of type 'Single'.");

        // Filter by score
        filteredQuestions = questionService.filterQuestions("", "All", "10");
        assertEquals(1, filteredQuestions.size(), "Only one question should have score '10'.");

        // No matching filter text
        filteredQuestions = questionService.filterQuestions("Non-existent question", "All", "");
        assertEquals(0, filteredQuestions.size(), "No questions should match the 'Non-existent question' filter.");

        // No matching score filter
        filteredQuestions = questionService.filterQuestions("", "All", "100");
        assertEquals(0, filteredQuestions.size(), "No questions should have score '100'.");
    }

    @Test
    public void testValidateInputs() {
        String validationMessage;

        // Test for missing fields
        validationMessage = questionService.validateInputs("", "A", "B", "C", "D", "A", "Single", "5");
        assertEquals("Please fill in all fields.", validationMessage, "Validation should fail when question text is empty.");

        // Test for non-numeric score
        validationMessage = questionService.validateInputs("Question", "A", "B", "C", "D", "A", "Single", "five");
        assertEquals("Score must be a number.", validationMessage, "Validation should fail when score is not a number.");

        // Test for valid inputs
        validationMessage = questionService.validateInputs("Question", "A", "B", "C", "D", "A", "Single", "5");
        assertNull(validationMessage, "Validation should pass for valid inputs.");

        // Test for invalid answer label
        validationMessage = questionService.validateInputs("Question", "A", "B", "C", "D", "E", "Single", "5");
        assertEquals("Answers must be A, B, C, or D.", validationMessage, "Validation should fail when an invalid answer label is provided.");

        // Test for empty answer
        validationMessage = questionService.validateInputs("Question", "A", "B", "C", "D", "", "Single", "5");
        assertEquals("Please fill in all fields.", validationMessage, "Validation should fail when answer is empty.");
    }

    @Test
    public void testAddQuestionWithInvalidAnswer() {
        Question invalidQuestion = new Question("Invalid Answer", "E", "B", "C", "D", "E", "Single", 5);
        assertThrows(IOException.class, () -> questionService.addQuestion(invalidQuestion), "Adding a question with invalid answer labels should throw an exception.");
    }

    @Test
    public void testUpdateNonExistentQuestion() {
        Question nonExistentQuestion = new Question("Non-existent question", "A", "B", "C", "D", "A", "Single", 5);
        assertThrows(IOException.class, () -> questionService.updateQuestion(nonExistentQuestion, "Non-existent question"),
                "Updating a non-existent question should throw an exception.");
    }

    @Test
    public void testAddDuplicateQuestion() throws IOException {
        Question duplicateQuestion = new Question("What is Java?", "A", "B", "C", "D", "A", "Single", 5);
        assertThrows(IOException.class, () -> questionService.addQuestion(duplicateQuestion), "Adding a duplicate question should throw an exception.");
    }

    @AfterEach
    public void tearDown() throws IOException {
        Files.deleteIfExists(testQuestionFilePath);
    }
}
