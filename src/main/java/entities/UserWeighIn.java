package entities;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "user_weigh_in")
@NamedQuery(name = "UserWeighIn.deleteAllRows", query = "DELETE from UserWeighIn ")
public class UserWeighIn {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Column(name = "weight")
    private double weight;

    @Column(name = "date")
    private Date date;


    public UserWeighIn() {
    }

    public UserWeighIn(User user, double weight) {
        this.user = user;
        this.weight = weight;
        this.date = new Date();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
}
