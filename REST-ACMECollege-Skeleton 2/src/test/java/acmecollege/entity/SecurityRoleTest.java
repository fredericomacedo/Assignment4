package acmecollege.entity;

import static org.junit.Assert.*;

import java.util.HashSet;

import org.junit.Before;
import org.junit.Test;

public class SecurityRoleTest {

    private SecurityRole role;

    @Before
    public void setUp() {
        role = new SecurityRole();
    }

    @Test
    public void testId() {
        int id = 1;
        role.setId(id);
        assertEquals(id, role.getId());
    }

    @Test
    public void testRoleName() {
        String roleName = "ROLE_USER";
        role.setRoleName(roleName);
        assertEquals(roleName, role.getRoleName());
    }

    @Test
    public void testUsers() {
        HashSet<SecurityUser> users = new HashSet<>();
        role.setUsers(users);
        assertEquals(users, role.getUsers());
    }

    @Test
    public void testAddUserToRole() {
        SecurityUser user = new SecurityUser();
        role.addUserToRole(user);
        assertTrue(role.getUsers().contains(user));
    }

    // Additional tests for other methods can be added here
}
