package comp3111.examsystem.service;

import comp3111.examsystem.entity.StudentQuestion;
import comp3111.examsystem.entity.Quiz;

import java.io.*;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class QuizService {
    private final String filePath;

    public QuizService(String filePath) {
        this.filePath = filePath;
    }

    /**
     * 加载指定课程和考试类型的 Quiz
     *
     * @param courseId 课程编号
     * @param examType 考试类型（例如：quiz1, final）
     * @return 对应的 Quiz 对象
     */
    public Quiz loadQuiz(String courseId, String examType) {
        List<StudentQuestion> questions = new ArrayList<>();
        String courseName = "";
        int duration = 0;

        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            boolean isTargetQuiz = false;

            while ((line = br.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty()) {
                    continue; // 跳过空行
                }

                String[] parts = line.split(",");
                // 检查是否为 Quiz 的开头（基于是否有 4 个部分）
                if (parts.length == 4) {
                    // 判断是否是目标 Quiz
                    if (parts[0].equalsIgnoreCase(courseId) && parts[2].equalsIgnoreCase(examType)) {
                        // 读取考试信息
                        courseName = parts[1].trim();
                        duration = Integer.parseInt(parts[3].trim());
                        isTargetQuiz = true;
                    } else {
                        // 如果当前不是目标 Quiz，忽略后续的问题
                        if (isTargetQuiz) {
                            break;
                        }
                    }
                } else if (isTargetQuiz && (line.startsWith("SINGLE") || line.startsWith("MULTI"))) {
                    // 读取题目信息
                    String[] questionParts = line.split(",");
                    if (questionParts.length < 7) {
                        System.err.println("问题行格式不正确: " + line);
                        continue; // 跳过格式不正确的行
                    }

                    String questionType = questionParts[0].trim(); // SINGLE 或 MULTI
                    String question = questionParts[1].trim();
                    String optionA = questionParts[2].trim();
                    String optionB = questionParts[3].trim();
                    String optionC = questionParts[4].trim();
                    String optionD = questionParts[5].trim();

                    List<String> answers = new ArrayList<>();
                    int score = 0;

                    if ("SINGLE".equalsIgnoreCase(questionType)) {
                        if (questionParts.length < 8) {
                            System.err.println("SINGLE 类型的问题缺少分数字段: " + line);
                            continue;
                        }
                        String answer = questionParts[6].trim();
                        try {
                            score = Integer.parseInt(questionParts[7].trim());
                        } catch (NumberFormatException e) {
                            System.err.println("分数解析错误，默认设为0: " + questionParts[7].trim());
                        }
                        answers.add(answer);
                    } else if ("MULTI".equalsIgnoreCase(questionType)) {
                        if (questionParts.length < 8) {
                            System.err.println("MULTI 类型的问题缺少答案和分数字段: " + line);
                            continue;
                        }
                        // 答案字段从 parts[6] 到 parts[parts.length - 2]
                        for (int i = 6; i < questionParts.length - 1; i++) {
                            String ans = questionParts[i].trim();
                            if (!ans.isEmpty()) {
                                answers.add(ans);
                            }
                        }
                        // 最后一个字段是分数
                        try {
                            score = Integer.parseInt(questionParts[questionParts.length - 1].trim());
                        } catch (NumberFormatException e) {
                            System.err.println("分数解析错误，默认设为0: " + questionParts[questionParts.length - 1].trim());
                        }
                    } else {
                        System.err.println("未知的问题类型: " + questionType);
                        continue; // 跳过未知类型的问题
                    }

                    StudentQuestion studentQuestion = new StudentQuestion(
                            question,
                            optionA,
                            optionB,
                            optionC,
                            optionD,
                            answers,
                            questionType,
                            score
                    );

                    questions.add(studentQuestion);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return new Quiz(courseId, courseName, examType, duration, questions);
    }

    /**
     * 加载所有的 Quiz
     *
     * @return 所有加载的 Quiz 列表
     */
    public List<Quiz> loadAllQuizzes() {
        List<Quiz> allQuizzes = new ArrayList<>();
        String currentCourseId = "";
        String currentCourseName = "";
        String currentExamType = "";
        int currentDuration = 0;
        List<StudentQuestion> currentQuestions = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;

            while ((line = br.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty()) {
                    continue; // 跳过空行
                }

                String[] parts = line.split(",");
                // 检查是否为 Quiz 的开头（基于是否有 4 个部分）
                if (parts.length == 4) {
                    // 如果之前有一个正在处理的 Quiz，保存它
                    if (!currentCourseId.isEmpty()) {
                        Quiz quiz = new Quiz(currentCourseId, currentCourseName, currentExamType, currentDuration, currentQuestions);
                        allQuizzes.add(quiz);
                        // 重置当前变量
                        currentQuestions = new ArrayList<>();
                    }
                    // 读取新的考试信息
                    currentCourseId = parts[0].trim();
                    currentCourseName = parts[1].trim();
                    currentExamType = parts[2].trim();
                    currentDuration = Integer.parseInt(parts[3].trim());
                } else if (parts.length >= 7 && (line.startsWith("SINGLE") || line.startsWith("MULTI"))) {
                    // 读取题目信息
                    String questionType = parts[0].trim(); // SINGLE 或 MULTI
                    String question = parts[1].trim();
                    String optionA = parts[2].trim();
                    String optionB = parts[3].trim();
                    String optionC = parts[4].trim();
                    String optionD = parts[5].trim();

                    List<String> answers = new ArrayList<>();
                    int score = 0;

                    if ("SINGLE".equalsIgnoreCase(questionType)) {
                        if (parts.length < 8) {
                            System.err.println("SINGLE 类型的问题缺少分数字段: " + line);
                            continue;
                        }
                        String answer = parts[6].trim();
                        try {
                            score = Integer.parseInt(parts[7].trim());
                        } catch (NumberFormatException e) {
                            System.err.println("分数解析错误，默认设为0: " + parts[7].trim());
                        }
                        answers.add(answer);
                    } else if ("MULTI".equalsIgnoreCase(questionType)) {
                        if (parts.length < 8) {
                            System.err.println("MULTI 类型的问题缺少答案和分数字段: " + line);
                            continue;
                        }
                        // 答案字段从 parts[6] 到 parts[parts.length - 2]
                        for (int i = 6; i < parts.length - 1; i++) {
                            String ans = parts[i].trim();
                            if (!ans.isEmpty()) {
                                answers.add(ans);
                            }
                        }
                        // 最后一个字段是分数
                        try {
                            score = Integer.parseInt(parts[parts.length - 1].trim());
                        } catch (NumberFormatException e) {
                            System.err.println("分数解析错误，默认设为0: " + parts[parts.length - 1].trim());
                        }
                    } else {
                        System.err.println("未知的问题类型: " + questionType);
                        continue; // 跳过未知类型的问题
                    }

                    StudentQuestion studentQuestion = new StudentQuestion(
                            question,
                            optionA,
                            optionB,
                            optionC,
                            optionD,
                            answers,
                            questionType,
                            score
                    );

                    currentQuestions.add(studentQuestion);
                }
            }

            // 添加最后一个 Quiz
            if (!currentCourseId.isEmpty()) {
                Quiz quiz = new Quiz(currentCourseId, currentCourseName, currentExamType, currentDuration, currentQuestions);
                allQuizzes.add(quiz);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return allQuizzes;
    }
}
