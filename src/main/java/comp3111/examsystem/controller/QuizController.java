package comp3111.examsystem.controller;

import comp3111.examsystem.entity.Quiz;
import comp3111.examsystem.entity.StudentQuestion;
import comp3111.examsystem.service.QuizService;
import javafx.application.Platform;
import javafx.fxml.FXML;
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
    private int remainingTime;
    private Timer timer;

    // 使用 Map<Integer, Object> 来存储用户的答案，Object 可以是 String 或 List<String>
    private Map<Integer, Object> userAnswers = new HashMap<>();

    private final String COMPLETED_GRADES_FILE = "data/completed_quizzes.txt";

    private String loggedInUsername; // 定义 loggedInUsername

    private long startTime; // 记录开始时间

    /**
     * 初始化数据，加载考试信息
     *
     * @param courseId 课程ID
     * @param examType 考试类型
     * @param username 登录用户名
     */
    public void initData(String courseId, String examType, String username) {
        this.loggedInUsername = username;

        // 加载数据
        QuizService quizService = new QuizService("data/quizzes.txt");
        quiz = quizService.loadQuiz(courseId, examType);

        if (quiz != null) {
            quizTitle.setText(quiz.getCourseName() + " - " + quiz.getExamType());
            totalQuestions.setText(String.valueOf(quiz.getQuestions().size()));
            remainingTime = quiz.getDuration();
            startTimer();
            loadQuestion(0);

            // 设置问题列表，显示具体问题内容
            for (int i = 0; i < quiz.getQuestions().size(); i++) {
                StudentQuestion q = quiz.getQuestions().get(i);
                questionListView.getItems().add("Question " + (i + 1) + ": " + q.getQuestion());
            }

            // 允许左右滑动查看问题
            questionListView.setPrefWidth(300); // 根据需要调整宽度
            questionListView.setCellFactory(param -> new ListCell<>() {
                @Override
                protected void updateItem(String item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty || item == null) {
                        setText(null);
                    } else {
                        setText(item);
                        setWrapText(true); // 允许文本换行
                    }
                }
            });

            // 监听问题选择事件
            questionListView.getSelectionModel().selectedIndexProperty().addListener((obs, oldVal, newVal) -> {
                if (newVal != null && newVal.intValue() != currentQuestionIndex) {
                    loadQuestion(newVal.intValue());
                }
            });

            // 记录开始时间
            startTime = System.currentTimeMillis();
        } else {
            showAlert("Error", "Failed to load quiz data.", Alert.AlertType.ERROR);
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
        // 保存当前题目的用户答案
        saveUserAnswer(currentQuestionIndex);

        // 检查索引有效性
        if (index < 0 || index >= quiz.getQuestions().size()) {
            showAlert("Error", "Invalid question index.", Alert.AlertType.ERROR);
            return;
        }

        currentQuestionIndex = index;
        StudentQuestion question = quiz.getQuestions().get(index);
        questionText.setText("Q" + (index + 1) + ": " + question.getQuestion());

        // 清空选项并重新加载
        optionsContainer.getChildren().clear();
        List<String> options = Arrays.asList(
                question.getOptionA(),
                question.getOptionB(),
                question.getOptionC(),
                question.getOptionD()
        );

        if ("MULTI".equalsIgnoreCase(question.getType())) {
            for (int i = 0; i < options.size(); i++) {
                String option = options.get(i);
                String label = String.valueOf((char) ('A' + i));
                CheckBox checkBox = new CheckBox(label + ". " + option);
                checkBox.setUserData(label);

                // 恢复之前的选择
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
        } else { // SINGLE
            ToggleGroup toggleGroup = new ToggleGroup();
            for (int i = 0; i < options.size(); i++) {
                String option = options.get(i);
                String label = String.valueOf((char) ('A' + i));
                RadioButton radioButton = new RadioButton(label + ". " + option);
                radioButton.setUserData(label);
                radioButton.setToggleGroup(toggleGroup);

                // 恢复之前的选择
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

        // 更新ListView的选择
        questionListView.getSelectionModel().select(index);
        questionListView.scrollTo(index);
    }

    private void saveUserAnswer(int index) {
        if (index < 0 || index >= quiz.getQuestions().size()) {
            return;
        }

        StudentQuestion question = quiz.getQuestions().get(index);
        if ("MULTI".equalsIgnoreCase(question.getType())) {
            // 收集选中的选项
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
        } else { // SINGLE
            // 获取选中的选项
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
    public void handleNext() {
        saveUserAnswer(currentQuestionIndex);
        if (currentQuestionIndex < quiz.getQuestions().size() - 1) {
            loadQuestion(currentQuestionIndex + 1);
        } else {
            showAlert("Info", "This is the last question.", Alert.AlertType.INFORMATION);
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
    public void handleSubmit() {
        if (timer != null) {
            timer.cancel();
        }
        // 保存当前题目的用户答案
        saveUserAnswer(currentQuestionIndex);

        int totalScore = 0;
        int totalPossibleScore = 0;
        int correctAnswers = 0;

        for (int i = 0; i < quiz.getQuestions().size(); i++) {
            StudentQuestion q = quiz.getQuestions().get(i);
            int questionScore = q.getScore();
            totalPossibleScore += questionScore;

            List<String> correctAnswerList = q.getAnswers(); // List<String> e.g., ["A"] or ["A", "B"]
            Set<String> correctOptionSet = new HashSet<>(correctAnswerList);

            if (userAnswers.containsKey(i)) {
                if ("MULTI".equalsIgnoreCase(q.getType())) {
                    @SuppressWarnings("unchecked")
                    List<String> userSelectedOptions = (List<String>) userAnswers.get(i);
                    Set<String> userOptionSet = new HashSet<>(userSelectedOptions);
                    if (userOptionSet.equals(correctOptionSet)) {
                        totalScore += questionScore;
                        correctAnswers++;
                    }
                } else { // SINGLE
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

        // 计算实际用时（秒）
        long endTime = System.currentTimeMillis();
        long timeTaken = (endTime - startTime) / 1000; // 转换为秒

        // 弹窗显示结果
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Quiz Result");
        alert.setHeaderText("Your Result");
        alert.setContentText(correctAnswers + "/" + totalQuestions + " Correct, the precision is " +
                String.format("%.2f", precision) + "%, the score is " + totalScore + "/" + totalPossibleScore +
                "\nTime Taken: " + formatTime(timeTaken));

        // 设置确定按钮的事件
        ButtonType okButton = new ButtonType("OK", ButtonBar.ButtonData.OK_DONE);
        alert.getButtonTypes().setAll(okButton);
        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == okButton) {
            // 记录已完成的考试及成绩
            recordCompletedGrade(quiz.getCourseId(), quiz.getExamType(), totalScore, totalPossibleScore, (int) timeTaken);
            returnToMainUI();
        }
    }

    /**
     * 记录已完成的考试及成绩
     *
     * @param courseId     课程ID
     * @param examType     考试类型
     * @param score        获得的分数
     * @param fullScore    满分
     * @param timeTakenSec 用时（秒）
     */
    private void recordCompletedGrade(String courseId, String examType, int score, int fullScore, int timeTakenSec) {
        String entry = String.format("%s,%s,%d,%d,%d", courseId, examType, score, fullScore, timeTakenSec);
        try {
            Path path = Paths.get(COMPLETED_GRADES_FILE);
            if (!Files.exists(path)) {
                Files.createFile(path);
            }
            Files.write(path, (entry + System.lineSeparator()).getBytes(), StandardOpenOption.APPEND);
        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Error", "Failed to record completed grade.", Alert.AlertType.ERROR);
        }
    }

    /**
     * 返回到 StudentMainUI
     */
    private void returnToMainUI() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/comp3111/examsystem/StudentMainUI.fxml"));
            Parent mainUI = loader.load();

            // 获取 StudentMainController 并传递登录用户名
            StudentMainController mainController = loader.getController();
            mainController.setLoggedInUsername(this.loggedInUsername); // 确保 loggedInUsername 已定义

            // 获取当前舞台并设置新的场景
            Stage stage = (Stage) quizTitle.getScene().getWindow();
            stage.getScene().setRoot(mainUI);
            stage.sizeToScene(); // 调整舞台大小以适应新场景
        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Error", "Failed to load StudentMainUI.", Alert.AlertType.ERROR);
        }
    }

    /**
     * 格式化时间（秒）为 HH:mm:ss
     *
     * @param totalSeconds 总秒数
     * @return 格式化后的时间字符串
     */
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
