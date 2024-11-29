package comp3111.examsystem.service;

import comp3111.examsystem.entity.StudentQuestion;
import comp3111.examsystem.entity.Quiz;

import java.io.*;
import java.nio.file.*;
import java.util.*;

public class QuizService {
    private final String examFilePath;
    private final String questionsFilePath;

    public QuizService(String examFilePath, String questionsFilePath) {
        this.examFilePath = examFilePath;
        this.questionsFilePath = questionsFilePath;
    }

    /**
     * 加载指定课程和考试类型的 Quiz
     *
     * @param courseId 课程编号
     * @param examType 考试类型（例如：Midterm Exam, Final Exam）
     * @return 对应的 Quiz 对象，如果未找到则返回 null
     */
    public Quiz loadQuiz(String courseId, String examType) {
        // 加载所有问题到一个 Map 中，键为 questionText
        Map<String, StudentQuestion> questionMap = loadAllQuestions();

        if (questionMap.isEmpty()) {
            System.err.println("没有找到任何问题。");
            return null;
        }

        try (BufferedReader br = new BufferedReader(new FileReader(examFilePath))) {
            String line;

            while ((line = br.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty()) {
                    continue; // 跳过空行
                }

                // 使用正则表达式分割，只分割前7个逗号，剩下的作为问题部分
                String[] parts = line.split(",", 7);
                if (parts.length < 7) {
                    System.err.println("exam.txt 中的行格式不正确: " + line);
                    continue; // 跳过格式不正确的行
                }

                String currentExamType = parts[0].trim();
                String examDate = parts[1].trim(); // 未使用
                String currentCourseId = parts[2].trim();
                String isPublish = parts[3].trim(); // 未使用
                String courseName = parts[4].trim();
                int duration = 0;
                try {
                    duration = Integer.parseInt(parts[5].trim());
                } catch (NumberFormatException e) {
                    System.err.println("解析 duration 失败，默认设为0: " + parts[5].trim());
                }
                String questionsPart = parts[6].trim();

                // 检查是否是目标 Quiz
                if (currentCourseId.equalsIgnoreCase(courseId) && currentExamType.equalsIgnoreCase(examType)) {
                    // 分割问题列表
                    String[] questionTexts = questionsPart.split("\\|");
                    List<StudentQuestion> questions = new ArrayList<>();

                    for (String qText : questionTexts) {
                        qText = qText.trim();
                        if (questionMap.containsKey(qText)) {
                            questions.add(questionMap.get(qText));
                        } else {
                            System.err.println("未在 questions.txt 中找到问题: " + qText);
                        }
                    }

                    return new Quiz(currentCourseId, courseName, currentExamType, duration, questions);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        System.err.println("未找到匹配的 Quiz: courseId=" + courseId + ", examType=" + examType);
        return null; // 如果未找到匹配的 Quiz
    }

    /**
     * 加载 questions.txt 中的所有问题
     *
     * @return 一个包含所有问题的 Map，键为 questionText
     */
    Map<String, StudentQuestion> loadAllQuestions() {
        Map<String, StudentQuestion> questionMap = new HashMap<>();

        try (BufferedReader br = new BufferedReader(new FileReader(questionsFilePath))) {
            String line;

            while ((line = br.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty()) {
                    continue; // 跳过空行
                }

                String[] parts = line.split(",");
                if (parts.length < 8) {
                    System.err.println("questions.txt 中的行格式不正确: " + line);
                    continue; // 跳过格式不正确的行
                }

                String questionText = parts[0].trim();
                String optionA = parts[1].trim();
                String optionB = parts[2].trim();
                String optionC = parts[3].trim();
                String optionD = parts[4].trim();
                String answersRaw = parts[5].trim();
                String questionType = parts[6].trim();
                int score = 0;
                try {
                    score = Integer.parseInt(parts[7].trim());
                } catch (NumberFormatException e) {
                    System.err.println("解析 score 失败，默认设为0: " + parts[7].trim());
                }

                List<String> answers = new ArrayList<>();
                if ("Multiple".equalsIgnoreCase(questionType) || "MultipleChoice".equalsIgnoreCase(questionType) || "Multiple Choice".equalsIgnoreCase(questionType)) {
                    // 多选题答案用 | 分隔
                    answers = Arrays.asList(answersRaw.split("\\|"));
                } else {
                    // 单选题答案为单个选项
                    answers.add(answersRaw);
                }

                StudentQuestion studentQuestion = new StudentQuestion(
                        questionText,
                        optionA,
                        optionB,
                        optionC,
                        optionD,
                        answers,
                        questionType,
                        score
                );

                questionMap.put(questionText, studentQuestion);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return questionMap;
    }

    /**
     * 加载所有的 Quiz
     *
     * @return 所有加载的 Quiz 列表
     */
    public List<Quiz> loadAllQuizzes() {
        List<Quiz> allQuizzes = new ArrayList<>();
        // 加载所有问题到一个 Map 中，键为 questionText
        Map<String, StudentQuestion> questionMap = loadAllQuestions();

        if (questionMap.isEmpty()) {
            System.err.println("没有找到任何问题。");
            return allQuizzes;
        }

        try (BufferedReader br = new BufferedReader(new FileReader(examFilePath))) {
            String line;

            while ((line = br.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty()) {
                    continue; // 跳过空行
                }

                // 使用正则表达式分割，只分割前7个逗号，剩下的作为问题部分
                String[] parts = line.split(",", 7);
                if (parts.length < 7) {
                    System.err.println("exam.txt 中的行格式不正确: " + line);
                    continue; // 跳过格式不正确的行
                }

                String currentExamType = parts[0].trim();
                String examDate = parts[1].trim(); // 未使用
                String currentCourseId = parts[2].trim();
                String isPublish = parts[3].trim(); // 未使用
                String courseName = parts[4].trim();
                int duration = 0;
                try {
                    duration = Integer.parseInt(parts[5].trim());
                } catch (NumberFormatException e) {
                    System.err.println("解析 duration 失败，默认设为0: " + parts[5].trim());
                }
                String questionsPart = parts[6].trim();

                // 分割问题列表
                String[] questionTexts = questionsPart.split("\\|");
                List<StudentQuestion> questions = new ArrayList<>();

                for (String qText : questionTexts) {
                    qText = qText.trim();
                    if (questionMap.containsKey(qText)) {
                        questions.add(questionMap.get(qText));
                    } else {
                        System.err.println("未在 questions.txt 中找到问题: " + qText);
                    }
                }

                Quiz quiz = new Quiz(currentCourseId, courseName, currentExamType, duration, questions);
                allQuizzes.add(quiz);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return allQuizzes;
    }

    /**
     * 读取 completed_quizzes.txt 文件中的所有完成测验记录
     *
     * @return 包含所有完成测验记录的列表，每条记录为一个字符串
     */
    public List<String> loadCompletedQuizzes() {
        List<String> completedQuizzes = new ArrayList<>();
        Path path = Paths.get("data/completed_quizzes.txt"); // 确保路径正确

        if (Files.exists(path)) {
            try (BufferedReader br = Files.newBufferedReader(path)) {
                String line;
                while ((line = br.readLine()) != null) {
                    if (!line.trim().isEmpty()) { // 跳过空行
                        completedQuizzes.add(line.trim());
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
                // 您可以选择抛出异常或返回一个空列表，并在调用处处理
            }
        } else {
            System.out.println("completed_quizzes.txt 文件不存在。");
        }

        return completedQuizzes;
    }

    /**
     * 根据用户名读取该用户的所有完成测验记录
     *
     * @param username 用户名
     * @return 该用户的完成测验记录列表
     */
    public List<String> loadCompletedQuizzesByUsername(String username) {
        List<String> allCompletedQuizzes = loadCompletedQuizzes();
        List<String> userCompletedQuizzes = new ArrayList<>();

        for (String record : allCompletedQuizzes) {
            String[] parts = record.split(",");
            if (parts.length >= 6 && parts[0].equalsIgnoreCase(username)) {
                userCompletedQuizzes.add(record);
            }
        }

        return userCompletedQuizzes;
    }
}
