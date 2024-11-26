package comp3111.examsystem.controller;

import comp3111.examsystem.entity.GradeRecord;
import comp3111.examsystem.service.GradeStatisticsService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.util.List;

public class GradeStatisticsController {
    @FXML
    private TableView<GradeRecord> gradeTable;

    @FXML
    private TableColumn<GradeRecord, String> courseColumn;

    @FXML
    private TableColumn<GradeRecord, String> examColumn;

    @FXML
    private TableColumn<GradeRecord, Integer> scoreColumn;

    @FXML
    private TableColumn<GradeRecord, Integer> fullScoreColumn;

    @FXML
    private TableColumn<GradeRecord, Integer> timeColumn;

    @FXML
    private ComboBox<String> courseComboBox;

    @FXML
    private BarChart<String, Number> gradeBarChart;

    @FXML
    private CategoryAxis xAxis;

    private GradeStatisticsService gradeStatisticsService;
    private ObservableList<GradeRecord> masterData = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        gradeStatisticsService = new GradeStatisticsService("data/completed_quizzes.txt");

        // Initialize table columns
        courseColumn.setCellValueFactory(new PropertyValueFactory<>("course"));
        examColumn.setCellValueFactory(new PropertyValueFactory<>("exam"));
        scoreColumn.setCellValueFactory(new PropertyValueFactory<>("score"));
        fullScoreColumn.setCellValueFactory(new PropertyValueFactory<>("fullScore"));
        timeColumn.setCellValueFactory(new PropertyValueFactory<>("time"));

        // Load all grades
        loadGradeData();

        // Populate ComboBox for course filtering
        populateCourseComboBox();

        // Initialize bar chart
        updateBarChart(masterData);
    }

    /**
     * Load all grade data into the table.
     */
    private void loadGradeData() {
        List<GradeRecord> records = gradeStatisticsService.getAllGradeRecords();
        masterData.addAll(records);
        gradeTable.setItems(masterData);
    }

    /**
     * Populate course ComboBox with unique courses from data.
     */
    private void populateCourseComboBox() {
        List<String> courses = gradeStatisticsService.getAllCourses();
        courses.add(0, "All Courses");
        courseComboBox.setItems(FXCollections.observableArrayList(courses));
        courseComboBox.getSelectionModel().selectFirst();
    }

    /**
     * Handle the "Filter" button action.
     * Filter data based on the selected course.
     */
    @FXML
    private void handleFilter() {
        String selectedCourse = courseComboBox.getSelectionModel().getSelectedItem();
        if (selectedCourse == null || selectedCourse.equals("All Courses")) {
            gradeTable.setItems(masterData);
            updateBarChart(masterData);
        } else {
            List<GradeRecord> filteredRecords = gradeStatisticsService.getGradeRecordsByCourse(selectedCourse);
            ObservableList<GradeRecord> filteredData = FXCollections.observableArrayList(filteredRecords);
            gradeTable.setItems(filteredData);
            updateBarChart(filteredData);
        }
    }

    /**
     * Handle the "Reset" button action.
     * Clear all filters and reset to original data.
     */
    @FXML
    private void handleReset() {
        courseComboBox.getSelectionModel().selectFirst();
        gradeTable.setItems(masterData);
        updateBarChart(masterData);
    }

    /**
     * Handle the "Refresh" button action.
     * Reload data from the file and update the table and chart.
     */
    @FXML
    private void handleRefresh() {
        masterData.clear();
        loadGradeData();
        populateCourseComboBox();
        updateBarChart(masterData);
    }

    /**
     * Update the bar chart with the provided data.
     *
     * @param data The data to display in the bar chart.
     */
    private void updateBarChart(ObservableList<GradeRecord> data) {
        gradeBarChart.getData().clear();

        // Clear x-axis to ensure labels are centered correctly
        xAxis.getCategories().clear();

        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.setName("Scores");

        for (GradeRecord record : data) {
            // Use "CourseID-QuizName" as the x-axis label
            String xAxisLabel = record.getCourse() + "-" + record.getExam();
            xAxis.getCategories().add(xAxisLabel); // Ensure x-axis is updated with new labels
            series.getData().add(new XYChart.Data<>(xAxisLabel, record.getScore()));
        }

        gradeBarChart.getData().add(series);
    }
}
