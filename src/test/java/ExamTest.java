import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class ExamTest {
    private Exam exam;
    private Question question;

    @BeforeEach
    void setUp() {
        exam = new Exam("E1", new Course("C1", "Sample Course"));
        question = new Question("Q1", "Sample Question");
    }

    @Test
    void testAddQuestion() {
        exam.addQuestion(question);
        assertEquals(1, exam.getQuestions().size());
        assertEquals(question, exam.getQuestions().get(0));
    }

    @Test
    void testCalculateGrades() {
        Result result = new Result();
        exam.getStudentResults().add(result);
        exam.calculateGrades();
        assertEquals(100.0f, result.getScore()); // Assuming calculateScore sets a dummy score of 100
    }
}
