package moska.rebora.Common;

import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Component;

import javax.mail.*;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;

@Slf4j
@Component
public class Util {

    static Properties mailServerProperties;
    static Session getMailSession;
    static MimeMessage generateMailMessage;

    @Value("${mail.username}")
    private String username;

    @Value("${mail.password}")
    private String password;

    public void sendEmail(@Param("userEmail") String userEmail, @Param("subject") String subject, @Param("body") String body) {

        try {

            mailServerProperties = System.getProperties();
            mailServerProperties.put("mail.smtp.host", "smtp.gmail.com");
            mailServerProperties.put("mail.smtp.port", 587);
            mailServerProperties.put("mail.smtp.auth", "true");
            mailServerProperties.put("mail.smtp.starttls.enable", "true");

            getMailSession = Session.getDefaultInstance(mailServerProperties, null);

            generateMailMessage = new MimeMessage(getMailSession);
            generateMailMessage.addRecipient(Message.RecipientType.TO, new InternetAddress(userEmail));
            generateMailMessage.addRecipient(Message.RecipientType.CC, new InternetAddress(username));
            generateMailMessage.setSubject(subject);
            generateMailMessage.setContent(body, "text/html; charset=UTF-8");


            Transport transport = getMailSession.getTransport("smtp");
            transport.connect("kkp02052@gmail.com", password);
            transport.sendMessage(generateMailMessage, generateMailMessage.getAllRecipients());
            transport.close();
        } catch (AddressException e) {
            log.error("Mail Auth Error");
            e.printStackTrace();
        } catch (MessagingException e) {
            log.error("Mail Message Error");
            e.printStackTrace();
        }
    }

    public String createRandomString(int size) {

        String result = "";
        if (size > 0) {
            char[] tmp = new char[size];
            for (int i = 0; i < tmp.length; i++) {
                int div = (int) Math.floor(Math.random() * 2);
                if (div == 0) {
                    tmp[i] = (char) (Math.random() * 10 + '0');
                } else {
                    tmp[i] = (char) (Math.random() * 26 + 'A');
                }
            }
            result = new String(tmp);
        }

        return result;
    }
}
