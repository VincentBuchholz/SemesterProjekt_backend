package dtos;

import entities.UserNutrition;

import java.util.Objects;

public class UserNutritionDTO {
    private int id;
    private int userID;
    private int calories;
    private int protein;
    private int fat;
    private int carbs;

    public UserNutritionDTO(UserNutrition userNutrition) {
        this.id = userNutrition.getId();
        this.calories = userNutrition.getCalories();
        this.protein = userNutrition.getProtein();
        this.fat = userNutrition.getFat();
        this.carbs = userNutrition.getCarbs();
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

    @Override
    public String toString() {
        return "UserNutritionDTO{" +
                "id=" + id +
                ", userID=" + userID +
                ", calories=" + calories +
                ", protein=" + protein +
                ", fat=" + fat +
                ", carbs=" + carbs +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserNutritionDTO that = (UserNutritionDTO) o;
        return id == that.id && userID == that.userID && calories == that.calories && protein == that.protein && fat == that.fat && carbs == that.carbs;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, userID, calories, protein, fat, carbs);
    }
}
