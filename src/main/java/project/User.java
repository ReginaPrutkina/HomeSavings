package project;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

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
    int passwordHash;
    @Column (unique = true, nullable = false)
    private String email;
    @Column
    private String family;
    @Column
    private String name;

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
        this.passwordHash = password.trim().hashCode();
    }

    public int getPasswordHash() {
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
