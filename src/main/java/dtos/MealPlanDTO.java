package dtos;

import entities.MealPlan;

import java.util.Objects;

public class MealPlanDTO {
    private int id;
    private int userID;
    private String fileName;

    public MealPlanDTO(MealPlan mealPlan) {
        this.id = mealPlan.getId();
        this.userID = mealPlan.getUser().getId();
        this.fileName = mealPlan.getFileName();
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

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    @Override
    public String toString() {
        return "MealPlanDTO{" +
                "id=" + id +
                ", userID=" + userID +
                ", fileName='" + fileName + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MealPlanDTO that = (MealPlanDTO) o;
        return id == that.id && userID == that.userID && Objects.equals(fileName, that.fileName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, userID, fileName);
    }
}
