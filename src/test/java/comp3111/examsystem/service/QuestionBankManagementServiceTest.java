package comp3111.examsystem.service;

import comp3111.examsystem.data.DataManager;
import comp3111.examsystem.entity.Question;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class QuestionBankManagementServiceTest {

    private QuestionBankManagementService questionService;

    @BeforeEach
    public void setUp() {
        // Mock initial data for questions
        Question question1 = new Question("1", "What is Java?", "A", "B", "C", "D", "A", "Single", 5);
        Question question2 = new Question("2", "Explain OOP concepts.", "Encapsulation", "Inheritance", "Polymorphism", "Abstraction", "All of the above", "Multiple", 10);

        // Initialize the service with mock data
        questionService = new QuestionBankManagementService(new MockDataManager(List.of(question1, question2)));
    }

    @Test
    public void testLoadQuestions() {
        ObservableList<Question> questions = questionService.getQuestionList();
        assertEquals(2, questions.size());
        assertEquals("What is Java?", questions.get(0).getQuestion());
    }

    @Test
    public void testAddQuestion() {
        Question newQuestion = new Question("3", "What is Python?", "A", "B", "C", "D", "B", "Single", 5);
        questionService.addQuestion(newQuestion);

        ObservableList<Question> questions = questionService.getQuestionList();
        assertEquals(3, questions.size());
        assertTrue(questions.contains(newQuestion));
    }

    @Test
    public void testUpdateQuestion() {
        Question updatedQuestion = new Question("1", "What is Java?", "Option1", "Option2", "Option3", "Option4", "Option1", "Single", 5);
        questionService.updateQuestion(updatedQuestion);

        ObservableList<Question> questions = questionService.getQuestionList();
        Question question = questions.stream().filter(q -> q.getId().equals("1")).findFirst().orElse(null);
        assertNotNull(question);
        assertEquals("Option1", question.getOptionA());
    }

    @Test
    public void testDeleteQuestion() {
        questionService.deleteQuestion("1");
        ObservableList<Question> questions = questionService.getQuestionList();
        assertEquals(1, questions.size());
        assertFalse(questions.stream().anyMatch(q -> q.getId().equals("1")));
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

    // Mock DataManager implementation
// Mock DataManager implementation
    static class MockDataManager extends DataManager {
        private final List<Question> mockQuestions;

        MockDataManager(List<Question> questions) {
            this.mockQuestions = new ArrayList<>(questions);
        }

        @Override
        public List<Question> getQuestions() {
            return new ArrayList<>(mockQuestions);
        }

        @Override
        public void addQuestion(Question question) {
            mockQuestions.add(question);
        }

        @Override
        public void updateQuestion(String id, Question updatedQuestion) {
            for (int i = 0; i < mockQuestions.size(); i++) {
                if (mockQuestions.get(i).getId().equals(id)) {
                    mockQuestions.set(i, updatedQuestion);
                    return;
                }
            }
        }

        @Override
        public void deleteQuestion(String id) {
            mockQuestions.removeIf(q -> q.getId().equals(id));
        }

        @Override
        public List<Question> filterQuestions(String questionFilter, String typeFilter, String scoreFilter) {
            return mockQuestions.stream()
                    .filter(q -> (questionFilter.isEmpty() || q.getQuestion().toLowerCase().contains(questionFilter.toLowerCase())) &&
                            (typeFilter.equals("All") || q.getType().equals(typeFilter)) &&
                            (scoreFilter.isEmpty() || String.valueOf(q.getScore()).equals(scoreFilter)))
                    .toList();
        }
    }
}

