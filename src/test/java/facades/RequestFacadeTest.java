package facades;

import dtos.RequestDTO;
import entities.Request;
import entities.Role;
import entities.User;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import utils.EMF_Creator;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import static org.junit.jupiter.api.Assertions.*;


class RequestFacadeTest {
    private static EntityManagerFactory emf;
    private static RequestFacade facade;
    User coach1;
    User coach2;
    Request request1;
    Request request2;

    @BeforeAll
    public static void setUpClass(){
        emf = EMF_Creator.createEntityManagerFactoryForTest();
        facade = RequestFacade.getRequestFacade(emf);

    }

    @BeforeEach
    void setUp() {
        coach1 = new User("coach1", "test123","Hans","Svendsen","hans@coach.dk","+4599112233");
        coach2 = new User("coach2", "test123","Emil","Karlson","emil@coach.dk","+4529112233");
        Role userRole = new Role("user");
        Role coachRole = new Role("coach");
        coach1.setRole(coachRole);
        coach2.setRole(coachRole);

        request1 = new Request(coach1,"test1","test1","test1@test.dk","6453424","desc1");
        request2 = new Request(coach1,"test2","test2","test2@test.dk","6453424","desc2");

        EntityManager em = emf.createEntityManager();

        em.getTransaction().begin();
        em.createNamedQuery("Request.deleteAllRows").executeUpdate();
        em.createNamedQuery("UserWeighIn.deleteAllRows").executeUpdate();
        em.createNamedQuery("UserNutrition.deleteAllRows").executeUpdate();
        em.createNamedQuery("MealPlan.deleteAllRows").executeUpdate();
        em.createNamedQuery("User.deleteAllRows").executeUpdate();
        em.createNamedQuery("Role.deleteAllRows").executeUpdate();
        em.getTransaction().commit();
        try{
            em.getTransaction().begin();
            em.persist(userRole);
            em.persist(coachRole);
            em.persist(coach1);
            em.persist(coach2);
            em.persist(request1);
            em.persist(request2);
            em.getTransaction().commit();
        } finally {
            em.close();
        }
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void createRequestTest() {
        System.out.println("TEST CREATE REQUEST");
        RequestDTO requestDTO = new RequestDTO(coach1.getId(),"viggo","mogens","viggo@mail.dk","645251551","get fit fam");
        int requestID = facade.createRequest(requestDTO).getID();
        EntityManager em = emf.createEntityManager();

        Request request = em.find(Request.class, requestID);

        assertEquals(coach1.getId(), request.getCoachID());
        assertEquals("get fit fam", request.getDesc());
    }


    @Test
    void getRequestList() {
        assertEquals(2,facade.getRequestsByCoachID(coach1.getId()).size());
    }

    @Test
    void getRequestByID() {
        RequestDTO requestDTO = new RequestDTO(request1);

        assertEquals(requestDTO,facade.getRequestByRequestID(request1.getId()));
    }
}