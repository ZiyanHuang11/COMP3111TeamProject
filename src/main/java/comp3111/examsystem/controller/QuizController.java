package comp3111.examsystem.controller;

import comp3111.examsystem.entity.Quiz;
import comp3111.examsystem.entity.StudentQuestion;
import comp3111.examsystem.service.QuizService;
import javafx.application.Platform;
import javafx.fxml.FXML;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.*;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.nio.file.*;
import java.util.*;

public class QuizController {
    @FXML
    private Label quizTitle;
    @FXML
    private Label questionText;
    @FXML
    private VBox optionsContainer;
    @FXML
    private Label timerLabel;
    @FXML
    private Label totalQuestions;
    @FXML
    private ListView<String> questionListView;

    private Quiz quiz;
    private int currentQuestionIndex = 0;
    private int remainingTime; // 以秒为单位
    private Timer timer;

    private Map<Integer, Object> userAnswers = new HashMap<>();

    private final String COMPLETED_GRADES_FILE = "data/completed_quizzes.txt";

    private String loggedInUsername;

    private long startTime;

    // 修改：新增 QuizService 的 exam.txt 和 questions.txt 路径
    private QuizService quizService;

    public void initData(String courseId, String examType, String username) {
        this.loggedInUsername = username;

        // 初始化 QuizService，传入 exam.txt 和 questions.txt 的路径
        quizService = new QuizService("data/exam.txt", "data/questions.txt");
        quiz = quizService.loadQuiz(courseId, examType);

        if (quiz != null) {
            quizTitle.setText(quiz.getCourseName() + " - " + quiz.getExamType());
            totalQuestions.setText(String.valueOf(quiz.getQuestions().size()));
            remainingTime = quiz.getDuration() * 60; // 假设 duration 是分钟，转换为秒
            startTimer();
            loadQuestion(0);

            for (int i = 0; i < quiz.getQuestions().size(); i++) {
                StudentQuestion q = quiz.getQuestions().get(i);
                questionListView.getItems().add("Question " + (i + 1) + ": " + q.getQuestion());
            }

            questionListView.setPrefWidth(300);
            questionListView.setCellFactory(param -> new ListCell<>() {
                @Override
                protected void updateItem(String item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty || item == null) {
                        setText(null);
                    } else {
                        setText(item);
                        setWrapText(true);
                    }
                }
            });

            questionListView.getSelectionModel().selectedIndexProperty().addListener((obs, oldVal, newVal) -> {
                if (newVal != null && newVal.intValue() != currentQuestionIndex) {
                    loadQuestion(newVal.intValue());
                }
            });

            startTime = System.currentTimeMillis();
        } else {
            showAlert("Error", "Failed to load quiz data.", Alert.AlertType.ERROR);
        }
    }

    @FXML
    public void handlePrevious() {
        saveUserAnswer(currentQuestionIndex);
        if (currentQuestionIndex > 0) {
            loadQuestion(currentQuestionIndex - 1);
        } else {
            showAlert("Info", "This is the first question.", Alert.AlertType.INFORMATION);
        }
    }

    @FXML
    public void handleNext() {
        saveUserAnswer(currentQuestionIndex);
        if (currentQuestionIndex < quiz.getQuestions().size() - 1) {
            loadQuestion(currentQuestionIndex + 1);
        } else {
            showAlert("Info", "This is the last question.", Alert.AlertType.INFORMATION);
        }
    }

    private void startTimer() {
        timer = new Timer(true);
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                Platform.runLater(() -> {
                    remainingTime--;
                    updateTimerLabel();
                    if (remainingTime <= 0) {
                        timer.cancel();
                        handleSubmit();
                    }
                });
            }
        }, 1000, 1000);
    }

    private void updateTimerLabel() {
        int minutes = remainingTime / 60;
        int seconds = remainingTime % 60;
        timerLabel.setText(String.format("%02d:%02d", minutes, seconds));
    }

    private void loadQuestion(int index) {
        saveUserAnswer(currentQuestionIndex);

        if (index < 0 || index >= quiz.getQuestions().size()) {
            showAlert("Error", "Invalid question index.", Alert.AlertType.ERROR);
            return;
        }

        currentQuestionIndex = index;
        StudentQuestion question = quiz.getQuestions().get(index);
        questionText.setText("Q" + (index + 1) + ": " + question.getQuestion());

        optionsContainer.getChildren().clear();
        List<String> options = Arrays.asList(
                question.getOptionA(),
                question.getOptionB(),
                question.getOptionC(),
                question.getOptionD()
        );

        if ("Multiple".equalsIgnoreCase(question.getType())) {
            for (int i = 0; i < options.size(); i++) {
                String option = options.get(i);
                String label = String.valueOf((char) ('A' + i));
                CheckBox checkBox = new CheckBox(label + ". " + option);
                checkBox.setUserData(label);

                if (userAnswers.containsKey(index)) {
                    @SuppressWarnings("unchecked")
                    List<String> selectedOptions = (List<String>) userAnswers.get(index);
                    if (selectedOptions.contains(label)) {
                        checkBox.setSelected(true);
                    }
                }

                checkBox.selectedProperty().addListener((obs, wasSelected, isNowSelected) -> {
                    saveUserAnswer(index);
                });

                optionsContainer.getChildren().add(checkBox);
            }
        } else { // Single choice
            ToggleGroup toggleGroup = new ToggleGroup();
            for (int i = 0; i < options.size(); i++) {
                String option = options.get(i);
                String label = String.valueOf((char) ('A' + i));
                RadioButton radioButton = new RadioButton(label + ". " + option);
                radioButton.setUserData(label);
                radioButton.setToggleGroup(toggleGroup);

                if (userAnswers.containsKey(index)) {
                    String selectedOption = (String) userAnswers.get(index);
                    if (selectedOption.equals(label)) {
                        radioButton.setSelected(true);
                    }
                }

                radioButton.setOnAction(e -> {
                    saveUserAnswer(index);
                });

                optionsContainer.getChildren().add(radioButton);
            }
        }

        questionListView.getSelectionModel().select(index);
        questionListView.scrollTo(index);
    }

    private void saveUserAnswer(int index) {
        if (index < 0 || index >= quiz.getQuestions().size()) {
            return;
        }

        StudentQuestion question = quiz.getQuestions().get(index);
        if ("Multiple".equalsIgnoreCase(question.getType())) {
            List<String> selectedOptions = new ArrayList<>();
            for (Node node : optionsContainer.getChildren()) {
                if (node instanceof CheckBox) {
                    CheckBox checkBox = (CheckBox) node;
                    if (checkBox.isSelected()) {
                        selectedOptions.add((String) checkBox.getUserData());
                    }
                }
            }
            userAnswers.put(index, selectedOptions);
        } else { // Single choice
            for (Node node : optionsContainer.getChildren()) {
                if (node instanceof RadioButton) {
                    RadioButton radioButton = (RadioButton) node;
                    if (radioButton.isSelected()) {
                        userAnswers.put(index, (String) radioButton.getUserData());
                        break;
                    }
                }
            }
        }
    }

    @FXML
    public void handleSubmit() {
        if (timer != null) {
            timer.cancel();
        }
        saveUserAnswer(currentQuestionIndex);

        int totalScore = 0;
        int totalPossibleScore = 0;
        int correctAnswers = 0;

        for (int i = 0; i < quiz.getQuestions().size(); i++) {
            StudentQuestion q = quiz.getQuestions().get(i);
            int questionScore = q.getScore();
            totalPossibleScore += questionScore;

            List<String> correctAnswerList = q.getAnswers();
            Set<String> correctOptionSet = new HashSet<>(correctAnswerList);

            if (userAnswers.containsKey(i)) {
                if ("Multiple".equalsIgnoreCase(q.getType())) {
                    @SuppressWarnings("unchecked")
                    List<String> userSelectedOptions = (List<String>) userAnswers.get(i);
                    Set<String> userOptionSet = new HashSet<>(userSelectedOptions);
                    if (userOptionSet.equals(correctOptionSet)) {
                        totalScore += questionScore;
                        correctAnswers++;
                    }
                } else { // Single choice
                    String userSelectedOption = (String) userAnswers.get(i);
                    if (correctOptionSet.contains(userSelectedOption)) {
                        totalScore += questionScore;
                        correctAnswers++;
                    }
                }
            }
        }

        int totalQuestions = quiz.getQuestions().size();
        double precision = ((double) correctAnswers / totalQuestions) * 100;
        long endTime = System.currentTimeMillis();
        long timeTaken = (endTime - startTime) / 1000;

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Quiz Result");
        alert.setHeaderText("Your Result");
        alert.setContentText(correctAnswers + "/" + totalQuestions + " Correct, the precision is " +
                String.format("%.2f", precision) + "%, the score is " + totalScore + "/" + totalPossibleScore +
                "\nTime Taken: " + formatTime(timeTaken));

        ButtonType okButton = new ButtonType("OK", ButtonBar.ButtonData.OK_DONE);
        alert.getButtonTypes().setAll(okButton);
        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == okButton) {
            recordCompletedGrade(quiz.getCourseId(), quiz.getExamType(), totalScore, totalPossibleScore, (int) timeTaken);
            returnToMainUI();
        }
    }

    private void recordCompletedGrade(String courseId, String examType, int score, int fullScore, int timeTakenSec) {
        // 修改记录格式：在最前面加上 username
        String entry = String.format("\n%s,%s,%s,%d,%d,%d", loggedInUsername, courseId, examType, score, fullScore, timeTakenSec);
        try {
            Path path = Paths.get(COMPLETED_GRADES_FILE);
            boolean fileExists = Files.exists(path);

            try (BufferedWriter writer = Files.newBufferedWriter(path, StandardOpenOption.CREATE, StandardOpenOption.APPEND)) {
                // entry 已经以 \n 开头，无需额外添加
                writer.write(entry);
            }
        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Error", "Failed to record completed grade.", Alert.AlertType.ERROR);
        }
    }

    private void returnToMainUI() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/comp3111/examsystem/StudentMainUI.fxml"));
            Parent mainUI = loader.load();

            StudentMainController mainController = loader.getController();
            mainController.setLoggedInUsername(this.loggedInUsername);

            Stage stage = (Stage) quizTitle.getScene().getWindow();
            stage.getScene().setRoot(mainUI);
            stage.sizeToScene();
        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Error", "Failed to load StudentMainUI.", Alert.AlertType.ERROR);
        }
    }

    private String formatTime(long totalSeconds) {
        long hours = totalSeconds / 3600;
        long minutes = (totalSeconds % 3600) / 60;
        long seconds = totalSeconds % 60;
        if (hours > 0) {
            return String.format("%02d:%02d:%02d", hours, minutes, seconds);
        } else {
            return String.format("%02d:%02d", minutes, seconds);
        }
    }

    private void showAlert(String title, String message, Alert.AlertType type) {
        Platform.runLater(() -> {
            Alert alert = new Alert(type);
            alert.setTitle(title);
            alert.setHeaderText(null);
            alert.setContentText(message);
            alert.showAndWait();
        });
    }
}
