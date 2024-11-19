package comp3111.examsystem.service;

import comp3111.examsystem.entity.Exam;
import comp3111.examsystem.entity.Question;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ExamManagementService {
    private ObservableList<Exam> examList;
    private ObservableList<Question> questionList;

    private String examFilePath;
    private String questionFilePath;

    public ExamManagementService(String examFilePath, String questionFilePath) {
        this.examFilePath = examFilePath;
        this.questionFilePath = questionFilePath;
        this.examList = FXCollections.observableArrayList();
        this.questionList = FXCollections.observableArrayList();
        loadQuestions();
        loadExams();
    }

    public ObservableList<Exam> getExamList() {
        return examList;
    }

    public ObservableList<Question> getQuestionList() {
        return questionList;
    }

    // Load questions from file
    public void loadQuestions() {
        try (BufferedReader br = new BufferedReader(new FileReader(questionFilePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length >= 8) {
                    String questionText = parts[0];
                    String optionA = parts[1];
                    String optionB = parts[2];
                    String optionC = parts[3];
                    String optionD = parts[4];
                    String answer = parts[5];
                    String type = parts[6];
                    int score = Integer.parseInt(parts[7]);

                    Question question = new Question(
                            questionText,
                            optionA,
                            optionB,
                            optionC,
                            optionD,
                            answer,
                            type,
                            score
                    );
                    questionList.add(question);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            // Handle exception appropriately
        }
    }

    // Load exams from file
    public void loadExams() {
        try (BufferedReader br = new BufferedReader(new FileReader(examFilePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",", 5);
                if (parts.length >= 5) {
                    String examName = parts[0];
                    String examTime = parts[1];
                    String courseID = parts[2];
                    String publishStatus = parts[3];
                    String questionNames = parts[4];

                    Exam exam = new Exam(examName, courseID, examTime, publishStatus);

                    // Parse question names
                    String[] questionArray = questionNames.split("\\|");
                    for (String qName : questionArray) {
                        for (Question q : questionList) {
                            if (q.getQuestion().equals(qName)) {
                                exam.getQuestions().add(q);
                                break;
                            }
                        }
                    }

                    examList.add(exam);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            // Handle exception appropriately
        }
    }

    // Save exams to file
    public void saveExams() throws IOException {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(examFilePath))) {
            for (Exam exam : examList) {
                StringBuilder sb = new StringBuilder();
                sb.append(exam.getExamName()).append(",");
                sb.append(exam.getExamTime()).append(",");
                sb.append(exam.getCourseID()).append(",");
                sb.append(exam.getPublish()).append(",");

                // Add question names
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

    // Add a new exam
    public boolean addExam(Exam newExam) throws IOException {
        for (Exam exam : examList) {
            if (exam.getExamName().equalsIgnoreCase(newExam.getExamName())) {
                return false; // Exam with same name exists
            }
        }
        examList.add(newExam);
        saveExams();
        return true;
    }

    // Update an existing exam
    public boolean updateExam(Exam updatedExam, String originalExamName) throws IOException {
        for (Exam exam : examList) {
            if (exam.getExamName().equalsIgnoreCase(originalExamName)) {
                // Check for duplicate name (excluding the exam being updated)
                for (Exam otherExam : examList) {
                    if (otherExam != exam && otherExam.getExamName().equalsIgnoreCase(updatedExam.getExamName())) {
                        return false; // Duplicate name exists
                    }
                }
                exam.setExamName(updatedExam.getExamName());
                exam.setExamTime(updatedExam.getExamTime());
                exam.setCourseID(updatedExam.getCourseID());
                exam.setPublish(updatedExam.getPublish());
                saveExams();
                return true;
            }
        }
        return false; // Exam not found
    }

    // Delete an exam
    public boolean deleteExam(String examName) throws IOException {
        Exam examToRemove = null;
        for (Exam exam : examList) {
            if (exam.getExamName().equalsIgnoreCase(examName)) {
                examToRemove = exam;
                break;
            }
        }
        if (examToRemove != null) {
            examList.remove(examToRemove);
            saveExams();
            return true;
        }
        return false; // Exam not found
    }

    // Add a question to an exam
    public boolean addQuestionToExam(Exam exam, Question question) throws IOException {
        if (!exam.getQuestions().contains(question)) {
            exam.getQuestions().add(question);
            saveExams();
            return true;
        }
        return false; // Question already exists in the exam
    }

    // Remove a question from an exam
    public boolean removeQuestionFromExam(Exam exam, Question question) throws IOException {
        if (exam.getQuestions().remove(question)) {
            saveExams();
            return true;
        }
        return false; // Question not found in the exam
    }

    // Filter exams based on criteria
    public List<Exam> filterExams(String examName, String courseID, String publishStatus) {
        return examList.stream()
                .filter(exam -> (examName.isEmpty() || exam.getExamName().toLowerCase().contains(examName.toLowerCase())) &&
                        (courseID.equals("All") || exam.getCourseID().equals(courseID)) &&
                        (publishStatus.equals("All") || exam.getPublish().equals(publishStatus)))
                .collect(Collectors.toList());
    }

    // Filter questions based on criteria
    public List<Question> filterQuestions(String questionText, String type, String scoreText) {
        return questionList.stream()
                .filter(q -> (questionText.isEmpty() || q.getQuestion().toLowerCase().contains(questionText.toLowerCase())) &&
                        (type.equals("All") || q.getType().equals(type)) &&
                        (scoreText.isEmpty() || String.valueOf(q.getScore()).equals(scoreText)))
                .collect(Collectors.toList());
    }
}
