package comp3111.examsystem.service;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * StudentMainService 负责管理和提供学生相关的考试信息。
 */
public class StudentMainService {
    private final String studentExamsFilePath;
    private final String completedQuizzesFilePath;

    /**
     * 构造函数，初始化考试文件路径。
     *
     * @param studentExamsFilePath    学生考试文件的路径
     * @param completedQuizzesFilePath 已完成考试文件的路径
     */
    public StudentMainService(String studentExamsFilePath, String completedQuizzesFilePath) {
        this.studentExamsFilePath = studentExamsFilePath;
        this.completedQuizzesFilePath = completedQuizzesFilePath;
    }

    /**
     * 获取指定学生的考试列表，并标记已完成的考试。
     *
     * @param username 登录用户名
     * @return 考试信息列表，格式为 "CourseId CourseName | ExamType (Completed)" 或 "CourseId CourseName | ExamType"
     */
    public List<String> getExamsForStudent(String username) {
        List<String> exams = new ArrayList<>();
        Set<String> completedExamsSet = new HashSet<>(getCompletedExams(username));

        // 检查文件是否存在
        Path path = Paths.get(studentExamsFilePath);
        if (!Files.exists(path)) {
            System.err.println("Error: File not found - " + studentExamsFilePath);
            return exams; // 返回空列表
        }

        try (BufferedReader br = new BufferedReader(new FileReader(studentExamsFilePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                // 跳过空行
                if (line.trim().isEmpty()) {
                    continue;
                }

                // 文件格式：username,courseID,examType,courseName
                String[] parts = splitCSVLine(line);

                if (parts.length < 4) {
                    System.err.println("Warning: Invalid line format - " + line);
                    continue; // 跳过格式不正确的行
                }

                String fileUsername = parts[0].trim();
                if (!fileUsername.equalsIgnoreCase(username)) {
                    continue; // 不是目标用户，跳过
                }

                String courseID = parts[1].trim();
                String examType = parts[2].trim();
                String courseName = parts[3].trim();

                if (courseID.isEmpty() || examType.isEmpty() || courseName.isEmpty()) {
                    System.err.println("Warning: Incomplete exam information - " + line);
                    continue; // 跳过不完整的信息
                }

                // 构建唯一标识符，用于检查是否已完成
                String identifier = courseID + "," + examType;

                // 格式化为：courseID courseName | examType
                String formattedExam = String.format("%s %s | %s", courseID, courseName, examType);
                if (completedExamsSet.contains(identifier)) {
                    formattedExam += " (Completed)";
                }
                exams.add(formattedExam);
            }
        } catch (IOException e) {
            System.err.println("Error reading file: " + studentExamsFilePath);
            e.printStackTrace();
        }

        return exams;
    }

    /**
     * 获取指定学生已完成的考试列表。
     *
     * @param username 登录用户名
     * @return 已完成的考试列表，格式为 "courseId,examType"
     */
    public List<String> getCompletedExams(String username) {
        List<String> completedExams = new ArrayList<>();

        // 检查文件是否存在
        Path path = Paths.get(completedQuizzesFilePath);
        if (!Files.exists(path)) {
            System.err.println("Error: File not found - " + completedQuizzesFilePath);
            return completedExams; // 返回空列表
        }

        try (BufferedReader br = new BufferedReader(new FileReader(completedQuizzesFilePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                // 跳过空行
                if (line.trim().isEmpty()) {
                    continue;
                }

                // 文件格式：username,courseId,examType,score,fullScore,time
                String[] parts = line.split(",");

                if (parts.length < 3) {
                    System.err.println("Warning: Invalid line format - " + line);
                    continue; // 跳过格式不正确的行
                }

                String fileUsername = parts[0].trim();
                if (!fileUsername.equalsIgnoreCase(username)) {
                    continue; // 不是目标用户，跳过
                }

                String courseId = parts[1].trim();
                String examType = parts[2].trim();

                completedExams.add(courseId + "," + examType);
            }
        } catch (IOException e) {
            System.err.println("Error reading file: " + completedQuizzesFilePath);
            e.printStackTrace();
        }

        return completedExams;
    }

    /**
     * 分割 CSV 行，处理可能包含逗号的字段。
     *
     * @param line 待分割的 CSV 行
     * @return 分割后的字段数组
     */
    private String[] splitCSVLine(String line) {
        List<String> tokens = new ArrayList<>();
        StringBuilder currentToken = new StringBuilder();
        boolean inQuotes = false;

        for (char ch : line.toCharArray()) {
            if (ch == '\"') {
                inQuotes = !inQuotes; // 切换引号状态
            } else if (ch == ',' && !inQuotes) {
                tokens.add(currentToken.toString());
                currentToken.setLength(0); // 清空 StringBuilder
            } else {
                currentToken.append(ch);
            }
        }

        // 添加最后一个字段
        tokens.add(currentToken.toString());

        return tokens.toArray(new String[0]);
    }
}
