package comp3111.examsystem.service;

import comp3111.examsystem.entity.Exam;
import comp3111.examsystem.entity.Question;
import javafx.collections.ObservableList;
import org.junit.jupiter.api.*;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.*;

public class ExamManagementServiceTest {
    private ExamManagementService examService;
    private final String testExamFilePath = "test_data/test_exams.txt";
    private final String testQuestionFilePath = "test_data/test_questions.txt";

    @BeforeEach
    public void setUp() throws IOException {
        // Create test data directory if it doesn't exist
        Files.createDirectories(Paths.get("test_data"));

        // Write test questions to file
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(testQuestionFilePath))) {
            writer.write("What is Java?,A programming language,A coffee brand,An island,All of the above,A programming language,Single,5\n");
            writer.write("Explain OOP concepts.,Encapsulation,Inheritance,Polymorphism,Abstraction,All of the above,Multiple,10\n");
        }

        // Write test exams to file
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(testExamFilePath))) {
            writer.write("Midterm Exam,2023-11-20,COMP3111,Yes,What is Java?\n");
            writer.write("Final Exam,2023-12-15,COMP5111,No,Explain OOP concepts.\n");
        }

        // Initialize the service with test file paths
        examService = new ExamManagementService(testExamFilePath, testQuestionFilePath);
    }

    @Test
    public void testLoadQuestions() {
        ObservableList<Question> questions = examService.getQuestionList();
        assertEquals(2, questions.size());
        assertEquals("What is Java?", questions.get(0).getQuestion());
    }

    @Test
    public void testLoadExams() {
        ObservableList<Exam> exams = examService.getExamList();
        assertEquals(2, exams.size());
        assertEquals("Midterm Exam", exams.get(0).getExamName());
        assertEquals(1, exams.get(0).getQuestions().size());
    }

    @Test
    public void testAddExam() throws IOException {
        Exam newExam = new Exam("Test Exam", "2024-01-01", "COMP1234", "No");
        boolean result = examService.addExam(newExam);
        assertTrue(result);

        ObservableList<Exam> exams = examService.getExamList();
        assertEquals(3, exams.size());
        assertTrue(exams.stream().anyMatch(e -> e.getExamName().equals("Test Exam")));
    }

    @Test
    public void testAddDuplicateExam() throws IOException {
        Exam newExam = new Exam("Midterm Exam", "2024-01-01", "COMP1234", "No");
        boolean result = examService.addExam(newExam);
        assertFalse(result); // Duplicate exam should not be added
    }

    @Test
    public void testUpdateExam() throws IOException {
        Exam updatedExam = new Exam("Updated Exam", "2023-12-20", "COMP5111", "Yes");
        boolean result = examService.updateExam(updatedExam, "Midterm Exam");
        assertTrue(result);

        ObservableList<Exam> exams = examService.getExamList();
        Exam exam = exams.stream().filter(e -> e.getExamName().equals("Updated Exam")).findFirst().orElse(null);
        assertNotNull(exam);
        assertEquals("COMP5111", exam.getExamTime());
    }

    @Test
    public void testDeleteExam() throws IOException {
        boolean result = examService.deleteExam("Midterm Exam");
        assertTrue(result);

        ObservableList<Exam> exams = examService.getExamList();
        assertEquals(1, exams.size());
        assertFalse(exams.stream().anyMatch(e -> e.getExamName().equals("Midterm Exam")));
    }

    @Test
    public void testAddQuestionToExam() throws IOException {
        Exam exam = examService.getExamList().get(0);
        Question question = examService.getQuestionList().get(1); // Second question

        boolean result = examService.addQuestionToExam(exam, question);
        assertTrue(result);
        assertEquals(2, exam.getQuestions().size());
    }

    @Test
    public void testRemoveQuestionFromExam() throws IOException {
        Exam exam = examService.getExamList().get(0);
        Question question = exam.getQuestions().get(0);

        boolean result = examService.removeQuestionFromExam(exam, question);
        assertTrue(result);
        assertEquals(0, exam.getQuestions().size());
    }

    @AfterEach
    public void tearDown() throws IOException {
        Files.deleteIfExists(Paths.get(testExamFilePath));
        Files.deleteIfExists(Paths.get(testQuestionFilePath));
    }
}