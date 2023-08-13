package acmecollege.entity;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

public class AcademicStudentClubTest {

    @Test
    public void testConstructor() {

        AcademicStudentClub club = new AcademicStudentClub();


        assertNotNull(club);
    }
}
