import java.io.*;
import java.util.Base64;

public class MailInfo implements Externalizable {
    public String userPassword;
    public String mailAddress;
    private static final long serialVersionUID = 1L;

    public MailInfo(){}

    public MailInfo (String mailAddress, String userPassword){
        this.mailAddress = mailAddress;
        this.userPassword = userPassword;
    }

    @Override
    public void writeExternal(ObjectOutput out) throws IOException {
        out.writeObject(mailAddress);
        out.writeObject(encodeString(userPassword));
    }
    public String encodeString(String data){
        return Base64.getEncoder().encodeToString(data.getBytes());
    }

    @Override
    public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
        mailAddress = (String) in.readObject();
        userPassword = decodeString((String) in.readObject());
    }
    private String decodeString(String data){
        return new String(Base64.getDecoder().decode(data));
    }



}
