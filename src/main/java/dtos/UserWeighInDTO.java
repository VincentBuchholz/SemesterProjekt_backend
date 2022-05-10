package dtos;

import entities.UserWeighIn;

import java.util.Date;
import java.util.Objects;

public class UserWeighInDTO {
    private int id;
    private int userID;
    private double weight;
    private Date date;

    public UserWeighInDTO(UserWeighIn userWeighIn) {
        this.userID = userWeighIn.getUser().getId();
        this.weight = userWeighIn.getWeight();
        this.date = userWeighIn.getDate();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUserID() {
        return userID;
    }

    public void setUserID(int userID) {
        this.userID = userID;
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

    @Override
    public String toString() {
        return "UserWeighInDTO{" +
                "id=" + id +
                ", userID=" + userID +
                ", weight=" + weight +
                ", date=" + date +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserWeighInDTO that = (UserWeighInDTO) o;
        return id == that.id && userID == that.userID && Double.compare(that.weight, weight) == 0 && Objects.equals(date, that.date);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, userID, weight, date);
    }
}
