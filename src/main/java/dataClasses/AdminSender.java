package dataClasses;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.Base64;
@Entity
@Table(name = "users")
public class AdminSender extends User{
    @JsonIgnore
    public String getPassword() {
        // для админа возвращаем не хэш, а сам пароль
        return decodeString(this.passwordHash);
    }
    @JsonProperty
    @Override
    public void setPasswordHash(String password) {
        //  для админа пароль не хэшируем, а шифруем, чтобы можно было расшифровать
        this.passwordHash = encodeString(password);
    }

    private String encodeString(String data){

        return Base64.getEncoder().encodeToString(data.getBytes());
    }

    private String decodeString(String data){

        return new String(Base64.getDecoder().decode(data));
    }

}
