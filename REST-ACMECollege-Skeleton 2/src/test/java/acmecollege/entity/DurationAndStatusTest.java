package acmecollege.entity;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDateTime;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class DurationAndStatusTest {

    private DurationAndStatus instance;

    @BeforeEach
    void setUp() {
        instance = new DurationAndStatus();
    }

    @Test
    void testStartDate() {
        LocalDateTime startDate = LocalDateTime.now();
        instance.setStartDate(startDate);
        assertEquals(startDate, instance.getStartDate());
    }

    @Test
    void testEndDate() {
        LocalDateTime endDate = LocalDateTime.now();
        instance.setEndDate(endDate);
        assertEquals(endDate, instance.getEndDate());
    }

    @Test
    void testActive() {
        byte active = 0b1;
        instance.setActive(active);
        assertEquals(active, instance.getActive());
    }

    @Test
    void testSetDurationAndStatus() {
        LocalDateTime startDate = LocalDateTime.now();
        LocalDateTime endDate = LocalDateTime.now().plusDays(1);
        String active = "+";
        instance.setDurationAndStatus(startDate, endDate, active);
        assertEquals(startDate, instance.getStartDate());
        assertEquals(endDate, instance.getEndDate());
        assertEquals((byte) 0b1, instance.getActive());
    }

    @Test
    void testEquals() {
        DurationAndStatus otherInstance = new DurationAndStatus();
        assertTrue(instance.equals(otherInstance));

        instance.setStartDate(LocalDateTime.now());
        assertFalse(instance.equals(otherInstance));
        otherInstance.setStartDate(instance.getStartDate());
        assertTrue(instance.equals(otherInstance));

        instance.setEndDate(LocalDateTime.now());
        assertFalse(instance.equals(otherInstance));
        otherInstance.setEndDate(instance.getEndDate());
        assertTrue(instance.equals(otherInstance));

        instance.setActive((byte) 0b1);
        assertFalse(instance.equals(otherInstance));
        otherInstance.setActive(instance.getActive());
        assertTrue(instance.equals(otherInstance));
    }

    @Test
    void testHashCode() {
        DurationAndStatus otherInstance = new DurationAndStatus();
        instance.setStartDate(LocalDateTime.now());
        instance.setEndDate(LocalDateTime.now().plusDays(1));
        instance.setActive((byte) 0b1);
        
        otherInstance.setStartDate(instance.getStartDate());
        otherInstance.setEndDate(instance.getEndDate());
        otherInstance.setActive(instance.getActive());
        
        assertEquals(instance.hashCode(), otherInstance.hashCode());
    }

}
