package comp3111.examsystem.controller;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.stream.Collectors;

public class ManageStudentController {

    @FXML
    private TextField usernameFilter;
    @FXML
    private TextField nameFilter;
    @FXML
    private TextField departmentFilter;
    @FXML
    private TableView<Student> studentTable;
    @FXML
    private TableColumn<Student, String> usernameColumn;
    @FXML
    private TableColumn<Student, String> nameColumn;
    @FXML
    private TableColumn<Student, Integer> ageColumn;
    @FXML
    private TableColumn<Student, String> genderColumn;
    @FXML
    private TableColumn<Student, String> departmentColumn;
    @FXML
    private TableColumn<Student, String> passwordColumn;
    @FXML
    private TextField usernameField;
    @FXML
    private TextField nameField;
    @FXML
    private TextField ageField;
    @FXML
    private ComboBox<String> genderComboBox;
    @FXML
    private TextField departmentField;
    @FXML
    private TextField passwordField;

    private String studentFilePath;

    private ObservableList<Student> studentList = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        loadStudentsFromFile();
        usernameColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getUsername()));
        nameColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getName()));
        ageColumn.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getAge()).asObject());
        genderColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getGender()));
        departmentColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getDepartment()));
        passwordColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getPassword()));
    }

    public ManageStudentController(){
        URL resource = getClass().getResource("/database/students.txt");
        if (resource != null) {
            studentFilePath = new File(resource.getFile()).getAbsolutePath();
        } else {
            System.out.println("Users file not found!");
        }
    }

    public void loadStudentsFromFile() {
        try (BufferedReader br = new BufferedReader(new FileReader(studentFilePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] data = line.split(",");
                if (data.length == 6) {
                    Student student = new Student(data[0], data[1], Integer.parseInt(data[2]), data[3], data[4], data[5]);
                    studentList.add(student);
                }
            }
            displayStudents(studentList);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // 方法来显示学生
    private void displayStudents(ObservableList<Student> students) {
        studentTable.setItems(students);
    }

    @FXML
    public void resetFilters() {
        usernameFilter.clear();
        nameFilter.clear();
        departmentFilter.clear();
        displayStudents(studentList); // 显示所有学生
    }

    @FXML
    public void filterStudents() {
        String username = usernameFilter.getText().toLowerCase();
        String name = nameFilter.getText().toLowerCase();
        String department = departmentFilter.getText().toLowerCase();

        List<Student> filteredList = studentList.stream()
                .filter(student -> student.getUsername().toLowerCase().contains(username) &&
                        student.getName().toLowerCase().contains(name) &&
                        student.getDepartment().toLowerCase().contains(department))
                .collect(Collectors.toList());

        displayStudents(FXCollections.observableArrayList(filteredList));
    }

    @FXML
    public void addStudent() {
        String username = usernameField.getText();
        String name = nameField.getText();
        int age = Integer.parseInt(ageField.getText());
        String gender = genderComboBox.getValue();
        String department = departmentField.getText();
        String password = passwordField.getText();

        Student newStudent = new Student(username, name, age, gender, department, password);
        studentList.add(newStudent);
        displayStudents(studentList);
        clearFields();
    }

    @FXML
    public void updateStudent() {
        // 更新学生的逻辑
        // 你可能需要从表格中选择一个学生进行更新
    }

    @FXML
    public void deleteStudent() {
        Student selectedStudent = studentTable.getSelectionModel().getSelectedItem();
        if (selectedStudent != null) {
            studentList.remove(selectedStudent);
            displayStudents(studentList);
        }
    }

    @FXML
    public void refreshStudent() {
        displayStudents(studentList); // 刷新显示
    }

    private void clearFields() {
        usernameField.clear();
        nameField.clear();
        ageField.clear();
        genderComboBox.getSelectionModel().clearSelection();
        departmentField.clear();
        passwordField.clear();
    }

    // 学生类
    public static class Student {
        private String username;
        private String name;
        private int age;
        private String gender;
        private String department;
        private String password;

        public Student(String username, String name, int age, String gender, String department, String password) {
            this.username = username;
            this.name = name;
            this.age = age;
            this.gender = gender;
            this.department = department;
            this.password = password;
        }

        public String getUsername() { return username; }
        public String getName() { return name; }
        public String getDepartment() { return department; }
        public int getAge() { return age; }
        public String getPassword() { return password; }
        public String getGender() { return gender; }
    }
}