package dtos;

import entities.Request;

import javax.persistence.Column;

public class RequestDTO {
    private int id;
    private int coachID;
    private String firstName;
    private String lastName;
    private String email;
    private String desc;

    public RequestDTO(Request request) {
        if (request.getId()>0){
            this.id= request.getId();
        }
        this.coachID = request.getCoachID();
        this.firstName = request.getFirstName();
        this.lastName = request.getLastName();
        this.email = request.getEmail();
        this.desc = request.getDesc();
    }

    public RequestDTO(int coachID, String firstName, String lastName, String email, String desc) {
        this.coachID = coachID;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.desc = desc;
    }

    public int getCoachID() {
        return coachID;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getEmail() {
        return email;
    }

    public String getDesc() {
        return desc;
    }

    public int getID() {
        return id;
    }

    public void setID(int id) {
        this.id = id;
    }

    public void setCoachID(int coachID) {
        this.coachID = coachID;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    @Override
    public String toString() {
        return "RequestDTO{" +
                "coachID=" + coachID +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", email='" + email + '\'' +
                ", desc='" + desc + '\'' +
                '}';
    }
}
