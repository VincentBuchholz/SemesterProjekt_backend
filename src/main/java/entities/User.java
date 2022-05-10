package entities;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import org.mindrot.jbcrypt.BCrypt;

@Entity
@Table(name = "users")
@NamedQuery(name = "User.deleteAllRows", query = "DELETE from User")
public class User implements Serializable {

  private static final long serialVersionUID = 1L;
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id")
  private int id;

  @Basic(optional = false)
  @NotNull
  @Column(name = "user_name", length = 25)
  private String userName;

  @Basic(optional = false)
  @NotNull
  @Size(min = 1, max = 255)
  @Column(name = "user_pass")
  private String userPass;

  @ManyToOne
  @JoinColumn(name = "role_name")
  private Role role;


  @OneToMany(mappedBy = "user")
  private List<UserWeighIn> weightInList;

  @Column(name = "first_name")
  private String firstName;

  @Column(name = "last_name")
  private String lastName;

  @Column(name = "email")
  private String email;

  @Column(name = "phone")
  private String phone;

  @Column(name = "coach_id")
  private int coachID;


  public User() {}

   public boolean verifyPassword(String pw){
        return(BCrypt.checkpw(pw,userPass));
    }



  public User(String userName, String userPass, String firstName, String lastName, String email, String phone) {
    this.userName = userName;
    this.userPass = BCrypt.hashpw(userPass,BCrypt.gensalt(12));
    this.firstName=firstName;
    this.lastName=lastName;
    this.email=email;
    this.phone=phone;
  }

  public User(String userName, String userPass, String firstName, String lastName, String email, String phone,int coachID) {
    this.userName = userName;
    this.userPass = BCrypt.hashpw(userPass,BCrypt.gensalt(12));
    this.firstName=firstName;
    this.lastName=lastName;
    this.email=email;
    this.phone=phone;
    this.coachID=coachID;
  }

  public User(String userName,String userPass) {
    this.userName = userName;
    this.userPass = BCrypt.hashpw(userPass,BCrypt.gensalt(12));
  }

  public String getUserName() {
    return userName;
  }

  public void setUserName(String userName) {
    this.userName = userName;
  }

  public String getUserPass() {
    return this.userPass;
  }

  public void setUserPass(String userPass) {
    this.userPass = userPass;
  }

  public Role getRole() {
    return role;
  }

  public void setRole(Role role) {
    this.role=role;
  }

  public String getFirstName() {
    return firstName;
  }

  public void setFirstName(String firstName) {
    this.firstName = firstName;
  }

  public String getLastName() {
    return lastName;
  }

  public void setLastName(String lastName) {
    this.lastName = lastName;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public String getPhone() {
    return phone;
  }

  public void setPhone(String phone) {
    this.phone = phone;
  }

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public int getCoachID() {
    return coachID;
  }

  public void setCoachID(int coachID) {
    this.coachID = coachID;
  }
}
