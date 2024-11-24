package comp3111.examsystem.service;

import comp3111.examsystem.data.DataManager;
import comp3111.examsystem.entity.Exam;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class StudentMainServiceTest {
    private StudentMainService studentMainService;
    private DataManager mockDataManager;

    @BeforeEach
    public void setUp() {
        // 创建模拟的 DataManager
        mockDataManager = new DataManager() {
            @Override
            public List<Exam> getExams() {
                List<Exam> mockExams = new ArrayList<>();
                Exam exam1 = new Exam("Midterm", "CS101", "2024-11-22", "Published");
                exam1.setCourseName("Introduction to Computer Science");
                mockExams.add(exam1);

                Exam exam2 = new Exam("Final", "CS101", "2024-12-10", "Draft");
                exam2.setCourseName("Introduction to Computer Science");
                mockExams.add(exam2);

                Exam exam3 = new Exam("Midterm", "MA101", "2024-11-23", "Published");
                exam3.setCourseName("Calculus I");
                mockExams.add(exam3);

                return mockExams;
            }
        };

        // 使用模拟的 DataManager 初始化 Service
        studentMainService = new StudentMainService(mockDataManager);
    }

    @Test
    public void testGetExamDisplayTexts() {
        List<String> examDisplayTexts = studentMainService.getExamDisplayTexts();

        assertNotNull(examDisplayTexts, "Display texts should not be null");
        assertEquals(3, examDisplayTexts.size(), "There should be 3 exams in the display texts");

        assertTrue(examDisplayTexts.contains("CS101 Introduction to Computer Science | Midterm"),
                "Should contain display text for Midterm");
        assertTrue(examDisplayTexts.contains("CS101 Introduction to Computer Science | Final"),
                "Should contain display text for Final");
        assertTrue(examDisplayTexts.contains("MA101 Calculus I | Midterm"),
                "Should contain display text for MA101 Midterm");
    }

    @Test
    public void testGetExamByDisplayText() {
        String validDisplayText = "CS101 Introduction to Computer Science | Midterm";
        Exam exam = studentMainService.getExamByDisplayText(validDisplayText);

        assertNotNull(exam, "Exam should not be null for a valid display text");
        assertEquals("CS101", exam.getCourseID(), "Course ID should match");
        assertEquals("Midterm", exam.getExamName(), "Exam name should match");

        String invalidDisplayText = "CS999 Nonexistent Course | Nonexistent Exam";
        Exam nonexistentExam = studentMainService.getExamByDisplayText(invalidDisplayText);

        assertNull(nonexistentExam, "Exam should be null for an invalid display text");
    }
}
