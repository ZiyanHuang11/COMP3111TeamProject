package comp3111.examsystem.controller;

import comp3111.examsystem.data.DataManager;
import comp3111.examsystem.entity.Teacher;
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

public class TeacherGradeStatisticController implements Initializable {

    public static class Grade {
        private final String studentName;
        private final String courseNum;
        private final String examName;
        private final String score;
        private final String fullScore;
        private final String passStatus;

        public Grade(String studentName, String courseNum, String examName, String score, String fullScore, String passStatus) {
            this.studentName = studentName;
            this.courseNum = courseNum;
            this.examName = examName;
            this.score = score;
            this.fullScore = fullScore;
            this.passStatus = passStatus;
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

        public String getPassStatus() {
            return passStatus;
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
    private TableColumn<Grade, String> passStatusColumn;
    @FXML
    private BarChart<String, Number> barChart;
    @FXML
    private PieChart pieChart;
    @FXML
    private LineChart<String, Number> lineChart;

    private final TeacherGradeStatisticService service;
    private final Teacher loggedInTeacher;

    public TeacherGradeStatisticController(DataManager dataManager, Teacher loggedInTeacher) {
        this.service = new TeacherGradeStatisticService(dataManager);
        this.loggedInTeacher = loggedInTeacher;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // Initialize table columns
        studentColumn.setCellValueFactory(new PropertyValueFactory<>("studentName"));
        courseColumn.setCellValueFactory(new PropertyValueFactory<>("courseNum"));
        examColumn.setCellValueFactory(new PropertyValueFactory<>("examName"));
        scoreColumn.setCellValueFactory(new PropertyValueFactory<>("score"));
        fullScoreColumn.setCellValueFactory(new PropertyValueFactory<>("fullScore"));
        passStatusColumn.setCellValueFactory(new PropertyValueFactory<>("passStatus"));

        gradeTable.setItems(FXCollections.observableArrayList(service.getGradeList()));
        populateChoiceBoxes();
        loadCharts(service.getGradeList());
    }

    private void populateChoiceBoxes() {
        ObservableList<String> courses = FXCollections.observableArrayList(service.getCourses());
        ObservableList<String> exams = FXCollections.observableArrayList(service.getExams());
        ObservableList<String> students = FXCollections.observableArrayList(service.getStudents());

        courseCombox.setItems(courses);
        examCombox.setItems(exams);
        studentCombox.setItems(students);
    }

    private void loadCharts(List<Grade> grades) {
        service.updateBarChart(barChart, grades);
        service.updatePieChart(pieChart, grades);
        service.updateLineChart(lineChart, grades);
    }

    @FXML
    public void refresh() {
        loadCharts(service.getGradeList());
    }

    @FXML
    public void reset() {
        courseCombox.getSelectionModel().clearSelection();
        examCombox.getSelectionModel().clearSelection();
        studentCombox.getSelectionModel().clearSelection();
        gradeTable.setItems(FXCollections.observableArrayList(service.getGradeList()));
        loadCharts(service.getGradeList());
    }

    @FXML
    public void query() {
        String selectedCourse = courseCombox.getValue();
        String selectedExam = examCombox.getValue();
        String selectedStudent = studentCombox.getValue();

        List<Grade> filteredGrades = service.filterGrades(selectedCourse, selectedExam, selectedStudent);
        gradeTable.setItems(FXCollections.observableArrayList(filteredGrades));
        loadCharts(filteredGrades);
    }
}
