package acmecollege.entity;

import acmecollege.entity.CourseRegistration;
import acmecollege.MyObjectMapperProvider;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.glassfish.jersey.client.ClientConfig;
import org.glassfish.jersey.client.authentication.HttpAuthenticationFeature;
import org.glassfish.jersey.logging.LoggingFeature;
import org.junit.jupiter.api.*;
import javax.ws.rs.client.*;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;
import java.lang.invoke.MethodHandles;
import java.net.URI;
import static acmecollege.utility.MyConstants.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

@TestMethodOrder(MethodOrderer.MethodName.class)
public class CourseRegistrationTest {

    private static final Class<?> _thisClaz = MethodHandles.lookup().lookupClass();
    private static final Logger logger = LogManager.getLogger(_thisClaz);
    static final String HTTP_SCHEMA = "http";
    static final String HOST = "localhost";
    static final int PORT = 8080;
    static URI uri;
    static HttpAuthenticationFeature adminAuth;
    static HttpAuthenticationFeature userAuth;
    static int studentId = 2; // example student ID
    static int courseId = 1; // example course ID

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
    public void test01_getCourseRegistrations_admin() {
        Response response = webTarget
                .register(adminAuth)
                .path(COURSE_REGISTRATION_RESOURCE_NAME)
                .request()
                .get();
        assertEquals(response.getStatus(), 200);
    }
    
    @Test
    public void test02_getCourseRegistrations_user() {
        Response response = webTarget
                .register(userAuth)
                .path(COURSE_REGISTRATION_RESOURCE_NAME)
                .request()
                .get();
        assertEquals(response.getStatus(), 403);
    }

//    @Test
//    public void test03_addCourseRegistration_admin() {
//        CourseRegistration newRegistration = new CourseRegistration();
//        // Set fields of newRegistration as required
//        try (Response response = webTarget
//                .register(adminAuth)
//                .path(COURSE_REGISTRATION_RESOURCE_NAME)
//                .request()
//                .post(Entity.json(newRegistration))) {
//            assertEquals(response.getStatus(), 200);
//        }
//    }
    
    @Test
    public void test04_addCourseRegistration_user() {
        CourseRegistration newRegistration = new CourseRegistration();
        // Set fields of newRegistration as required
        try (Response response = webTarget
                .register(userAuth)
                .path(COURSE_REGISTRATION_RESOURCE_NAME)
                .request()
                .post(Entity.json(newRegistration))) {
            assertEquals(response.getStatus(), 403);
        }
    }

//    @Test
//    public void test05_getCourseRegistrationById_admin() {
//        Response response = webTarget
//                .register(adminAuth)
//                .path(COURSE_REGISTRATION_RESOURCE_NAME + "/" + studentId + "/" + courseId)
//                .request()
//                .get();
//        assertEquals(response.getStatus(), 200);
//    }
//    
//    @Test
//    public void test06_getCourseRegistrationById_user() {
//        Response response = webTarget
//                .register(userAuth)
//                .path(COURSE_REGISTRATION_RESOURCE_NAME + "/" + studentId + "/" + courseId)
//                .request()
//                .get();
//        assertEquals(response.getStatus(), 403);
//    }
//
//    @Test
//    public void test07_deleteCourseRegistrationById_admin() {
//        try (Response response = webTarget
//                .register(adminAuth)
//                .path(COURSE_REGISTRATION_RESOURCE_NAME + "/" + studentId + "/" + courseId)
//                .request()
//                .delete()) {
//            assertEquals(response.getStatus(), 200);
//        }
//    }
//    
//    @Test
//    public void test07_deleteCourseRegistrationById_user() {
//        try (Response response = webTarget
//                .register(userAuth)
//                .path(COURSE_REGISTRATION_RESOURCE_NAME + "/" + studentId + "/" + courseId)
//                .request()
//                .delete()) {
//            assertEquals(response.getStatus(), 403);
//        }
//    }
}

