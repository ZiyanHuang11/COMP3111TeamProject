package comp3111.examsystem.service;

import comp3111.examsystem.entity.Quiz;
import comp3111.examsystem.entity.StudentQuestion;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.FileWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class QuizServiceTest {

    private static final String TEST_FILE_PATH = "test_data/test_quizzes.txt";
    private QuizService service;

    @BeforeEach
    void setUp() throws Exception {
        // Create a temporary test file with sample data
        try (FileWriter writer = new FileWriter(TEST_FILE_PATH)) {
            writer.write("COMP3111,Software Engineering,quiz1,30\n");
            writer.write("SINGLE,What is OOP?,Option A,Option B,Option C,Option D,A,10\n");
            writer.write("MULTI,Select design patterns,Option A,Option B,Option C,Option D,A,B,15\n");
            writer.write("\n");
            writer.write("COMP5111,Advanced Software Engineering,final,45\n");
            writer.write("SINGLE,What is inheritance?,Option A,Option B,Option C,Option D,B,20\n");
        }
        service = new QuizService(TEST_FILE_PATH);
    }

    @AfterEach
    void tearDown() throws Exception {
        // Delete the temporary test file after each test
        Files.deleteIfExists(Path.of(TEST_FILE_PATH));
    }

    @Test
    void testLoadQuiz() {
        // Act
        Quiz quiz = service.loadQuiz("COMP3111", "quiz1");

        // Assert
        assertNotNull(quiz, "Quiz should not be null");
        assertEquals("COMP3111", quiz.getCourseId(), "Course ID should match");
        assertEquals("Software Engineering", quiz.getCourseName(), "Course name should match");
        assertEquals("quiz1", quiz.getExamType(), "Exam type should match");
        assertEquals(30, quiz.getDuration(), "Duration should match");
        assertEquals(2, quiz.getQuestions().size(), "Number of questions should match");

        // Verify first question
        StudentQuestion question1 = quiz.getQuestions().get(0);
        assertEquals("What is OOP?", question1.getQuestion(), "Question text should match");
        assertEquals("Option A", question1.getOptionA(), "Option A should match");
        assertEquals("A", question1.getAnswers().get(0), "Answer should match");
        assertEquals(10, question1.getScore(), "Score should match");

        // Verify second question
        StudentQuestion question2 = quiz.getQuestions().get(1);
        assertEquals("Select design patterns", question2.getQuestion(), "Question text should match");
        assertEquals(15, question2.getScore(), "Score should match");
        assertEquals(2, question2.getAnswers().size(), "Number of answers should match");
        assertTrue(question2.getAnswers().contains("A"), "Answers should contain A");
        assertTrue(question2.getAnswers().contains("B"), "Answers should contain B");
    }

    @Test
    void testLoadQuiz_NoMatch() {
        // Act
        Quiz quiz = service.loadQuiz("NONEXISTENT", "quiz1");

        // Assert
        assertNotNull(quiz, "Quiz should not be null");
        assertEquals("", quiz.getCourseName(), "Course name should be empty for non-matching quiz");
        assertTrue(quiz.getQuestions().isEmpty(), "Questions list should be empty for non-matching quiz");
    }

    @Test
    void testLoadAllQuizzes() {
        // Act
        List<Quiz> quizzes = service.loadAllQuizzes();

        // Assert
        assertEquals(2, quizzes.size(), "There should be 2 quizzes");

        // Verify first quiz
        Quiz quiz1 = quizzes.get(0);
        assertEquals("COMP3111", quiz1.getCourseId(), "Course ID of first quiz should match");
        assertEquals("Software Engineering", quiz1.getCourseName(), "Course name of first quiz should match");
        assertEquals("quiz1", quiz1.getExamType(), "Exam type of first quiz should match");
        assertEquals(30, quiz1.getDuration(), "Duration of first quiz should match");
        assertEquals(2, quiz1.getQuestions().size(), "First quiz should have 2 questions");

        // Verify second quiz
        Quiz quiz2 = quizzes.get(1);
        assertEquals("COMP5111", quiz2.getCourseId(), "Course ID of second quiz should match");
        assertEquals("Advanced Software Engineering", quiz2.getCourseName(), "Course name of second quiz should match");
        assertEquals("final", quiz2.getExamType(), "Exam type of second quiz should match");
        assertEquals(45, quiz2.getDuration(), "Duration of second quiz should match");
        assertEquals(1, quiz2.getQuestions().size(), "Second quiz should have 1 question");
    }

    @Test
    void testLoadAllQuizzes_InvalidData() throws Exception {
        // Write invalid data to the test file
        Files.writeString(Path.of(TEST_FILE_PATH), "Invalid,Data,Here");

        // Act
        List<Quiz> quizzes = service.loadAllQuizzes();

        // Assert
        assertTrue(quizzes.isEmpty(), "Quizzes list should be empty when the file contains invalid data");
    }

    @Test
    void testLoadAllQuizzes_EmptyFile() throws Exception {
        // Clear the test file
        Files.writeString(Path.of(TEST_FILE_PATH), "");

        // Act
        List<Quiz> quizzes = service.loadAllQuizzes();

        // Assert
        assertTrue(quizzes.isEmpty(), "Quizzes list should be empty for an empty file");
    }
}
