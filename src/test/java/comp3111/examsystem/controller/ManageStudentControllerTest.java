package comp3111.examsystem.controller;

import comp3111.examsystem.entity.Student;
import comp3111.examsystem.controller.ManageStudentController;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.testfx.framework.junit5.ApplicationTest;

import static org.junit.jupiter.api.Assertions.*;

public class ManageStudentControllerTest extends ApplicationTest {

    private ManageStudentController controller;
    private ObservableList<Student> studentList;

    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/path/to/your/fxml/file.fxml")); // Update with your FXML path
        Scene scene = new Scene(loader.load());
        stage.setScene(scene);
        stage.show();

        controller = loader.getController();
        studentList = FXCollections.observableArrayList();
        controller.studentTable.setItems(studentList); // Set the table items to the observable list
    }

    @BeforeEach
    public void setUp() {
        // Clear the student list before each test
        studentList.clear();
    }

    @Test
    public void testAddStudent() {
        // Simulate user input
        controller.usernameField.setText("john_doe");
        controller.nameField.setText("John Doe");
        controller.ageField.setText("20");
        controller.genderComboBox.setValue("Male");
        controller.departmentField.setText("Computer Science");
        controller.passwordField.setText("Password123");

        // Call the method to add a student
        controller.addStudent();

        // Assert that the student was added
        assertEquals(1, studentList.size());
        assertEquals("john_doe", studentList.get(0).getUsername());
    }

    @Test
    public void testAddDuplicateStudent() {
        // Add a student first
        controller.usernameField.setText("john_doe");
        controller.nameField.setText("John Doe");
        controller.ageField.setText("20");
        controller.genderComboBox.setValue("Male");
        controller.departmentField.setText("Computer Science");
        controller.passwordField.setText("Password123");
        controller.addStudent();

        // Attempt to add a duplicate student
        controller.usernameField.setText("john_doe");
        controller.nameField.setText("Jane Doe");
        controller.ageField.setText("22");
        controller.genderComboBox.setValue("Female");
        controller.departmentField.setText("Mathematics");
        controller.passwordField.setText("Password456");
        controller.addStudent();

        // Assert that only one student was added
        assertEquals(1, studentList.size());
    }

    @Test
    public void testUpdateStudent() {
        // Add a student first
        controller.usernameField.setText("john_doe");
        controller.nameField.setText("John Doe");
        controller.ageField.setText("20");
        controller.genderComboBox.setValue("Male");
        controller.departmentField.setText("Computer Science");
        controller.passwordField.setText("Password123");
        controller.addStudent();

        // Select the student to update
        controller.studentTable.getSelectionModel().selectFirst();

        // Update student details
        controller.usernameField.setText("john_doe_updated");
        controller.nameField.setText("John Doe Updated");
        controller.ageField.setText("21");
        controller.genderComboBox.setValue("Male");
        controller.departmentField.setText("Computer Science");
        controller.passwordField.setText("NewPassword123");
        controller.updateStudent();

        // Assert that the student details were updated
        assertEquals("john_doe_updated", studentList.get(0).getUsername());
        assertEquals("John Doe Updated", studentList.get(0).getName());
    }

    @Test
    public void testDeleteStudent() {
        // Add a student first
        controller.usernameField.setText("john_doe");
        controller.nameField.setText("John Doe");
        controller.ageField.setText("20");
        controller.genderComboBox.setValue("Male");
        controller.departmentField.setText("Computer Science");
        controller.passwordField.setText("Password123");
        controller.addStudent();

        // Select the student to delete
        controller.studentTable.getSelectionModel().selectFirst();
        controller.deleteStudent();

        // Assert that the student was deleted
        assertEquals(0, studentList.size());
    }

    @Test
    public void testFilterStudents() {
        // Add students for filtering
        studentList.add(new Student("john_doe", "John Doe", 20, "Male", "Computer Science", "Password123"));
        studentList.add(new Student("jane_doe", "Jane Doe", 21, "Female", "Mathematics", "Password456"));

        // Set filter criteria
        controller.usernameField.setText("jane");
        controller.filterStudents();

        // Assert that the filter works
        assertEquals(1, controller.studentTable.getItems().size()); // Should only show Jane Doe
    }

    @Test
    public void testResetFilters() {
        // Add students for testing reset
        studentList.add(new Student("john_doe", "John Doe", 20, "Male", "Computer Science", "Password123"));
        studentList.add(new Student("jane_doe", "Jane Doe", 21, "Female", "Mathematics", "Password456"));

        // Set filter criteria
        controller.usernameField.setText("jane");
        controller.filterStudents();

        // Reset filters
        controller.resetFilters();

        // Assert that the filter is reset
        assertEquals(2, controller.studentTable.getItems().size()); // Should show both students
    }
}