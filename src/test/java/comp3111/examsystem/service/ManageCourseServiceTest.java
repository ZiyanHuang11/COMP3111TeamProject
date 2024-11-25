package comp3111.examsystem.service;

import comp3111.examsystem.entity.Course;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class ManageCourseServiceTest {
    private ManageCourseService courseService;
    private String testFilePath;

    @BeforeEach
    public void setUp() throws IOException {
        testFilePath = "test_courses.txt";

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(testFilePath))) {
            writer.write("CS101,Introduction to Computer Science,CS\n");
            writer.write("CS102,Data Structures,CS\n");
            writer.write("MATH101,Calculus I,MATH\n");
        }
        courseService = new ManageCourseService(testFilePath);
    }

    @Test
    public void testLoadCoursesFromFile() {
        List<Course> courses = courseService.getCourseList();
        assertEquals(3, courses.size());
        assertEquals("CS101", courses.get(0).getCourseID());
        assertEquals("Introduction to Computer Science", courses.get(0).getCourseName());
        assertEquals("CS", courses.get(0).getDepartment());
    }

    @Test
    public void testAddCourse() throws IOException {
        Course newCourse = new Course("CS103", "Algorithms", "CS");
        courseService.addCourse(newCourse);
        List<Course> courses = courseService.getCourseList();
        assertEquals(4, courses.size());
        assertEquals(newCourse, courses.get(3));
    }

    @Test
    public void testUpdateCourse() throws IOException {
        Course updatedCourse = new Course("CS101", "Intro to CS", "CS");
        courseService.updateCourse(updatedCourse, "CS101");
        List<Course> courses = courseService.getCourseList();
        assertEquals("Intro to CS", courses.get(0).getCourseName());
    }

    @Test
    public void testUpdateNonExistentCourse() throws IOException {
        Course updatedCourse = new Course("CS105", "Non-existent Course", "CS");
        assertThrows(IOException.class, () -> {
            courseService.updateCourse(updatedCourse, "CS999");
        });
    }

    @Test
    public void testDeleteCourse() throws IOException {
        courseService.deleteCourse("CS101");
        List<Course> courses = courseService.getCourseList();
        assertEquals(2, courses.size());
        assertFalse(courses.stream().anyMatch(course -> course.getCourseID().equals("CS101")));
    }

    @Test
    public void testDeleteNonExistentCourse() throws IOException {
        assertThrows(IOException.class, () -> {
            courseService.deleteCourse("CS999");
        });
    }

    @Test
    public void testFilterCourses() {
        List<Course> filteredCourses = courseService.filterCourses("CS", "", "");
        assertEquals(2, filteredCourses.size());
    }

    @Test
    public void testValidateCourseID() {
        String validationMessage = courseService.validateCourseID("CS101");
        assertEquals("The course ID already exists", validationMessage);

        validationMessage = courseService.validateCourseID("CS104");
        assertNull(validationMessage);
    }

    @Test
    public void testValidateInputs() {
        String validationMessage = courseService.validateInputs("", "Course", "CS");
        assertEquals("Each field should be filled in", validationMessage);

        validationMessage = courseService.validateInputs("CS101", "Course", "CS");
        assertEquals("The course ID already exists", validationMessage);

        validationMessage = courseService.validateInputs("CS104", "Course", "CS");
        assertNull(validationMessage);
    }

    @AfterEach
    public void tearDown() throws IOException {
        Files.deleteIfExists(Paths.get(testFilePath));
    }
}