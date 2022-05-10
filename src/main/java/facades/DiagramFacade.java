package facades;

import dtos.UserNutritionDTO;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.TypedQuery;
import java.text.DecimalFormat;

public class DiagramFacade {

    private static EntityManagerFactory emf;
    private static DiagramFacade instance;

    private DiagramFacade() {
    }

    /**
     * @param _emf
     * @return the instance of this facade.
     */
    public static DiagramFacade getInstance(EntityManagerFactory _emf) {
        if (instance == null) {
            emf = _emf;
            instance = new DiagramFacade();
        }
        return instance;
    }


    public String getMacroChartByUserID(int userID) {
        EntityManager em = emf.createEntityManager();
        UserNutritionDTO unDTO;
        try{
            TypedQuery<UserNutritionDTO> query = em.createQuery("SELECT new dtos.UserNutritionDTO(u) from UserNutrition u where u.userID=:userID",UserNutritionDTO.class);
            query.setParameter("userID",userID);
            unDTO = query.getSingleResult();
        } finally {
            em.close();
        }
        double protein = unDTO.getProtein();
        double fat = unDTO.getFat();
        double carbs = unDTO.getCarbs();
        double sum = protein+fat+carbs;
        protein = protein/sum*100;
        fat = fat/sum*100;
        carbs = carbs/sum*100;
        protein = Math.round(protein);
        fat = Math.round(fat);
        carbs = Math.round(carbs);
        DecimalFormat format = new DecimalFormat("0.#");

        return "https://image-charts.com/chart?cht=p3&chs=700x500&chd=t:"+format.format(protein)+","+format.format(carbs)+","+format.format(fat)+"&chl=Protein|Carbs|Fat&chdl="+format.format(protein)+"%|"+format.format(carbs)+"%|"+format.format(fat)+"%&chco=201dc2";
    }
}