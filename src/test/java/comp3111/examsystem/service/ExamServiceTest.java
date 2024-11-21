package comp3111.examsystem.service;
import comp3111.examsystem.entity.Exam;
import comp3111.examsystem.entity.Question;
import comp3111.examsystem.service.ExamService;
import javafx.collections.ObservableList;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class ExamServiceTest {

    private ExamService examService;

    @BeforeEach
    public void setUp() {
        examService = new ExamService();
    }

    @Test
    public void testLoadExams() {
        ObservableList<Exam> exams = examService.loadExams();
        assertNotNull(exams, "Exams should not be null");
        assertFalse(exams.isEmpty(), "Exams should not be empty");

        Exam firstExam = exams.get(0);
        assertNotNull(firstExam.getCourseID(), "Course ID should not be null");
        assertTrue(firstExam.getQuestions().size() > 0, "Exam should have questions");
    }

    @Test
    public void testLoadQuestions() {
        ObservableList<Exam> exams = examService.loadExams();
        Exam exam = exams.get(0);
        List<Question> questions = exam.getQuestions();

        assertNotNull(questions, "Questions should not be null");
        assertFalse(questions.isEmpty(), "Questions should not be empty");

        Question firstQuestion = questions.get(0);
        assertNotNull(firstQuestion.getQuestion(), "Question text should not be null");
        assertNotNull(firstQuestion.getAnswer(), "Question answer should not be null");
    }

    @Test
    public void testInitializeExam() {
        ObservableList<Exam> exams = examService.loadExams();
        Exam firstExam = exams.get(0);

        examService.initializeExam(firstExam);

        assertEquals(firstExam, examService.getQuizName().split(" - ")[1]);
        assertEquals(firstExam.getQuestions().size(), examService.getTotalQuestions());
    }

    @Test
    public void testQuestionNavigation() {
        ObservableList<Exam> exams = examService.loadExams();
        Exam firstExam = exams.get(0);

        examService.initializeExam(firstExam);

        assertTrue(examService.hasNextQuestion(), "Should have next question");
        examService.goToNextQuestion();
        assertEquals(1, examService.getCurrentQuestionIndex(), "Current index should be 1");

        assertTrue(examService.hasPreviousQuestion(), "Should have previous question");
        examService.goToPreviousQuestion();
        assertEquals(0, examService.getCurrentQuestionIndex(), "Current index should be 0");
    }

    @Test
    public void testSetCurrentQuestionIndex() {
        ObservableList<Exam> exams = examService.loadExams();
        Exam firstExam = exams.get(0);

        examService.initializeExam(firstExam);

        examService.setCurrentQuestionIndex(2);
        assertEquals(2, examService.getCurrentQuestionIndex(), "Current index should be 2");

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            examService.setCurrentQuestionIndex(-1);
        });
        assertEquals("Invalid question index: -1", exception.getMessage());
    }

    @Test
    public void testSaveAndRetrieveAnswer() {
        ObservableList<Exam> exams = examService.loadExams();
        Exam firstExam = exams.get(0);

        examService.initializeExam(firstExam);

        examService.saveAnswer("A");
        assertEquals("A", examService.getUserAnswer(), "Answer should be 'A'");
    }

    @Test
    public void testCalculateResults() {
        ObservableList<Exam> exams = examService.loadExams();
        Exam firstExam = exams.get(0);

        examService.initializeExam(firstExam);

        // 模拟答题
        examService.saveAnswer("A");
        examService.goToNextQuestion();
        examService.saveAnswer("B");

        int[] results = examService.calculateResults();

        assertTrue(results[0] >= 0, "Correct answers should be non-negative");
        assertTrue(results[1] >= 0, "Total score should be non-negative");
    }

    @Test
    public void testDecrementTime() {
        ObservableList<Exam> exams = examService.loadExams();
        Exam firstExam = exams.get(0);

        examService.initializeExam(firstExam);

        int initialTime = examService.getRemainingTime();
        boolean decremented = examService.decrementTime();

        assertTrue(decremented, "Time should decrement");
        assertEquals(initialTime - 1, examService.getRemainingTime(), "Remaining time should decrease by 1");
    }

    @Test
    public void testPrecisionCalculation() {
        ObservableList<Exam> exams = examService.loadExams();
        Exam firstExam = exams.get(0);

        examService.initializeExam(firstExam);

        examService.saveAnswer("A");
        examService.goToNextQuestion();
        examService.saveAnswer("B");

        double precision = examService.getPrecision();
        assertTrue(precision >= 0.0 && precision <= 100.0, "Precision should be between 0 and 100");
    }
}
