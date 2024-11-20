/**
 * StudentLoginController.java
 *
 * This class serves as the controller for the student login and registration interfaces
 * in the Examination Management System. It manages user interactions, validates user inputs,
 * and handles navigation between different scenes (login, registration, and main UI).
 * The associated FXML file defines the layout and UI components for login and registration forms.
 *
 * Key Features:
 * 1. **Dynamic Data File Path Configuration**:
 *    - The path to the student data file (`students.txt`) is dynamically loaded from a configuration file (`config.properties`).
 *    - If the file is missing or cannot be read, an error is logged for debugging purposes.
 *
 * 2. **Login Functionality**:
 *    - Validates the provided username and password against the stored credentials in the data file.
 *    - Provides detailed feedback on login failure causes (e.g., username not found, incorrect password).
 *    - On successful login, loads the `StudentMainUI.fxml` interface and closes the login window.
 *
 * 3. **Registration Functionality**:
 *    - Collects user inputs from the registration form, including username, name, gender, age, department, password, and password confirmation.
 *    - Validates user inputs:
 *        - Ensures all fields are filled.
 *        - Checks that the password and confirmation password match.
 *        - Verifies password strength (at least 6 characters, containing both letters and numbers).
 *        - Ensures the username is unique.
 *        - Validates age as a positive number.
 *    - If validation passes, writes the new user information to the student data file in the following format:
 *        `username,name,gender,age,department,password`
 *    - Displays appropriate success or error messages.
 *
 * 4. **Input Validation**:
 *    - Checks for empty fields and provides user-friendly feedback.
 *    - Validates input formats (e.g., numeric age, password strength).
 *
 * 5. **Logging**:
 *    - Logs all major events (e.g., login attempts, registration success or failure) using Java's logging framework.
 *    - Helps trace and debug user activity and potential issues.
 *
 * 6. **Error Handling**:
 *    - Gracefully handles errors related to file reading and writing, ensuring the application remains user-friendly.
 *
 * Usage:
 * - This class is associated with the `StudentLoginUI.fxml` and `StudentRegister.fxml` files, which define the visual layouts for the login and registration interfaces.
 * - User interactions such as entering credentials, clicking buttons, and submitting forms are managed through this controller.
 *
 * Dependencies:
 * - JavaFX for the UI components and navigation.
 * - `java.util.logging.Logger` for logging system events.
 * - `config.properties` file for the student data file path configuration.
 *
 * Author: [Ziyan Huang]
 * Date: [2024/11/17]
 */


package comp3111.examsystem.controller;

import comp3111.examsystem.entity.Student;
import comp3111.examsystem.Main;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.*;
import java.net.URL;
import java.util.Properties;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

public class StudentLoginController implements Initializable {
    @FXML
    private TextField usernameTxt;
    @FXML
    private PasswordField passwordTxt;

    // Register form fields
    @FXML
    private TextField nameTxt;
    @FXML
    private ComboBox<String> genderCombo;
    @FXML
    private TextField ageTxt;
    @FXML
    private TextField departmentTxt;
    @FXML
    private PasswordField passwordConfirmTxt;


    private String studentFilePath; // 学生数据文件路径
    private static final Logger logger = Logger.getLogger(StudentLoginController.class.getName()); // 日志记录

    public StudentLoginController() {
        // 动态加载数据文件路径
        try {
            Properties properties = new Properties();
            try (FileInputStream fis = new FileInputStream("config.properties")) {
                properties.load(fis);
                studentFilePath = properties.getProperty("student.file.path", "data/students.txt");
            }
            File studentFile = new File(studentFilePath);
            if (studentFile.exists()) {
                logger.info("Student file found at: " + studentFile.getAbsolutePath());
            } else {
                logger.warning("Student file not found at: " + studentFilePath);
            }
        } catch (IOException e) {
            logger.log(Level.SEVERE, "Error loading configuration file", e);
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // 初始化逻辑
        if (genderCombo != null) {
            genderCombo.getItems().addAll("Female", "Male"); // 初始化 Gender 下拉框
        }
    }

    @FXML
    public void closeWindow(ActionEvent event) {
        Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
        stage.close();
    }

    @FXML
    public void login(ActionEvent e) {
        String username = usernameTxt.getText();
        String password = passwordTxt.getText();

        // 输入提示：检查用户名或密码是否为空
        if (username.isEmpty() || password.isEmpty()) {
            if (username.isEmpty()) {
                showAlert(Alert.AlertType.WARNING, "Input Error", "Username cannot be empty.");
            }
            if (password.isEmpty()) {
                showAlert(Alert.AlertType.WARNING, "Input Error", "Password cannot be empty.");
            }
            logger.warning("Login attempt with empty fields.");
            return;
        }

        if (validate(username, password)) {
            // 登录成功
            logger.info("Login successful for username: " + username);

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Hint");
            alert.setHeaderText(null);
            alert.setContentText("Login successful");
            alert.showAndWait();

            // 加载主界面
            FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("StudentMainUI.fxml"));
            Stage stage = new Stage();
            stage.setTitle("Hi " + username + ", Welcome to HKUST Examination System");
            try {
                stage.setScene(new Scene(fxmlLoader.load()));
            } catch (IOException e1) {
                logger.log(Level.SEVERE, "Error loading StudentMainUI.fxml", e1);
            }
            stage.show();
            ((Stage) ((Button) e.getSource()).getScene().getWindow()).close();
        } else {
            // 登录失败
            showAlert(Alert.AlertType.ERROR, "Login Failed", "Invalid username or password.");
            logger.warning("Failed login attempt for username: " + username);
        }
    }

    @FXML
    public void register() {
        // 验证输入字段是否为空
        if (usernameTxt.getText().isEmpty() || nameTxt.getText().isEmpty() ||
                genderCombo.getValue() == null || ageTxt.getText().isEmpty() ||
                departmentTxt.getText().isEmpty() || passwordTxt.getText().isEmpty() ||
                passwordConfirmTxt.getText().isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "Input Error", "All fields are required!");
            logger.warning("Registration attempt with empty fields.");
            return;
        }

        // 验证年龄
        int age;
        try {
            age = Integer.parseInt(ageTxt.getText());
            if (age <= 0) {
                showAlert(Alert.AlertType.WARNING, "Input Error", "Age must be a positive number.");
                return;
            }
        } catch (NumberFormatException e) {
            showAlert(Alert.AlertType.WARNING, "Input Error", "Age must be a valid number.");
            return;
        }

        // 验证密码一致性
        if (!passwordTxt.getText().equals(passwordConfirmTxt.getText())) {
            showAlert(Alert.AlertType.WARNING, "Password Mismatch", "Passwords do not match!");
            return;
        }

        // 验证用户名是否已存在
        if (isUsernameTaken(usernameTxt.getText())) {
            showAlert(Alert.AlertType.WARNING, "Username Taken", "This username is already taken. Please choose another one.");
            return;
        }

        // 创建学生对象
        Student student = new Student(
                usernameTxt.getText(),
                nameTxt.getText(),
                genderCombo.getValue(),
                age,
                departmentTxt.getText(),
                passwordTxt.getText()
        );

        // 写入学生信息
        if (writeNewUser(student)) {
            showAlert(Alert.AlertType.INFORMATION, "Registration Successful", "You have successfully registered! Please log in.");
            logger.info("New user registered: " + student.toString());
            clearRegisterForm(); // 清空表单
        } else {
            showAlert(Alert.AlertType.ERROR, "Registration Failed", "An error occurred while saving your information. Please try again.");
        }
    }

    // 清空表单
    private void clearRegisterForm() {
        usernameTxt.clear();
        nameTxt.clear();
        genderCombo.setValue(null);
        ageTxt.clear();
        departmentTxt.clear();
        passwordTxt.clear();
        passwordConfirmTxt.clear();
    }


    private boolean writeNewUser(Student student) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(studentFilePath, true))) {
            // 将学生信息写入文件
            bw.write(student.getUsername() + "," +
                    student.getName() + "," +
                    student.getGender() + "," +
                    student.getAge() + "," +
                    student.getDepartment() + "," +
                    student.getPassword());
            bw.newLine();
            return true;
        } catch (IOException e) {
            logger.log(Level.SEVERE, "Error writing new user to student file", e);
            return false;
        }
    }


    private boolean validate(String username, String password) {
        try (BufferedReader br = new BufferedReader(new FileReader(studentFilePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] credentials = line.split(",");
                if (credentials.length == 6) {
                    Student student = new Student(
                            credentials[0], credentials[1], credentials[2],
                            Integer.parseInt(credentials[3]), credentials[4], credentials[5]
                    );
                    if (student.getUsername().equals(username) && student.getPassword().equals(password)) {
                        return true; // 登录成功
                    }
                }
            }
        } catch (IOException e) {
            logger.log(Level.SEVERE, "Error reading student file", e);
        }
        return false;
    }


    private boolean isPasswordStrong(String password) {
        return password.length() >= 6 && password.matches(".*[a-zA-Z].*") && password.matches(".*\\d.*");
    }

    private boolean isUsernameTaken(String username) {
        try (BufferedReader br = new BufferedReader(new FileReader(studentFilePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] credentials = line.split(",");
                if (credentials.length >= 6 && credentials[0].trim().equals(username)) {
                    return true;
                }
            }
        } catch (IOException e) {
            logger.log(Level.SEVERE, "Error reading student file", e);
        }
        return false;
    }

    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}

