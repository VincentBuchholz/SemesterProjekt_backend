package entities;

import javax.persistence.*;

@Entity
@Table(name = "workout_plan")
@NamedQuery(name = "WorkoutPlan.deleteAllRows", query = "DELETE from WorkoutPlan ")
public class WorkoutPlan {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Column(name = "file_name")
    private String fileName;

    public WorkoutPlan() {
    }

    public WorkoutPlan(User user, String fileName) {
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
