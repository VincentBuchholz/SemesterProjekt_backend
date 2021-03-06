package rest;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import dtos.RequestDTO;
import dtos.UserDTO;
import entities.User;
import java.util.List;
import javax.annotation.security.RolesAllowed;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.TypedQuery;
import javax.ws.rs.*;
import javax.ws.rs.core.*;

import errorhandling.NotFoundException;
import facades.RequestFacade;
import facades.UserFacade;
import utils.EMF_Creator;

@Path("request")
public class RequestResource {

    private static final EntityManagerFactory EMF = EMF_Creator.createEntityManagerFactory();
    private static final RequestFacade REQUESTFACADE =  RequestFacade.getRequestFacade(EMF);
    private static final UserFacade USERFACADE =  UserFacade.getUserFacade(EMF);
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    @Context
    private UriInfo context;

    @Context
    SecurityContext securityContext;


    @GET
    @Path("/coaches")
    @Produces({MediaType.APPLICATION_JSON})
    public Response getAllCoaches() {
        List<UserDTO> coachesDTOList = USERFACADE.getCoaches();
        return Response.ok().entity(GSON.toJson(coachesDTOList)).build();
    }

    @POST
    @Produces({MediaType.APPLICATION_JSON})
    @Consumes({MediaType.APPLICATION_JSON})
    public Response createRequest(String content){
        RequestDTO pdto = GSON.fromJson(content, RequestDTO.class);
        RequestDTO newPdto = REQUESTFACADE.createRequest(pdto);
        return Response.ok().entity(GSON.toJson(newPdto)).build();
    }

    @GET
    @Path("/coach/{coachID}")
    @RolesAllowed("coach")
    @Produces({MediaType.APPLICATION_JSON})
    public Response getRequestsByCoach(@PathParam("coachID") int coachID) {
        List<RequestDTO> requestDTOS = REQUESTFACADE.getRequestsByCoachID(coachID);
        return Response.ok().entity(GSON.toJson(requestDTOS)).build();
    }

    @GET
    @Path("/{requestID}")
    @RolesAllowed("coach")
    @Produces({MediaType.APPLICATION_JSON})
    public Response getRequestByRequestID(@PathParam("requestID") int requestID) {
        RequestDTO requestDTO = REQUESTFACADE.getRequestByRequestID(requestID);
        return Response.ok().entity(GSON.toJson(requestDTO)).build();
    }


    @DELETE
    @Path("/delete/{id}")
    @RolesAllowed("coach")
    @Produces({MediaType.APPLICATION_JSON})
    @Consumes({MediaType.APPLICATION_JSON})
    public Response deleteRequestByID(@PathParam("id") int id) {
        REQUESTFACADE.deleteRequestByID(id);
        return Response.ok().entity("Deleted").build();
    }

    @GET
    @Path("/amount/{coachID}")
    @RolesAllowed({"coach"})
    @Produces({MediaType.APPLICATION_JSON})
    public Response getAmountOfRequestsByCoachID(@PathParam("coachID") int coachID) {
        int amount = REQUESTFACADE.getAmountOfRequestsByCoachID(coachID);
        JsonObject jobj = new JsonObject();
        jobj.addProperty("amount",amount);
        return Response.ok().entity(GSON.toJson(jobj)).build();
    }
}