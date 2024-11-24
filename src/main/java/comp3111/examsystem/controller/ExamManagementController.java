package comp3111.examsystem.controller;

import comp3111.examsystem.data.DataManager;
import comp3111.examsystem.entity.Exam;
import comp3111.examsystem.entity.Question;
import comp3111.examsystem.service.ExamManagementService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.util.List;

public class ExamManagementController {

    @FXML
    private TextField examNameFilterTxt;
    @FXML
    private ComboBox<String> courseIDFilterComboBox;
    @FXML
    private ComboBox<String> publishFilterComboBox;

    @FXML
    private TableView<Exam> examTable;
    @FXML
    private TableColumn<Exam, String> examNameColumn;
    @FXML
    private TableColumn<Exam, String> courseIDColumn;
    @FXML
    private TableColumn<Exam, String> examTimeColumn;
    @FXML
    private TableColumn<Exam, String> publishColumn;

    @FXML
    private TableView<Question> questionTable;
    @FXML
    private TableColumn<Question, String> questionColumn;
    @FXML
    private TableColumn<Question, String> typeColumn;
    @FXML
    private TableColumn<Question, Integer> scoreColumn;

    private ExamManagementService examService;

    @FXML
    public void initialize() {
        DataManager dataManager = new DataManager();
        examService = new ExamManagementService(dataManager);

        // 初始化考试表
        examTable.setItems(examService.getExamList());
        examNameColumn.setCellValueFactory(cellData -> cellData.getValue().examNameProperty());
        courseIDColumn.setCellValueFactory(cellData -> cellData.getValue().courseIDProperty());
        examTimeColumn.setCellValueFactory(cellData -> cellData.getValue().examTimeProperty());
        publishColumn.setCellValueFactory(cellData -> cellData.getValue().publishProperty());

        // 初始化问题表
        questionTable.setItems(examService.getQuestionList());
        questionColumn.setCellValueFactory(cellData -> cellData.getValue().questionProperty());
        typeColumn.setCellValueFactory(cellData -> cellData.getValue().typeProperty());
        scoreColumn.setCellValueFactory(cellData -> cellData.getValue().scoreProperty().asObject());

        // 初始化过滤选项
        courseIDFilterComboBox.setItems(FXCollections.observableArrayList("All", "COMP3111", "COMP5111"));
        publishFilterComboBox.setItems(FXCollections.observableArrayList("All", "Yes", "No"));
        courseIDFilterComboBox.setValue("All");
        publishFilterComboBox.setValue("All");
    }

    @FXML
    private void handleFilter() {
        String examName = examNameFilterTxt.getText().trim();
        String courseID = courseIDFilterComboBox.getValue();
        String publishStatus = publishFilterComboBox.getValue();

        List<Exam> filteredExams = examService.filterExams(examName, courseID, publishStatus);
        examTable.setItems(FXCollections.observableArrayList(filteredExams));
    }

    @FXML
    private void handleReset() {
        examNameFilterTxt.clear();
        courseIDFilterComboBox.setValue("All");
        publishFilterComboBox.setValue("All");
        examTable.setItems(examService.getExamList());
    }
}
