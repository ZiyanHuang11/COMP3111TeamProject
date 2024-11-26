package comp3111.examsystem.service;

import comp3111.examsystem.entity.GradeRecord;

import java.io.BufferedReader;
import java.io.FileReader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

public class GradeStatisticsService {
    private final String gradesFilePath;

    public GradeStatisticsService(String gradesFilePath) {
        this.gradesFilePath = gradesFilePath;
    }

    /**
     * 获取所有成绩记录。
     *
     * @return 成绩记录列表
     */
    public List<GradeRecord> getAllGradeRecords() {
        List<GradeRecord> records = new ArrayList<>();

        if (!Files.exists(Paths.get(gradesFilePath))) {
            System.err.println("Grades file not found: " + gradesFilePath);
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
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return records;
    }

    /**
     * 获取所有课程名称。
     *
     * @return 课程名称列表
     */
    public List<String> getAllCourses() {
        Set<String> courses = new HashSet<>();
        for (GradeRecord record : getAllGradeRecords()) {
            courses.add(record.getCourse());
        }
        List<String> courseList = new ArrayList<>(courses);
        Collections.sort(courseList); // 按字母排序
        return courseList;
    }

    /**
     * 根据课程名称获取成绩记录。
     *
     * @param courseName 课程名称
     * @return 过滤后的成绩记录列表
     */
    public List<GradeRecord> getGradeRecordsByCourse(String courseName) {
        List<GradeRecord> filtered = new ArrayList<>();
        for (GradeRecord record : getAllGradeRecords()) {
            if (record.getCourse().equalsIgnoreCase(courseName)) {
                filtered.add(record);
            }
        }
        return filtered;
    }
}
