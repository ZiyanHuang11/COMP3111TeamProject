package comp3111.examsystem.service;

import comp3111.examsystem.data.DataManager;
import comp3111.examsystem.entity.Exam;
import comp3111.examsystem.entity.Question;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ExamServiceTest {

    private ExamService examService;
    private MockDataManager mockDataManager;

    @BeforeEach
    void setUp() {
        // Initialize MockDataManager with in-memory data
        mockDataManager = new MockDataManager();

        // Initialize ExamService with mock data
        examService = new ExamService(mockDataManager);
        mockData();
    }

    private void mockData() {
        // Add mock questions
        List<Question> questions = new ArrayList<>();
        questions.add(new Question("1", "What is Java?", "A programming language", "A coffee brand",
                "An island", "All of the above", "A programming language", "Single", 5));
        questions.add(new Question("2", "Explain OOP concepts.", "Encapsulation", "Inheritance",
                "Polymorphism", "Abstraction", "All of the above", "Multiple", 10));

        // Add mock questions to the mockDataManager
        mockDataManager.getQuestions().addAll(questions);

        // Add mock exam
        List<String> questionIds = new ArrayList<>();
        questionIds.add("1");
        questionIds.add("2");
        Exam exam = new Exam("Quiz 1", "COMP3111", "2023-11-20", "Yes", questionIds, 600);
        exam.setCourseName("COMP3111");
        exam.setDuration(600);
        mockDataManager.getExams().add(exam);
    }

    @Test
    void testInitializeExam() {
        // Get an exam from mock data
        Exam exam = mockDataManager.getExams().get(0);

        // Initialize the exam
        examService.initializeExam(exam);

        // Verify the exam details
        assertEquals("COMP3111 - Quiz 1", examService.getQuizName());
        assertEquals(2, examService.getTotalQuestions());
        assertEquals(600, examService.getRemainingTime());
    }

    @Test
    void testGetCurrentQuestion() {
        Exam exam = mockDataManager.getExams().get(0);
        examService.initializeExam(exam);

        // Verify the first question
        Question currentQuestion = examService.getCurrentQuestion();
        assertEquals("What is Java?", currentQuestion.getQuestion());
    }

    @Test
    void testSaveAndRetrieveAnswer() {
        Exam exam = mockDataManager.getExams().get(0);
        examService.initializeExam(exam);

        // Save the answer
        examService.saveAnswer("A");
        assertEquals("A", examService.getUserAnswer());
    }

    @Test
    void testCalculateResults() {
        Exam exam = mockDataManager.getExams().get(0);
        examService.initializeExam(exam);

        // Simulate answering questions
        examService.saveAnswer("A"); // Correct answer for question 1
        examService.goToNextQuestion();
        examService.saveAnswer("All of the above"); // Correct answer for question 2

        // Calculate and verify results
        int[] results = examService.calculateResults();
        assertEquals(2, results[0]); // Correct answers count
        assertEquals(15, results[1]); // Total score (5 + 10)
    }

    @Test
    void testNavigationBetweenQuestions() {
        Exam exam = mockDataManager.getExams().get(0);
        examService.initializeExam(exam);

        // Verify navigation between questions
        assertFalse(examService.hasPreviousQuestion());
        assertTrue(examService.hasNextQuestion());

        examService.goToNextQuestion();
        assertTrue(examService.hasPreviousQuestion());
        assertFalse(examService.hasNextQuestion());
    }

    @Test
    void testDecrementTime() {
        Exam exam = mockDataManager.getExams().get(0);
        examService.initializeExam(exam);

        // Verify time decrement
        assertTrue(examService.decrementTime());
        assertEquals(599, examService.getRemainingTime());

        // Exhaust time
        for (int i = 0; i < 599; i++) {
            examService.decrementTime();
        }
        assertFalse(examService.decrementTime());
        assertEquals(0, examService.getRemainingTime());
    }

    @Test
    void testPrecisionCalculation() {
        Exam exam = mockDataManager.getExams().get(0);
        examService.initializeExam(exam);

        // Simulate answering questions
        examService.saveAnswer("A"); // Correct answer for question 1
        examService.goToNextQuestion();
        examService.saveAnswer("All of the above"); // Correct answer for question 2

        // Calculate precision (accuracy)
        double precision = examService.getPrecision();
        assertEquals(100.0, precision, 0.01); // Should be 100% since both answers are correct
    }

    // Mock DataManager class
    static class MockDataManager extends DataManager {
        private final List<Exam> examList = new ArrayList<>();
        private final List<Question> questionList = new ArrayList<>();

        @Override
        public List<Exam> getExams() {
            return examList;
        }

        @Override
        public List<Question> getQuestions() {
            return questionList;
        }

        // Additional methods to add mock exams and questions
        public List<Exam> getMockExams() {
            return examList;
        }

        public List<Question> getMockQuestions() {
            return questionList;
        }
    }
}
