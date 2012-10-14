package util;

import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.simpleemail.AWSJavaMailTransport;
import com.amazonaws.services.simpleemail.AmazonSimpleEmailService;
import com.amazonaws.services.simpleemail.AmazonSimpleEmailServiceClient;

/**
 * Mail send utils for AmazonSES
 * 
 * @author akiok
 * 
 */
public class AmazonSESUtils {

	private static final ConfigHelper configHelper = ConfigHelper.getInstance();

	public static AmazonSimpleEmailService createSESClient() {
		String swfAccessId = configHelper
				.getValueFromConfig(Constants.AWS_ACCESS_ID_KEY);
		String swfSecretKey = configHelper
				.getValueFromConfig(Constants.AWS_SECRET_KEY_KEY);

		BasicAWSCredentials credentials = new BasicAWSCredentials(swfAccessId,
				swfSecretKey);
		AmazonSimpleEmailService ses = new AmazonSimpleEmailServiceClient(
				credentials);
		return ses;
	}

	public void send(String fromAddress, String toAddress, String subject,
			String body) {
		try {
			String sesfAccessId = configHelper.getAwsAccessId();
			String sesSecretKey = configHelper.getAwsSecretKey();
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
