package acmecollege.entity;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import acmecollege.entity.SecurityUser;

class SecurityUserTest {

    @Test
    void testSecurityUser() {
        SecurityUser user = new SecurityUser();
        user.setUsername("testUser");
        user.setPwHash("1234");
        assertEquals("testUser", user.getUsername());
        assertEquals("1234", user.getPwHash());
    }
    
    // More tests for the other methods and properties can be added
}
