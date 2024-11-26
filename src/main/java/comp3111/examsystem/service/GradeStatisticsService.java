package comp3111.examsystem.service;

import comp3111.examsystem.entity.GradeRecord;

import java.io.BufferedReader;
import java.io.FileReader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

/**
 * Service to manage grade statistics for students.
 */
public class GradeStatisticsService {
    private final String gradesFilePath;

    /**
     * Constructor to initialize the grades file path.
     *
     * @param gradesFilePath Path to the file containing grade records.
     */
    public GradeStatisticsService(String gradesFilePath) {
        this.gradesFilePath = gradesFilePath;
    }

    /**
     * Retrieves all grade records from the file.
     *
     * @return A list of grade records.
     */
    public List<GradeRecord> getAllGradeRecords() {
        List<GradeRecord> records = new ArrayList<>();

        if (!Files.exists(Paths.get(gradesFilePath))) {
            System.err.println("Error: Grades file not found - " + gradesFilePath);
            return records;
        }

        try (BufferedReader br = new BufferedReader(new FileReader(gradesFilePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 5) {
                    String course = parts[0].trim();
                    String exam = parts[1].trim();
                    int score = Integer.parseInt(parts[2].trim());
                    int fullScore = Integer.parseInt(parts[3].trim());
                    int time = Integer.parseInt(parts[4].trim());
                    records.add(new GradeRecord(course, exam, score, fullScore, time));
                } else {
                    System.err.println("Warning: Invalid line format - " + line);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return records;
    }

    /**
     * Retrieves a list of unique course names from the grade records.
     *
     * @return A sorted list of course names.
     */
    public List<String> getAllCourses() {
        Set<String> courses = new HashSet<>();
        for (GradeRecord record : getAllGradeRecords()) {
            courses.add(record.getCourse());
        }
        List<String> courseList = new ArrayList<>(courses);
        Collections.sort(courseList); // Sort alphabetically
        return courseList;
    }

    /**
     * Retrieves grade records for a specific course.
     *
     * @param courseName The course name to filter by.
     * @return A list of grade records for the specified course.
     */
    public List<GradeRecord> getGradeRecordsByCourse(String courseName) {
        List<GradeRecord> filteredRecords = new ArrayList<>();
        for (GradeRecord record : getAllGradeRecords()) {
            if (record.getCourse().equalsIgnoreCase(courseName)) {
                filteredRecords.add(record);
            }
        }
        return filteredRecords;
    }
}
