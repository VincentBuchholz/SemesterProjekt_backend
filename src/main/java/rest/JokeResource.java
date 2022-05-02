package rest;


import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import dtos.JokeDTO;
import facades.JokeFacade;

import javax.annotation.security.RolesAllowed;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import java.io.IOException;

/**
 * REST Web Service
 *
 * @author lam
 */
@Path("jokes")
public class JokeResource {
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

    @Context
    private UriInfo context;

   
    @GET
//    @RolesAllowed("user")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getJokes() throws IOException {
        JokeFacade jf = new JokeFacade();
        JokeDTO jokeDTO = jf.getCombined();
        return Response.ok().entity(GSON.toJson(jokeDTO)).build();
    }

   
}
