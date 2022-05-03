package facades;

import dtos.UserDTO;
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

class UserFacadeTest {
    private static EntityManagerFactory emf;
    private static UserFacade facade;

    User user1;
    User coach1;
    User coach2;

    @BeforeAll
    public static void setUpClass(){
        emf = EMF_Creator.createEntityManagerFactoryForTest();
        facade = UserFacade.getUserFacade(emf);

    }

    @BeforeEach
    void setUp() {
        user1 = new User("user", "test123","Karl","Larsen","karl@larsen.dk","+4565432211");
        coach1 = new User("coach1", "test123","Hans","Svendsen","hans@coach.dk","+4599112233");
        coach2 = new User("coach2", "test123","Emil","Karlson","emil@coach.dk","+4529112233");

        Role userRole = new Role("user");
        Role coachRole = new Role("coach");
        user1.setRole(userRole);
        coach1.setRole(coachRole);
        coach2.setRole(coachRole);

        EntityManager em = emf.createEntityManager();

        em.getTransaction().begin();
        em.createNamedQuery("User.deleteAllRows").executeUpdate();
        em.createNamedQuery("Role.deleteAllRows").executeUpdate();
        em.getTransaction().commit();
        try{
            em.getTransaction().begin();
            em.persist(userRole);
            em.persist(coachRole);
            em.persist(user1);
            em.persist(coach1);
            em.persist(coach2);
            em.getTransaction().commit();
        } finally {
            em.close();
        }
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void getCoaches() {
        System.out.println("Get coaches test!");
        for (UserDTO coach:facade.getCoaches()) {
            System.out.println(coach);
        }
        assertEquals(2,facade.getCoaches().size());
    }
}