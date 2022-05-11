package facades;

import dtos.UserDTO;
import dtos.UserNutritionDTO;
import dtos.UserWeighInDTO;
import entities.Request;
import entities.User;
import entities.UserNutrition;
import entities.UserWeighIn;
import errorhandling.UsernameTakenException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import utils.EMF_Creator;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class DiagramFacadeTest {

    private static EntityManagerFactory emf;
    private static DiagramFacade facade;
    private static UserFacade userFacade;
    User user1;

    @BeforeAll
    public static void setUpClass() throws UsernameTakenException {
        emf = EMF_Creator.createEntityManagerFactoryForTest();
        facade = DiagramFacade.getInstance(emf);
        userFacade = UserFacade.getUserFacade(emf);

        UserNutrition un = new UserNutrition(1,3500,50,30,20);

        EntityManager em = emf.createEntityManager();


        try{
            em.getTransaction().begin();
            em.persist(un);
            em.getTransaction().commit();
        } finally {
            em.close();
        }

    }

    @BeforeEach
    void setUp() {

        user1 = new User("test","test123","test","test","test","test");
        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();
        em.createNamedQuery("UserWeighIn.deleteAllRows").executeUpdate();
        em.getTransaction().commit();
        try{
            em.getTransaction().begin();
            em.persist(user1);
            em.getTransaction().commit();
        } finally {
            em.close();
        }

    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void getMacroChartByUserID() {
        assertEquals("https://image-charts.com/chart?cht=p3&chs=700x500&chd=t:50,20,30&chl=Protein|Carbs|Fat&chdl=50%|20%|30%&chco=201dc2",facade.getMacroChartByUserID(1));
    }
    @Test
    void getUserWieghtChart(){
        List<UserWeighInDTO> userWeighInDTOS = new ArrayList<>();
        UserWeighInDTO userWeighInDTO1 = userFacade.addWeighInByUserID(user1.getId(),80);
        UserWeighInDTO userWeighInDTO2 = userFacade.addWeighInByUserID(user1.getId(),81);
        userWeighInDTOS.add(userWeighInDTO1);
        userWeighInDTOS.add(userWeighInDTO2);

        StringBuilder date = new StringBuilder("0%3A");

        for (UserWeighInDTO userWeighInDTO: userWeighInDTOS) {
            Calendar cal = Calendar.getInstance();
            cal.setTime(userWeighInDTO.getDate());
            int month = cal.get(Calendar.MONTH);
            int day = cal.get(Calendar.DAY_OF_MONTH);
            int year = cal.get(Calendar.YEAR);
            month++;
            date.append("%7C"+day+"-"+month+"-"+year);
        }

        assertEquals("https://image-charts.com/chart?chco=201DC2&chd=a%3A80.0%2C81.0&chdlp=r&chg=1%2C1&chls=3&chs=700x450&cht=ls&chtt=V%C3%A6gt&chxl="+date+"&chxt=x%2Cy",facade.getWeightChartByUserID(user1.getId()));
    }
}