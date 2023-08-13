package acmecollege.entity;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import acmecollege.entity.Student;

class StudentTest {

    @Test
    void testStudent() {
        Student student = new Student();
        student.setFirstName("John");
        student.setLastName("Doe");
        assertEquals("John", student.getFirstName());
        assertEquals("Doe", student.getLastName());
    }
    
    // More tests for the other methods and properties can be added
}
