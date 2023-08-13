package acmecollege.entity;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class CourseTest {

    private Course course;

    @BeforeEach
    public void setUp() {
        course = new Course();
    }

    @Test
    public void testSetCourseCode() {
        String courseCode = "CS101";
        course.setCourseCode(courseCode);
        assertEquals(courseCode, course.getCourseCode());
    }

    @Test
    public void testSetCourseTitle() {
        String courseTitle = "Introduction to Programming";
        course.setCourseTitle(courseTitle);
        assertEquals(courseTitle, course.getCourseTitle());
    }

    @Test
    public void testSetYear() {
        int year = 2023;
        course.setYear(year);
        assertEquals(year, course.getYear());
    }

    @Test
    public void testSetSemester() {
        String semester = "Fall";
        course.setSemester(semester);
        assertEquals(semester, course.getSemester());
    }

    @Test
    public void testSetCreditUnits() {
        int creditUnits = 3;
        course.setCreditUnits(creditUnits);
        assertEquals(creditUnits, course.getCreditUnits());
    }

    @Test
    public void testSetOnline() {
        byte online = 1;
        course.setOnline(online);
        assertEquals(online, course.getOnline());
    }

    @Test
    public void testSetCourse() {
        String courseCode = "CS101";
        String courseTitle = "Introduction to Programming";
        int year = 2023;
        String semester = "Fall";
        int creditUnits = 3;
        byte online = 1;

        course.setCourse(courseCode, courseTitle, year, semester, creditUnits, online);

        assertEquals(courseCode, course.getCourseCode());
        assertEquals(courseTitle, course.getCourseTitle());
        assertEquals(year, course.getYear());
        assertEquals(semester, course.getSemester());
        assertEquals(creditUnits, course.getCreditUnits());
        assertEquals(online, course.getOnline());
    }
}
