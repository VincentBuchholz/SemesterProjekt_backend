package dtos;

import entities.User;

public class UserDTO {
    private int id;
    private String userName;
    private String firstName;
    private String lastName;
    private String phone;
    private String email;

    public UserDTO(User user) {
        this.id = user.getId();
        this.userName = user.getUserName();
        this.firstName = user.getFirstName();
        this.lastName = user.getLastName();
        this.phone = user.getPhone();
        this.email = user.getEmail();
    }

    @Override
    public String toString() {
        return "UserDTO{" +
                "id=" + id +
                ", userName='" + userName + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", phone='" + phone + '\'' +
                ", email='" + email + '\'' +
                '}';
    }
}
