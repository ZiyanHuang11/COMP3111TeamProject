package comp3111.examsystem.controller;

import comp3111.examsystem.entity.Student;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

class ManageStudentControllerTest {
    private ManageStudentController controller;
    private Path tempFilePath;

    @BeforeEach
    void setUp() throws IOException {
        controller = new ManageStudentController();
        tempFilePath = Files.createTempFile("students", ".txt");
        controller.studentFilePath = tempFilePath.toString();
        try (BufferedWriter writer = Files.newBufferedWriter(tempFilePath)) {
            writer.write("user1,Alice,20,Female,CS,Password123\n");
            writer.write("user2,Bob,22,Male,Math,Password123\n");
        }

        loadStudentsFromFile();
    }

    private void loadStudentsFromFile() {
        try (BufferedReader br = new BufferedReader(new FileReader(controller.studentFilePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] data = line.split(",");
                if (data.length == 6) {
                    Student student = new Student(data[0], data[1], Integer.parseInt(data[2]), data[3], data[4], data[5]);
                    controller.studentList.add(student);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    void testLoadStudentsFromFile() {
        List<Student> students = controller.studentList;
        assertEquals(2, students.size());
        assertEquals("Alice", students.get(0).getName());
        assertEquals("Bob", students.get(1).getName());
    }

    @Test
    void testAddStudentToFile() throws IOException {

        Student newStudent = new Student("user3", "Charlie", 21, "Male", "Physics", "Password123");
        controller.studentList.add(newStudent);
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(controller.studentFilePath, true))) {
            writer.write("user3,Charlie,21,Male,Physics,Password123\n");
        }
        controller.studentList.clear();
        loadStudentsFromFile();
        assertEquals(3, controller.studentList.size());
        assertTrue(controller.studentList.stream().anyMatch(s -> s.getUsername().equals("user3")));
    }

    @Test
    void testDeleteStudentFromFile() throws IOException {

        Student studentToDelete = controller.studentList.get(0);
        controller.studentList.remove(studentToDelete);

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(controller.studentFilePath))) {
            for (Student student : controller.studentList) {
                writer.write(String.join(",", student.getUsername(), student.getName(), String.valueOf(student.getAge()),
                        student.getGender(), student.getDepartment(), student.getPassword()));
                writer.newLine();
            }
        }

        controller.studentList.clear();
        loadStudentsFromFile();
        assertEquals(1, controller.studentList.size());
        assertFalse(controller.studentList.stream().anyMatch(s -> s.getUsername().equals("user1")));
    }

    @Test
    void testUpdateStudentInFile() throws IOException {
        Student studentToUpdate = controller.studentList.get(0);
        studentToUpdate.setName("Alice Updated");
        studentToUpdate.setAge(21);
        studentToUpdate.setPassword("NewPassword123");
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(controller.studentFilePath))) {
            for (Student student : controller.studentList) {
                writer.write(String.join(",", student.getUsername(), student.getName(), String.valueOf(student.getAge()),
                        student.getGender(), student.getDepartment(), student.getPassword()));
                writer.newLine();
            }
        }
        controller.studentList.clear();
        loadStudentsFromFile();
        Student updatedStudent = controller.studentList.get(0);
        assertEquals("Alice Updated", updatedStudent.getName());
        assertEquals(21, updatedStudent.getAge());
        assertEquals("NewPassword123", updatedStudent.getPassword());
    }

    @Test
    void testValidateInputs() {
        assertNull(controller.validateInputs("user3", "Charlie", "21", "Male", "Physics", "Password123"));
        assertEquals("Each field should be filled in", controller.validateInputs("", "name", "20", "Male", "CS", "Password123"));
        assertEquals("Age must be a valid number", controller.validateInputs("username", "name", "twenty", "Male", "CS", "Password123"));
        assertEquals("The user name already exists", controller.validateInputs("user1", "name", "20", "Male", "CS", "Password123"));
        assertEquals("The password must contain both letters and numbers and be at least eight characters long",
                controller.validateInputs("newUser", "name", "20", "Male", "CS", "short"));
    }

    @Test
    void testIsValidPassword() {
        assertFalse(controller.isValidPassword("Password123"));
        assertTrue(controller.isValidPassword("short"));
        assertTrue(controller.isValidPassword("Password"));
        assertTrue(controller.isValidPassword("12345678"));
    }

    @Test
    void testFilterStudentsLogic() {
        String username = "user1";
        String name = "Alice";
        String department = "CS";

        List<Student> filteredList = controller.studentList.stream()
                .filter(student -> student.getUsername().toLowerCase().contains(username.toLowerCase()) &&
                        student.getName().toLowerCase().contains(name.toLowerCase()) &&
                        student.getDepartment().toLowerCase().contains(department.toLowerCase()))
                .collect(Collectors.toList());

        assertEquals(1, filteredList.size());
        assertEquals("Alice", filteredList.get(0).getName());
    }
}