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

    public SendMail(String username, String password) {
        this.username = username;
        this.password = password;

        props = new Properties();
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.socketFactory.port", "465");
        props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.port", "465");
    }

    public void send(String subject, String text, String toEmail) throws MessagingException {
        Session session = Session.getDefaultInstance(props, new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(username, password);
            }
        });

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
            Transport.send(message);
            System.out.println("сообщение отправлено на адрес "+ toEmail);

    }

    public void sendFile(String subject, String fileName, String toEmail) throws MessagingException {
        Session session = Session.getDefaultInstance(props, new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(username, password);
            }
        });

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
            messageBodyPart.setFileName(fileName);
            multipart.addBodyPart(messageBodyPart);

            //  складываем все части в контент письма
            message.setContent(multipart );
            //отправляем сообщение
            Transport.send(message);
            System.out.println("сообщение с вложенным файлом " + fileName +" отправлено на адрес "+ toEmail);

    }
}