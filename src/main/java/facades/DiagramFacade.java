package facades;

import dtos.UserNutritionDTO;
import dtos.UserWeighInDTO;
import utils.HttpUtils;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.TypedQuery;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.Calendar;
import java.util.List;

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
            TypedQuery<UserNutritionDTO> query = em.createQuery("SELECT new dtos.UserNutritionDTO(u) from UserNutrition u where u.user.id=:userID",UserNutritionDTO.class);
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

        return "https://image-charts.com/chart?cht=p3&chs=700x500&chd=t:"+format.format(protein)+","+format.format(carbs)+","+format.format(fat)+"&chl=Protein|Carbs|Fat&chdl="+format.format(protein)+"%|"+format.format(carbs)+"%|"+format.format(fat)+"%&chco=005F6A";
    }


    public String getWeightChartByUserID(int userID) {
        EntityManager em = emf.createEntityManager();
        List<UserWeighInDTO> userWeighInDTOS;
        try{
            TypedQuery<UserWeighInDTO> query = em.createQuery("SELECT new dtos.UserWeighInDTO (u) from UserWeighIn u where u.user.id =:userID order by u.date asc",UserWeighInDTO.class);
            query.setParameter("userID",userID);
            userWeighInDTOS = query.getResultList();
        } finally {
            em.close();
        }

        StringBuilder date = new StringBuilder("0%3A");
        StringBuilder weight = new StringBuilder("a%3A");
        int count = 0;

        for (UserWeighInDTO userWeighInDTO: userWeighInDTOS) {
            if(count == 0) {
                weight.append(userWeighInDTO.getWeight());
            } else {
                weight.append("%2C"+userWeighInDTO.getWeight());
            }

            Calendar cal = Calendar.getInstance();
            cal.setTime(userWeighInDTO.getDate());
            int month = cal.get(Calendar.MONTH);
            int day = cal.get(Calendar.DAY_OF_MONTH);
            int year = cal.get(Calendar.YEAR);
            month++;

            date.append("%7C"+day+"-"+month+"-"+year);
            count++;
        }
       return "https://image-charts.com/chart?chco=005F6A&chd="+weight+"&chdlp=r&chg=1%2C1&chls=3&chs=700x450&cht=ls&chtt=Weight&chxl="+date+"&chxt=x%2Cy";
    }
}