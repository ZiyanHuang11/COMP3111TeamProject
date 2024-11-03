import java.util.ArrayList;
import java.util.List;

public class Exam {
    private String examID;
    private Course course;
    private List<Question> questions;
    private List<Result> studentResults;

    public Exam(String examID, Course course) {
        this.examID = examID;
        this.course = course;
        this.questions = new ArrayList<>();
        this.studentResults = new ArrayList<>();
    }

    // Method to add a question
    public void addQuestion(Question question) {
        questions.add(question);
    }

    // Method to calculate grades
    public void calculateGrades() {
        for (Result result : studentResults) {
            result.calculateScore(); // Assuming Result class has a calculateScore method
        }
    }

    // Getter methods (if needed for testing)
    public List<Question> getQuestions() {
        return questions;
    }

    public List<Result> getStudentResults() {
        return studentResults;
    }
}

