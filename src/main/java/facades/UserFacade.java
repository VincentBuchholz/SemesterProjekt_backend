package facades;

import dtos.UserDTO;
import entities.User;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.TypedQuery;

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
}
