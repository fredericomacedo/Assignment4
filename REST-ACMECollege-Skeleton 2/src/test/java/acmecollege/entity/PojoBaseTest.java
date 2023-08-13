package acmecollege.entity;

import static org.junit.Assert.*;

import java.time.LocalDateTime;

import org.junit.Before;
import org.junit.Test;

public class PojoBaseTest {

    private PojoBase pojoBase;

    @Before
    public void setUp() {
        pojoBase = new PojoBase() {};
    }

    @Test
    public void testId() {
        int id = 5;
        pojoBase.setId(id);
        assertEquals(id, pojoBase.getId());
    }

    @Test
    public void testVersion() {
        int version = 1;
        pojoBase.setVersion(version);
        assertEquals(version, pojoBase.getVersion());
    }

    @Test
    public void testCreated() {
        LocalDateTime created = LocalDateTime.now();
        pojoBase.setCreated(created);
        assertEquals(created, pojoBase.getCreated());
    }

    @Test
    public void testUpdated() {
        LocalDateTime updated = LocalDateTime.now();
        pojoBase.setUpdated(updated);
        assertEquals(updated, pojoBase.getUpdated());
    }

    // Additional tests for equals and hashCode can be added here
}
