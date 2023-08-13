package acmecollege.entity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class CourseRegistrationTest {

    private CourseRegistration registration;
    private Student student;
    private Course course;
    private Professor professor;

    @BeforeEach
    public void setUp() {
        registration = new CourseRegistration();
        student = new Student(); // You'd set any necessary attributes here
        course = new Course();   // Set necessary attributes here
        professor = new Professor(); // Set necessary attributes here
    }

    @Test
    public void testGetId() {
        CourseRegistrationPK pk = new CourseRegistrationPK();
        registration.setId(pk);
        assertNotNull(registration.getId());
    }

    @Test
    public void testSetStudent() {
        registration.setStudent(student);
        assertEquals(student, registration.getStudent());
    }

    @Test
    public void testSetCourse() {
        registration.setCourse(course);
        assertEquals(course, registration.getCourse());
    }

    @Test
    public void testSetProfessor() {
        registration.setProfessor(professor);
        assertEquals(professor, registration.getProfessor());
    }

    @Test
    public void testSetNumericGrade() {
        int grade = 90;
        registration.setNumericGrade(grade);
        assertEquals(grade, registration.getNumericGrade());
    }

    @Test
    public void testSetLetterGrade() {
        String letterGrade = "A";
        registration.setLetterGrade(letterGrade);
        assertEquals(letterGrade, registration.getLetterGrade());
    }
}
