package comp3111.examsystem.controller;

import comp3111.examsystem.service.TeacherGradeStatisticService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.*;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

/**
 * Controller for managing the grade statistics interface for teachers.
 */
public class TeacherGradeStatisticController implements Initializable {

    /**
     * Represents a student's grade information.
     */
    public static class Grade {
        private String studentName;
        private String courseNum;
        private String examName;
        private String score;
        private String fullScore;
        private String timeSpend;

        /**
         * Constructs a Grade object with the specified details.
         *
         * @param studentName the name of the student
         * @param courseNum   the course number
         * @param examName    the exam name
         * @param score       the score achieved
         * @param fullScore   the full score of the exam
         * @param timeSpend   the time spent on the exam
         */
        public Grade(String studentName, String courseNum, String examName, String score, String fullScore, String timeSpend) {
            this.studentName = studentName;
            this.courseNum = courseNum;
            this.examName = examName;
            this.score = score;
            this.fullScore = fullScore;
            this.timeSpend = timeSpend;
        }

        public String getStudentName() {
            return studentName;
        }

        public String getCourseNum() {
            return courseNum;
        }

        public String getExamName() {
            return examName;
        }

        public String getScore() {
            return score;
        }

        public String getFullScore() {
            return fullScore;
        }

        public String getTimeSpend() {
            return timeSpend;
        }
    }

    @FXML
    private ChoiceBox<String> courseCombox;
    @FXML
    private ChoiceBox<String> examCombox;
    @FXML
    private ChoiceBox<String> studentCombox;
    @FXML
    private TableView<Grade> gradeTable;
    @FXML
    private TableColumn<Grade, String> studentColumn;
    @FXML
    private TableColumn<Grade, String> courseColumn;
    @FXML
    private TableColumn<Grade, String> examColumn;
    @FXML
    private TableColumn<Grade, String> scoreColumn;
    @FXML
    private TableColumn<Grade, String> fullScoreColumn;
    @FXML
    private TableColumn<Grade, String> timeSpendColumn;
    @FXML
    BarChart<String, Number> barChart;
    @FXML
    PieChart pieChart;
    @FXML
    LineChart<String, Number> lineChart;

    private final TeacherGradeStatisticService service = new TeacherGradeStatisticService();

    /**
     * Initializes the controller after its root element has been processed.
     *
     * @param url           the location used to resolve relative paths for the root object,
     *                      or null if the location is not known
     * @param resourceBundle the resources used to localize the root object,
     *                      or null if the root object was not localized
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        barChart.setLegendVisible(false);
        pieChart.setLegendVisible(false);
        pieChart.setTitle("Student Scores");
        lineChart.setLegendVisible(false);

        // Set up table columns to display grade information
        studentColumn.setCellValueFactory(new PropertyValueFactory<>("studentName"));
        courseColumn.setCellValueFactory(new PropertyValueFactory<>("courseNum"));
        examColumn.setCellValueFactory(new PropertyValueFactory<>("examName"));
        scoreColumn.setCellValueFactory(new PropertyValueFactory<>("score"));
        fullScoreColumn.setCellValueFactory(new PropertyValueFactory<>("fullScore"));
        timeSpendColumn.setCellValueFactory(new PropertyValueFactory<>("timeSpend"));

        // Load grades from file and populate the table and choice boxes
        service.loadGradesFromFile("data/students_exams.txt");
        gradeTable.setItems(service.getGradeList());
        populateChoiceBoxes();
        loadChart();
    }

    /**
     * Populates the choice boxes with unique course numbers, exam names, and student names from the grade list.
     */
    private void populateChoiceBoxes() {
        ObservableList<String> courses = FXCollections.observableArrayList();
        ObservableList<String> exams = FXCollections.observableArrayList();
        ObservableList<String> students = FXCollections.observableArrayList();

        for (Grade grade : service.getGradeList()) {
            if (!courses.contains(grade.getCourseNum())) {
                courses.add(grade.getCourseNum());
            }
            if (!exams.contains(grade.getExamName())) {
                exams.add(grade.getExamName());
            }
            if (!students.contains(grade.getStudentName())) {
                students.add(grade.getStudentName());
            }
        }

        courseCombox.setItems(courses);
        examCombox.setItems(exams);
        studentCombox.setItems(students);
    }

    /**
     * Loads the charts with the grade data.
     */
    private void loadChart() {
        service.updateBarChart(barChart, service.getGradeList());
        service.updatePieChart(pieChart, service.getGradeList());
        service.updateLineChart(lineChart, service.getGradeList());
    }

    /**
     * Refreshes the charts with the current grade data.
     */
    @FXML
    public void refresh() {
        loadChart();
    }

    /**
     * Resets the choice boxes and grade table to their default states.
     */
    @FXML
    public void reset() {
        courseCombox.getSelectionModel().clearSelection();
        examCombox.getSelectionModel().clearSelection();
        studentCombox.getSelectionModel().clearSelection();
        gradeTable.setItems(service.getGradeList());
        loadChart();
    }

    /**
     * Queries the grades based on the selected course, exam, and student, and updates the table and charts.
     */
    @FXML
    public void query() {
        String selectedCourse = courseCombox.getValue();
        String selectedExam = examCombox.getValue();
        String selectedStudent = studentCombox.getValue();

        List<Grade> filteredGrades = service.filterGrades(selectedCourse, selectedExam, selectedStudent);
        gradeTable.setItems(FXCollections.observableArrayList(filteredGrades));

        service.updateBarChart(barChart, filteredGrades);
        service.updatePieChart(pieChart, filteredGrades);
        service.updateLineChart(lineChart, filteredGrades);
    }
}