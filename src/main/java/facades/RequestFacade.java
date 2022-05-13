package facades;

import dtos.RequestDTO;
import dtos.UserDTO;
import entities.Request;
import entities.User;
import security.errorhandling.AuthenticationException;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.TypedQuery;
import java.util.Collection;
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
        EntityManager em = emf.createEntityManager();
        User user = em.find(User.class,requestDTO.getCoachID());
        Request request = new Request(user,requestDTO.getFirstName(), requestDTO.getLastName(),requestDTO.getEmail(),requestDTO.getPhone(), requestDTO.getDesc());
        try{
            em.getTransaction().begin();
            em.persist(request);
            em.getTransaction().commit();
            return new RequestDTO(request);
        } finally {
            em.close();
        }
    }

    public List<RequestDTO> getRequestsByCoachID(int coachID) {
        List<RequestDTO> requestDTOS;

        EntityManager em = emf.createEntityManager();
        try {
            TypedQuery<RequestDTO> query = em.createQuery("SELECT NEW dtos.RequestDTO(r) FROM Request r where r.user.id =:coachID ", RequestDTO.class);
            query.setParameter("coachID",coachID);
            requestDTOS = query.getResultList();
            return requestDTOS;
        } finally {
            em.close();
        }
    }

    public RequestDTO getRequestByRequestID(int requestID) {
        EntityManager em = emf.createEntityManager();
        try{
            Request request = em.find(Request.class,requestID);
            return new RequestDTO(request);
        } finally {
            em.close();
        }
    }
}
