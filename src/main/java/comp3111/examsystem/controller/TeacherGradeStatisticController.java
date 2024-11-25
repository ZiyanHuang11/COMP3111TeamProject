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
import javafx.beans.property.SimpleStringProperty;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.MapValueFactory;
import javafx.util.Callback;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.TableColumn.CellDataFeatures;

import java.net.URL;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

public class TeacherGradeStatisticController implements Initializable {

    @FXML
    private ChoiceBox<String> courseCombox;
    @FXML
    private ChoiceBox<String> examCombox;
    @FXML
    private ChoiceBox<String> studentCombox;
    @FXML
    private TableView<Map<String, String>> gradeTable;
    @FXML
    private TableColumn<Map<String, String>, String> studentColumn;
    @FXML
    private TableColumn<Map<String, String>, String> courseColumn;
    @FXML
    private TableColumn<Map<String, String>, String> examColumn;
    @FXML
    private TableColumn<Map<String, String>, String> scoreColumn;
    @FXML
    private TableColumn<Map<String, String>, String> fullScoreColumn;
    @FXML
    private TableColumn<Map<String, String>, String> passStatusColumn;
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
        // Initialize table columns with custom cell value factories
        studentColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().get("studentName")));
        courseColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().get("courseNum")));
        examColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().get("examName")));
        scoreColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().get("score")));
        fullScoreColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().get("fullScore")));
        passStatusColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().get("passStatus")));

        gradeTable.setItems(service.getGradeList());
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

    private void loadCharts(List<Map<String, String>> grades) {
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
        gradeTable.setItems(service.getGradeList());
        loadCharts(service.getGradeList());
    }

    @FXML
    public void query() {
        String selectedCourse = courseCombox.getValue();
        String selectedExam = examCombox.getValue();
        String selectedStudent = studentCombox.getValue();

        List<Map<String, String>> filteredGrades = service.filterGrades(selectedCourse, selectedExam, selectedStudent);
        gradeTable.setItems(FXCollections.observableArrayList(filteredGrades));
        loadCharts(filteredGrades);
    }
}
