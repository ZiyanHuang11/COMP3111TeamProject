package comp3111.examsystem.service;

import comp3111.examsystem.data.DataManager;
import comp3111.examsystem.entity.Exam;

import java.util.List;
import java.util.stream.Collectors;

public class StudentMainService {
    private final DataManager dataManager;

    public StudentMainService(DataManager dataManager) {
        this.dataManager = dataManager;
    }

    /**
     * 获取所有考试的显示文本列表
     *
     * @return 包含所有考试的显示文本的列表
     */
    public List<String> getExamDisplayTexts() {
        return dataManager.getExams().stream()
                .map(exam -> exam.getCourseID() + " " + exam.getCourseName() + " | " + exam.getExamName())
                .collect(Collectors.toList());
    }

    /**
     * 根据显示文本获取对应的考试
     *
     * @param displayText 考试的显示文本
     * @return 对应的 Exam 对象，如果未找到则返回 null
     */
    public Exam getExamByDisplayText(String displayText) {
        return dataManager.getExams().stream()
                .filter(exam -> (exam.getCourseID() + " " + exam.getCourseName() + " | " + exam.getExamName())
                        .equals(displayText))
                .findFirst()
                .orElse(null);
    }
}

