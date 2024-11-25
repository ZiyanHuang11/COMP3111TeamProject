package comp3111.examsystem.service;

import comp3111.examsystem.entity.ExamResult;
import comp3111.examsystem.data.DataManager;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.chart.XYChart;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GradeStatisticsService {
    private final DataManager dataManager;
    private final Map<String, String> courseIDToNameMap;

    public GradeStatisticsService() {
        this.dataManager = new DataManager();
        this.courseIDToNameMap = new HashMap<>();
        loadCourseData();
    }

    /**
     * 加载课程数据到 courseIDToNameMap
     */
    private void loadCourseData() {
        List<Map<String, String>> courses = dataManager.loadCourseData();
        for (Map<String, String> course : courses) {
            String courseID = course.get("courseID");
            String courseName = course.get("courseName");
            courseIDToNameMap.put(courseID, courseName);
        }
    }

    /**
     * 加载 exam_summary.txt 的数据并封装为 ObservableList
     */
    public ObservableList<ExamResult> loadExamSummaryResults() {
        List<Map<String, String>> examSummaryData = dataManager.loadExamSummaryData();
        ObservableList<ExamResult> results = FXCollections.observableArrayList();

        for (Map<String, String> data : examSummaryData) {
            ExamResult result = new ExamResult(
                    data.get("id"),
                    data.get("studentID"),
                    data.get("examName"),
                    data.get("courseID"),
                    courseIDToNameMap.getOrDefault(data.get("courseID"), "Unknown Course"),
                    Integer.parseInt(data.get("time")),
                    Integer.parseInt(data.get("score")),
                    Integer.parseInt(data.get("fullScore"))
            );
            results.add(result);
        }
        return results;
    }

    /**
     * 获取下拉框的课程列表 (格式为: CourseID + CourseName)
     */
    public ObservableList<String> getCourseList(ObservableList<ExamResult> results) {
        ObservableList<String> courseList = FXCollections.observableArrayList();
        for (ExamResult result : results) {
            String courseDisplay = result.getCourseID() + " " + result.getCourseName();
            if (!courseList.contains(courseDisplay)) {
                courseList.add(courseDisplay);
            }
        }
        return courseList;
    }

    /**
     * 根据课程名称过滤 ExamResult
     */
    public ObservableList<ExamResult> filterResultsByCourseName(ObservableList<ExamResult> results, String courseName) {
        ObservableList<ExamResult> filteredResults = FXCollections.observableArrayList();
        for (ExamResult result : results) {
            String courseDisplay = result.getCourseID() + " " + result.getCourseName();
            if (courseName.equals(courseDisplay)) {
                filteredResults.add(result);
            }
        }
        return filteredResults;
    }

    /**
     * 计算平均分数
     */
    public double calculateAverageScore(ObservableList<ExamResult> results) {
        if (results.isEmpty()) return 0.0;
        int totalScore = results.stream().mapToInt(ExamResult::getScore).sum();
        return (double) totalScore / results.size();
    }

    /**
     * 生成柱状图数据
     */
    public XYChart.Series<String, Number> generateBarChartSeries(ObservableList<ExamResult> results) {
        XYChart.Series<String, Number> series = new XYChart.Series<>();
        for (ExamResult result : results) {
            series.getData().add(new XYChart.Data<>(result.getExamName(), result.getScore()));
        }
        return series;
    }
}
