package comp3111.examsystem.service;

import comp3111.examsystem.entity.Exam;
import comp3111.examsystem.entity.Question;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.io.*;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ExamManagementService {
    private ObservableList<Exam> examList;
    private ObservableList<Question> questionList;

    private String examFilePath;
    private String questionFilePath;

    /**
     * 构造函数
     *
     * @param examFilePath     考试文件路径
     * @param questionFilePath 问题文件路径
     */
    public ExamManagementService(String examFilePath, String questionFilePath) {
        this.examFilePath = examFilePath;
        this.questionFilePath = questionFilePath;
        this.examList = FXCollections.observableArrayList();
        this.questionList = FXCollections.observableArrayList();
        loadQuestions();
        loadExams();
    }

    /**
     * 获取所有考试列表
     *
     * @return 考试的可观察列表
     */
    public ObservableList<Exam> getExamList() {
        return examList;
    }

    /**
     * 获取所有问题列表
     *
     * @return 问题的可观察列表
     */
    public ObservableList<Question> getQuestionList() {
        return questionList;
    }

    /**
     * 从文件加载问题
     */
    public void loadQuestions() {
        try (BufferedReader br = new BufferedReader(new FileReader(questionFilePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                // Expected format:
                // questionText,optionA,optionB,optionC,optionD,answers,type,score
                String[] parts = line.split(",");
                if (parts.length >= 8) {
                    String questionText = parts[0].trim();
                    String optionA = parts[1].trim();
                    String optionB = parts[2].trim();
                    String optionC = parts[3].trim();
                    String optionD = parts[4].trim();
                    String answersRaw = parts[5].trim(); // 使用标签如 A, B, etc.
                    String type = parts[6].trim();
                    int score;
                    try {
                        score = Integer.parseInt(parts[7].trim());
                    } catch (NumberFormatException e) {
                        System.err.println("Invalid score in questions.txt: " + parts[7].trim());
                        score = 0; // 默认分数
                    }

                    List<String> answers = new ArrayList<>();
                    if (type.equalsIgnoreCase("Multiple")) {
                        // 多选答案使用 '|' 分隔
                        String[] answerParts = answersRaw.split("\\|");
                        for (String ans : answerParts) {
                            ans = ans.trim().toUpperCase();
                            if (ans.matches("[A-D]")) { // 验证答案标签
                                answers.add(ans);
                            } else {
                                System.err.println("Invalid answer label in questions.txt: " + ans);
                            }
                        }
                    } else { // 单选答案
                        String ans = answersRaw.trim().toUpperCase();
                        if (ans.matches("[A-D]")) { // 验证答案标签
                            answers.add(ans);
                        } else {
                            System.err.println("Invalid answer label in questions.txt: " + ans);
                        }
                    }

                    Question question = new Question(
                            questionText,
                            optionA,
                            optionB,
                            optionC,
                            optionD,
                            String.join("|", answers), // 使用 '|' 分隔标签
                            type,
                            score
                    );
                    questionList.add(question);
                } else {
                    System.err.println("questions.txt line does not have enough fields: " + line);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            // 适当处理异常
        }
    }

    /**
     * 从文件加载考试
     */
    public void loadExams() {
        try (BufferedReader br = new BufferedReader(new FileReader(examFilePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                // Expected format:
                // examName, examTime, courseID, publish, courseName, duration, question1|question2|...
                String[] parts = line.split(",", 7);
                if (parts.length >= 7) {
                    String examName = parts[0].trim();
                    String examTime = parts[1].trim(); // 考试时间
                    String courseID = parts[2].trim();
                    String publishStatus = parts[3].trim();
                    String courseName = parts[4].trim();
                    int duration = 0;
                    try {
                        duration = Integer.parseInt(parts[5].trim());
                    } catch (NumberFormatException e) {
                        System.err.println("Invalid duration in exam.txt: " + parts[5].trim());
                    }
                    String questionsPart = parts[6].trim();

                    // 解析问题名称
                    String[] questionNames = questionsPart.split("\\|");
                    List<Question> examQuestions = new ArrayList<>();
                    for (String qName : questionNames) {
                        qName = qName.trim();
                        for (Question q : questionList) {
                            if (q.getQuestion().equalsIgnoreCase(qName)) {
                                examQuestions.add(q);
                                break;
                            }
                        }
                    }

                    Exam exam = new Exam(examName, courseID, examTime, publishStatus, courseName, duration, examQuestions);
                    examList.add(exam);
                } else {
                    System.err.println("exam.txt line does not have enough fields: " + line);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            // 适当处理异常
        }
    }

    /**
     * 将考试列表保存到文件
     *
     * @throws IOException 如果文件写入失败
     */
    public void saveExams() throws IOException {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(examFilePath))) {
            for (Exam exam : examList) {
                StringBuilder sb = new StringBuilder();
                sb.append(exam.getExamName()).append(",");
                sb.append(exam.getExamTime()).append(",");
                sb.append(exam.getCourseID()).append(",");
                sb.append(exam.getPublish()).append(",");
                sb.append(exam.getCourseName()).append(",");
                sb.append(exam.getDuration()).append(",");

                // 添加问题名称
                List<String> questionNames = new ArrayList<>();
                for (Question q : exam.getQuestions()) {
                    questionNames.add(q.getQuestion());
                }
                sb.append(String.join("|", questionNames));

                bw.write(sb.toString());
                bw.newLine();
            }
        }
    }

    /**
     * 添加新的考试
     *
     * @param newExam 新考试对象
     * @return 如果成功添加返回 true，否则返回 false（例如，重复的考试）
     * @throws IOException 如果保存考试到文件失败
     */
    public boolean addExam(Exam newExam) throws IOException {
        for (Exam exam : examList) {
            if (exam.getExamName().equalsIgnoreCase(newExam.getExamName())
                    && exam.getCourseID().equalsIgnoreCase(newExam.getCourseID())) {
                return false; // 已存在相同类型和课程ID的考试
            }
        }
        examList.add(newExam);
        saveExams();
        return true;
    }

    /**
     * 更新现有的考试
     *
     * @param updatedExam       更新后的考试对象
     * @param originalExamName   原始考试名称
     * @param originalCourseID   原始课程ID
     * @return 如果成功更新返回 true，否则返回 false（例如，重复的考试）
     * @throws IOException 如果保存考试到文件失败
     */
    public boolean updateExam(Exam updatedExam, String originalExamName, String originalCourseID) throws IOException {
        for (Exam exam : examList) {
            if (exam.getExamName().equalsIgnoreCase(originalExamName)
                    && exam.getCourseID().equalsIgnoreCase(originalCourseID)) {
                // 检查是否存在重复的类型和课程ID（不包括当前考试）
                for (Exam otherExam : examList) {
                    if (otherExam != exam
                            && otherExam.getExamName().equalsIgnoreCase(updatedExam.getExamName())
                            && otherExam.getCourseID().equalsIgnoreCase(updatedExam.getCourseID())) {
                        return false; // 存在重复
                    }
                }
                // 更新字段
                exam.setExamName(updatedExam.getExamName());
                exam.setExamTime(updatedExam.getExamTime());
                exam.setCourseID(updatedExam.getCourseID());
                exam.setPublish(updatedExam.getPublish());
                exam.setCourseName(updatedExam.getCourseName());
                exam.setDuration(updatedExam.getDuration());
                exam.setQuestions(updatedExam.getQuestions());
                saveExams();
                return true;
            }
        }
        return false; // 未找到考试
    }

    /**
     * 删除考试
     *
     * @param examName 考试名称
     * @param courseID 课程ID
     * @return 如果成功删除返回 true，否则返回 false
     * @throws IOException 如果保存考试到文件失败
     */
    public boolean deleteExam(String examName, String courseID) throws IOException {
        Exam examToRemove = null;
        for (Exam exam : examList) {
            if (exam.getExamName().equalsIgnoreCase(examName)
                    && exam.getCourseID().equalsIgnoreCase(courseID)) {
                examToRemove = exam;
                break;
            }
        }
        if (examToRemove != null) {
            examList.remove(examToRemove);
            saveExams();
            return true;
        }
        return false; // 未找到考试
    }

    /**
     * 向考试中添加问题
     *
     * @param exam     要添加问题的考试
     * @param question 要添加的问题
     * @return 如果成功添加返回 true，否则返回 false（例如，问题已存在）
     * @throws IOException 如果保存考试到文件失败
     */
    public boolean addQuestionToExam(Exam exam, Question question) throws IOException {
        if (!exam.getQuestions().contains(question)) {
            exam.addQuestion(question);
            saveExams();
            return true;
        }
        return false; // 问题已存在于考试中
    }

    /**
     * 从考试中移除问题
     *
     * @param exam     要移除问题的考试
     * @param question 要移除的问题
     * @return 如果成功移除返回 true，否则返回 false（例如，问题未找到）
     * @throws IOException 如果保存考试到文件失败
     */
    public boolean removeQuestionFromExam(Exam exam, Question question) throws IOException {
        if (exam.getQuestions().remove(question)) {
            saveExams();
            return true;
        }
        return false; // 问题未找到
    }

    /**
     * 根据过滤条件过滤考试
     *
     * @param examName      考试名称过滤
     * @param courseID      课程ID过滤
     * @param publishStatus 发布状态过滤
     * @return 过滤后的考试列表
     */
    public List<Exam> filterExams(String examName, String courseID, String publishStatus) {
        return examList.stream()
                .filter(exam -> (examName.isEmpty() || exam.getExamName().toLowerCase().contains(examName.toLowerCase()))
                        && (courseID.equalsIgnoreCase("All") || exam.getCourseID().equalsIgnoreCase(courseID))
                        && (publishStatus.equalsIgnoreCase("All") || exam.getPublish().equalsIgnoreCase(publishStatus)))
                .collect(Collectors.toList());
    }

    /**
     * 根据过滤条件过滤问题
     *
     * @param questionText 问题文本过滤
     * @param typeFilter   问题类型过滤
     * @param scoreFilter  分数过滤
     * @return 过滤后的问题列表
     */
    public List<Question> filterQuestions(String questionText, String typeFilter, String scoreFilter) {
        return questionList.stream()
                .filter(q -> (questionText.isEmpty() || q.getQuestion().toLowerCase().contains(questionText.toLowerCase()))
                        && (typeFilter.equalsIgnoreCase("All") || q.getType().equalsIgnoreCase(typeFilter))
                        && (scoreFilter.isEmpty() || String.valueOf(q.getScore()).equals(scoreFilter)))
                .collect(Collectors.toList());
    }
}
