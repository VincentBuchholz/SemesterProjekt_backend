package facades;

import dtos.RequestDTO;
import dtos.UserDTO;
import entities.Request;
import entities.Role;
import entities.User;

import javax.persistence.*;

import errorhandling.UsernameTakenException;
import security.errorhandling.AuthenticationException;

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
        EntityManager em = emf.createEntityManager();
        try {
            TypedQuery<UserDTO> query = em.createQuery("SELECT NEW dtos.UserDTO(u) FROM User u where u.role.roleName='coach'", UserDTO.class);
            List<UserDTO> coaches = query.getResultList();
            return coaches;
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
            return new UserDTO(user);
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
}
