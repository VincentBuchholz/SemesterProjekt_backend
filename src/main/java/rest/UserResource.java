package rest;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import dtos.*;
import errorhandling.UsernameTakenException;
import facades.DiagramFacade;
import facades.UserFacade;
import utils.EMF_Creator;

import javax.annotation.security.RolesAllowed;
import javax.persistence.EntityManagerFactory;
import javax.ws.rs.*;
import javax.ws.rs.core.*;
import java.io.IOException;
import java.util.List;

@Path("user")
public class UserResource {

    private static final EntityManagerFactory EMF = EMF_Creator.createEntityManagerFactory();
    private static final UserFacade USERFACADE =  UserFacade.getUserFacade(EMF);
    private static final DiagramFacade DIAGRAM_FACADE =  DiagramFacade.getInstance(EMF);
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create();
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
    @Path("/coach/{coachID}")
    @RolesAllowed("coach")
    @Produces({MediaType.APPLICATION_JSON})
    public Response getCoachByID(@PathParam("coachID") int coachID) {
        UserDTO userDTO = USERFACADE.getCoachByID(coachID);
        return Response.ok().entity(GSON.toJson(userDTO)).build();
    }

    @GET
    @Path("/customer/{customerID}")
    @RolesAllowed("coach")
    @Produces({MediaType.APPLICATION_JSON})
    public Response getCustomerByCustomerID(@PathParam("customerID") int customerID) {
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

    @GET
    @Path("/weightchart/{customerID}")
    @RolesAllowed({"coach","user"})
    @Produces({MediaType.APPLICATION_JSON})
    public Response getWeightChart(@PathParam("customerID") int customerID) {
        String chart = DIAGRAM_FACADE.getWeightChartByUserID(customerID);
        JsonObject jobj = new JsonObject();
        jobj.addProperty("url",chart);
        return Response.ok().entity(GSON.toJson(jobj)).build();
    }

    @GET
    @Path("/latestweight/{customerID}")
    @RolesAllowed({"coach","user"})
    @Produces({MediaType.APPLICATION_JSON})
    public Response getUserLatestWeight(@PathParam("customerID") int customerID) {
        UserWeighInDTO userWeighInDTO = USERFACADE.getLatestUserWeighin(customerID);
        return Response.ok().entity(GSON.toJson(userWeighInDTO)).build();
    }
    @POST
    @Path("/mealplan")
    @RolesAllowed("coach")
    @Produces({MediaType.APPLICATION_JSON})
    @Consumes({MediaType.APPLICATION_JSON})
    public Response setMealPlan(String content) {
        MealPlanDTO mealPlanDTO = GSON.fromJson(content, MealPlanDTO.class);
        MealPlanDTO mealPlanNew = USERFACADE.setMealPlan(mealPlanDTO);
        return Response.ok().entity(GSON.toJson(mealPlanNew)).build();
    }
    @GET
    @Path("/mealplan/{customerID}")
    @RolesAllowed({"user"})
    @Produces({MediaType.APPLICATION_JSON})
    public Response getMealPlanByUserID(@PathParam("customerID") int customerID) {
        MealPlanDTO mealPlanDTO = USERFACADE.getMealPlanByUserID(customerID);
        return Response.ok().entity(GSON.toJson(mealPlanDTO)).build();
    }

    @POST
    @Path("/workoutplan")
    @RolesAllowed("coach")
    @Produces({MediaType.APPLICATION_JSON})
    @Consumes({MediaType.APPLICATION_JSON})
    public Response setWorkoutPlan(String content) {
        WorkoutPlanDTO workoutPlanDTO = GSON.fromJson(content, WorkoutPlanDTO.class);
        WorkoutPlanDTO workoutPlanNew = USERFACADE.setWorkoutPlan(workoutPlanDTO);
        return Response.ok().entity(GSON.toJson(workoutPlanNew)).build();
    }
    @GET
    @Path("/workoutplan/{customerID}")
    @RolesAllowed({"user"})
    @Produces({MediaType.APPLICATION_JSON})
    public Response getWorkoutPlanByUserID(@PathParam("customerID") int customerID) {
        WorkoutPlanDTO workoutPlanDTO = USERFACADE.getWorkoutPlanByUserID(customerID);
        return Response.ok().entity(GSON.toJson(workoutPlanDTO)).build();
    }

    @GET
    @Path("/caloriesburned/{customerID}/{activityID}/{activitymin}")
    @RolesAllowed({"user"})
    @Produces({MediaType.APPLICATION_JSON})
    public Response calculateCaloriesBurned(@PathParam("customerID") int customerID,@PathParam("activityID") String activityID,@PathParam("activitymin") int activityMin) throws IOException {
        CalorieBurnedDTO calorieBurnedDTO = USERFACADE.calculateCaloriesBurned(activityID,customerID,activityMin);
        return Response.ok().entity(GSON.toJson(calorieBurnedDTO)).build();
    }

    @GET
    @Path("/amountofcustomers/{coachID}")
    @RolesAllowed({"coach"})
    @Produces({MediaType.APPLICATION_JSON})
    public Response getAmountOfClientsByCoachID(@PathParam("coachID") int coachID) {
        int amount = USERFACADE.getAmountOfCustomersByCoachID(coachID);
        JsonObject jobj = new JsonObject();
        jobj.addProperty("amount",amount);
        return Response.ok().entity(GSON.toJson(jobj)).build();
    }

}