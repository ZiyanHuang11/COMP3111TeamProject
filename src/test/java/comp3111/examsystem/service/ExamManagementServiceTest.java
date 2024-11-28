package comp3111.examsystem.service;

import comp3111.examsystem.entity.Exam;
import comp3111.examsystem.entity.Question;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ExamManagementServiceTest {

    private static final String TEST_EXAM_FILE = "test_exams.txt";
    private static final String TEST_QUESTION_FILE = "test_questions.txt";
    private ExamManagementService service;

    @BeforeEach
    void setUp() throws IOException {
        // Prepare test files
        Files.write(Paths.get(TEST_QUESTION_FILE), ("Question1, A, B, C, D, A, Multiple, 5\n" +
                "Question2, A, B, C, D, B, Single, 3\n").getBytes());
        Files.write(Paths.get(TEST_EXAM_FILE), ("Final Exam, 2024-12-01, CSE101, Published, 2, 120, Question1|Question2\n").getBytes());

        // Create the ExamManagementService instance
        service = new ExamManagementService(TEST_EXAM_FILE, TEST_QUESTION_FILE);
    }

    @AfterEach
    void tearDown() throws IOException {
        // Clean up the files after each test
        Files.deleteIfExists(Paths.get(TEST_EXAM_FILE));
        Files.deleteIfExists(Paths.get(TEST_QUESTION_FILE));
    }

    @Test
    void testConstructor_loadQuestionsAndExams() {
        assertEquals(2, service.getQuestionList().size(), "Questions should be loaded correctly.");
        assertEquals(1, service.getExamList().size(), "Exams should be loaded correctly.");
    }

    @Test
    void testAddExam() throws IOException {
        Exam newExam = new Exam("Midterm Exam", "CSE101", "2024-11-15", "Draft", 90, List.of(service.getQuestionList().get(0)));
        assertTrue(service.addExam(newExam), "Exam should be added successfully.");
        assertEquals(2, service.getExamList().size(), "Exam list size should increase after adding an exam.");
    }

    @Test
    void testAddDuplicateExam() throws IOException {
        Exam duplicateExam = new Exam("Final Exam", "CSE101", "2024-12-01", "Published", 120, List.of(service.getQuestionList().get(0)));
        assertFalse(service.addExam(duplicateExam), "Duplicate exam should not be added.");
    }

    @Test
    void testUpdateExam() throws IOException {
        Exam updatedExam = new Exam("Updated Final Exam", "CSE101", "2024-12-01", "Published", 150, List.of(service.getQuestionList().get(0)));
        assertTrue(service.updateExam(updatedExam, "Final Exam", "CSE101"), "Exam should be updated successfully.");
        assertEquals("Updated Final Exam", service.getExamList().get(0).getExamName(), "Exam name should be updated.");
    }

    @Test
    void testDeleteExam() throws IOException {
        assertTrue(service.deleteExam("Final Exam", "CSE101"), "Exam should be deleted successfully.");
        assertEquals(0, service.getExamList().size(), "Exam list should be empty after deletion.");
    }

    @Test
    void testAddQuestionToExam() throws IOException {
        Exam exam = service.getExamList().get(0);
        Question question = new Question("Question3", "A", "B", "C", "D", "A", "Single", 4);
        assertTrue(service.addQuestionToExam(exam, question), "Question should be added to exam.");
        assertEquals(3, exam.getQuestions().size(), "Exam should have 3 questions after adding.");
    }

    @Test
    void testAddDuplicateQuestionToExam() throws IOException {
        Exam exam = service.getExamList().get(0);
        Question question = service.getQuestionList().get(0);
        assertFalse(service.addQuestionToExam(exam, question), "Duplicate question should not be added.");
    }

    @Test
    void testRemoveQuestionFromExam() throws IOException {
        Exam exam = service.getExamList().get(0);
        Question question = service.getQuestionList().get(0);
        assertTrue(service.removeQuestionFromExam(exam, question), "Question should be removed from exam.");
        assertEquals(1, exam.getQuestions().size(), "Exam should have 1 question after removal.");
    }

    @Test
    void testRemoveNonExistentQuestionFromExam() throws IOException {
        Exam exam = service.getExamList().get(0);
        Question question = new Question("NonExistent", "A", "B", "C", "D", "A", "Single", 5);
        assertFalse(service.removeQuestionFromExam(exam, question), "Non-existent question should not be removed.");
    }

    @Test
    void testFilterExams() {
        List<Exam> filteredExams = service.filterExams("Final Exam", "CSE101", "Published");
        assertEquals(1, filteredExams.size(), "Filter by exam name, course ID, and publish status should work.");
    }

    @Test
    void testFilterQuestions() {
        List<Question> filteredQuestions = service.filterQuestions("Question1", "Multiple", "5");
        assertEquals(1, filteredQuestions.size(), "Filter by question text, type, and score should work.");
    }

    @Test
    void testSaveExams() throws IOException {
        // Add a new exam
        Exam newExam = new Exam("Midterm Exam", "CSE102", "2024-11-15", "Draft", 90, List.of(service.getQuestionList().get(1)));
        service.addExam(newExam);

        // Save the exams to the file
        service.saveExams();

        // Debugging: Print the size of the exam list
        System.out.println("Exam list size after saving: " + service.getExamList().size());

        // Verify that the exam file has been updated
        List<String> lines = Files.readAllLines(Paths.get(TEST_EXAM_FILE));
        boolean examFound = lines.stream().anyMatch(line -> line.contains("Midterm Exam"));
        assertTrue(examFound, "The saved exam should appear in the file.");
    }

    @Test
    void testLoadQuestions_invalidFormat() throws IOException {
        // Simulate an invalid question file format
        Files.write(Paths.get(TEST_QUESTION_FILE), ("InvalidQuestionFormat\n").getBytes());

        ExamManagementService serviceWithInvalidFile = new ExamManagementService(TEST_EXAM_FILE, TEST_QUESTION_FILE);
        assertEquals(0, serviceWithInvalidFile.getQuestionList().size(), "Invalid questions should be skipped.");
    }
}
