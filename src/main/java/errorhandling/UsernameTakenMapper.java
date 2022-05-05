package errorhandling;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;
import java.util.logging.Level;
import java.util.logging.Logger;

@Provider
public class UsernameTakenMapper implements ExceptionMapper<UsernameTakenException> {
    static Gson gson = new GsonBuilder().setPrettyPrinting().create();
    @Override
    public Response toResponse(UsernameTakenException ex) {
        Logger.getLogger(UsernameTakenException.class.getName())
                .log(Level.SEVERE, null, ex);
        ExceptionDTO err = new ExceptionDTO(409,ex.getMessage());
        return Response
                .status(409)
                .entity(gson.toJson(err))
                .type(MediaType.APPLICATION_JSON)
                .build();
    }
}