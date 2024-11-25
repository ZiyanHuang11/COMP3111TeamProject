package comp3111.examsystem.controller;

import comp3111.examsystem.entity.Exam;
import comp3111.examsystem.entity.Question;
import comp3111.examsystem.service.ExamManagementService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.io.IOException;
import java.util.List;

/**
 * Controller for the exams
 */

public class ExamManagementController {

    // FXML fields
    @FXML
    private TextField examNameFilterTxt;
    @FXML
    private ComboBox<String> courseIDFilterComboBox;
    @FXML
    private ComboBox<String> publishFilterComboBox;

    @FXML
    private TextField questionFilterTxt;
    @FXML
    private ComboBox<String> typeFilterComboBox;
    @FXML
    private TextField scoreFilterTxt;

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

    @FXML
    private TableView<Question> selectedQuestionTable;
    @FXML
    private TableColumn<Question, String> selectedQuestionColumn;
    @FXML
    private TableColumn<Question, String> selectedTypeColumn;
    @FXML
    private TableColumn<Question, Integer> selectedScoreColumn;

    @FXML
    private TextField examNameTxt;
    @FXML
    private TextField examTimeTxt;
    @FXML
    private ComboBox<String> courseIDComboBox;
    @FXML
    private ComboBox<String> publishComboBox;

    @FXML
    private Button addButton;
    @FXML
    private Button updateButton;
    @FXML
    private Button deleteButton;
    @FXML
    private Button refreshButton;

    private ExamManagementService examService;
    private ObservableList<Question> selectedQuestionList;

    /**
     * Initializes the controller and sets up the necessary data and listeners.
     */
    @FXML
    public void initialize() {
        examService = new ExamManagementService("data/exam.txt", "data/questions.txt");

        // Initialize exam table
        examTable.setItems(examService.getExamList());
        examNameColumn.setCellValueFactory(cellData -> cellData.getValue().examNameProperty());
        courseIDColumn.setCellValueFactory(cellData -> cellData.getValue().courseIDProperty());
        examTimeColumn.setCellValueFactory(cellData -> cellData.getValue().examTimeProperty());
        publishColumn.setCellValueFactory(cellData -> cellData.getValue().publishProperty());

        // Initialize question table
        questionTable.setItems(examService.getQuestionList());
        questionColumn.setCellValueFactory(cellData -> cellData.getValue().questionProperty());
        typeColumn.setCellValueFactory(cellData -> cellData.getValue().typeProperty());
        scoreColumn.setCellValueFactory(cellData -> cellData.getValue().scoreProperty().asObject());

        // Initialize selected question table
        selectedQuestionList = FXCollections.observableArrayList();
        selectedQuestionTable.setItems(selectedQuestionList);
        selectedQuestionColumn.setCellValueFactory(cellData -> cellData.getValue().questionProperty());
        selectedTypeColumn.setCellValueFactory(cellData -> cellData.getValue().typeProperty());
        selectedScoreColumn.setCellValueFactory(cellData -> cellData.getValue().scoreProperty().asObject());

        // Set default values for ComboBoxes
        courseIDFilterComboBox.setItems(FXCollections.observableArrayList("All", "COMP3111", "COMP5111"));
        publishFilterComboBox.setItems(FXCollections.observableArrayList("All", "Yes", "No"));
        typeFilterComboBox.setItems(FXCollections.observableArrayList("All", "Single", "Multiple"));
        courseIDComboBox.setItems(FXCollections.observableArrayList("COMP3111", "COMP5111"));
        publishComboBox.setItems(FXCollections.observableArrayList("Yes", "No"));

        courseIDFilterComboBox.setValue("All");
        publishFilterComboBox.setValue("All");
        typeFilterComboBox.setValue("All");
        courseIDComboBox.setValue("COMP3111");
        publishComboBox.setValue("No");

        // Listen to exam selection changes
        examTable.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                selectedQuestionList.setAll(newValue.getQuestions());
                examNameTxt.setText(newValue.getExamName());
                examTimeTxt.setText(newValue.getExamTime());
                courseIDComboBox.setValue(newValue.getCourseID());
                publishComboBox.setValue(newValue.getPublish());
            } else {
                selectedQuestionList.clear();
                examNameTxt.clear();
                examTimeTxt.clear();
                courseIDComboBox.setValue(null);
                publishComboBox.setValue(null);
            }
        });
    }

    /**
     * Handles the action of adding a new exam.
     * Validates input fields and adds the exam to the list.
     */
    @FXML
    private void handleAdd() {
        String examName = examNameTxt.getText().trim();
        String examTime = examTimeTxt.getText().trim();
        String courseID = courseIDComboBox.getValue();
        String publish = publishComboBox.getValue();

        if (examName.isEmpty() || examTime.isEmpty() || courseID == null || publish == null) {
            showAlert("Incomplete Data", "Please fill in all fields.", Alert.AlertType.ERROR);
            return;
        }

        Exam newExam = new Exam(examName, courseID, examTime, publish);

        try {
            boolean success = examService.addExam(newExam);
            if (!success) {
                showAlert("Duplicate Exam", "An exam with this name already exists.", Alert.AlertType.ERROR);
            } else {
                examNameTxt.clear();
                examTimeTxt.clear();
                courseIDComboBox.setValue(null);
                publishComboBox.setValue("No");
            }
        } catch (IOException e) {
            showAlert("Error", "Failed to save exams to file.", Alert.AlertType.ERROR);
        }
    }

    /**
     * Handles the action of updating the selected exam.
     * Validates input fields and updates the exam in the list.
     */
    @FXML
    private void handleUpdate() {
        Exam selectedExam = examTable.getSelectionModel().getSelectedItem();
        if (selectedExam == null) {
            showAlert("No Selection", "Please select an exam to update.", Alert.AlertType.WARNING);
            return;
        }

        String examName = examNameTxt.getText().trim();
        String examTime = examTimeTxt.getText().trim();
        String courseID = courseIDComboBox.getValue();
        String publish = publishComboBox.getValue();

        if (examName.isEmpty() || examTime.isEmpty() || courseID == null || publish == null) {
            showAlert("Incomplete Data", "Please fill in all fields.", Alert.AlertType.ERROR);
            return;
        }

        Exam updatedExam = new Exam(examName, courseID, examTime, publish);

        try {
            boolean success = examService.updateExam(updatedExam, selectedExam.getExamName());
            if (!success) {
                showAlert("Duplicate Exam", "An exam with this name already exists.", Alert.AlertType.ERROR);
            } else {
                examTable.refresh();
                examNameTxt.clear();
                examTimeTxt.clear();
                courseIDComboBox.setValue(null);
                publishComboBox.setValue("No");
                examTable.getSelectionModel().clearSelection();
            }
        } catch (IOException e) {
            showAlert("Error", "Failed to save exams to file.", Alert.AlertType.ERROR);
        }
    }

    /**
     * Handles the action of deleting the selected exam.
     */
    @FXML
    private void handleDelete() {
        Exam selectedExam = examTable.getSelectionModel().getSelectedItem();
        if (selectedExam != null) {
            try {
                boolean success = examService.deleteExam(selectedExam.getExamName());
                if (success) {
                    selectedQuestionList.clear();
                    showAlert("Deleted", "Selected exam has been deleted.", Alert.AlertType.INFORMATION);
                } else {
                    showAlert("Error", "Failed to delete exam.", Alert.AlertType.ERROR);
                }
            } catch (IOException e) {
                showAlert("Error", "Failed to save exams to file.", Alert.AlertType.ERROR);
            }
        } else {
            showAlert("No Selection", "Please select an exam to delete.", Alert.AlertType.WARNING);
        }
    }

    /**
     * Handles the action of adding a selected question to the current exam.
     */

    @FXML
    private void handleAddToLeft() {
        Question selectedQuestion = questionTable.getSelectionModel().getSelectedItem();
        Exam selectedExam = examTable.getSelectionModel().getSelectedItem();

        if (selectedExam == null) {
            showAlert("No Exam Selected", "Please select an exam to add questions to.", Alert.AlertType.WARNING);
            return;
        }

        if (selectedQuestion != null) {
            try {
                boolean success = examService.addQuestionToExam(selectedExam, selectedQuestion);
                if (success) {
                    selectedQuestionList.add(selectedQuestion);
                } else {
                    showAlert("Duplicate Question", "Question already exists in the exam.", Alert.AlertType.WARNING);
                }
            } catch (IOException e) {
                showAlert("Error", "Failed to save exams to file.", Alert.AlertType.ERROR);
            }
        } else {
            showAlert("No Selection", "Please select a question to add.", Alert.AlertType.WARNING);
        }
    }
    /**
     * Handles the action of deleting the selected question from the current exam.
     * Displays an alert if no exam or question is selected.
     */
    @FXML
    private void handleDeleteFromLeft() {
        Question selectedQuestion = selectedQuestionTable.getSelectionModel().getSelectedItem();
        Exam selectedExam = examTable.getSelectionModel().getSelectedItem();

        if (selectedExam == null) {
            showAlert("No Exam Selected", "Please select an exam to delete questions from.", Alert.AlertType.WARNING);
            return;
        }

        if (selectedQuestion != null) {
            try {
                boolean success = examService.removeQuestionFromExam(selectedExam, selectedQuestion);
                if (success) {
                    selectedQuestionList.remove(selectedQuestion);
                } else {
                    showAlert("Error", "Failed to remove question from exam.", Alert.AlertType.ERROR);
                }
            } catch (IOException e) {
                showAlert("Error", "Failed to save exams to file.", Alert.AlertType.ERROR);
            }
        } else {
            showAlert("No Selection", "Please select a question to delete.", Alert.AlertType.WARNING);
        }
    }

    /**
     * Handles the action of filtering the exam list based on the provided filters.
     * Updates the exam table with the filtered results.
     */
    @FXML
    private void handleFilter() {
        String examName = examNameFilterTxt.getText().trim();
        String courseID = courseIDFilterComboBox.getValue();
        String publishStatus = publishFilterComboBox.getValue();

        List<Exam> filteredExams = examService.filterExams(examName, courseID, publishStatus);
        examTable.setItems(FXCollections.observableArrayList(filteredExams));
    }

    /**
     * Handles the action of filtering the question list based on the provided filters.
     * Updates the question table with the filtered results.
     */
    @FXML
    private void handleQuestionFilter() {
        String questionText = questionFilterTxt.getText().trim();
        String type = typeFilterComboBox.getValue();
        String scoreText = scoreFilterTxt.getText().trim();

        List<Question> filteredQuestions = examService.filterQuestions(questionText, type, scoreText);
        questionTable.setItems(FXCollections.observableArrayList(filteredQuestions));
    }

    /**
     * Resets the exam filter fields to their default values and refreshes the exam table.
     */
    @FXML
    private void handleReset() {
        examNameFilterTxt.clear();
        courseIDFilterComboBox.setValue("All");
        publishFilterComboBox.setValue("All");
        examTable.setItems(examService.getExamList());
    }

    /**
     * Resets the question filter fields to their default values and refreshes the question table.
     */
    @FXML
    private void handleQuestionFilterReset() {
        questionFilterTxt.clear();
        typeFilterComboBox.setValue("All");
        scoreFilterTxt.clear();
        questionTable.setItems(examService.getQuestionList());
    }

    /**
     * Refreshes the exam, question, and selected question tables to show the latest data.
     */
    @FXML
    private void handleRefresh() {
        examTable.refresh();
        questionTable.refresh();
        selectedQuestionTable.refresh();
    }

    /**
     * Displays an alert with the specified title, message, and alert type.
     *
     * @param title   The title of the alert.
     * @param message The message to be displayed in the alert.
     * @param type    The type of the alert (e.g., ERROR, WARNING, INFORMATION).
     */
    private void showAlert(String title, String message, Alert.AlertType type) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null); // Remove header
        alert.setContentText(message);
        alert.showAndWait();
    }
}