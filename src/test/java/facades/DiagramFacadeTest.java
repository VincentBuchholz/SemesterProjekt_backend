package facades;

import dtos.UserDTO;
import dtos.UserNutritionDTO;
import entities.Request;
import entities.User;
import errorhandling.UsernameTakenException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import utils.EMF_Creator;

import javax.persistence.EntityManagerFactory;

import static org.junit.jupiter.api.Assertions.*;

class DiagramFacadeTest {

    private static EntityManagerFactory emf;
    private static DiagramFacade facade;
    private static UserFacade userFacade;
    @BeforeAll
    public static void setUpClass() throws UsernameTakenException {
        emf = EMF_Creator.createEntityManagerFactoryForTest();
        facade = DiagramFacade.getInstance(emf);
        userFacade = UserFacade.getUserFacade(emf);
        User user = new User("test","test123","test","test","test@test.dk","2222222");
        userFacade.createUser(new UserDTO(user));
        UserNutritionDTO unt = userFacade.getNutritionsByUser(1);
        unt.setCalories(2500);
        unt.setProtein(50);
        unt.setCarbs(20);
        unt.setFat(30);
        userFacade.updateUserNutrition(unt);

    }

    @BeforeEach
    void setUp() {
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void getMacroChartByUserID() {
        assertEquals("https://image-charts.com/chart?cht=p3&chs=700x500&chd=t:50,20,30&chl=Protein|Carbs|Fat&chdl=50%|20%|30%&chco=201dc2",facade.getMacroChartByUserID(1));
    }
}