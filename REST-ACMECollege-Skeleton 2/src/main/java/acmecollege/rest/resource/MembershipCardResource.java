/***************************************************************************
 * File:  MembershipCardResource.java Course materials (23W) CST 8277
 * 
 * @author Teddy Yap
 * @author Shariar (Shawn) Emami
 * @date August 28, 2022
 * 
 * Updated by:  Group 07
 *   041029397, Frederico Lucio, Macedo
 *   041046587, Natalia, Pirath  
 *   041042876, Tongwe, Kasaji 
 *   041025651, Daniel, Barboza 
 */
package acmecollege.rest.resource;

import static acmecollege.utility.MyConstants.ADMIN_ROLE;
import static acmecollege.utility.MyConstants.USER_ROLE;
import static acmecollege.utility.MyConstants.MEMBERSHIP_CARD_RESOURCE_NAME;
import static acmecollege.utility.MyConstants.RESOURCE_PATH_ID_ELEMENT;
import static acmecollege.utility.MyConstants.RESOURCE_PATH_ID_PATH;

import java.util.List;


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


import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.glassfish.soteria.WrappingCallerPrincipal;

import acmecollege.ejb.ACMECollegeService;
import acmecollege.entity.CourseRegistration;
import acmecollege.entity.MembershipCard;
import acmecollege.entity.SecurityUser;
import acmecollege.entity.Student;



@Path(MEMBERSHIP_CARD_RESOURCE_NAME)
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)

public class MembershipCardResource {
	
	private static final Logger LOG = LogManager.getLogger();
	
	@EJB
	protected ACMECollegeService service;
	
	 @Inject
	protected SecurityContext sc;

	 @GET
	 @RolesAllowed({ADMIN_ROLE})
	 public Response getMembershipCards() {
		 LOG.debug("retrieving all memberships ...");
		 List<MembershipCard> membershipCards = service.getMembershipCards();
		 Response response = Response.ok(membershipCards).build();
		 
		 return response;
	 }
	 
	 @GET
	 @Path(RESOURCE_PATH_ID_PATH)
	 @RolesAllowed({ADMIN_ROLE, USER_ROLE})
	 public Response getMembershipCardById (@PathParam(RESOURCE_PATH_ID_ELEMENT) int id) {
		 
		 LOG.debug("try to retrieve specific membershipCard " + id);
	        Response response = null;
	        Student student = null;
	        
	       	MembershipCard membershipCard = service.getMembershipCardById(id);

	        if (sc.isCallerInRole(ADMIN_ROLE)) {
			 response = Response.status(membershipCard == null ? Status.NOT_FOUND : Status.OK).entity(membershipCard).build();
	        } else if (sc.isCallerInRole(USER_ROLE)) {
	            WrappingCallerPrincipal wCallerPrincipal = (WrappingCallerPrincipal) sc.getCallerPrincipal();
	            SecurityUser sUser = (SecurityUser) wCallerPrincipal.getWrapped();
	            student = sUser.getStudent();
	            if (student != null && student.getId() == membershipCard.getOwner().getId()) {
	            	 response = Response.status(membershipCard == null ? Status.NOT_FOUND : Status.OK).entity(membershipCard).build();
	            } else {
	                throw new ForbiddenException("User trying to access resource it does not own (wrong userid)");
	            }
	        } else {
	            response = Response.status(Status.BAD_REQUEST).build();
	        }
	        return response;
	        
	 }

	 @POST
	 @RolesAllowed({ADMIN_ROLE})
	 @Path("/student/{studentId}/clubMembership/{membershipId}/")
	 public Response addMembershipCard(@PathParam("studentId") int studentId, @PathParam("membershipId") int membershipID) {
		 LOG.debug("Inside addMembershipCard");
		 Response response = null;
		 MembershipCard newMembershipCard = service.persistMembershipCardForStudent(studentId, membershipID);
	     if(newMembershipCard!=null) response = Response.ok(newMembershipCard).build();
    	 else response = Response.status(Status.NO_CONTENT).build();
    	 return response;
	 }
	 
	 @DELETE
	 @RolesAllowed({ADMIN_ROLE})
	 @Path(RESOURCE_PATH_ID_PATH)
	 public Response deleteMembershipCard (@PathParam(RESOURCE_PATH_ID_ELEMENT) int mId) {
		 LOG.debug("Deleting membershipCard with id = {}", mId);
		 MembershipCard m =service.deleteMembershipCardById(mId);
		 Response response = Response.ok(m).build();
		 return response;
	 }
}