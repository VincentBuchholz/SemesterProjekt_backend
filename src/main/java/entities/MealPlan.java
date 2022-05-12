package entities;

import javax.persistence.*;

@Entity
@Table(name = "meal_plan")
@NamedQuery(name = "MealPlan.deleteAllRows", query = "DELETE from MealPlan ")
public class MealPlan {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Column(name = "file_name")
    private String fileName;

    public MealPlan() {
    }

    public MealPlan(User user, String fileName) {
        this.user = user;
        this.fileName = fileName;
    }

    public User getUser() {
        return user;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }
}
