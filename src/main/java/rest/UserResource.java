package rest;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import dtos.RequestDTO;
import dtos.UserDTO;
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
    @Produces({MediaType.APPLICATION_JSON})
    @Consumes({MediaType.APPLICATION_JSON})
    public Response createUser(String content){
        UserDTO pdto = GSON.fromJson(content, UserDTO.class);
        UserDTO newPdto = USERFACADE.createUser(pdto);
        return Response.ok().entity(GSON.toJson(newPdto)).build();
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