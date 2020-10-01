package project;

import javax.persistence.*;
import java.io.Serializable;
import java.util.*;

@Entity
@Table(name = "users")
public class User implements Serializable {
    @Id
    @Column
    @GeneratedValue
    private Long id;
    @Column (unique = true, nullable = false)
    private String login;
    @Column (name = "password")
    protected String passwordHash;
    @Column (unique = true, nullable = false)
    private String email;
    @Column
    private String family;
    @Column
    private String name;
    @Column
    private String role;

    @OneToMany (mappedBy = "user",cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    private List<Deposit> deposits = new ArrayList<>();

    public User(){}

    public void setEmail(String email) {
        this.email = email.trim().toLowerCase();
    }

    public String getEmail() {
        return email;
    }

    public void setLogin(String login) {
        this.login = login.trim().toLowerCase();
    }

    public String getLogin() {
        return login;
    }

    public void setPasswordHash(String password) {
        this.passwordHash = Integer.toString(password.trim().hashCode());
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public void setFamily(String family) {
        this.family = family.trim();
    }

    public String getFamily() {
        return family;
    }

    public void setName(String name) {
        this.name = name.trim();
    }

    public String getName() {
        return name;
    }

    public Long getId() {
        return id;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public List<Deposit> getDeposits() {
        return deposits;
    }

    public void setDeposits(List<Deposit> deposits) {
        this.deposits = deposits;
    }
    public void addDeposit(Deposit deposit){
        deposit.setUser(this);
        deposits.add(deposit);
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", family='" + family + '\'' +
                ", name='" + name + '\'' +
                ", login='" + login + '\'' +
                ", email='" + email + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof User)) return false;
        User user = (User) o;
        return getId().equals(user.getId()) &&
                getLogin().equals(user.getLogin()) &&
                getEmail().equals(user.getEmail());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getLogin(), getEmail());
    }
}
