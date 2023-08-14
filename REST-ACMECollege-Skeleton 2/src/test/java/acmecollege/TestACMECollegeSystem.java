/**
 * File:  TestACMECollegeSystem.java
 * Course materials (23W) CST 8277
 *
 * @author Teddy Yap
 * @author Shariar (Shawn) Emami
 * @author (original) Mike Norman
 * 
 * Updated by:  Group 07
 *   041029397, Frederico Lucio, Macedo
 *   041046587, Natalia, Pirath  
 *   041042876, Tongwe, Kasaji 
 *   041025651, Daniel, Barboza 
 */
package acmecollege;

import static acmecollege.utility.MyConstants.*;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.hamcrest.collection.IsEmptyCollection.empty;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assumptions.assumeTrue;

import java.lang.invoke.MethodHandles;
import java.net.URI;
import java.time.LocalDateTime;
import java.util.List;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.glassfish.jersey.client.ClientConfig;
import org.glassfish.jersey.client.authentication.HttpAuthenticationFeature;
import org.glassfish.jersey.logging.LoggingFeature;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;

import acmecollege.ejb.ACMECollegeService;
import acmecollege.entity.AcademicStudentClub;
import acmecollege.entity.ClubMembership;
import acmecollege.entity.Course;
import acmecollege.entity.DurationAndStatus;
import acmecollege.entity.MembershipCard;
import acmecollege.entity.NonAcademicStudentClub;
import acmecollege.entity.Professor;
import acmecollege.entity.Student;
import acmecollege.entity.StudentClub;

@SuppressWarnings("unused")

@TestMethodOrder(MethodOrderer.MethodName.class)
public class TestACMECollegeSystem {
    private static final Class<?> _thisClaz = MethodHandles.lookup().lookupClass();
    private static final Logger logger = LogManager.getLogger(_thisClaz);

    static final String HTTP_SCHEMA = "http";
    static final String HOST = "localhost";
    static final int PORT = 8080;

    // Test fixture(s)
    static URI uri;
    static HttpAuthenticationFeature adminAuth;
    static HttpAuthenticationFeature userAuth;

    @BeforeAll
    public static void oneTimeSetUp() throws Exception {
        logger.debug("oneTimeSetUp");
        uri = UriBuilder
            .fromUri(APPLICATION_CONTEXT_ROOT + APPLICATION_API_VERSION)
            .scheme(HTTP_SCHEMA)
            .host(HOST)
            .port(PORT)
            .build();
        adminAuth = HttpAuthenticationFeature.basic(DEFAULT_ADMIN_USER, DEFAULT_ADMIN_USER_PASSWORD);
        userAuth = HttpAuthenticationFeature.basic(DEFAULT_USER, DEFAULT_USER_PASSWORD);
    }

    protected WebTarget webTarget;
    @BeforeEach
    public void setUp() {
        Client client = ClientBuilder.newClient(
            new ClientConfig().register(MyObjectMapperProvider.class).register(new LoggingFeature()));
        webTarget = client.target(uri);
    }

    @Test
    public void test01_all_students_with_adminrole() throws JsonMappingException, JsonProcessingException {
        Response response = webTarget
            //.register(userAuth)
            .register(adminAuth)
            .path(STUDENT_RESOURCE_NAME)
            .request()
            .get();
        assertThat(response.getStatus(), is(200));
        List<Student> students = response.readEntity(new GenericType<List<Student>>(){});
        assertThat(students, is(not(empty())));
        assertThat(students, hasSize(1));
    }
    
    @Test
    public void test02_all_students_with_userrole() throws JsonMappingException, JsonProcessingException {
        Response response = webTarget
            .register(userAuth)
            .path(STUDENT_RESOURCE_NAME)
            .request()
            .get();
        assertThat(response.getStatus(), is(403));
    }
    
    @Test
    public void test03_get_student_by_id_with_adminrole() throws JsonMappingException, JsonProcessingException {
        Response response = webTarget
            .register(adminAuth)
            .path(STUDENT_RESOURCE_NAME + "/1")
            .request()
            .get();
        assertThat(response.getStatus(), is(200));
        Student student = response.readEntity(new GenericType<Student>() {});
        assertThat(student, notNullValue());
    }
    
    @Test
    public void test04_get_student_by_id_with_userrole() throws JsonMappingException, JsonProcessingException {
        Response response = webTarget
            .register(userAuth)
            .path(STUDENT_RESOURCE_NAME + "/1")
            .request()
            .get();
        assertThat(response.getStatus(), is(200));
        Student student = response.readEntity(new GenericType<Student>() {});
        assertThat(student, notNullValue());
    }
    
    @Test
    public void test05_post_student_with_adminrole() throws JsonMappingException, JsonProcessingException {
    	Student newStudent = new Student();
        newStudent.setFullName("Michael", "Smith");
        Entity<Student> addNewStudent = Entity.json(newStudent);

        Response response = webTarget
            .register(adminAuth)
    		.path(STUDENT_RESOURCE_NAME)
            .request()
            .post(addNewStudent);
        assertThat(response.getStatus(), is(200));  
        Student student = response.readEntity(new GenericType<Student>() {});
        assertThat(student, notNullValue());
    }
    
    @Test
    public void test06_post_student_with_userrole() throws JsonMappingException, JsonProcessingException {
    	Student newStudent = new Student();
        newStudent.setFullName("Michael", "Smith");
        Entity<Student> addNewStudent = Entity.json(newStudent);

        Response response = webTarget
            .register(userAuth)
    		.path(STUDENT_RESOURCE_NAME)
            .request()
            .post(addNewStudent);
        assertThat(response.getStatus(), is(403));   
    }
    
    @Test
    public void test07_delete_student_by_id_with_adminrole() throws JsonMappingException, JsonProcessingException {
    	//create a student to delete
    	Student newStudent = new Student();
        newStudent.setFullName("Test", "Delete");
        Entity<Student> addNewStudent = Entity.json(newStudent);

        Response response1 = webTarget
            .register(adminAuth)
    		.path(STUDENT_RESOURCE_NAME)
            .request()
            .post(addNewStudent);
        assumeTrue(response1.getStatus()==200); 
        Student student = response1.readEntity(new GenericType<Student>() {});
        
        //get the id of new student to delete
        int newStudentId = student.getId();
        
        Response response = webTarget
            .register(adminAuth)
            .path(STUDENT_RESOURCE_NAME + "/" + newStudentId)
            .request()
            .delete();
        assertThat(response.getStatus(), is(200));
    }
    
    @Test
    public void test08_delete_student_by_id_with_userrole() throws JsonMappingException, JsonProcessingException {
        Response response = webTarget
            .register(userAuth)
            .path(STUDENT_RESOURCE_NAME + "/1")
            .request()
            .delete();
        assertThat(response.getStatus(), is(403));

    }
    
    @Test
    public void test09_get_all_professors_with_adminrole() throws JsonMappingException, JsonProcessingException {
    	Response response = webTarget
    			.register(adminAuth)
    			.path(PROFESSOR_SUBRESOURCE_NAME)
    			.request()
	            .get();;
    	assertThat(response.getStatus(), is(200));
    	List<Professor> professors = response.readEntity(new GenericType<List<Professor>>(){});
    	assertThat(professors, is(not(empty())));
        assertThat(professors, hasSize(1));
    	
    }
    @Test
    public void test10_get_all_professors_with_userrole() throws JsonMappingException, JsonProcessingException {
    	  Response response = webTarget
    	            .register(userAuth)
    	            .path(PROFESSOR_SUBRESOURCE_NAME)
    	            .request()
    	            .get();
    	  assertThat(response.getStatus(), is(403));
    }
    @Test
    public void test11_get_professor_by_id_with_adminrole() throws JsonMappingException, JsonProcessingException {
        Response response = webTarget
            .register(adminAuth)
            .path(PROFESSOR_SUBRESOURCE_NAME + "/1")
            .request()
            .get();
        assertThat(response.getStatus(), is(200));
        Professor professor = response.readEntity(new GenericType<Professor>() {});
        assertThat(professor, notNullValue());
    }
    
    @Test
    public void test12_get_professor_by_id_with_userrole() throws JsonMappingException, JsonProcessingException {
    	Response response = webTarget
                .register(userAuth)
                .path(PROFESSOR_SUBRESOURCE_NAME + "/1")
                .request()
                .get();
            assertThat(response.getStatus(), is(200));
            Professor professor = response.readEntity(new GenericType<Professor>() {});
            assertThat(professor, notNullValue());
    }
    @Test
    public void test13_post_professor_with_adminrole() throws JsonMappingException, JsonProcessingException {
    	Professor newProfessor = new Professor();
        newProfessor.setProfessor("John", "Doe","Computer Programming");
        Entity<Professor> addNewProfessor = Entity.json(newProfessor);

        Response response = webTarget
            .register(adminAuth)
    		.path(PROFESSOR_SUBRESOURCE_NAME)
            .request()
            .post(addNewProfessor);
        assertThat(response.getStatus(), is(200));  
        Professor professor = response.readEntity(new GenericType<Professor>() {});
        assertThat(professor, notNullValue());
    }
    @Test
    public void  test14_post_student_with_userrole() throws JsonMappingException, JsonProcessingException {
    	Professor newProfessor = new Professor();
    	newProfessor.setProfessor("John", "Doe","Computer Programming");
    	Entity<Professor> addNewProfessor = Entity.json(newProfessor);

        Response response = webTarget
            .register(userAuth)
    		.path(PROFESSOR_SUBRESOURCE_NAME)
            .request()
            .post(addNewProfessor);
        assertThat(response.getStatus(), is(403));  
    }
    @Test
    public void test15_delete_professor_by_id_with_adminrole() throws JsonMappingException, JsonProcessingException {
    	//create a professor to delete.
    	Professor newProfessor = new Professor();
    	newProfessor.setProfessor("Test", "Case","Test Program");
    	Entity<Professor> addNewProfessor = Entity.json(newProfessor);
    	
        Response response1 = webTarget
            .register(adminAuth)
    		.path(PROFESSOR_SUBRESOURCE_NAME)
            .request()
            .post(addNewProfessor);
        assumeTrue(response1.getStatus()==200); 
        Professor professor = response1.readEntity(new GenericType<Professor>() {});
        
        //get the id of new professor to delete
        int newProfessorId = professor.getId();
        
        Response response = webTarget
            .register(adminAuth)
            .path(PROFESSOR_SUBRESOURCE_NAME + "/" + newProfessorId)
            .request()
            .delete();
        assertThat(response.getStatus(), is(200));
    }
    @Test
    public void test16_delete_professor_by_id_with_userrole() throws JsonMappingException, JsonProcessingException {
    	Response response = webTarget
                .register(userAuth)
                .path(PROFESSOR_SUBRESOURCE_NAME + "/1")
                .request()
                .delete();
            assertThat(response.getStatus(), is(403));
    }
    @Test
    public void test17_get_all_courses_with_adminrole() throws JsonMappingException, JsonProcessingException {
    	Response response = webTarget
    			.register(adminAuth)
    			.path(COURSE_RESOURCE_NAME)
    			.request()
    			.get();
    	assertThat(response.getStatus(), is(200));
    	List<Course> courses = response.readEntity(new GenericType<List<Course>>() {});
    	assertThat(courses, is(not(empty())));
        assertThat(courses, hasSize(2));
    }
    @Test
    public void test18_get_all_courses_with_userrole() throws JsonMappingException, JsonProcessingException {
        Response response = webTarget
                .register(userAuth)
                .path(COURSE_RESOURCE_NAME)
                .request()
                .get();
      assertThat(response.getStatus(), is(403));
    }
    @Test
    public void test19_get_course_by_id_with_adminrole() throws JsonMappingException, JsonProcessingException {
    	Response response = webTarget
                .register(adminAuth)
                .path(COURSE_RESOURCE_NAME + "/1")
                .request()
                .get();
            assertThat(response.getStatus(), is(200));
            Course course = response.readEntity(new GenericType<Course>() {});
            assertThat(course, notNullValue());
    }
    @Test
    public void test20_get_course_id_with_userrole() throws JsonMappingException, JsonProcessingException {
    	  Response response = webTarget
    	            .register(userAuth)
    	            .path(COURSE_RESOURCE_NAME + "/1")
    	            .request()
    	            .get();
    	  assertThat(response.getStatus(), is(403));
    }
    @Test
    public void test21_post_course_with_adminrole() throws JsonMappingException, JsonProcessingException {
    	Course newCourse = new Course();
    	newCourse.setCourse("CPtest","Computer Programming Test ", 2022, "22F.T", 10, (byte) 0); // turn to byte after.
        Entity<Course> addNewCourse = Entity.json(newCourse);

        Response response = webTarget
            .register(adminAuth)
    		.path(COURSE_RESOURCE_NAME)
            .request()
            .post(addNewCourse);
        assertThat(response.getStatus(), is(200));  
        Course course = response.readEntity(new GenericType<Course>() {});
        assertThat(course, notNullValue());
    }
    @Test
    public void test22_post_course_with_userrole() throws JsonMappingException, JsonProcessingException {
    	Course newCourse = new Course();
    	newCourse.setCourse("CPtest","Computer Programming Test ", 2022, "22F.T", 10, (byte) 0); // turn to byte after.
    	Entity<Course> addNewCourse = Entity.json(newCourse);

        Response response = webTarget
            .register(userAuth)
    		.path(COURSE_RESOURCE_NAME)
            .request()
            .post(addNewCourse);
        assertThat(response.getStatus(), is(403)); 
        Course course = response.readEntity(new GenericType<Course>() {});
        assertThat(course, notNullValue());
    }
    @Test
    public void test23_delete_course_by_id_with_adminrole() throws JsonMappingException, JsonProcessingException {
    	//create a course to delete.
    	Course newCourse = new Course();
    	newCourse.setCourse("TEST", "TEST PROGRAM", 4444, "22TEST", 12, (byte) 0);// change to a byte
    	Entity<Course> addNewCourse = Entity.json(newCourse);
    	
        Response response1 = webTarget
            .register(adminAuth)
    		.path(COURSE_RESOURCE_NAME)
            .request()
            .post(addNewCourse);
        assumeTrue(response1.getStatus()==200); 
        Course course = response1.readEntity(new GenericType<Course> () {});
        
        //get the id of new course to delete
        int newCourseId = course.getId();
        
        Response response = webTarget
            .register(adminAuth)
            .path(COURSE_RESOURCE_NAME + "/" + newCourseId)
            .request()
            .delete();
        assertThat(response.getStatus(), is(200));
    }
    
    @Test
    public void test24_delete_course_by_id_with_userrole() throws JsonMappingException, JsonProcessingException {
        Response response = webTarget
                .register(userAuth)
                .path(COURSE_RESOURCE_NAME + "/1")
                .request()
                .delete();
            assertThat(response.getStatus(), is(403));
    }
    @Test
    public void test25_get_all_student_clubs_with_adminrole() throws JsonMappingException, JsonProcessingException {
    	Response response = webTarget
                .register(adminAuth)
                .path(STUDENT_CLUB_RESOURCE_NAME)
                .request()
                .get();
        assertThat(response.getStatus(), is(200));
    }
    @Test
    public void test26_get_all_student_clubs_with_userrole() throws JsonMappingException, JsonProcessingException {
    	Response response = webTarget
                .register(userAuth)
                .path(STUDENT_CLUB_RESOURCE_NAME)
                .request()
                .get();
      assertThat(response.getStatus(), is(200));
    }
    @Test
    public void test27_get_student_club_by_id_with_adminrole() throws JsonMappingException, JsonProcessingException {
    	 Response response = webTarget
    	            .register(adminAuth)
    	            .path(STUDENT_CLUB_RESOURCE_NAME + "/1")
    	            .request()
    	            .get();
     assertThat(response.getStatus(), is(200));
     StudentClub studentClub = response.readEntity(new GenericType<StudentClub>() {});
     assertThat(studentClub, notNullValue());
    }
    @Test
    public void test28_get_student_club_by_id_with_userrole() throws JsonMappingException, JsonProcessingException {
    	Response response = webTarget
                .register(userAuth)
                .path(STUDENT_CLUB_RESOURCE_NAME + "/1")
                .request()
                .get();
            assertThat(response.getStatus(), is(200));
            StudentClub studentClub = response.readEntity(new GenericType<StudentClub>() {});
            assertThat(studentClub, notNullValue());
    }
    @Test
    public void test29_post_student_club_with_adminrole() throws JsonMappingException, JsonProcessingException {
    	StudentClub newStudentClub = new AcademicStudentClub();
    	newStudentClub.setName("TEST CLUB");
        Entity<StudentClub> addNewStudentClub = Entity.json(newStudentClub);

        Response response = webTarget
            .register(adminAuth)
    		.path(STUDENT_CLUB_RESOURCE_NAME)
            .request()
            .post(addNewStudentClub);
        assertThat(response.getStatus(), is(200));  
        StudentClub studentClub = response.readEntity(new GenericType<StudentClub>() {});
        assertThat(studentClub, notNullValue());
    }
    @Test
    public void test30_post_student_club_with_userrole() throws JsonMappingException, JsonProcessingException {
    	StudentClub newStudentClub = new NonAcademicStudentClub();
    	newStudentClub.setName("TEST CLUB NON ACADEMIC");
    	Entity<StudentClub> addNewStudentClub = Entity.json(newStudentClub);

        Response response = webTarget
            .register(adminAuth)
    		.path(STUDENT_CLUB_RESOURCE_NAME)
            .request()
            .post(addNewStudentClub);
        assertThat(response.getStatus(), is(200));  
        StudentClub studentClub = response.readEntity(new GenericType<StudentClub>() {});
        assertThat(studentClub, notNullValue());
    
    }
    @Test
    public void test31_delete_course_by_id_with_adminrole() throws JsonMappingException, JsonProcessingException {
    	//create a studentClub to delete.
    	StudentClub newStudentClub = new AcademicStudentClub();
    	newStudentClub.setName("TEST CLUB2");
    	Entity<StudentClub> addNewStudentClub = Entity.json(newStudentClub);
    	
    	Response response1 = webTarget
                .register(adminAuth)
                .path(STUDENT_CLUB_RESOURCE_NAME)
                .request()
                .post(addNewStudentClub);
            assumeTrue(response1.getStatus()==200); 
            StudentClub studentClub = response1.readEntity(new GenericType<StudentClub>() {});
            
            //get the id of studentClub to delete.
            int newStudentClubID = studentClub.getId();
            
            Response response = webTarget
                .register(adminAuth)
                .path(STUDENT_CLUB_RESOURCE_NAME + "/" + newStudentClubID)
                .request()
                .delete();
            assertThat(response.getStatus(), is(200));
            Course deletedCourse = response.readEntity(new GenericType<Course> () {});
            assertThat(deletedCourse, notNullValue());
        }
    @Test
    public void test32_delete_course_by_id_with_userrole() throws JsonMappingException, JsonProcessingException {
    	Response response = webTarget
                .register(userAuth)
                .path(STUDENT_CLUB_RESOURCE_NAME + "/1")
                .request()
                .delete();
            assertThat(response.getStatus(), is(403));
    }
    
    @Test
    public void test33_get_ClubMembership_by_id_with_adminrole() throws JsonMappingException, JsonProcessingException {
        Response response = webTarget
            .register(adminAuth)
            .path(CLUB_MEMBERSHIP_RESOURCE_NAME + "/1")
            .request()
            .get();
        assertThat(response.getStatus(), is(200));
        ClubMembership cm = response.readEntity(new GenericType<ClubMembership>() {});
        assertThat(cm, notNullValue());
    }
    
    
    @Test
    public void test34_post_ClubMembership_with_userRole() throws JsonMappingException, JsonProcessingException {
    	ClubMembership newCm = new ClubMembership();
        Entity<ClubMembership> addNewCm = Entity.json(newCm);

        Response response = webTarget
            .register(userAuth)
    		.path(CLUB_MEMBERSHIP_RESOURCE_NAME)
            .request()
            .post(addNewCm);
        assertThat(response.getStatus(), is(403));   
    }
    
    @Test
    public void test35_post_ClubMembership_with_adminRole() throws JsonMappingException, JsonProcessingException {
    	AcademicStudentClub asc = new AcademicStudentClub();
    	asc.setName("Test StudentClub");
    	Student newStudent = new Student();
        newStudent.setFullName("Test", "Delete");
    	MembershipCard mc = new MembershipCard();
    	mc.setOwner(newStudent);
    	mc.setSigned(false);
    	ClubMembership newClubMembership = new ClubMembership();
    	DurationAndStatus das = new DurationAndStatus();
    	das.setStartDate(LocalDateTime.now());
    	das.setEndDate(LocalDateTime.now());
    	das.setActive((byte)1);
    	newClubMembership.setStudentClub(asc);
    	newClubMembership.setCard(mc);
    	newClubMembership.setDurationAndStatus(das);
        Entity<ClubMembership> addNewClubMembership = Entity.json(newClubMembership);
        
        Response postResponse = webTarget
                .register(adminAuth)
        		.path(CLUB_MEMBERSHIP_RESOURCE_NAME)
                .request()
                .post(addNewClubMembership);
            assertThat(postResponse.getStatus(), is(200)); 
            ClubMembership clubMembership = postResponse.readEntity(new GenericType<ClubMembership>() {});
       
            assertThat(clubMembership, notNullValue());

    }
    
    @Test
    public void test36_delete_ClubMembership_by_id_with_userrole() throws JsonMappingException, JsonProcessingException {
        Response response = webTarget
            .register(userAuth)
            .path(CLUB_MEMBERSHIP_RESOURCE_NAME + "/1")
            .request()
            .delete();
        assertThat(response.getStatus(), is(403));

    }
    
//    @Test
//    public void test37_delete_ClubMembership_by_id_with_adminRole() throws JsonMappingException, JsonProcessingException {
//    	AcademicStudentClub asc = new AcademicStudentClub();
//    	asc.setName("Test DeleteStudentClub");
//    	Student newStudent = new Student();
//        newStudent.setFullName("Shingai", "Magumbe");
//    	MembershipCard mc = new MembershipCard();
//    	mc.setOwner(newStudent);
//    	mc.setSigned(false);
//    	ClubMembership newClubMembership = new ClubMembership();
//    	DurationAndStatus das = new DurationAndStatus();
//    	das.setStartDate(LocalDateTime.now());
//    	das.setEndDate(LocalDateTime.now());
//    	das.setActive((byte)1);
//    	newClubMembership.setStudentClub(asc);
//    	newClubMembership.setCard(mc);
//    	newClubMembership.setDurationAndStatus(das);
//        Entity<ClubMembership> addNewClubMembership = Entity.json(newClubMembership);
//        
//        Response postResponse = webTarget
//                .register(adminAuth)
//        		.path(CLUB_MEMBERSHIP_RESOURCE_NAME)
//                .request()
//                .post(addNewClubMembership);
//            assertThat(postResponse.getStatus(), is(200)); 
//            ClubMembership clubMembership = postResponse.readEntity(new GenericType<ClubMembership>() {});
//            
//            //get the id of new student to delete
//            int newCmId = clubMembership.getId();
//    	
//        Response response = webTarget
//            .register(adminAuth)
//            .path(CLUB_MEMBERSHIP_RESOURCE_NAME + "/" + newCmId)
//            .request()
//            .delete();
//        assertThat(response.getStatus(), is(200));
//        ClubMembership cm = response.readEntity(new GenericType<ClubMembership>() {});
//        assertThat(cm, notNullValue());
//    }
    
    @Test
    public void test38_getAllMembershipCards_with_adminRole() throws JsonMappingException, JsonProcessingException {
        Response response = webTarget
                .register(adminAuth)
                .path("membershipcard")
                .request()
                .get();
        assertEquals(response.getStatus(), 200);
    }

    @Test
    public void test39_getAllMembershipCards_with_userRole() throws JsonMappingException, JsonProcessingException {
        Response response = webTarget
                .register(userAuth)
                .path("membershipcard")
                .request()
                .get();
        assertEquals(response.getStatus(), 403);
    }
    
    @Test
    public void test40_getCourseRegistrations_with_adminRole() {
        Response response = webTarget
                .register(adminAuth)
                .path(COURSE_REGISTRATION_RESOURCE_NAME)
                .request()
                .get();
        assertEquals(response.getStatus(), 200);
    }
    
    @Test
    public void test41_getCourseRegistrations_with_userRole() {
        Response response = webTarget
                .register(userAuth)
                .path(COURSE_REGISTRATION_RESOURCE_NAME)
                .request()
                .get();
        assertEquals(response.getStatus(), 403);
    }
    
}