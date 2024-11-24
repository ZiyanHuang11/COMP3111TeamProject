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
        // 创建模拟的 DataManager
        mockDataManager = new DataManager();

        // 在内存中初始化一些课程数据
        List<Course> mockCourses = new ArrayList<>();
        mockCourses.add(new Course("COMP3111", "Software Engineering", "CSE"));
        mockCourses.add(new Course("COMP5111", "Software Engineering II", "CSE"));
        mockCourses.add(new Course("IT2020", "Cybersecurity", "IT"));
        mockCourses.add(new Course("COMP4462", "Data Visualization", "CSE"));

        // 将模拟数据加入到 DataManager
        mockDataManager.getCourses().clear();
        mockDataManager.getCourses().addAll(mockCourses);

        // 使用模拟的 DataManager 初始化服务
        manageCourseService = new ManageCourseService(mockDataManager);
    }

    @Test
    public void testGetCourses() {
        // 验证获取课程列表的功能
        List<Course> courses = manageCourseService.getCourses();
        assertEquals(4, courses.size(), "There should be 4 courses in the list.");
    }

    @Test
    public void testAddCourse() {
        // 添加新课程
        Course newCourse = new Course("MGT201", "Operations Management", "MGT");
        manageCourseService.addCourse(newCourse);

        // 验证新课程是否添加成功
        List<Course> courses = manageCourseService.getCourses();
        assertEquals(5, courses.size(), "There should be 5 courses in the list.");
        assertTrue(courses.stream().anyMatch(course -> course.getCourseID().equals("MGT201")),
                "The course MGT201 should exist in the list.");
    }

    @Test
    public void testUpdateCourse() {
        // 更新已有课程的信息
        Course updatedCourse = new Course("COMP3111", "Advanced Software Engineering", "CSE");
        manageCourseService.updateCourse(updatedCourse, "COMP3111");

        // 验证课程是否更新成功
        List<Course> courses = manageCourseService.getCourses();
        assertEquals(4, courses.size(), "The total number of courses should remain the same.");
        Course course = courses.stream().filter(c -> c.getCourseID().equals("COMP3111")).findFirst().orElse(null);
        assertNotNull(course, "The updated course should still exist.");
        assertEquals("Advanced Software Engineering", course.getCourseName(), "The course name should be updated.");
    }

    @Test
    public void testDeleteCourse() {
        // 删除某一课程
        manageCourseService.deleteCourse("COMP5111");

        // 验证课程是否删除成功
        List<Course> courses = manageCourseService.getCourses();
        assertEquals(3, courses.size(), "There should be 3 courses in the list after deletion.");
        assertFalse(courses.stream().anyMatch(course -> course.getCourseID().equals("COMP5111")),
                "The course COMP5111 should no longer exist.");
    }

    @Test
    public void testFilterCourses() {
        // 按条件过滤课程
        List<Course> filteredCourses = manageCourseService.filterCourses("COMP", "", "CSE");
        assertEquals(3, filteredCourses.size(), "There should be 3 courses matching the filter.");
        assertTrue(filteredCourses.stream().allMatch(course -> course.getDepartment().equals("CSE")),
                "All filtered courses should belong to the CSE department.");
    }

    @Test
    public void testValidateInputs() {
        // 验证课程输入
        String validationMessage = manageCourseService.validateInputs("", "Course Name", "CSE");
        assertEquals("Each field should be filled in", validationMessage, "Validation should catch empty course ID.");

        validationMessage = manageCourseService.validateInputs("COMP3111", "Course Name", "CSE");
        assertEquals("The course ID already exists", validationMessage, "Validation should catch duplicate course ID.");
    }

    @Test
    public void testValidateUpdateInputs() {
        // 验证更新课程输入
        String validationMessage = manageCourseService.validateUpdateInputs("", "Course Name", "CSE");
        assertEquals("Each field should be filled in", validationMessage, "Validation should catch empty fields.");

        validationMessage = manageCourseService.validateUpdateInputs("COMP5111", "Updated Course", "CSE");
        assertNull(validationMessage, "Validation should pass for valid inputs.");
    }
}
