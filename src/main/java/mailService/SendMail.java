package mailService;

import myException.MyException;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import java.util.Properties;

public class SendMail {
    private String username;
    private String password;
    private Properties props;
    private String propHost;
    private String propSocketFactory;
    private String propSocketFactoryClass;
    private String propAuth;
    private String propPort;


    public SendMail(String propHost, String propSocketFactory,String propSocketFactoryClass, String propAuth, String propPort){
        props = new Properties();
        props.put("mail.smtp.host", propHost);
        props.put("mail.smtp.socketFactory.port", propSocketFactory);
        props.put("mail.smtp.socketFactory.class", propSocketFactoryClass);
        props.put("mail.smtp.auth", propAuth);
        props.put("mail.smtp.port", propPort);
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Properties getProps() {
        return props;
    }

    public void setProps(Properties props) {
        this.props = props;
    }

    public String getPropHost() {
        return propHost;
    }

    public void setPropHost(String propHost) {
        this.propHost = propHost;
    }

    public String getPropSocketFactory() {
        return propSocketFactory;
    }

    public void setPropSocketFactory(String propSocketFactory) {
        this.propSocketFactory = propSocketFactory;
    }

    public String getPropSocketFactoryClass() {
        return propSocketFactoryClass;
    }

    public void setPropSocketFactoryClass(String propSocketFactoryClass) {
        this.propSocketFactoryClass = propSocketFactoryClass;
    }

    public String getPropAuth() {
        return propAuth;
    }

    public void setPropAuth(String propAuth) {
        this.propAuth = propAuth;
    }

    public String getPropPort() {
        return propPort;
    }

    public void setPropPort(String propPort) {
        this.propPort = propPort;
    }

    public void send(String subject, String text, String toEmail) throws MyException {
        Session session = Session.getDefaultInstance(props, new Authenticator() {
            protected javax.mail.PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(username, password);
            }
        });

        try {
            Message message = new MimeMessage(session);
            //от кого
           // message.setFrom(new InternetAddress(username));
            //кому
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(toEmail));
            //тема сообщения
            message.setSubject(subject);
            //текст
            message.setText(text);
           // message.setContent("<h1>"+text+"</h1>", "text/html");

            //отправляем сообщение
            System.out.println("Подождите, отправляем сообщение...");
            Transport.send(message);
            System.out.println("сообщение отправлено на адрес "+ toEmail);
        } catch (MessagingException e) {
            throw new MyException( "Ошибка отправки сообщения", e);
        }
    }

    public void sendFile(String subject, String fileName, String toEmail) throws MyException {
        Session session = Session.getDefaultInstance(props, new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(username, password);
            }
        });

        try {
            Message message = new MimeMessage(session);
            //от кого
            // message.setFrom(new InternetAddress(username));
            //кому
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(toEmail));
            //тема сообщения
            message.setSubject(subject);

            // Создаем сложное тело письма
            BodyPart messageBodyPart = new MimeBodyPart();

            // Это текст письма
            messageBodyPart.setText("Информация по депозитам во вложении");

            // Добавляем кусок с тектом
            Multipart multipart = new MimeMultipart();
            multipart.addBodyPart(messageBodyPart);

            // формируем и добавляем кусок с вложением файла
            messageBodyPart = new MimeBodyPart();
            DataSource source = new FileDataSource(fileName);
            messageBodyPart.setDataHandler(new DataHandler(source));
            messageBodyPart.setFileName(fileName.substring(fileName.lastIndexOf("\\")+1));
            multipart.addBodyPart(messageBodyPart);

            //  складываем все части в контент письма
            message.setContent(multipart );
            System.out.println("Подождите, отправляем сообщение...");
            //отправляем сообщение
            Transport.send(message);
            System.out.println("сообщение с вложенным файлом " + fileName +" отправлено на адрес "+ toEmail);
        } catch (MessagingException e) {
            throw new MyException( "Ошибка отправки сообщения", e);
        }
    }
}
