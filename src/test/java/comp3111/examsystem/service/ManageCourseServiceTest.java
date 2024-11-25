package comp3111.examsystem.service;

import comp3111.examsystem.data.DataManager;
import comp3111.examsystem.entity.Course;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ManageCourseServiceTest {

    private ManageCourseService manageCourseService;
    private DataManager mockDataManager;

    @BeforeEach
    public void setUp() {
        List<Course> mockCourses = new ArrayList<>();
        mockCourses.add(new Course("COMP3111", "Software Engineering", "CSE"));
        mockCourses.add(new Course("COMP5111", "Software Engineering II", "CSE"));
        mockCourses.add(new Course("IT2020", "Cybersecurity", "IT"));
        mockCourses.add(new Course("COMP4462", "Data Visualization", "CSE"));

        mockDataManager = new MockDataManager(mockCourses);
        manageCourseService = new ManageCourseService(mockDataManager);
    }

    @Test
    public void testGetCourses() {
        List<Course> courses = manageCourseService.getCourses();
        assertEquals(4, courses.size(), "There should be 4 courses in the list.");
    }

    @Test
    public void testAddCourse() {
        Course newCourse = new Course("MGT201", "Operations Management", "MGT");
        manageCourseService.addCourse(newCourse);

        List<Course> courses = manageCourseService.getCourses();
        assertEquals(5, courses.size(), "There should be 5 courses in the list.");
        assertTrue(courses.stream().anyMatch(course -> course.getCourseID().equals("MGT201")),
                "The course MGT201 should exist in the list.");
    }

    @Test
    public void testUpdateCourse() {
        Course updatedCourse = new Course("COMP3111", "Advanced Software Engineering", "CSE");
        manageCourseService.updateCourse(updatedCourse, "COMP3111");

        List<Course> courses = manageCourseService.getCourses();
        assertEquals(4, courses.size(), "The total number of courses should remain the same.");
        Course course = courses.stream().filter(c -> c.getCourseID().equals("COMP3111")).findFirst().orElse(null);
        assertNotNull(course, "The updated course should still exist.");
        assertEquals("Advanced Software Engineering", course.getCourseName(), "The course name should be updated.");
    }

    @Test
    public void testDeleteCourse() {
        manageCourseService.deleteCourse("COMP5111");

        List<Course> courses = manageCourseService.getCourses();
        assertEquals(3, courses.size(), "There should be 3 courses in the list after deletion.");
        assertFalse(courses.stream().anyMatch(course -> course.getCourseID().equals("COMP5111")),
                "The course COMP5111 should no longer exist.");
    }

    @Test
    public void testFilterCourses() {
        List<Course> filteredCourses = manageCourseService.filterCourses("COMP", "", "CSE");
        assertEquals(3, filteredCourses.size(), "There should be 3 courses matching the filter.");
        assertTrue(filteredCourses.stream().allMatch(course -> course.getDepartment().equals("CSE")),
                "All filtered courses should belong to the CSE department.");
    }

    @Test
    public void testValidateInputs() {
        String validationMessage = manageCourseService.validateInputs("", "Course Name", "CSE");
        assertEquals("All fields must be filled.", validationMessage, "Validation should catch empty course ID.");

        validationMessage = manageCourseService.validateInputs("COMP3111", "Course Name", "CSE");
        assertEquals("Course ID already exists.", validationMessage, "Validation should catch duplicate course ID.");
    }


    @Test
    public void testValidateUpdateInputs() {
        String validationMessage = manageCourseService.validateUpdateInputs("", "Course Name", "CSE");
        assertEquals("All fields must be filled.", validationMessage, "Validation should catch empty fields.");

        validationMessage = manageCourseService.validateUpdateInputs("COMP5111", "Updated Course", "CSE");
        assertNull(validationMessage, "Validation should pass for valid inputs.");
    }

    // MockDataManager 实现
    class MockDataManager extends DataManager {
        private final List<Course> mockCourses;

        public MockDataManager(List<Course> courses) {
            this.mockCourses = new ArrayList<>(courses);
        }

        @Override
        public List<Course> getCourses() {
            return mockCourses;
        }

        @Override
        public void addCourse(Course course) {
            mockCourses.add(course);
        }

        @Override
        public void updateCourse(String id, Course updatedCourse) {
            for (int i = 0; i < mockCourses.size(); i++) {
                if (mockCourses.get(i).getCourseID().equals(id)) {
                    mockCourses.set(i, updatedCourse);
                    return;
                }
            }
        }

        @Override
        public void deleteCourse(String id) {
            mockCourses.removeIf(course -> course.getCourseID().equals(id));
        }
    }
}
