/**
 * File:  ProfessorResource.java Course materials (23W) CST 8277
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

import acmecollege.ejb.ACMECollegeService;
import acmecollege.entity.CourseRegistration;
import acmecollege.entity.CourseRegistrationPK;
import acmecollege.entity.SecurityUser;
import acmecollege.entity.Student;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.glassfish.soteria.WrappingCallerPrincipal;

import javax.annotation.security.RolesAllowed;
import javax.ejb.EJB;
import javax.inject.Inject;
import javax.security.enterprise.SecurityContext;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.ForbiddenException;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import java.util.List;

import static acmecollege.utility.MyConstants.*;

@Path(COURSE_REGISTRATION_RESOURCE_NAME)
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class CourseRegistrationResource {

    private static final Logger LOG = LogManager.getLogger();

    @EJB
    protected ACMECollegeService service;

    @Inject
    protected SecurityContext sc;
    
    @DELETE
    @RolesAllowed({ADMIN_ROLE})
    public Response deleteCourseRegistrationById(@PathParam("studentId") int studentId, @PathParam("courseId") int courseId) {
        LOG.debug("Deleting Course Registration with course id = {}, student id = {}", courseId, studentId);
        service.deleteCourseRegistrationById(studentId, courseId);
        return Response.ok(new CourseRegistrationPK(studentId, courseId)).build();
    }

    @GET
    @RolesAllowed({ADMIN_ROLE})
    public Response getCourseRegistrations() {
        LOG.debug("retrieving all Course Registrations ...");
        List<CourseRegistration> courseRegistrations =
                service.getAllRegistration();
        return Response.ok(courseRegistrations).build();
    }

    @POST
    @RolesAllowed({ADMIN_ROLE})
    public Response addCourseRegistration(CourseRegistration newRegistration) {
        Response response = null;
        CourseRegistration card = service.persistCourseRegistration(newRegistration);
        // Build a SecurityUser linked to the new student
        response = Response.ok(card).build();
        return response;
    }

    @GET
    @RolesAllowed({ADMIN_ROLE, USER_ROLE})
    @Path(RESOURCE_PATH_ID_PATH)
    public Response getCourseRegistrationById(@PathParam("studentId") int studentId, @PathParam("courseId") int courseId) {
        LOG.debug("Get Course Registration with course id = {}, student id = {}", courseId, studentId);
        Response response = null;
        Student student = null;

        CourseRegistration registration = null;

        if (sc.isCallerInRole(ADMIN_ROLE)) {
            registration = service.getCourseRegistrationById(studentId, courseId);
            response = Response.status(student == null ? Status.NOT_FOUND : Status.OK).entity(registration).build();
        } else if (sc.isCallerInRole(USER_ROLE)) {
            WrappingCallerPrincipal wCallerPrincipal = (WrappingCallerPrincipal) sc.getCallerPrincipal();
            SecurityUser sUser = (SecurityUser) wCallerPrincipal.getWrapped();
            student = sUser.getStudent();
            if (student != null && student.getId() == studentId) {
                response = Response.status(Status.OK).entity(student).build();
            } else {
                throw new ForbiddenException("User trying to access resource it does not own (wrong userid)");
            }
        } else {
            response = Response.status(Status.BAD_REQUEST).build();
        }
        return response;
    }

}