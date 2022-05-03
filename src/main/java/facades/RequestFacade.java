package facades;

import dtos.RequestDTO;
import dtos.UserDTO;
import entities.Request;
import entities.User;
import security.errorhandling.AuthenticationException;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.TypedQuery;
import java.util.List;
public class RequestFacade {

    private static EntityManagerFactory emf;
    private static RequestFacade instance;

    private RequestFacade() {
    }

    /**
     *
     * @param _emf
     * @return the instance of this facade.
     */
    public static RequestFacade getRequestFacade(EntityManagerFactory _emf) {
        if (instance == null) {
            emf = _emf;
            instance = new RequestFacade();
        }
        return instance;
    }

    public RequestDTO createRequest(RequestDTO requestDTO){
        Request request = new Request(requestDTO.getCoachID(),requestDTO.getFirstName(), requestDTO.getLastName(),requestDTO.getEmail(), requestDTO.getDesc());
        EntityManager em = emf.createEntityManager();
        try{
            em.getTransaction().begin();
            em.persist(request);
            em.getTransaction().commit();
            return new RequestDTO(request);
        } finally {
            em.close();
        }
    }

}
