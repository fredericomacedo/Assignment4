package acmecollege.entity;

import static org.junit.Assert.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.HashSet;

import org.junit.Before;
import org.junit.Test;

public class ProfessorTest {

    private Professor professor;

    @Before
    public void setUp() {
        professor = new Professor();
    }

    @Test
    public void testFirstName() {
        String firstName = "John";
        professor.setFirstName(firstName);
        assertEquals(firstName, professor.getFirstName());
    }

    @Test
    public void testLastName() {
        String lastName = "Doe";
        professor.setLastName(lastName);
        assertEquals(lastName, professor.getLastName());
    }

    @Test
    public void testDepartment() {
        String department = "Computer Science";
        professor.setDepartment(department);
        assertEquals(department, professor.getDepartment());
    }

    @Test
    public void testCourseRegistrations() {
        HashSet<CourseRegistration> courseRegistrations = new HashSet<>();
        professor.setCourseRegistrations(courseRegistrations);
        assertEquals(courseRegistrations, professor.getCourseRegistrations());
    }

    // Additional tests for other methods can be added here
}
