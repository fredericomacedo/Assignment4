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

import static acmecollege.utility.MyConstants.PROFESSOR_SUBRESOURCE_NAME;
import static acmecollege.utility.MyConstants.RESOURCE_PATH_ID_ELEMENT;
import static acmecollege.utility.MyConstants.RESOURCE_PATH_ID_PATH;
import static acmecollege.utility.MyConstants.ADMIN_ROLE;
import static acmecollege.utility.MyConstants.USER_ROLE;
import java.util.List;
import javax.annotation.security.RolesAllowed;
import javax.ejb.EJB;
import javax.inject.Inject;
import javax.security.enterprise.SecurityContext;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import acmecollege.ejb.ACMECollegeService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import acmecollege.entity.Professor;

@Path(PROFESSOR_SUBRESOURCE_NAME)
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class ProfessorResource {

    private static final Logger LOG = LogManager.getLogger();

    @EJB
    protected ACMECollegeService service;

    @Inject
    protected SecurityContext sc;
    
    @DELETE
    @RolesAllowed({ADMIN_ROLE})
    @Path(RESOURCE_PATH_ID_PATH)
    public Response deleteProfessor(@PathParam(RESOURCE_PATH_ID_ELEMENT) int id) {
        LOG.debug("Deleting a professor with id = {}", id);
        Response response = null;
        service.deleteProfessorById(id);
        response = Response.ok().build();
        return response;
    }
    
    @POST
    @RolesAllowed({ADMIN_ROLE})
    public Response addProfessor(Professor newProfessor) {
        LOG.debug("Adding a new professor = {}", newProfessor);
        Response response = null;
        service.persistProfessor(newProfessor);
        response = Response.ok(newProfessor).build();
        return response;
    }

    @GET
    @RolesAllowed({ADMIN_ROLE})
    public Response getProfessors() {
        LOG.debug("Retrieving all professors ...");
        List<Professor> professors = service.getAllProfessors();
        Response response = Response.ok(professors).build();
        return response;
    }

    @GET
    @RolesAllowed({ADMIN_ROLE, USER_ROLE})
    @Path(RESOURCE_PATH_ID_PATH)
    public Response getProfessorById(@PathParam(RESOURCE_PATH_ID_ELEMENT) int id) {
        LOG.debug("Retrieving specific professor with id = {}", id);
        Professor prof = service.getProfessorById(id);
        if (prof == null)
            return Response.status(Status.NOT_FOUND).build();
        return Response.ok(prof).build();
    }

}