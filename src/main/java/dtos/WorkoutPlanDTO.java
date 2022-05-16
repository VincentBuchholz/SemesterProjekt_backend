package dtos;

import entities.MealPlan;
import entities.WorkoutPlan;

import java.util.Objects;

public class WorkoutPlanDTO {
    private int id;
    private int userID;
    private String fileName;

    public WorkoutPlanDTO(WorkoutPlan workoutPlan) {
        this.id = workoutPlan.getId();
        this.userID = workoutPlan.getUser().getId();
        this.fileName = workoutPlan.getFileName();
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
        WorkoutPlanDTO that = (WorkoutPlanDTO) o;
        return id == that.id && userID == that.userID && Objects.equals(fileName, that.fileName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, userID, fileName);
    }
}
