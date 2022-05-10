package facades;

import dtos.RequestDTO;
import dtos.UserDTO;
import dtos.UserNutritionDTO;
import dtos.UserWeighInDTO;
import entities.*;

import javax.persistence.*;

import errorhandling.NotFoundException;
import errorhandling.UsernameTakenException;
import security.errorhandling.AuthenticationException;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
public class UserFacade {

    private static EntityManagerFactory emf;
    private static UserFacade instance;

    private UserFacade() {
    }

    /**
     *
     * @param _emf
     * @return the instance of this facade.
     */
    public static UserFacade getUserFacade(EntityManagerFactory _emf) {
        if (instance == null) {
            emf = _emf;
            instance = new UserFacade();
        }
        return instance;
    }

    public User getVeryfiedUser(String username, String password) throws AuthenticationException {
        EntityManager em = emf.createEntityManager();
        User user;
        try {
            TypedQuery<User> query = em.createQuery("SELECT u FROM User u where u.userName=:username",User.class);
            query.setParameter("username",username);
            query.setMaxResults(1);
            user = query.getSingleResult();
            if (user == null || !user.verifyPassword(password)) {
                throw new AuthenticationException("Invalid user name or password");
            }
        } finally {
            em.close();
        }
        return user;
    }

    public List<UserDTO> getCoaches(){
        List<UserDTO> coachDTOs = new ArrayList<>();
        EntityManager em = emf.createEntityManager();
        try {
            TypedQuery<User> query = em.createQuery("SELECT u FROM User u where u.role.roleName='coach'", User.class);
            List<User> coaches = query.getResultList();
            for (User coach : coaches) {
                coachDTOs.add(new UserDTO(coach.getId(),coach.getFirstName(),coach.getLastName()));
            }
            return coachDTOs;
        } finally {
            em.close();
        }
    }

    public List<UserDTO> getCustomersByCoachID(int id){
        List<UserDTO> customersDTO = new ArrayList<>();
        EntityManager em = emf.createEntityManager();
        try {
            TypedQuery<User> query = em.createQuery("SELECT u FROM User u where u.coachID=:id", User.class);
            query.setParameter("id",id);
            List<User> customers = query.getResultList();
            for (User customer : customers) {
                customersDTO.add(new UserDTO(customer.getId(),customer.getFirstName(),customer.getLastName()));
            }
            return customersDTO;
        } finally {
            em.close();
        }
    }

    public UserDTO getCustomerByID(int id){
        EntityManager em = emf.createEntityManager();
        try {
            TypedQuery<User> query = em.createQuery("SELECT u FROM User u where u.id=:id", User.class);
            query.setParameter("id",id);
            User customer = query.getSingleResult();

            TypedQuery<UserNutritionDTO> nutritionQuery = em.createQuery("SELECT new dtos.UserNutritionDTO(n) FROM UserNutrition n where n.userID=:customerID", UserNutritionDTO.class);
            nutritionQuery.setParameter("customerID",customer.getId());
            UserNutritionDTO nutritionDTO = nutritionQuery.getSingleResult();

            UserDTO customerDTO = new UserDTO(customer.getId(), customer.getFirstName(), customer.getLastName(),customer.getEmail(),customer.getPhone());
            customerDTO.setNutritionDTO(nutritionDTO);
            return customerDTO;
        } finally {
            em.close();
        }
    }

    public UserDTO createUser(UserDTO userDTO) throws UsernameTakenException {

        System.out.println(usernameTaken(userDTO.getUserName()));

        if(usernameTaken(userDTO.getUserName())){
            throw new UsernameTakenException("Username is taken");
        }

        User user = new User(userDTO.getUserName(), userDTO.getPassword(), userDTO.getFirstName(), userDTO.getLastName(), userDTO.getEmail(), userDTO.getPhone(), userDTO.getCoachID());
        EntityManager em = emf.createEntityManager();

        Role userRole = em.find(Role.class,"user");
        user.setRole(userRole);

        try{
            em.getTransaction().begin();
            em.persist(user);
            em.getTransaction().commit();
            UserNutrition userNutrition = new UserNutrition(user.getId(),0,0,0,0);
            em.getTransaction().begin();
            em.persist(userNutrition);
            em.getTransaction().commit();
            UserDTO newUserDTO = new UserDTO(user);
            newUserDTO.setNutritionDTO(new UserNutritionDTO(userNutrition));
            return newUserDTO;
        } finally {
            em.close();
        }
    }

    private static boolean usernameTaken(String username){
        EntityManager em = emf.createEntityManager();
        try {
            TypedQuery<User> query = em.createQuery("SELECT u from User u where u.userName =:username",User.class);
            query.setParameter("username",username);
            try {
                User userFound = query.getSingleResult();
            } catch (NoResultException e){
                return false;
            }
        }
        finally {
            em.close();
        }
        return true;
    }

    public UserNutritionDTO updateUserNutrition(UserNutritionDTO userNutritionDTO) {
        EntityManager em = emf.createEntityManager();
        try{
            UserNutrition userNutrition = em.find(UserNutrition.class,userNutritionDTO.getId());
            userNutrition.setCalories(userNutritionDTO.getCalories());
            userNutrition.setProtein(userNutritionDTO.getProtein());
            userNutrition.setFat(userNutritionDTO.getFat());
            userNutrition.setCarbs(userNutritionDTO.getCarbs());
            em.getTransaction().begin();
            em.merge(userNutrition);
            em.getTransaction().commit();
            return userNutritionDTO;

        } finally {
            em.close();
        }
    }

    public UserNutritionDTO getNutritionsByUser(int userID) {
        EntityManager em = emf.createEntityManager();
        try {
            TypedQuery<UserNutrition> query = em.createQuery("SELECT n FROM UserNutrition n where n.userID=:userID", UserNutrition.class);
            query.setParameter("userID",userID);
            UserNutrition nutrition = query.getSingleResult();
            return new UserNutritionDTO(nutrition);
        } finally {
            em.close();
        }
    }

    public UserWeighInDTO addWeighInByUserID(int userID, double weight) {
        EntityManager em = emf.createEntityManager();
        UserWeighIn userWeighIn;
        try{
            User userFound = em.find(User.class,userID);
            userWeighIn = new UserWeighIn(userFound,weight);
            em.getTransaction().begin();
            em.persist(userWeighIn);
            em.getTransaction().commit();
        }finally {
            em.close();
        }
        return new UserWeighInDTO(userWeighIn);

    }
}
