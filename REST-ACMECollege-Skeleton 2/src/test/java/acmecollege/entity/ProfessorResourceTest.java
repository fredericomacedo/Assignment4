package acmecollege.entity;

import acmecollege.entity.Professor;
import acmecollege.MyObjectMapperProvider;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.glassfish.jersey.client.ClientConfig;
import org.glassfish.jersey.client.authentication.HttpAuthenticationFeature;
import org.glassfish.jersey.logging.LoggingFeature;
import org.junit.jupiter.api.*;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;
import java.lang.invoke.MethodHandles;
import java.net.URI;

import static acmecollege.utility.MyConstants.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

@TestMethodOrder(MethodOrderer.MethodName.class)
public class ProfessorResourceTest {

    private static final Class<?> _thisClaz = MethodHandles.lookup().lookupClass();
    private static final Logger logger = LogManager.getLogger(_thisClaz);
    static final String HTTP_SCHEMA = "http";
    static final String HOST = "localhost";
    static final int PORT = 8080;
    static URI uri;
    static HttpAuthenticationFeature adminAuth;
    static HttpAuthenticationFeature userAuth;
    static int record_id = 2;

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
    public void test01_getAllProfessors_admin() throws JsonMappingException, JsonProcessingException {
        Response response = webTarget
                .register(adminAuth)
                .path("professor")
                .request()
                .get();
        assertEquals(response.getStatus(), 200);
    }

    @Test
    public void test02_getAllProfessors_user() throws JsonMappingException, JsonProcessingException {
        Response response = webTarget
                .register(userAuth)
                .path("professor")
                .request()
                .get();
        assertEquals(response.getStatus(), 403);
    }

//    @Test
//    public void test03_getProfessorById_admin() throws JsonMappingException, JsonProcessingException {
//        Response response = webTarget
//                .register(adminAuth)
//                .path("professor/{id}")
//                .resolveTemplate("id", record_id)
//                .request()
//                .get();
//        assertEquals(response.getStatus(), 200);
//    }
//
//    @Test
//    public void test04_getProfessorById_user() throws JsonMappingException, JsonProcessingException {
//        Response response = webTarget
//                .register(userAuth)
//                .path("professor/{id}")
//                .resolveTemplate("id", record_id)
//                .request()
//                .get();
//        assertEquals(response.getStatus(), 200);
//    }
//
//    @Test
//    public void test05_postProfessor_admin() throws JsonMappingException, JsonProcessingException {
//        Professor professor = new Professor();
//        professor.setFirstName("John");
//        professor.setDepartment("Computer Science");
//        professor.setId(2);
//        try (Response response = webTarget
//                .register(adminAuth)
//                .path("professor")
//                .request()
//                .post(Entity.json(professor))) {
//            assertEquals(response.getStatus(), 200);
//        }
//    }

    @Test
    public void test06_postProfessor_user() throws JsonMappingException, JsonProcessingException {
        Professor professor = new Professor();
        professor.setFirstName("Jane");
        professor.setDepartment("Mathematics");
        try (Response response = webTarget
                .register(userAuth)
                .path("professor")
                .request()
                .post(Entity.json(professor))) {
            assertEquals(response.getStatus(), 403);
        }
    }

//    @Test
//    public void test07_deleteProfessor_admin() throws JsonMappingException, JsonProcessingException {
//        try (Response response = webTarget
//                .register(adminAuth)
//                .path("professor/{id}")
//                .resolveTemplate("id", record_id)
//                .request()
//                .delete()) {
//            assertEquals(response.getStatus(), 200);
//        }
//    }

    @Test
    public void test08_deleteProfessor_user() throws JsonMappingException, JsonProcessingException {
        try (Response response = webTarget
                .register(userAuth)
                .path("professor/{id}")
                .resolveTemplate("id", record_id)
                .request()
                .delete()) {
            assertEquals(response.getStatus(), 403);
        }
    }
}