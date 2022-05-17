package dtos;

public class CalorieBurnedDTO {

private String burnedCalorie;
private String unit;


    public CalorieBurnedDTO(String burnedCalorie, String unit) {
        this.burnedCalorie = burnedCalorie;
        this.unit = unit;
    }

    public CalorieBurnedDTO() {
    }

    public String getBurnedCalorie() {
        return burnedCalorie;
    }

    public void setBurnedCalorie(String burnedCalorie) {
        this.burnedCalorie = burnedCalorie;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    @Override
    public String toString() {
        return "CalorieBurnedDTO{" +
                "burnedCalorie='" + burnedCalorie + '\'' +
                ", unit='" + unit + '\'' +
                '}';
    }
}
