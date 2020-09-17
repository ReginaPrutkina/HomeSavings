import java.io.*;

public class FileMailInfo {

    public void writeToFile(MailInfo mailInfo) {
        try (ObjectOutputStream objectOutputStream = new ObjectOutputStream(
                new FileOutputStream(".\\userinfo.ser"))) {
            objectOutputStream.writeObject(mailInfo);
            System.out.println(mailInfo);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public MailInfo readFromFile(){
        MailInfo mailInfo = null;
        try (ObjectInputStream objectInputStream = new ObjectInputStream(
                new FileInputStream(".\\userinfo.ser"))){
             mailInfo =  (MailInfo) objectInputStream.readObject();

        }catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        // здесь нужно проверить на null  и выбросить MyException?
        return mailInfo;
    }
}
