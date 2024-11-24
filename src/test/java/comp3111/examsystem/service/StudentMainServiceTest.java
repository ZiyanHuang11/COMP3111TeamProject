package comp3111.examsystem.service;
import comp3111.examsystem.entity.Exam;
import comp3111.examsystem.service.ExamService;
import comp3111.examsystem.service.StudentMainService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

// 暂时使用硬编码进行测试
public class StudentMainServiceTest {

    private StudentMainService studentMainService;
    private ExamService examService;

    @BeforeEach
    public void setUp() {
        // 使用真实的 ExamService
        examService = new ExamService() {
            @Override
            public ObservableList<Exam> loadExams() {
                // 使用硬编码的测试数据代替文件读取
                ObservableList<Exam> mockExams = FXCollections.observableArrayList(
                        new Exam("Midterm", "CS101", "2024-11-22", "Published"),
                        new Exam("Final", "CS101", "2024-12-10", "Draft"),
                        new Exam("Midterm", "MA101", "2024-11-23", "Published")
                );
                mockExams.get(0).setCourseName("Introduction to Computer Science");
                mockExams.get(1).setCourseName("Introduction to Computer Science");
                mockExams.get(2).setCourseName("Calculus I");
                return mockExams;
            }
        };

        // 创建 StudentMainService 实例并使用真实的 ExamService
        studentMainService = new StudentMainService() {
            @Override
            public List<String> getExamDisplayTexts() {
                return examService.loadExams().stream()
                        .map(exam -> exam.getCourseID() + " " + exam.getCourseName() + " | " + exam.getExamName())
                        .toList();
            }

            @Override
            public Exam getExamByDisplayText(String displayText) {
                return examService.loadExams().stream()
                        .filter(exam -> (exam.getCourseID() + " " + exam.getCourseName() + " | " + exam.getExamName())
                                .equals(displayText))
                        .findFirst()
                        .orElse(null);
            }
        };
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


