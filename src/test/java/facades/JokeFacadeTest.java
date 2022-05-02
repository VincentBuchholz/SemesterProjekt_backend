package facades;

import dtos.JokeDTO;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

class JokeFacadeTest {
    private JokeFacade jokeFacade = new JokeFacade();
    @Test
    void getCombined() throws IOException {
        JokeDTO jokeDTO = jokeFacade.getCombined();
        assertNotNull(jokeDTO.getJoke1());
        assertNotNull(jokeDTO.getJoke2());
    }
}