package comp3111.examsystem.service;

import comp3111.examsystem.data.DataManager;
import comp3111.examsystem.entity.Exam;
import comp3111.examsystem.entity.Question;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ExamManagementServiceTest {

    private ExamManagementService examService;
    private DataManager dataManager;

    @BeforeEach
    void setUp() {
        // Mock DataManager data
        dataManager = new DataManager() {
            @Override
            public List<Exam> getExams() {
                return Arrays.asList(
                        new Exam("Quiz 1", "COMP3111", "2023-11-20", "Published", Arrays.asList("1", "2"), 600),
                        new Exam("Quiz 2", "COMP3111", "2023-12-15", "Draft", Arrays.asList("3", "4"), 900)
                );
            }

            @Override
            public List<Question> getQuestions() {
                return Arrays.asList(
                        new Question("1", "What is Java?", "A programming language", "A coffee brand", "An island", "All of the above", "A programming language", "Single", 5),
                        new Question("2", "Explain OOP concepts", "Encapsulation", "Inheritance", "Polymorphism", "Abstraction", "All of the above", "Multiple", 10),
                        new Question("3", "Define Agile", "Iterative development", "Static planning", "No testing", "None of the above", "Iterative development", "Single", 5),
                        new Question("4", "What is UML?", "A modeling language", "A programming language", "A testing framework", "None of the above", "A modeling language", "Single", 5)
                );
            }
        };

        // Initialize ExamManagementService
        examService = new ExamManagementService(dataManager);
    }

    @Test
    void testLoadExams() {
        assertEquals(2, examService.getExamList().size(), "Exam list should contain 2 exams.");
        assertEquals("Quiz 1", examService.getExamList().get(0).getExamName(), "First exam should be 'Quiz 1'.");
    }

    @Test
    void testLoadQuestions() {
        assertEquals(4, examService.getQuestionList().size(), "Question list should contain 4 questions.");
        assertEquals("What is Java?", examService.getQuestionList().get(0).getQuestion(), "First question should be 'What is Java?'.");
    }

    @Test
    void testAddExam() {
        Exam newExam = new Exam("Midterm", "COMP5111", "2023-11-30", "Published", Arrays.asList("1", "3"), 1200);
        boolean result = examService.addExam(newExam);
        assertTrue(result, "New exam should be added successfully.");
        assertEquals(3, examService.getExamList().size(), "Exam list should now contain 3 exams.");
    }

    @Test
    void testAddExamDuplicate() {
        Exam duplicateExam = new Exam("Quiz 1", "COMP3111", "2023-11-20", "Published", Arrays.asList("1", "2"), 600);
        boolean result = examService.addExam(duplicateExam);
        assertFalse(result, "Duplicate exam should not be added.");
        assertEquals(2, examService.getExamList().size(), "Exam list should still contain 2 exams.");
    }

    @Test
    void testUpdateExam() {
        Exam updatedExam = new Exam("Updated Quiz 1", "COMP3111", "2023-11-25", "Published", Arrays.asList("1", "3"), 700);
        boolean result = examService.updateExam(updatedExam, "Quiz 1");
        assertTrue(result, "Exam should be updated successfully.");
        assertEquals("Updated Quiz 1", examService.getExamList().get(0).getExamName(), "First exam name should be updated.");
    }

    @Test
    void testDeleteExam() {
        boolean result = examService.deleteExam("Quiz 1");
        assertTrue(result, "Exam should be deleted successfully.");
        assertEquals(1, examService.getExamList().size(), "Exam list should now contain 1 exam.");
    }

    @Test
    void testAddQuestionToExam() {
        Exam exam = examService.getExamList().get(0); // Quiz 1
        Question question = examService.getQuestionList().get(2); // Define Agile
        boolean result = examService.addQuestionToExam(exam, question);
        assertTrue(result, "Question should be added to exam.");
        assertTrue(exam.getQuestionIds().contains(question.getId()), "Exam should now contain the added question ID.");
    }

    @Test
    void testRemoveQuestionFromExam() {
        Exam exam = examService.getExamList().get(0); // Quiz 1
        Question question = examService.getQuestionList().get(0); // What is Java?
        boolean result = examService.removeQuestionFromExam(exam, question);
        assertTrue(result, "Question should be removed from exam.");
        assertFalse(exam.getQuestionIds().contains(question.getId()), "Exam should no longer contain the removed question ID.");
    }

    @Test
    void testFilterExams() {
        List<Exam> filteredExams = examService.filterExams("", "COMP3111", "All");
        assertEquals(2, filteredExams.size(), "Both exams should match the courseID 'COMP3111'.");
    }

    @Test
    void testFilterQuestions() {
        List<Question> filteredQuestions = examService.filterQuestions("Java", "All", "");
        assertEquals(1, filteredQuestions.size(), "Only one question should contain 'Java'.");
    }
}
