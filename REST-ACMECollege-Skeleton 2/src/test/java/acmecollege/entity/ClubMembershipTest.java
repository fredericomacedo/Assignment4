package acmecollege.entity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

public class ClubMembershipTest {

    @Test
    public void testSetAndGetStudentClub() {
        ClubMembership clubMembership = new ClubMembership();
        StudentClub club = new AcademicStudentClub();
        clubMembership.setStudentClub(club);

        assertEquals(club, clubMembership.getStudentClub());
    }

    @Test
    public void testSetAndGetCard() {
        ClubMembership clubMembership = new ClubMembership();
        MembershipCard card = new MembershipCard(); // assuming constructor
        clubMembership.setCard(card);

        assertEquals(card, clubMembership.getCard());
    }

    @Test
    public void testSetAndGetDurationAndStatus() {
        ClubMembership clubMembership = new ClubMembership();
        DurationAndStatus durationAndStatus = new DurationAndStatus();
        LocalDateTime startDate = LocalDateTime.now();
        LocalDateTime endDate = startDate.plusDays(1);
        durationAndStatus.setStartDate(startDate);
        durationAndStatus.setEndDate(endDate);
        durationAndStatus.setActive((byte) 1);

        clubMembership.setDurationAndStatus(durationAndStatus);

        assertEquals(durationAndStatus, clubMembership.getDurationAndStatus());
    }

    @Test
    public void testEqualsAndHashCode() {
        ClubMembership clubMembership1 = new ClubMembership();
        ClubMembership clubMembership2 = new ClubMembership();

        assertEquals(clubMembership1, clubMembership1); // reflexive
        assertEquals(clubMembership1, clubMembership2); // symmetric
        assertEquals(clubMembership1.hashCode(), clubMembership2.hashCode());

        DurationAndStatus durationAndStatus = new DurationAndStatus();
        LocalDateTime startDate = LocalDateTime.now();
        LocalDateTime endDate = startDate.plusDays(1);
        durationAndStatus.setStartDate(startDate);
        durationAndStatus.setEndDate(endDate);
        durationAndStatus.setActive((byte) 1);

        clubMembership1.setDurationAndStatus(durationAndStatus); // assuming it affects identity

        assertNotEquals(clubMembership1, clubMembership2);
        assertNotEquals(clubMembership1.hashCode(), clubMembership2.hashCode());
    }
}
