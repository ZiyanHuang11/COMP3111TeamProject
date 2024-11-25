package comp3111.examsystem.service;

import comp3111.examsystem.entity.Teacher;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.io.*;
import java.util.List;
import java.util.stream.Collectors;

/**
 * The ManageTeacherService class provides functionalities to manage teachers in the exam system.
 * It allows adding, updating, deleting, and filtering teachers, as well as validating input data.
 */
public class ManageTeacherService {

    String teacherFilePath;
    private ObservableList<Teacher> teacherList;

    /**
     * Constructs a ManageTeacherService instance and loads teachers from the specified file.
     */
    public ManageTeacherService() {
        teacherFilePath = "data/teachers.txt";
        teacherList = FXCollections.observableArrayList();
        loadTeachersFromFile();
    }

    /**
     * Returns the list of teachers.
     *
     * @return An ObservableList of Teacher objects.
     */
    public ObservableList<Teacher> getTeacherList() {
        return teacherList;
    }

    /**
     * Loads teachers from the specified file into the teacherList.
     */
    void loadTeachersFromFile() {
        File file = new File(teacherFilePath);
        if (!file.exists()) {
            return;
        }

        try (BufferedReader br = new BufferedReader(new FileReader(teacherFilePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] data = line.split(",");
                if (data.length == 7) {
                    Teacher teacher = new Teacher(data[0], data[1], data[2], data[3], Integer.parseInt(data[4]), data[5], data[6]);
                    teacherList.add(teacher);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Adds a new teacher to the list and writes to the file.
     *
     * @param newTeacher The Teacher object to be added.
     * @throws IOException If the username already exists or an I/O error occurs.
     */
    public void addTeacher(Teacher newTeacher) throws IOException {
        if (isUsernameExists(newTeacher.getUsername())) {
            throw new IOException("The user name already exists");
        }
        teacherList.add(newTeacher);
        writeTeacherToFile(newTeacher);
    }

    /**
     * Writes a teacher's information to the file.
     *
     * @param teacher The Teacher object to be written.
     * @throws IOException If an I/O error occurs.
     */
    private void writeTeacherToFile(Teacher teacher) throws IOException {
        String teacherInput = String.join(",", teacher.getUsername(), teacher.getPassword(), teacher.getName(),
                teacher.getGender(), String.valueOf(teacher.getAge()), teacher.getPosition(), teacher.getDepartment());
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(teacherFilePath, true))) {
            bw.write(teacherInput);
            bw.newLine();
        }
    }

    /**
     * Updates an existing teacher's information.
     *
     * @param updatedTeacher The updated Teacher object.
     * @param originalUsername The original username of the teacher to be updated.
     * @throws IOException If the teacher is not found or an I/O error occurs.
     */
    public void updateTeacher(Teacher updatedTeacher, String originalUsername) throws IOException {
        boolean found = false;
        for (int i = 0; i < teacherList.size(); i++) {
            if (teacherList.get(i).getUsername().equals(originalUsername)) {
                teacherList.set(i, updatedTeacher);
                found = true;
                break;
            }
        }
        if (!found) {
            throw new IOException("Teacher not found");
        }
        writeAllTeachersToFile();
    }

    /**
     * Writes all teachers' information to the file.
     *
     * @throws IOException If an I/O error occurs.
     */
    void writeAllTeachersToFile() throws IOException {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(teacherFilePath))) {
            for (Teacher teacher : teacherList) {
                String teacherInput = String.join(",", teacher.getUsername(), teacher.getPassword(), teacher.getName(),
                        teacher.getGender(), String.valueOf(teacher.getAge()), teacher.getPosition(), teacher.getDepartment());
                bw.write(teacherInput);
                bw.newLine();
            }
        }
    }

    /**
     * Deletes a teacher from the list and updates the file.
     *
     * @param teacherToDelete The Teacher object to be deleted.
     * @throws IOException If the teacher is not found or an I/O error occurs.
     */
    public void deleteTeacher(Teacher teacherToDelete) throws IOException {
        if (!teacherList.remove(teacherToDelete)) {
            throw new IOException("Teacher not found");
        }
        writeAllTeachersToFile();
    }

    /**
     * Filters teachers based on the provided criteria.
     *
     * @param username The username to filter by.
     * @param name The name to filter by.
     * @param department The department to filter by.
     * @return A list of teachers matching the criteria.
     */
    public List<Teacher> filterTeachers(String username, String name, String department) {
        return teacherList.stream()
                .filter(teacher -> teacher.getUsername().toLowerCase().contains(username) &&
                        teacher.getName().toLowerCase().contains(name) &&
                        teacher.getDepartment().toLowerCase().contains(department))
                .collect(Collectors.toList());
    }

    /**
     * Validates the input data for adding a new teacher.
     *
     * @param username The username to validate.
     * @param name The name to validate.
     * @param gender The gender to validate.
     * @param ageText The age to validate.
     * @param position The position to validate.
     * @param department The department to validate.
     * @param password The password to validate.
     * @return An error message if validation fails, otherwise null.
     */
    public String validateInputs(String username, String name, String gender, String ageText, String position, String department, String password) {
        if (username.isEmpty() || name.isEmpty() || ageText.isEmpty() || gender == null || position == null || department.isEmpty() || password.isEmpty()) {
            return "Each field should be filled in";
        }
        try {
            Integer.parseInt(ageText);
        } catch (NumberFormatException e) {
            return "Age must be a valid number";
        }
        if (isUsernameExists(username)) {
            return "The user name already exists";
        }
        if (isValidPassword(password)) {
            return "The password must contain both letters and numbers and be at least eight characters long";
        }
        return null;
    }

    /**
     * Checks if a username already exists in the teacher list.
     *
     * @param username The username to check.
     * @return true if the username exists, false otherwise.
     */
    boolean isUsernameExists(String username) {
        return teacherList.stream().anyMatch(teacher -> teacher.getUsername().equals(username));
    }

    /**
     * Validates the password format.
     *
     * @param password The password to validate.
     * @return true if the password is invalid, false otherwise.
     */
    boolean isValidPassword(String password) {
        return password.length() < 8 || !password.matches("^(?=.*[a-zA-Z])(?=.*[0-9]).+$");
    }

    /**
     * Validates the input data for updating a teacher's information.
     *
     * @param name The name to validate.
     * @param gender The gender to validate.
     * @param ageText The age to validate.
     * @param position The position to validate.
     * @param department The department to validate.
     * @param password The password to validate.
     * @return An error message if validation fails, otherwise null.
     */
    public String validateUpdateInputs(String name, String gender, String ageText, String position, String department, String password) {
        if (name.isEmpty() || ageText.isEmpty() || gender == null || position == null || department.isEmpty() || password.isEmpty()) {
            return "Each field should be filled in";
        }
        try {
            Integer.parseInt(ageText);
        } catch (NumberFormatException e) {
            return "Age must be a valid number";
        }
        if (isValidPassword(password)) {
            return "The password must contain both letters and numbers and be at least eight characters long";
        }
        return null;
    }
}