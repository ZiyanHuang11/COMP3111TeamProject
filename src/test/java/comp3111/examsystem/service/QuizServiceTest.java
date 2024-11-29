package comp3111.examsystem.service;

import comp3111.examsystem.entity.Quiz;
import comp3111.examsystem.entity.StudentQuestion;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class QuizServiceTest {
    private QuizService quizService;
    private final String examFilePath = "test_exam.txt";
    private final String questionsFilePath = "test_questions.txt";

    @BeforeEach
    public void setUp() throws IOException {
        // 创建测试文件
        new File(examFilePath).createNewFile();
        new File(questionsFilePath).createNewFile();

        // 初始化 QuizService
        quizService = new QuizService(examFilePath, questionsFilePath);
    }

    @Test
    public void testLoadAllQuestions() throws IOException {
        // 写入测试问题
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(questionsFilePath))) {
            bw.write("What is 2+2?,2,3,4,5,4,Multiple,1");
            bw.newLine();
            bw.write("What is the capital of France?,Paris,London,Berlin,Madrid,Paris,Multiple,1");
            bw.newLine();
        }

        // 加载问题
        var questions = quizService.loadAllQuestions();
        assertEquals(2, questions.size());
        assertNotNull(questions.get("What is 2+2?"));
        assertNotNull(questions.get("What is the capital of France?"));
    }

    @Test
    public void testLoadQuiz() throws IOException {
        // 写入测试问题
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(questionsFilePath))) {
            bw.write("What is 2+2?,2,3,4,5,4,Multiple,1");
            bw.newLine();
            bw.write("What is the capital of France?,Paris,London,Berlin,Madrid,Paris,Multiple,1");
            bw.newLine();
        }

        // 写入测试考试
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(examFilePath))) {
            bw.write("Midterm Exam,2023-05-15,CS101,Yes,Computer Science,60,What is 2+2?|What is the capital of France?");
            bw.newLine();
        }

        // 加载 Quiz
        Quiz quiz = quizService.loadQuiz("CS101", "Midterm Exam");
        assertNotNull(quiz);
        assertEquals("CS101", quiz.getCourseId());
        assertEquals("Computer Science", quiz.getCourseName());
        assertEquals("Midterm Exam", quiz.getExamType());
        assertEquals(60, quiz.getDuration());
        assertEquals(2, quiz.getQuestions().size());
    }

    @Test
    public void testLoadQuizWithInvalidCourseId() throws IOException {
        // 写入测试问题
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(questionsFilePath))) {
            bw.write("What is 2+2?,2,3,4,5,4,Multiple,1");
            bw.newLine();
        }

        // 写入测试考试
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(examFilePath))) {
            bw.write("Midterm Exam,2023-05-15,CS101,Yes,Computer Science,60,What is 2+2?");
            bw.newLine();
        }

        // 尝试加载不存在的 Quiz
        Quiz quiz = quizService.loadQuiz("CS102", "Midterm Exam");
        assertNull(quiz);
    }

    @Test
    public void testLoadQuizWithInvalidExamType() throws IOException {
        // 写入测试问题
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(questionsFilePath))) {
            bw.write("What is 2+2?,2,3,4,5,4,Multiple,1");
            bw.newLine();
        }

        // 写入测试考试
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(examFilePath))) {
            bw.write("Midterm Exam,2023-05-15,CS101,Yes,Computer Science,60,What is 2+2?");
            bw.newLine();
        }

        // 尝试加载不存在的 Quiz
        Quiz quiz = quizService.loadQuiz("CS101", "Final Exam");
        assertNull(quiz);
    }

    @Test
    public void testLoadAllQuizzes() throws IOException {
        // 写入测试问题
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(questionsFilePath))) {
            bw.write("What is 2+2?,2,3,4,5,4,Multiple,1");
            bw.newLine();
            bw.write("What is the capital of France?,Paris,London,Berlin,Madrid,Paris,Multiple,1");
            bw.newLine();
        }

        // 写入测试考试
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(examFilePath))) {
            bw.write("Midterm Exam,2023-05-15,CS101,Yes,Computer Science,60,What is 2+2?|What is the capital of France?");
            bw.newLine();
            bw.write("Final Exam,2023-06-15,CS101,Yes,Computer Science,60,What is 2+2?|What is the capital of France?");
            bw.newLine();
        }

        // 加载所有 Quiz
        List<Quiz> quizzes = quizService.loadAllQuizzes();
        assertEquals(2, quizzes.size());
    }

    @Test
    public void testLoadCompletedQuizzes() throws IOException {
        // 创建测试文件
        File completedQuizzesFile = new File("data/completed_quizzes.txt");
        completedQuizzesFile.createNewFile();

        // 写入测试完成测验记录
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(completedQuizzesFile))) {
            bw.write("john_doe,CS101,Midterm Exam,2023-05-15,80,Pass");
            bw.newLine();
            bw.write("jane_doe,CS101,Final Exam,2023-06-15,90,Pass");
            bw.newLine();
        }

        // 加载完成测验记录
        List<String> completedQuizzes = quizService.loadCompletedQuizzes();
        assertEquals(2, completedQuizzes.size());
        assertTrue(completedQuizzes.contains("john_doe,CS101,Midterm Exam,2023-05-15,80,Pass"));
    }

    @Test
    public void testLoadCompletedQuizzesByUsername() throws IOException {
        // 创建测试文件
        File completedQuizzesFile = new File("data/completed_quizzes.txt");
        completedQuizzesFile.createNewFile();

        // 写入测试完成测验记录
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(completedQuizzesFile))) {
            bw.write("john_doe,CS101,Midterm Exam,2023-05-15,80,Pass");
            bw.newLine();
            bw.write("jane_doe,CS101,Final Exam,2023-06-15,90,Pass");
            bw.newLine();
        }

        // 加载用户的完成测验记录
        List<String> johnQuizzes = quizService.loadCompletedQuizzesByUsername("john_doe");
        assertEquals(1, johnQuizzes.size());
        assertEquals("john_doe,CS101,Midterm Exam,2023-05-15,80,Pass", johnQuizzes.get(0));
    }

    @Test
    public void testLoadCompletedQuizzesByNonExistentUser() throws IOException {
        // 创建测试文件
        File completedQuizzesFile = new File("data/completed_quizzes.txt");
        completedQuizzesFile.createNewFile();

        // 写入测试完成测验记录
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(completedQuizzesFile))) {
            bw.write("john_doe,CS101,Midterm Exam,2023-05-15,80,Pass");
            bw.newLine();
        }

        // 加载不存在用户的完成测验记录
        List<String> janeQuizzes = quizService.loadCompletedQuizzesByUsername("jane_doe");
        assertEquals(0, janeQuizzes.size());
    }

    @AfterEach
    public void tearDown() {
        new File(examFilePath).delete();
        new File(questionsFilePath).delete();
        new File("data/completed_quizzes.txt").delete();
    }
}