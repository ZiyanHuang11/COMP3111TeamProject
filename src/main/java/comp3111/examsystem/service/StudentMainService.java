package comp3111.examsystem.service;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

/**
 * StudentMainService manages and provides student-related exam and grade information.
 */
public class StudentMainService {
    private final String studentExamsFilePath;
    private final String completedQuizzesFilePath;

    public StudentMainService(String studentExamsFilePath, String completedQuizzesFilePath) {
        this.studentExamsFilePath = studentExamsFilePath;
        this.completedQuizzesFilePath = completedQuizzesFilePath;
    }

    /**
     * Retrieves the list of exams for a specific student and marks completed ones.
     *
     * @param username The username of the logged-in student.
     * @return A list of exams in the format "CourseId CourseName | ExamType (Completed)" or "CourseId CourseName | ExamType".
     */
    public List<String> getExamsForStudent(String username) {
        List<String> exams = new ArrayList<>();
        Set<String> completedExamsSet = new HashSet<>(getCompletedExamsIdentifiers(username));

        try (BufferedReader br = new BufferedReader(new FileReader(studentExamsFilePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                if (line.trim().isEmpty()) {
                    continue;
                }

                String[] parts = splitCSVLine(line);
                if (parts.length < 4) {
                    continue;
                }

                String fileUsername = parts[0].trim();
                if (!fileUsername.equalsIgnoreCase(username)) {
                    continue;
                }

                String courseID = parts[1].trim();
                String examType = parts[2].trim();
                String courseName = parts[3].trim();

                String identifier = courseID + "," + examType;
                String formattedExam = String.format("%s %s | %s", courseID, courseName, examType);

                if (completedExamsSet.contains(identifier)) {
                    formattedExam += " (Completed)";
                }
                exams.add(formattedExam);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return exams;
    }

    /**
     * Retrieves all completed exams for the specified user.
     *
     * @param username The username of the logged-in student.
     * @return A list of completed exam records in the format "courseId,examType,score,fullScore,time".
     */
    public List<String> getGradesForStudent(String username) {
        List<String> grades = new ArrayList<>();
        Path path = Paths.get(completedQuizzesFilePath);

        if (!Files.exists(path)) {
            return grades; // Return an empty list if the file doesn't exist
        }

        try (BufferedReader br = new BufferedReader(new FileReader(completedQuizzesFilePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                if (line.trim().isEmpty()) {
                    continue;
                }

                String[] parts = line.split(",");
                if (parts.length < 6) {
                    continue; // Skip invalid lines
                }

                String fileUsername = parts[0].trim();
                if (!fileUsername.equalsIgnoreCase(username)) {
                    continue; // Skip lines not matching the logged-in username
                }

                grades.add(line); // Add the full grade record
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return grades;
    }

    /**
     * Retrieves identifiers for all completed exams for the specified user.
     *
     * @param username The username of the logged-in student.
     * @return A set of identifiers in the format "courseId,examType".
     */
    public Set<String> getCompletedExamsIdentifiers(String username) {
        Set<String> identifiers = new HashSet<>();
        Path path = Paths.get(completedQuizzesFilePath);

        if (!Files.exists(path)) {
            return identifiers;
        }

        try (BufferedReader br = new BufferedReader(new FileReader(completedQuizzesFilePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                if (line.trim().isEmpty()) {
                    continue;
                }

                String[] parts = line.split(",");
                if (parts.length < 3) {
                    continue;
                }

                String fileUsername = parts[0].trim();
                if (!fileUsername.equalsIgnoreCase(username)) {
                    continue;
                }

                String courseId = parts[1].trim();
                String examType = parts[2].trim();

                identifiers.add(courseId + "," + examType);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return identifiers;
    }

    /**
     * Appends a new completed exam record to the completed_quizzes file.
     *
     * @param username   The username of the student.
     * @param courseId   The course ID of the exam.
     * @param examType   The type of exam.
     * @param score      The score obtained by the student.
     * @param fullScore  The full score of the exam.
     * @param time       The time taken by the student to complete the exam.
     */
    public void addCompletedExam(String username, String courseId, String examType, int score, int fullScore, int time) {
        try (FileWriter writer = new FileWriter(completedQuizzesFilePath, true)) {
            String record = String.format("%s,%s,%s,%d,%d,%d\n", username, courseId, examType, score, fullScore, time);
            writer.write(record);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Checks if the specified username exists in the completed quizzes file.
     *
     * @param username The username to check.
     * @return True if the username exists in the file, otherwise false.
     */
    public boolean hasCompletedExams(String username) {
        Path path = Paths.get(completedQuizzesFilePath);

        if (!Files.exists(path)) {
            return false; // File doesn't exist, so no completed exams
        }

        try (BufferedReader br = new BufferedReader(new FileReader(completedQuizzesFilePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                if (line.trim().isEmpty()) {
                    continue;
                }

                String[] parts = line.split(",");
                if (parts.length >= 1 && parts[0].trim().equalsIgnoreCase(username)) {
                    return true; // Found a matching username
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return false; // No matching username found
    }

    private String[] splitCSVLine(String line) {
        List<String> tokens = new ArrayList<>();
        StringBuilder currentToken = new StringBuilder();
        boolean inQuotes = false;

        for (char ch : line.toCharArray()) {
            if (ch == '\"') {
                inQuotes = !inQuotes;
            } else if (ch == ',' && !inQuotes) {
                tokens.add(currentToken.toString());
                currentToken.setLength(0);
            } else {
                currentToken.append(ch);
            }
        }

        tokens.add(currentToken.toString());
        return tokens.toArray(new String[0]);
    }
}
