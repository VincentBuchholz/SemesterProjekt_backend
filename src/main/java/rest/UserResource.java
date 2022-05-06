package rest;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import dtos.RequestDTO;
import dtos.UserDTO;
import errorhandling.UsernameTakenException;
import facades.RequestFacade;
import facades.UserFacade;
import utils.EMF_Creator;

import javax.annotation.security.RolesAllowed;
import javax.persistence.EntityManagerFactory;
import javax.ws.rs.*;
import javax.ws.rs.core.*;
import java.util.List;

@Path("user")
public class UserResource {

    private static final EntityManagerFactory EMF = EMF_Creator.createEntityManagerFactory();
    private static final UserFacade USERFACADE =  UserFacade.getUserFacade(EMF);
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    @Context
    private UriInfo context;

    @Context
    SecurityContext securityContext;


//    @GET
//    @Path("/coaches")
//    @Produces({MediaType.APPLICATION_JSON})
//    public Response getAllCoaches() {
//        List<UserDTO> coachesDTOList = USERFACADE.getCoaches();
//        return Response.ok().entity(GSON.toJson(coachesDTOList)).build();
//    }

    @POST
    @RolesAllowed("coach")
    @Produces({MediaType.APPLICATION_JSON})
    @Consumes({MediaType.APPLICATION_JSON})
    public Response createUser(String content) throws UsernameTakenException {
        UserDTO userDTO = GSON.fromJson(content, UserDTO.class);
        UserDTO newUserDTO = USERFACADE.createUser(userDTO);
        return Response.ok().entity(GSON.toJson(newUserDTO)).build();
    }

    @GET
    @RolesAllowed("coach")
    @Path("/customers/{coachID}")
    @Produces({MediaType.APPLICATION_JSON})
    public Response getCustomersByCoachID(@PathParam("coachID") int coachID) {
        List<UserDTO> customersDTOList = USERFACADE.getCustomersByCoachID(coachID);
        return Response.ok().entity(GSON.toJson(customersDTOList)).build();
    }

    @GET
    @Path("/customer/{customerID}")
    @RolesAllowed("coach")
    @Produces({MediaType.APPLICATION_JSON})
    public Response getRequestByRequestID(@PathParam("customerID") int customerID) {
        UserDTO userDTO = USERFACADE.getCustomerByID(customerID);
        return Response.ok().entity(GSON.toJson(userDTO)).build();
    }

//    @GET
//    @Path("/{requestID}")
//    @RolesAllowed("coach")
//    @Produces({MediaType.APPLICATION_JSON})
//    public Response getRequestByRequestID(@PathParam("requestID") int requestID) {
//        RequestDTO requestDTO = REQUESTFACADE.getRequestByRequestID(requestID);
//        return Response.ok().entity(GSON.toJson(requestDTO)).build();
//    }
}