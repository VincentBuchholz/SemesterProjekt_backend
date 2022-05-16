package facades;

import dtos.*;
import entities.MealPlan;
import entities.Role;
import entities.User;
import entities.WorkoutPlan;
import errorhandling.UsernameTakenException;
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
    User user2;
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
        user2 = new User("user2", "test123","Karlhans","Larsen","karlhans@larsen.dk","+4565432211");
        coach1 = new User("coach1", "test123","Hans","Svendsen","hans@coach.dk","+4599112233");
        coach2 = new User("coach2", "test123","Emil","Karlson","emil@coach.dk","+4529112233");

        Role userRole = new Role("user");
        Role coachRole = new Role("coach");
        user1.setRole(userRole);
        coach1.setRole(coachRole);
        coach2.setRole(coachRole);

        EntityManager em = emf.createEntityManager();

        em.getTransaction().begin();
        em.createNamedQuery("Request.deleteAllRows").executeUpdate();
        em.createNamedQuery("UserWeighIn.deleteAllRows").executeUpdate();
        em.createNamedQuery("UserNutrition.deleteAllRows").executeUpdate();
        em.createNamedQuery("MealPlan.deleteAllRows").executeUpdate();
        em.createNamedQuery("WorkoutPlan.deleteAllRows").executeUpdate();
        em.createNamedQuery("User.deleteAllRows").executeUpdate();
        em.createNamedQuery("Role.deleteAllRows").executeUpdate();
        em.getTransaction().commit();
        try{
            em.getTransaction().begin();
            em.persist(userRole);
            em.persist(coachRole);
            em.persist(user1);
            em.persist(user2);
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

    @Test
    void createUserTest() throws UsernameTakenException {
        System.out.println("Create user test!");
        User user = new User("testuser", "testuserpass","testuser","testuser","testuser@mail.dk","+4565432211",coach1.getId());
        UserDTO userDTO = new UserDTO(user);
        assertEquals("testuser",facade.createUser(userDTO).getUserName());
    }

    @Test
    void getCustomersByCoachID() throws UsernameTakenException {
        System.out.println("get customers by coach test!");
        User user = new User("testuser", "testuserpass","testuser","testuser","testuser@mail.dk","+4565432211",coach1.getId());
        UserDTO userDTO = new UserDTO(user);
        facade.createUser(userDTO);
        for (UserDTO customer : facade.getCustomersByCoachID(coach1.getId())) {
            System.out.println(customer);
        }
        assertEquals(1,facade.getCustomersByCoachID(coach1.getId()).size());
    }

    @Test
    void getCustomerByID() throws UsernameTakenException {
        System.out.println("get customer by id test!");
        User user = new User("testuser", "testuserpass","testuser","testuser","testuser@mail.dk","+4565432211",coach1.getId());
        UserDTO userDTO = new UserDTO(user);
        UserDTO newUserDTO = facade.createUser(userDTO);
        assertEquals("testuser",facade.getCustomerByID(newUserDTO.getId()).getFirstName());
    }

    @Test
    void getNutritionsByUserID() throws UsernameTakenException {
        System.out.println("get Nutritions by userID test!");
        User user = new User("testuser", "testuserpass","testuser","testuser","testuser@mail.dk","+4565432211",coach1.getId());
        UserDTO userDTO = new UserDTO(user);
        assertEquals(0,facade.getNutritionsByUser(facade.createUser(userDTO).getId()).getCalories());
    }

    @Test
    void updateUserNutritionTest() throws UsernameTakenException {
        System.out.println("Update user nutrition test");
        User user3 = new User("testuser3", "testuserpass3","testuser3","testuser3","testuser@mail.dk","+4565432211",2);
        UserDTO userDTO =facade.createUser(new UserDTO(user3));
        UserNutritionDTO userNutritionDTO = facade.getNutritionsByUser(userDTO.getId());
        userNutritionDTO.setCalories(3700);
        userNutritionDTO.setProtein(250);
        userNutritionDTO.setFat(100);
        userNutritionDTO.setCarbs(400);
        assertEquals(3700,facade.updateUserNutrition(userNutritionDTO).getCalories());
    }

    @Test
    void addWeighInByUserID() throws UsernameTakenException {
        System.out.println("add weigh in by userID test!");
        facade.addWeighInByUserID(user1.getId(),80);
        assertEquals(90,facade.addWeighInByUserID(user1.getId(),90).getWeight());
    }

    @Test
    void getLatestWeightByUserID(){
        System.out.println("Get Latest weight test!");
        UserWeighInDTO userWeighInDTO = facade.addWeighInByUserID(user1.getId(),81);
        assertEquals(userWeighInDTO,facade.getLatestUserWeighin(user1.getId()));

    }

    @Test
    void setMealPlanTest() {
        System.out.println("Set mealplan test!");
        MealPlanDTO mealPlanDTO1 = new MealPlanDTO(new MealPlan(user1,"test.pdf"));
        MealPlanDTO mealPlanDTO2 = new MealPlanDTO(new MealPlan(user1,"test2.pdf"));
        facade.setMealPlan(mealPlanDTO1);
       assertEquals("test2.pdf",facade.setMealPlan(mealPlanDTO2).getFileName());
    }

    @Test
    void getMealPlanByUserID() {
        System.out.println("get mealplan test!");
        MealPlanDTO mealPlanDTO1 = new MealPlanDTO(new MealPlan(user1,"test.pdf"));
        MealPlanDTO mealPlanDTO2 = new MealPlanDTO(new MealPlan(user1,"test2.pdf"));
        facade.setMealPlan(mealPlanDTO1);
        facade.setMealPlan(mealPlanDTO2);
        assertEquals("test2.pdf",facade.getMealPlanByUserID(user1.getId()).getFileName());
    }


    @Test
    void setWorkoutPlanTest() {
        System.out.println("Set workoutplan test!");
        WorkoutPlanDTO workoutPlanDTO = new WorkoutPlanDTO(new WorkoutPlan(user1,"test.pdf"));
        WorkoutPlanDTO workoutPlanDTO2 = new WorkoutPlanDTO(new WorkoutPlan(user1,"test2.pdf"));
        facade.setWorkoutPlan(workoutPlanDTO);
        assertEquals("test2.pdf",facade.setWorkoutPlan(workoutPlanDTO2).getFileName());
    }

    @Test
    void getWorkoutPlanByUserID() {
        System.out.println("get workoutplan test!");
        WorkoutPlanDTO workoutPlanDTO = new WorkoutPlanDTO(new WorkoutPlan(user1,"test.pdf"));
        WorkoutPlanDTO workoutPlanDTO2 = new WorkoutPlanDTO(new WorkoutPlan(user2,"test2.pdf"));
        facade.setWorkoutPlan(workoutPlanDTO);
        facade.setWorkoutPlan(workoutPlanDTO2);
        assertEquals("test2.pdf",facade.getWorkoutPlanByUserID(user2.getId()).getFileName());
    }
}