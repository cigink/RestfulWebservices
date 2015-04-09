/**
 * 
 */
package HireEngine.integration.email;

import java.util.Date;

import org.springframework.mail.MailMessage;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Service;

/**
 * @author Vinod Rockson
 *
 */
@Service
public class NotificationManager {

    public MailMessage sendPaymentConfirmation(String invoice) {
        MailMessage mailMessage = new SimpleMailMessage();

        mailMessage.setTo("hireengineesi@gmail.com");
        mailMessage.setSentDate(new Date());
        mailMessage.setSubject("The payment is being processed");
        mailMessage.setText("Message to send");
        return mailMessage;
    }
}
