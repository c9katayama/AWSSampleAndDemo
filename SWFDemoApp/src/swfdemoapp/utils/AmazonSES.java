package swfdemoapp.utils;

import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import com.amazonaws.services.simpleemail.AWSJavaMailTransport;

/**
 * AmazonSESを使ったメール送信
 * 
 * @author c9katayama
 * 
 */
public class AmazonSES {

	public void send(String fromAddress, String toAddress, String subject,
			String body) {
		try {
			String sesfAccessId = Helper
					.getValueFromConfig(Constants.SWF_ACCESS_ID_KEY);
			String sesSecretKey = Helper
					.getValueFromConfig(Constants.SWF_SECRET_KEY_KEY);
			Properties props = new Properties();
			props.setProperty("mail.transport.protocol", "aws");
			props.setProperty("mail.aws.user", sesfAccessId);
			props.setProperty("mail.aws.password", sesSecretKey);
			Session session = Session.getInstance(props);

			// Create a new Message
			Message msg = new MimeMessage(session);
			msg.setFrom(new InternetAddress(fromAddress));
			msg.addRecipient(Message.RecipientType.TO, new InternetAddress(
					toAddress));
			msg.setSubject(subject);
			msg.setText(body);
			msg.saveChanges();
			Transport t = new AWSJavaMailTransport(session, null);
			t.connect();
			t.sendMessage(msg, null);
			t.close();
		} catch (AddressException e) {
			e.printStackTrace();
			System.out
					.println("Caught an AddressException, which means one or more of your "
							+ "addresses are improperly formatted.");
			throw new RuntimeException(e);
		} catch (MessagingException e) {
			e.printStackTrace();
			System.out
					.println("Caught a MessagingException, which means that there was a "
							+ "problem sending your message to Amazon's E-mail Service check the "
							+ "stack trace for more information.");
			throw new RuntimeException(e);
		}
	}
}
