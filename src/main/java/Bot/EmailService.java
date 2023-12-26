package Bot;



import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;

public class EmailService {
    private static String username = "";
    private static String password = "";
    private static final Properties properties = getMailCredentials();
    private final Session session = Session.getInstance(properties, new Authenticator() {

        @Override
        protected PasswordAuthentication getPasswordAuthentication() {
            return new PasswordAuthentication(username,password);
        }
    });

    public void sendMessage(String email, String title, String description) {
        try {

            MimeMessage message = new MimeMessage(session);
            message.setFrom(new InternetAddress("norepdipl@gmail.com"));
            message.addRecipient(Message.RecipientType.TO,new InternetAddress(email));
            message.setSubject(title);
            message.setText(description);

            Transport.send(message);
            System.out.println("sending");
        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
    }

    private static Properties getMailCredentials(){
        Properties properties = new Properties();
        properties.put("mail.smtp.auth",true);
        properties.put("mail.smtp.starttls.enable", true);
        properties.put("mail.smtp.host", "smtp-relay.sendinblue.com");
        properties.put("mail.smtp.port", "587");

        properties.put("mail.debug", "true");

        return properties;
    }

}
