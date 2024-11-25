package comp3111.examsystem.service;

import comp3111.examsystem.entity.Question;
import javafx.collections.ObservableList;
import org.junit.jupiter.api.*;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class QuestionBankManagementServiceTest {
    private QuestionBankManagementService questionService;
    private String testQuestionFilePath = "test_data/test_questions.txt";

    @BeforeEach
    public void setUp() throws IOException {
        // Create test data directory if it doesn't exist
        Files.createDirectories(Paths.get("test_data"));

        // Write test questions to file
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(testQuestionFilePath))) {
            writer.write("What is Java?,A,B,C,D,A,Single,5\n");
            writer.write("Explain OOP concepts.,Encapsulation,Inheritance,Polymorphism,Abstraction,All of the above,Multiple,10\n");
        }

        // Initialize the service with test file paths
        questionService = new QuestionBankManagementService(testQuestionFilePath);
    }

    @Test
    public void testLoadQuestions() {
        ObservableList<Question> questions = questionService.getQuestionList();
        assertEquals(2, questions.size());
        assertEquals("What is Java?", questions.get(0).getQuestion());
    }

    @Test
    public void testAddQuestion() throws IOException {
        Question newQuestion = new Question("New Question", "A", "B", "C", "D", "A", "Single", 5);
        questionService.addQuestion(newQuestion);

        ObservableList<Question> questions = questionService.getQuestionList();
        assertEquals(3, questions.size());
        assertTrue(questions.contains(newQuestion));
    }

    @Test
    public void testUpdateQuestion() throws IOException {
        Question updatedQuestion = new Question("What is Java?", "Option1", "Option2", "Option3", "Option4", "Option1", "Single", 5);
        questionService.updateQuestion(updatedQuestion, "What is Java?");

        ObservableList<Question> questions = questionService.getQuestionList();
        Question question = questions.stream().filter(q -> q.getQuestion().equals("What is Java?")).findFirst().orElse(null);
        assertNotNull(question);
        assertEquals("Option1", question.getOptionA());
    }

    @Test
    public void testDeleteQuestion() throws IOException {
        questionService.deleteQuestion("What is Java?");
        ObservableList<Question> questions = questionService.getQuestionList();
        assertEquals(1, questions.size());
        assertFalse(questions.stream().anyMatch(q -> q.getQuestion().equals("What is Java?")));
    }

    @Test
    public void testFilterQuestions() {
        // Filter by question text
        List<Question> filteredQuestions = questionService.filterQuestions("Java", "All", "");
        assertEquals(1, filteredQuestions.size());

        // Filter by type
        filteredQuestions = questionService.filterQuestions("", "Single", "");
        assertEquals(1, filteredQuestions.size());

        // Filter by score
        filteredQuestions = questionService.filterQuestions("", "All", "10");
        assertEquals(1, filteredQuestions.size());
    }

    @Test
    public void testValidateInputs() {
        String validationMessage = questionService.validateInputs("", "A", "B", "C", "D", "A", "Single", "5");
        assertEquals("Please fill in all fields.", validationMessage);

        validationMessage = questionService.validateInputs("Question", "A", "B", "C", "D", "A", "Single", "five");
        assertEquals("Score must be a number.", validationMessage);

        validationMessage = questionService.validateInputs("Question", "A", "B", "C", "D", "A", "Single", "5");
        assertNull(validationMessage);
    }

    @AfterEach
    public void tearDown() throws IOException {
        Files.deleteIfExists(Paths.get(testQuestionFilePath));
    }
}
