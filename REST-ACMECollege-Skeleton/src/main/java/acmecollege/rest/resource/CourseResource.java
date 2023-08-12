/**
 * File:  Courses.java Course materials (23W) CST 8277
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
 * 
 */
package acmecollege.rest.resource;



import static acmecollege.utility.MyConstants.ADMIN_ROLE;
import static acmecollege.utility.MyConstants.RESOURCE_PATH_ID_ELEMENT;
import static acmecollege.utility.MyConstants.RESOURCE_PATH_ID_PATH;

import java.util.List;

import javax.annotation.security.RolesAllowed;
import javax.ejb.EJB;
import javax.inject.Inject;
import javax.security.enterprise.SecurityContext;
import javax.ws.rs.Consumes;
import javax.ws.rs.ForbiddenException;
import javax.ws.rs.GET;
import javax.ws.rs.POST;

import javax.ws.rs.DELETE;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import acmecollege.ejb.ACMECollegeService;
import acmecollege.entity.Course;

@Path("/course")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class CourseResource {

    private static final Logger LOG = LogManager.getLogger();

    @EJB
    protected ACMECollegeService service;

    @Inject
    protected SecurityContext sc;

    @GET
    @RolesAllowed({ADMIN_ROLE})
    public Response getAllCourses() {
        LOG.debug("retrieving all courses ...");
        List<Course> courses = service.getAllCourses();
        Response response = Response.ok(courses).build();
        return response;
    }

    @GET
    @RolesAllowed({ADMIN_ROLE})
    @Path(RESOURCE_PATH_ID_PATH)
    public Response getCourseById(@PathParam(RESOURCE_PATH_ID_ELEMENT) int id) {
        LOG.debug("try to retrieve specific course " + id);
        Response response = null;
        Course course = null;
        
        if(sc.isCallerInRole(ADMIN_ROLE)) {
        	course = service.getCourseById(id);
        	response = Response.status(course == null ? Status.NOT_FOUND : Status.OK).entity(course).build();
        } else if (sc.isCallerInRole(ADMIN_ROLE)) {
        	throw new ForbiddenException("User trying to access resource it does not permit");
        } else {
        	response = Response.status(Status.BAD_REQUEST).build();
        }
        
        
        return response;
    }

    @POST
    @RolesAllowed({ADMIN_ROLE})
    public Response addCourse(Course newCourse) {
        Course newCourseWithIdTimestamps = service.persistCourse(newCourse);
        Response response = Response.ok(newCourseWithIdTimestamps).build();
        return response;
    }


    @DELETE
    @RolesAllowed({ADMIN_ROLE})
    @Path(RESOURCE_PATH_ID_PATH)
    public Response deleteCourseById(@PathParam(RESOURCE_PATH_ID_ELEMENT) int id) {
        LOG.debug("Deleting course with id " + id);
        try {
            service.deleteCourseById(id);
            return Response.status(Status.NO_CONTENT).build(); // Return 204 No Content on successful deletion
        } catch (Exception e) {
            LOG.error("Error deleting course: " + e.getMessage());
            return Response.status(Status.INTERNAL_SERVER_ERROR).entity("Error deleting course with id: " + id).build();
        }
    }
}
