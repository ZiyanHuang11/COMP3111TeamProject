package comp3111.examsystem.controller;

import comp3111.examsystem.entity.ExamResult;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class GradeStatisticsController {

    @FXML
    private TableView<ExamResult> gradeTable;

    @FXML
    private TableColumn<ExamResult, String> courseIDColumn; // 显示课程代码
    @FXML
    private TableColumn<ExamResult, String> examNameColumn; // 显示考试名称
    @FXML
    private TableColumn<ExamResult, Integer> totalScoreColumn; // 显示总分
    @FXML
    private TableColumn<ExamResult, Integer> scoreColumn; // 显示得分
    @FXML
    private TableColumn<ExamResult, String> passColumn; // 显示是否通过

    @FXML
    private Label averageScoreLabel;

    @FXML
    private Label passRateLabel;

    private ObservableList<ExamResult> examResults;

    @FXML
    public void initialize() {
        // 初始化表格列
        courseIDColumn.setCellValueFactory(new PropertyValueFactory<>("courseID"));
        examNameColumn.setCellValueFactory(new PropertyValueFactory<>("examName"));
        totalScoreColumn.setCellValueFactory(new PropertyValueFactory<>("totalScore"));
        scoreColumn.setCellValueFactory(new PropertyValueFactory<>("score"));
        passColumn.setCellValueFactory(new PropertyValueFactory<>("passStatus"));

        // 加载数据
        examResults = loadExamResults();
        gradeTable.setItems(examResults);

        // 更新统计信息
        updateStatistics();
    }

    private ObservableList<ExamResult> loadExamResults() {
        ObservableList<ExamResult> results = FXCollections.observableArrayList();
        try (BufferedReader br = new BufferedReader(new FileReader("data/exam_results.txt"))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] fields = line.split(",");
                if (fields.length >= 5) {
                    String courseID = fields[0].trim();
                    String examName = fields[1].trim();
                    int totalScore = Integer.parseInt(fields[2].trim());
                    int score = Integer.parseInt(fields[3].trim());
                    String passStatus = fields[4].trim();

                    results.add(new ExamResult(courseID, examName, totalScore, score, passStatus));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return results;
    }

    private void updateStatistics() {
        if (examResults.isEmpty()) {
            averageScoreLabel.setText("Average Score: N/A");
            passRateLabel.setText("Pass Rate: N/A");
            return;
        }

        int totalScore = 0;
        int passCount = 0;
        for (ExamResult result : examResults) {
            totalScore += result.getScore();
            if ("Yes".equalsIgnoreCase(result.getPassStatus())) {
                passCount++;
            }
        }

        double averageScore = (double) totalScore / examResults.size();
        double passRate = (double) passCount / examResults.size() * 100;

        averageScoreLabel.setText(String.format("Average Score: %.2f", averageScore));
        passRateLabel.setText(String.format("Pass Rate: %.2f%%", passRate));
    }
}
