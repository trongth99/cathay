package fis.com.vn.common;

import java.util.ArrayList;
import java.util.Properties;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import fis.com.vn.component.ConfigProperties;
import fis.com.vn.entities.FileObject;

@Component
public class Email {

	@Autowired
	ConfigProperties configProperties;

	public static void main(String[] args) {
		Email email = new Email();
		email.sendText("trongth7@fpt.com.vn", "[KBANK DIGITAL] " + "vdc" + " RESGISTER CONTRACT",
				"This email is attached with your completed contract.<br/>  \r\n"
						+ "Thank you for banking with KBank! <br/>\r\n" + "\r\n"
						+ "For further infomation, please direct your inquiries to:<br/>\r\n" + "\r\n"
						+ "- Our website: http/KBank.com <br/> \r\n");
	}

	private final Logger LOGGER = LoggerFactory.getLogger(Email.class);
//	@Value("${email.email}")
	private String email = "duaneid@fpt.com.vn";

//	@Value("${email.pass}")
	private String emailPass = "abcD1234!@#$%^&*11";

	public boolean sendText(String emailTo, String title, String content) {
		try {
			Properties props = new Properties();
			props.put("mail.smtp.auth", "true");
			props.put("mail.smtp.starttls.enable", "true");
			props.put("mail.smtp.host", "smtp.office365.com");
			props.put("mail.smtp.port", "587");
			props.put("mail.smtp.ssl.trust", "smtp.office365.com");

			Session session = Session.getInstance(props, new javax.mail.Authenticator() {
				@Override
				protected PasswordAuthentication getPasswordAuthentication() {
					return new PasswordAuthentication(email, emailPass);
				}
			});
			session.setDebug(true);

			try {

				Message message = new MimeMessage(session);
				message.setFrom(new InternetAddress(email));
				message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(emailTo));
				message.setSubject(title);
				message.setContent(content, "text/html; charset=utf-8");
				Transport.send(message);

				return true;

			} catch (MessagingException e) {
				LOGGER.error("MessagingException: ", e);
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
		return false;
	}

	public boolean sendFile(String emailTo, String title, String content, String fileName, String pathFile) {
		try {
			LOGGER.info("Email send: " + emailTo);
			Properties props = new Properties();
			props.put("mail.smtp.auth", "true");
			props.put("mail.smtp.starttls.enable", "true");
			props.put("mail.smtp.host", "smtp.office365.com");
			props.put("mail.smtp.port", "587");
			props.put("mail.smtp.ssl.trust", "smtp.office365.com");

			Session session = Session.getInstance(props, new javax.mail.Authenticator() {
				@Override
				protected PasswordAuthentication getPasswordAuthentication() {
					return new PasswordAuthentication(email, emailPass);
				}
			});
			session.setDebug(true);

			try {
				Message message = new MimeMessage(session);
				message.setFrom(new InternetAddress(email));
				message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(emailTo));
				message.setSubject(title);
				message.setText(content);

				MimeBodyPart textBodyPart = new MimeBodyPart();
				textBodyPart.setText(content);

				Multipart multipart = new MimeMultipart();
				multipart.addBodyPart(textBodyPart);

				//
				MimeBodyPart messageBodyPart = new MimeBodyPart();
				DataSource source = new FileDataSource(pathFile);
				messageBodyPart.setDataHandler(new DataHandler(source));
				messageBodyPart.setFileName(fileName);
				multipart.addBodyPart(messageBodyPart);
				//

				message.setContent(multipart);

				Transport.send(message);
				LOGGER.info("Send email file success: " + emailTo);
				return true;

			} catch (Exception e) {
				e.printStackTrace();
				LOGGER.error("MessagingException: ", e.getMessage());
			}
		} catch (Exception e) {
			LOGGER.error("MessagingException: ", e.getMessage());
			e.printStackTrace();
		}
		return false;
	}

	public boolean sendMultipleFile(String emailTo, String title, String content, ArrayList<FileObject> fileObjects) {
		try {
			LOGGER.info("Email send: " + emailTo);
			Properties props = new Properties();
			props.put("mail.smtp.auth", "true");
			props.put("mail.smtp.starttls.enable", "true");
			props.put("mail.smtp.host", "smtp.office365.com");
			props.put("mail.smtp.port", "587");
			props.put("mail.smtp.ssl.trust", "smtp.office365.com");

			Session session = Session.getInstance(props, new javax.mail.Authenticator() {
				@Override
				protected PasswordAuthentication getPasswordAuthentication() {
					return new PasswordAuthentication(email, emailPass);
				}
			});
			session.setDebug(true);

			try {
				Message message = new MimeMessage(session);
				message.setFrom(new InternetAddress(email));
				message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(emailTo));
				message.setSubject(title);
				message.setText(content);

				MimeBodyPart textBodyPart = new MimeBodyPart();
				textBodyPart.setText(content);

				Multipart multipart = new MimeMultipart();
				multipart.addBodyPart(textBodyPart);

				//
				for (FileObject fileObject : fileObjects) {
					MimeBodyPart messageBodyPart = new MimeBodyPart();
					DataSource source = new FileDataSource(fileObject.getDuongDan());
					messageBodyPart.setDataHandler(new DataHandler(source));
					messageBodyPart.setFileName(fileObject.getTen());
					multipart.addBodyPart(messageBodyPart);
				}
				//

				message.setContent(multipart, "text/html; charset=utf-8");

				Transport.send(message);
				LOGGER.info("Send email file success: " + emailTo);
				return true;

			} catch (Exception e) {
				e.printStackTrace();
				LOGGER.error("MessagingException: ", e.getMessage());
			}
		} catch (Exception e) {
			LOGGER.error("MessagingException: ", e.getMessage());
			e.printStackTrace();
		}
		return false;
	}
}
