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

        // Load exams and questions from DataManager
        this.examList = FXCollections.observableArrayList(dataManager.getExams());
        this.questionList = FXCollections.observableArrayList(dataManager.getQuestions());
    }

    public ObservableList<Exam> getExamList() {
        return examList;
    }

    public ObservableList<Question> getQuestionList() {
        return questionList;
    }

    // Get unique course IDs from the exam list
    public List<String> getCourseIDs() {
        return examList.stream()
                .map(Exam::getCourseID)
                .distinct()
                .sorted()
                .collect(Collectors.toList());
    }

    // Add a new exam
    public boolean addExam(Exam newExam) {
        for (Exam exam : examList) {
            if (exam.getExamName().equalsIgnoreCase(newExam.getExamName())) {
                return false; // Exam with the same name already exists
            }
        }
        examList.add(newExam);
        dataManager.addExam(newExam); // Update DataManager
        return true;
    }

    // Update an existing exam
    public boolean updateExam(Exam updatedExam, String originalExamName) {
        for (Exam exam : examList) {
            if (exam.getExamName().equalsIgnoreCase(originalExamName)) {
                // Check for duplicate exam names (excluding the current exam)
                for (Exam otherExam : examList) {
                    if (otherExam != exam && otherExam.getExamName().equalsIgnoreCase(updatedExam.getExamName())) {
                        return false; // Duplicate name exists
                    }
                }
                // Update exam information
                exam.setExamName(updatedExam.getExamName());
                exam.setExamTime(updatedExam.getExamTime());
                exam.setCourseID(updatedExam.getCourseID());
                exam.setPublish(updatedExam.getPublish());
                dataManager.updateExam(exam.getId(), exam); // Persist changes
                return true;
            }
        }
        return false; // Exam not found
    }

    // Delete an exam
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
            dataManager.deleteExam(examToRemove.getId()); // Update DataManager
            return true;
        }
        return false; // Exam not found
    }

    // Add a question to an exam
    public boolean addQuestionToExam(Exam exam, Question question) {
        if (!exam.getQuestionIds().contains(question.getId())) {
            exam.addQuestionId(question.getId()); // Add question ID to the exam
            dataManager.updateExam(exam.getId(), exam); // Persist changes
            return true;
        }
        return false; // Question already exists in the exam
    }

    // Remove a question from an exam
    public boolean removeQuestionFromExam(Exam exam, Question question) {
        if (exam.getQuestionIds().contains(question.getId())) {
            return exam.removeQuestionId(question.getId());
        }
        return false; // 如果问题 ID 不在考试中，返回 false
    }

    // Filter exams
    public List<Exam> filterExams(String examName, String courseID, String publishStatus) {
        return examList.stream()
                .filter(exam -> (examName.isEmpty() || exam.getExamName().toLowerCase().contains(examName.toLowerCase())) &&
                        (courseID.equals("All") || exam.getCourseID().equals(courseID)) &&
                        (publishStatus.equals("All") || exam.getPublish().equals(publishStatus)))
                .collect(Collectors.toList());
    }

    // Filter questions
    public List<Question> filterQuestions(String questionText, String type, String scoreText) {
        return questionList.stream()
                .filter(q -> (questionText.isEmpty() || q.getQuestion().toLowerCase().contains(questionText.toLowerCase())) &&
                        (type.equals("All") || q.getType().equals(type)) &&
                        (scoreText.isEmpty() || String.valueOf(q.getScore()).equals(scoreText)))
                .collect(Collectors.toList());
    }
}
