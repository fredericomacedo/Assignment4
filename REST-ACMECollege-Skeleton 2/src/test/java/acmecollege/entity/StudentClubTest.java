package acmecollege.entity;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class StudentClubTest {

    private static class TestableStudentClub extends StudentClub {
        public TestableStudentClub(boolean isAcademic) {
            super(isAcademic);
        }
    }

    @Test
    public void testHashCode() {
        StudentClub studentClub1 = new TestableStudentClub(false);
        studentClub1.setId(42);
        studentClub1.setName("Math Club");

        StudentClub anotherStudentClub = new TestableStudentClub(false);
        anotherStudentClub.setId(42);
        anotherStudentClub.setName("Math Club");

        assertEquals(studentClub1.hashCode(), anotherStudentClub.hashCode());
    }
}
