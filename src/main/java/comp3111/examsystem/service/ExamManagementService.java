package comp3111.examsystem.service;

import comp3111.examsystem.data.DataManager;
import comp3111.examsystem.entity.Exam;
import comp3111.examsystem.entity.Question;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.List;
import java.util.stream.Collectors;

public class ExamManagementService {
    private final DataManager dataManager;
    private final ObservableList<Exam> examList;
    private final ObservableList<Question> questionList;

    public ExamManagementService(DataManager dataManager) {
        this.dataManager = dataManager;

        // 从 DataManager 中加载考试和问题列表
        this.examList = FXCollections.observableArrayList(dataManager.getExams());
        this.questionList = FXCollections.observableArrayList(dataManager.getQuestions());
    }

    public ObservableList<Exam> getExamList() {
        return examList;
    }

    public ObservableList<Question> getQuestionList() {
        return questionList;
    }

    // 添加考试
    public boolean addExam(Exam newExam) {
        for (Exam exam : examList) {
            if (exam.getExamName().equalsIgnoreCase(newExam.getExamName())) {
                return false; // 已存在同名考试
            }
        }
        examList.add(newExam);
        dataManager.getExams().add(newExam); // 更新到 DataManager
        return true;
    }

    // 更新考试
    public boolean updateExam(Exam updatedExam, String originalExamName) {
        for (Exam exam : examList) {
            if (exam.getExamName().equalsIgnoreCase(originalExamName)) {
                // 检查是否存在重复的考试名称（排除当前考试）
                for (Exam otherExam : examList) {
                    if (otherExam != exam && otherExam.getExamName().equalsIgnoreCase(updatedExam.getExamName())) {
                        return false; // 存在重复名称
                    }
                }
                // 更新考试信息
                exam.setExamName(updatedExam.getExamName());
                exam.setExamTime(updatedExam.getExamTime());
                exam.setCourseID(updatedExam.getCourseID());
                exam.setPublish(updatedExam.getPublish());
                return true;
            }
        }
        return false; // 未找到指定考试
    }

    // 删除考试
    public boolean deleteExam(String examName) {
        Exam examToRemove = null;
        for (Exam exam : examList) {
            if (exam.getExamName().equalsIgnoreCase(examName)) {
                examToRemove = exam;
                break;
            }
        }
        if (examToRemove != null) {
            examList.remove(examToRemove);
            dataManager.getExams().remove(examToRemove); // 更新到 DataManager
            return true;
        }
        return false; // 未找到考试
    }

    // 添加问题到考试
    public boolean addQuestionToExam(Exam exam, Question question) {
        if (!exam.getQuestions().contains(question)) {
            exam.getQuestions().add(question);
            return true;
        }
        return false; // 问题已存在于考试中
    }

    // 从考试中移除问题
    public boolean removeQuestionFromExam(Exam exam, Question question) {
        if (exam.getQuestions().remove(question)) {
            return true;
        }
        return false; // 问题未找到
    }

    // 过滤考试
    public List<Exam> filterExams(String examName, String courseID, String publishStatus) {
        return examList.stream()
                .filter(exam -> (examName.isEmpty() || exam.getExamName().toLowerCase().contains(examName.toLowerCase())) &&
                        (courseID.equals("All") || exam.getCourseID().equals(courseID)) &&
                        (publishStatus.equals("All") || exam.getPublish().equals(publishStatus)))
                .collect(Collectors.toList());
    }

    // 过滤问题
    public List<Question> filterQuestions(String questionText, String type, String scoreText) {
        return questionList.stream()
                .filter(q -> (questionText.isEmpty() || q.getQuestion().toLowerCase().contains(questionText.toLowerCase())) &&
                        (type.equals("All") || q.getType().equals(type)) &&
                        (scoreText.isEmpty() || String.valueOf(q.getScore()).equals(scoreText)))
                .collect(Collectors.toList());
    }
}
