package acmecollege.entity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class CourseRegistrationPKTest {

    private CourseRegistrationPK pk1;
    private CourseRegistrationPK pk2;

    @BeforeEach
    public void setUp() {
        pk1 = new CourseRegistrationPK(1, 100);
        pk2 = new CourseRegistrationPK(1, 100);
    }

    @Test
    public void testGetStudentId() {
        assertEquals(1, pk1.getStudentId());
    }

    @Test
    public void testSetStudentId() {
        pk1.setStudentId(2);
        assertEquals(2, pk1.getStudentId());
    }

    @Test
    public void testGetCourseId() {
        assertEquals(100, pk1.getCourseId());
    }

    @Test
    public void testSetCourseId() {
        pk1.setCourseId(200);
        assertEquals(200, pk1.getCourseId());
    }

    @Test
    public void testEqualsSameObject() {
        assertTrue(pk1.equals(pk1));
    }

    @Test
    public void testEqualsDifferentObjectSameValues() {
        assertTrue(pk1.equals(pk2));
    }

    @Test
    public void testHashCode() {
        assertEquals(pk1.hashCode(), pk2.hashCode());
    }

    @Test
    public void testHashCodeDifferentValues() {
        pk2.setStudentId(2);
        assertNotEquals(pk1.hashCode(), pk2.hashCode());
    }

    @Test
    public void testToString() {
        String expected = "CourseRegistrationPK [studentId = 1, courseId = 100]";
        assertEquals(expected, pk1.toString());
    }
}
