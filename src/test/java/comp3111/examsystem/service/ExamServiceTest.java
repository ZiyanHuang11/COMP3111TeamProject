package comp3111.examsystem.service;

import comp3111.examsystem.data.DataManager;
import comp3111.examsystem.entity.Exam;
import comp3111.examsystem.entity.Question;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ExamServiceTest {

    private DataManager dataManager;
    private ExamService examService;

    @BeforeEach
    void setUp() {
        // 初始化 DataManager 并模拟数据
        dataManager = new DataManager();
        mockData();

        // 初始化 ExamService
        examService = new ExamService(dataManager);
    }

    private void mockData() {
        // 添加问题
        List<Question> questions = new ArrayList<>();
        questions.add(new Question("1", "What is Java?", "A programming language", "A coffee brand",
                "An island", "All of the above", "A programming language", "Single", 5));
        questions.add(new Question("2", "Explain OOP concepts.", "Encapsulation", "Inheritance",
                "Polymorphism", "Abstraction", "All of the above", "Multiple", 10));
        dataManager.getQuestions().addAll(questions);

        // 添加考试
        List<String> questionIds = new ArrayList<>();
        questionIds.add("1");
        questionIds.add("2");
        Exam exam = new Exam("Quiz 1", "COMP3111", "2023-11-20", "Yes", questionIds,600);
        exam.setDuration(600);
        dataManager.getExams().add(exam);
    }

    @Test
    void testInitializeExam() {
        // 从 DataManager 获取一个考试
        Exam exam = dataManager.getExams().get(0);

        // 初始化考试
        examService.initializeExam(exam);

        // 验证考试信息
        assertEquals("COMP3111 - Quiz 1", examService.getQuizName());
        assertEquals(2, examService.getTotalQuestions());
        assertEquals(600, examService.getRemainingTime());
    }

    @Test
    void testGetCurrentQuestion() {
        Exam exam = dataManager.getExams().get(0);
        examService.initializeExam(exam);

        // 验证第一道问题
        Question currentQuestion = examService.getCurrentQuestion();
        assertEquals("What is Java?", currentQuestion.getQuestion());
    }

    @Test
    void testSaveAndRetrieveAnswer() {
        Exam exam = dataManager.getExams().get(0);
        examService.initializeExam(exam);

        // 保存答案
        examService.saveAnswer("A");
        assertEquals("A", examService.getUserAnswer());
    }

    @Test
    void testCalculateResults() {
        Exam exam = dataManager.getExams().get(0);
        examService.initializeExam(exam);

        // 模拟用户回答
        examService.saveAnswer("A");
        examService.goToNextQuestion();
        examService.saveAnswer("All of the above");

        // 计算结果
        int[] results = examService.calculateResults();
        assertEquals(2, results[0]); // 正确答案数
        assertEquals(15, results[1]); // 总分
    }

    @Test
    void testNavigationBetweenQuestions() {
        Exam exam = dataManager.getExams().get(0);
        examService.initializeExam(exam);

        // 验证导航
        assertFalse(examService.hasPreviousQuestion());
        assertTrue(examService.hasNextQuestion());

        examService.goToNextQuestion();
        assertTrue(examService.hasPreviousQuestion());
        assertFalse(examService.hasNextQuestion());
    }

    @Test
    void testDecrementTime() {
        Exam exam = dataManager.getExams().get(0);
        examService.initializeExam(exam);

        // 验证时间递减
        assertTrue(examService.decrementTime());
        assertEquals(599, examService.getRemainingTime());

        // 时间耗尽
        for (int i = 0; i < 599; i++) {
            examService.decrementTime();
        }
        assertFalse(examService.decrementTime());
        assertEquals(0, examService.getRemainingTime());
    }

    @Test
    void testPrecisionCalculation() {
        Exam exam = dataManager.getExams().get(0);
        examService.initializeExam(exam);

        // 模拟回答
        examService.saveAnswer("A");
        examService.goToNextQuestion();
        examService.saveAnswer("Wrong Answer");

        // 验证准确率
        double precision = examService.getPrecision();
        assertEquals(50.0, precision, 0.01);
    }
}
