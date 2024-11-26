package comp3111.examsystem.controller;

import comp3111.examsystem.service.StudentMainService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.nio.file.*;
import java.util.HashSet;
import java.util.List;
import java.util.ResourceBundle;
import java.util.Set;

public class StudentMainController implements Initializable {
    @FXML
    private ComboBox<String> examCombox;

    private String loggedInUsername; // 当前登录用户名
    private StudentMainService studentMainService;

    private Set<String> completedQuizzes = new HashSet<>();
    private final String COMPLETED_QUIZZES_FILE = "data/completed_quizzes.txt";

    /**
     * 设置登录用户名 (通过 LoginController 传递)
     *
     * @param username 登录用户名
     */
    public void setLoggedInUsername(String username) {
        this.loggedInUsername = username;

        // 初始化考试服务
        String studentExamsFilePath = "data/studentsexams.txt";
        studentMainService = new StudentMainService(studentExamsFilePath);

        // 加载已完成的考试
        loadCompletedQuizzes();

        // 加载用户相关的考试信息到 ComboBox
        List<String> exams = studentMainService.getExamsForStudent(loggedInUsername);
        if (exams.isEmpty()) {
            showAlert("No Exams Found", "No exams available for the user: " + username, Alert.AlertType.INFORMATION);
        } else {
            for (String exam : exams) {
                // 解析考试信息，假设格式为 "CourseId CourseName | ExamType"
                String[] parts = exam.split(" \\| ");
                if (parts.length < 2) {
                    // 格式不正确，跳过
                    continue;
                }
                String courseInfo = parts[0];
                String examType = parts[1];
                String[] courseParts = courseInfo.split(" ", 2);
                String courseId = courseParts[0];
                // 构建唯一标识符
                String identifier = courseId + "," + examType;
                boolean isCompleted = completedQuizzes.contains(identifier);
                if (isCompleted) {
                    examCombox.getItems().add(exam + " (Completed)");
                } else {
                    examCombox.getItems().add(exam);
                }
            }
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // 留空，等待 setLoggedInUsername 动态传递数据
    }

    /**
     * 加载已完成的考试记录
     */
    private void loadCompletedQuizzes() {
        Path path = Paths.get(COMPLETED_QUIZZES_FILE);
        if (Files.exists(path)) {
            try {
                List<String> lines = Files.readAllLines(path);
                completedQuizzes.addAll(lines);
            } catch (IOException e) {
                e.printStackTrace();
                showAlert("Error", "Failed to load completed quizzes.", Alert.AlertType.ERROR);
            }
        }
    }

    @FXML
    public void openExamUI(ActionEvent event) {
        String selectedExam = examCombox.getValue();
        if (selectedExam == null || selectedExam.isEmpty()) {
            showAlert("Error", "Please select an exam before proceeding.", Alert.AlertType.ERROR);
            return;
        }

        // 检查所选考试是否已完成
        boolean isCompleted = selectedExam.endsWith("(Completed)");
        if (isCompleted) {
            showAlert("Info", "You have already completed this exam.", Alert.AlertType.INFORMATION);
            return;
        }

        try {
            // 如果考试名包含 " (Completed)"，则去除该部分
            if (selectedExam.endsWith("(Completed)")) {
                selectedExam = selectedExam.substring(0, selectedExam.length() - " (Completed)".length()).trim();
            }

            // 解析选中的考试信息，获取 courseId 和 examType
            String[] parts = selectedExam.split(" \\| ");
            if (parts.length < 2) {
                showAlert("Error", "Invalid exam format.", Alert.AlertType.ERROR);
                return;
            }
            String courseInfo = parts[0];
            String examType = parts[1];

            String[] courseParts = courseInfo.split(" ", 2);
            String courseId = courseParts[0];

            // 加载 QuizUI.fxml 界面
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/comp3111/examsystem/QuizUI.fxml"));
            Parent quizUI = loader.load();
            Stage stage = new Stage();
            stage.setTitle("Exam: " + selectedExam);
            stage.setScene(new Scene(quizUI));

            // 获取控制器并传递数据
            QuizController controller = loader.getController();
            controller.initData(courseId, examType, loggedInUsername); // 传递三个参数

            stage.show();

            // 关闭当前窗口
            closeCurrentWindow();
        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Error", "Failed to open the exam UI.", Alert.AlertType.ERROR);
        }
    }

    @FXML
    public void openGradeStatistic(ActionEvent event) {
        try {
            // 加载 GradeStatistics.fxml 界面
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/comp3111/examsystem/GradeStatistics.fxml"));
            Parent gradeStatisticsUI = loader.load();

            // 获取 GradeStatisticsController 并传递必要的数据（如果需要）
            GradeStatisticsController controller = loader.getController();
            // 如果 GradeStatisticsController 需要用户名，可以在这里传递
            // controller.setLoggedInUsername(this.loggedInUsername);

            // 获取当前舞台并设置新的场景
            Stage stage = (Stage) examCombox.getScene().getWindow();
            stage.getScene().setRoot(gradeStatisticsUI);
            stage.sizeToScene(); // 根据新界面调整窗口大小

        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Error", "Failed to open the grade statistics UI.", Alert.AlertType.ERROR);
        }
    }

    /**
     * 显示警告弹窗
     *
     * @param title   弹窗标题
     * @param message 弹窗消息
     * @param type    弹窗类型
     */
    private void showAlert(String title, String message, Alert.AlertType type) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        ButtonType buttonTypeOk = new ButtonType("OK", ButtonBar.ButtonData.OK_DONE);
        alert.getButtonTypes().setAll(buttonTypeOk);
        alert.showAndWait();
    }

    /**
     * 关闭当前窗口
     */
    private void closeCurrentWindow() {
        Stage currentStage = (Stage) examCombox.getScene().getWindow();
        currentStage.close();
    }

    @FXML
    public void exit(ActionEvent event) {
        System.exit(0); // 退出程序
    }
}
