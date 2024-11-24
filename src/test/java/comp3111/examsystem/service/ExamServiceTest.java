package comp3111.examsystem.service;

import comp3111.examsystem.entity.Exam;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class ExamServiceTest {
    private ExamService examService;

    @BeforeEach
    public void setUp() {
        examService = new ExamService();
    }

    @Test
    public void testDecrementTime() {
        Exam mockExam = new Exam("Midterm", "CS101", "2024-11-22", "Published");
        mockExam.setDuration(10);
        examService.initializeExam(mockExam);
        assertTrue(examService.decrementTime(), "Time should decrement");
        assertEquals(9, examService.getRemainingTime());
    }

    @Test
    public void testSetCurrentQuestionIndex() {
        Exam mockExam = new Exam("Midterm", "CS101", "2024-11-22", "Published");
        examService.initializeExam(mockExam);
        Exception exception = assertThrows(IllegalArgumentException.class,
                () -> examService.setCurrentQuestionIndex(100));
        assertTrue(exception.getMessage().contains("Invalid question index"));
    }
}

