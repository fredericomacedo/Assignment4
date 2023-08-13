package acmecollege.entity;

import static org.junit.Assert.*;

import java.time.LocalDateTime;

import org.junit.Before;
import org.junit.Test;

public class PojoBaseCompositeKeyTest {

    private PojoBaseCompositeKey<String> pojoBaseCompositeKey;

    @Before
    public void setUp() {
        pojoBaseCompositeKey = new PojoBaseCompositeKey<String>() {
            private String id;

            @Override
            public String getId() {
                return id;
            }

            @Override
            public void setId(String id) {
                this.id = id;
            }
        };
    }

    @Test
    public void testId() {
        String id = "ID123";
        pojoBaseCompositeKey.setId(id);
        assertEquals(id, pojoBaseCompositeKey.getId());
    }

    @Test
    public void testVersion() {
        int version = 2;
        pojoBaseCompositeKey.setVersion(version);
        assertEquals(version, pojoBaseCompositeKey.getVersion());
    }

    @Test
    public void testCreated() {
        LocalDateTime created = LocalDateTime.now();
        pojoBaseCompositeKey.setCreated(created);
        assertEquals(created, pojoBaseCompositeKey.getCreated());
    }

    @Test
    public void testUpdated() {
        LocalDateTime updated = LocalDateTime.now();
        pojoBaseCompositeKey.setUpdated(updated);
        assertEquals(updated, pojoBaseCompositeKey.getUpdated());
    }

    // Additional tests for equals and hashCode can be added here
}
