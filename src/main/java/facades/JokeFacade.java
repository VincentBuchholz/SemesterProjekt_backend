package facades;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import dtos.JokeDTO;
import utils.HttpUtils;

import java.io.IOException;


public class JokeFacade {

    public JokeDTO getCombined () throws IOException {
        String chuck = HttpUtils.fetchData("https://api.chucknorris.io/jokes/random");
        String dad = HttpUtils.fetchData("https://icanhazdadjoke.com");



        JsonObject jobjChuck = new Gson().fromJson(chuck, JsonObject.class);
        JsonObject jobjDad = new Gson().fromJson(dad, JsonObject.class);

        String resultChuck = jobjChuck.get("value").getAsString();
        String resultDad = jobjDad.get("joke").getAsString();

        JokeDTO jokeDTO = new JokeDTO(resultChuck,resultDad);

        return jokeDTO;
    }
}
