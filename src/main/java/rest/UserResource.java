package rest;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import dtos.UserDTO;
import dtos.UserNutritionDTO;
import dtos.UserWeighInDTO;
import errorhandling.UsernameTakenException;
import facades.DiagramFacade;
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
    private static final DiagramFacade DIAGRAM_FACADE =  DiagramFacade.getInstance(EMF);
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
    @GET
    @Path("/nutrition/{customerID}")
    @RolesAllowed({"coach","user"})
    @Produces({MediaType.APPLICATION_JSON})
    public Response getNutritionByUserID(@PathParam("customerID") int customerID) {
        UserNutritionDTO userNutritionDTO = USERFACADE.getNutritionsByUser(customerID);
        return Response.ok().entity(GSON.toJson(userNutritionDTO)).build();
    }

    @GET
    @Path("/macrochart/{customerID}")
    @RolesAllowed({"coach","user"})
    @Produces({MediaType.APPLICATION_JSON})
    public Response getMacroChartByUserID(@PathParam("customerID") int customerID) {
        String chart = DIAGRAM_FACADE.getMacroChartByUserID(customerID);
        JsonObject jobj = new JsonObject();
        jobj.addProperty("url",chart);
        return Response.ok().entity(GSON.toJson(jobj)).build();
    }


    @PUT
    @Path("updatenutrition/")
    @RolesAllowed("coach")
    @Produces({MediaType.APPLICATION_JSON})
    @Consumes({MediaType.APPLICATION_JSON})
    public Response updateNutrition(String content){
        UserNutritionDTO userNutritionDTO = GSON.fromJson(content, UserNutritionDTO.class);
        UserNutritionDTO updatedNutrition = USERFACADE.updateUserNutrition(userNutritionDTO);
        return Response.ok().entity(GSON.toJson(updatedNutrition)).build();
    }

    @POST
    @RolesAllowed("user")
    @Path("updateweight/{userID}/{weight}")
    @Produces({MediaType.APPLICATION_JSON})
    @Consumes({MediaType.APPLICATION_JSON})
    public Response addWeighInByUserID(@PathParam("userID") int userID, @PathParam("weight") double weight ) {
        UserWeighInDTO userWeighInDTO = USERFACADE.addWeighInByUserID(userID,weight);
        return Response.ok().entity(GSON.toJson(userWeighInDTO)).build();
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