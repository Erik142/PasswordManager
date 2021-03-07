package passwordmanager.util;

import java.util.Date;

import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

/**
 * A utility class used to verify the validity of e-mail addresses as well as to
 * send e-mails
 * 
 * @author Erik Wahlberger
 * @version 2021-03-07
 */
public class EmailUtil {
	/**
	 * Send an e-mail from the specified e-mail address to the specified e-mail
	 * address with the specified subject and body strings
	 * 
	 * @param session        The session used to send the e-mail
	 * @param senderEmail    The sender's e-mail address
	 * @param recipientEmail The recipient's e-mail address
	 * @param subject        The subject String of the e-mail
	 * @param body           The body of the e-mail
	 */
	public static void sendEmail(Session session, String senderEmail, String recipientEmail, String subject,
			String body) {
		try {
			MimeMessage msg = new MimeMessage(session);
			// set message headers
			msg.addHeader("Content-type", "text/HTML; charset=UTF-8");
			msg.addHeader("format", "flowed");
			msg.addHeader("Content-Transfer-Encoding", "8bit");
			msg.setFrom(new InternetAddress(senderEmail, "No-reply PasswordManager"));
			msg.setReplyTo(InternetAddress.parse(senderEmail, false));
			msg.setSubject(subject, "UTF-8");
			msg.setText(body, "UTF-8");
			msg.setSentDate(new Date());
			msg.setRecipients(javax.mail.Message.RecipientType.TO, InternetAddress.parse(recipientEmail, false));

			Transport.send(msg);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Checks if the specified String is an e-mail address
	 * 
	 * @param email The String to be verified
	 * @return true if the email parameter is an e-mail address, false otherwise
	 */
	public static boolean isValidEmail(String email) {
		boolean containsDomain = email.contains("@") && (email.split("@").length >= 2);

		if (!containsDomain) {
			return false;
		}

		String domain = email.split("@")[1];

		boolean containsDot = domain.contains(".");

		if (!containsDot) {
			return false;
		}

		boolean lastCharacterIsDot = domain.lastIndexOf(".") == (domain.length() - 1);
		boolean firstCharacterIsDot = domain.indexOf(".") == 0;
		boolean domainContainsAt = domain.contains("@");

		return !lastCharacterIsDot && !firstCharacterIsDot && !domainContainsAt;
	}
}
