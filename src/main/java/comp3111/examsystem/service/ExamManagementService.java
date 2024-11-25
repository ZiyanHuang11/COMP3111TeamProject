package comp3111.examsystem.service;

import comp3111.examsystem.entity.Exam;
import comp3111.examsystem.entity.Question;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Service class for managing exams and their associated questions in the examination system.
 */
public class ExamManagementService {
    private ObservableList<Exam> examList;
    private ObservableList<Question> questionList;

    private String examFilePath;
    private String questionFilePath;

    /**
     * Constructs an ExamManagementService with the specified file paths for exams and questions.
     *
     * @param examFilePath    the file path for storing exam data
     * @param questionFilePath the file path for storing question data
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
     * Returns the list of exams.
     *
     * @return an observable list of exams
     */
    public ObservableList<Exam> getExamList() {
        return examList;
    }

    /**
     * Returns the list of questions.
     *
     * @return an observable list of questions
     */
    public ObservableList<Question> getQuestionList() {
        return questionList;
    }

    /**
     * Loads questions from a file into the question list.
     */
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

    /**
     * Loads exams from a file into the exam list.
     */
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

    /**
     * Saves the current list of exams to the exam file.
     *
     * @throws IOException if an error occurs while writing to the file
     */
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

    /**
     * Adds a new exam to the exam list and saves it to the file.
     *
     * @param newExam the new exam to be added
     * @return true if the exam was added successfully, false if an exam with the same name exists
     * @throws IOException if an error occurs while saving to the file
     */
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

    /**
     * Updates an existing exam in the exam list and saves the changes to the file.
     *
     * @param updatedExam     the exam with updated details
     * @param originalExamName the original name of the exam to be updated
     * @return true if the exam was updated successfully, false if the exam was not found or a duplicate name exists
     * @throws IOException if an error occurs while saving to the file
     */
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

    /**
     * Deletes an exam from the exam list and saves the changes to the file.
     *
     * @param examName the name of the exam to be deleted
     * @return true if the exam was deleted successfully, false if the exam was not found
     * @throws IOException if an error occurs while saving to the file
     */
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

    /**
     * Adds a question to a specified exam and saves the changes to the file.
     *
     * @param exam    the exam to which the question will be added
     * @param question the question to be added
     * @return true if the question was added successfully, false if the question already exists in the exam
     * @throws IOException if an error occurs while saving to the file
     */
    public boolean addQuestionToExam(Exam exam, Question question) throws IOException {
        if (!exam.getQuestions().contains(question)) {
            exam.getQuestions().add(question);
            saveExams();
            return true;
        }
        return false; // Question already exists in the exam
    }

    /**
     * Removes a question from a specified exam and saves the changes to the file.
     *
     * @param exam    the exam from which the question will be removed
     * @param question the question to be removed
     * @return true if the question was removed successfully, false if the question was not found in the exam
     * @throws IOException if an error occurs while saving to the file
     */
    public boolean removeQuestionFromExam(Exam exam, Question question) throws IOException {
        if (exam.getQuestions().remove(question)) {
            saveExams();
            return true;
        }
        return false; // Question not found in the exam
    }

    /**
     * Filters exams based on the specified criteria.
     *
     * @param examName     the name of the exam to filter by
     * @param courseID     the course ID to filter by
     * @param publishStatus the publish status to filter by
     * @return a list of exams that match the specified criteria
     */
    public List<Exam> filterExams(String examName, String courseID, String publishStatus) {
        return examList.stream()
                .filter(exam -> (examName.isEmpty() || exam.getExamName().toLowerCase().contains(examName.toLowerCase())) &&
                        (courseID.equals("All") || exam.getCourseID().equals(courseID)) &&
                        (publishStatus.equals("All") || exam.getPublish().equals(publishStatus)))
                .collect(Collectors.toList());
    }

    /**
     * Filters questions based on the specified criteria.
     *
     * @param questionText the text of the question to filter by (can be empty to include all)
     * @param type         the type of the question to filter by (e.g., "Single", "Multiple", or "All" to include all types)
     * @param scoreText    the score of the question to filter by (can be empty to include all)
     * @return a list of questions that match the specified criteria
     */
    public List<Question> filterQuestions(String questionText, String type, String scoreText) {
        return questionList.stream()
                .filter(q -> (questionText.isEmpty() || q.getQuestion().toLowerCase().contains(questionText.toLowerCase())) &&
                        (type.equals("All") || q.getType().equals(type)) &&
                        (scoreText.isEmpty() || String.valueOf(q.getScore()).equals(scoreText)))
                .collect(Collectors.toList());
    }
}