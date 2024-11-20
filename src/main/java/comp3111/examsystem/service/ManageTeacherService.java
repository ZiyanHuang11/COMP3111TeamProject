package comp3111.examsystem.service;

import comp3111.examsystem.entity.Teacher;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.io.*;
import java.util.List;
import java.util.stream.Collectors;

public class ManageTeacherService {

    String teacherFilePath;
    private ObservableList<Teacher> teacherList;

    public ManageTeacherService() {
        teacherFilePath = "data/teachers.txt";
        teacherList = FXCollections.observableArrayList();
        loadTeachersFromFile();
    }

    public ObservableList<Teacher> getTeacherList() {
        return teacherList;
    }

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
                    // Updated constructor order
                    Teacher teacher = new Teacher(data[0], data[1], data[2], data[3], Integer.parseInt(data[4]), data[5], data[6]);
                    teacherList.add(teacher);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void addTeacher(Teacher newTeacher) throws IOException {
        if (isUsernameExists(newTeacher.getUsername())) {
            throw new IOException("The user name already exists");
        }
        teacherList.add(newTeacher);
        writeTeacherToFile(newTeacher);
    }

    private void writeTeacherToFile(Teacher teacher) throws IOException {
        // Updated order for writing to file
        String teacherInput = String.join(",", teacher.getUsername(), teacher.getPassword(), teacher.getName(),
                teacher.getGender(), String.valueOf(teacher.getAge()), teacher.getPosition(), teacher.getDepartment());
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(teacherFilePath, true))) {
            bw.write(teacherInput);
            bw.newLine();
        }
    }

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

    void writeAllTeachersToFile() throws IOException {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(teacherFilePath))) {
            for (Teacher teacher : teacherList) {
                // Updated order for writing to file
                String teacherInput = String.join(",", teacher.getUsername(), teacher.getPassword(), teacher.getName(),
                        teacher.getGender(), String.valueOf(teacher.getAge()), teacher.getPosition(), teacher.getDepartment());
                bw.write(teacherInput);
                bw.newLine();
            }
        }
    }

    public void deleteTeacher(Teacher teacherToDelete) throws IOException {
        if (!teacherList.remove(teacherToDelete)) {
            throw new IOException("Teacher not found");
        }
        writeAllTeachersToFile();
    }

    public List<Teacher> filterTeachers(String username, String name, String department) {
        return teacherList.stream()
                .filter(teacher -> teacher.getUsername().toLowerCase().contains(username) &&
                        teacher.getName().toLowerCase().contains(name) &&
                        teacher.getDepartment().toLowerCase().contains(department))
                .collect(Collectors.toList());
    }

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

    boolean isUsernameExists(String username) {
        return teacherList.stream().anyMatch(teacher -> teacher.getUsername().equals(username));
    }

    boolean isValidPassword(String password) {
        return password.length() < 8 || !password.matches("^(?=.*[a-zA-Z])(?=.*[0-9]).+$");
    }

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