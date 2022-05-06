package entities;

import javax.persistence.*;

@Entity
@Table(name = "user_nutrition")
@NamedQuery(name = "UserNutrition.deleteAllRows", query = "DELETE from UserNutrition ")
public class UserNutrition {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @Column(name = "user_id")
    private int userID;

    @Column(name = "calories")
    private int calories;

    @Column(name = "protein")
    private int protein;

    @Column(name = "fat")
    private int fat;

    @Column(name = "carbs")
    private int carbs;


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getCalories() {
        return calories;
    }

    public void setCalories(int calories) {
        this.calories = calories;
    }

    public int getProtein() {
        return protein;
    }

    public void setProtein(int protein) {
        this.protein = protein;
    }

    public int getFat() {
        return fat;
    }

    public void setFat(int fat) {
        this.fat = fat;
    }

    public int getCarbs() {
        return carbs;
    }

    public void setCarbs(int carbs) {
        this.carbs = carbs;
    }

    public int getUserID() {
        return userID;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }

    public UserNutrition() {
    }

    public UserNutrition(int userID, int calories, int protein, int fat, int carbs) {
        this.userID = userID;
        this.calories = calories;
        this.protein = protein;
        this.fat = fat;
        this.carbs = carbs;
    }

    @Override
    public String toString() {
        return "UserNutrition{" +
                "id=" + id +
                ", userID=" + userID +
                ", calories=" + calories +
                ", protein=" + protein +
                ", fat=" + fat +
                ", carbs=" + carbs +
                '}';
    }
}
