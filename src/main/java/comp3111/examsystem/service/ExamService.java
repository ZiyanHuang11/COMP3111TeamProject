package comp3111.examsystem.service;

import comp3111.examsystem.entity.Exam;
import comp3111.examsystem.entity.Question;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ExamService {
    private String examFilePath;
    private String questionFilePath;

    public ExamService() {
        this.examFilePath = "data/exam.txt";
        this.questionFilePath = "data/question.txt";
    }

    public ObservableList<Exam> loadExams() {
        ObservableList<Exam> exams = FXCollections.observableArrayList();
        try (BufferedReader br = new BufferedReader(new FileReader(examFilePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] fields = line.split(",");
                if (fields.length >= 7) {
                    String examName = fields[0].trim();
                    String examDate = fields[1].trim();
                    String courseID = fields[2].trim();
                    String courseName = fields[3].trim();
                    String publish = fields[4].trim();
                    String[] questionIDs = fields[5].split("\\|");
                    int duration = Integer.parseInt(fields[6].trim());

                    Exam exam = new Exam(examName, courseID, examDate, publish);
                    exam.setCourseName(courseName);
                    exam.setDuration(duration);

                    ObservableList<Question> questions = loadQuestions(questionIDs); // 修复返回类型
                    exam.setQuestions(questions);

                    exams.add(exam);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return exams;
    }

    private ObservableList<Question> loadQuestions(String[] questionIDs) {
        List<Question> questionList = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(questionFilePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] fields = line.split(",");
                if (fields.length >= 9) {
                    String questionID = fields[0].trim();
                    for (String id : questionIDs) {
                        if (questionID.equals(id.trim())) {
                            Question question = new Question(
                                    fields[1], fields[2], fields[3], fields[4], fields[5],
                                    fields[6], fields[7], Integer.parseInt(fields[8])
                            );
                            questionList.add(question);
                            break;
                        }
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return FXCollections.observableArrayList(questionList); // 返回 ObservableList
    }
}



