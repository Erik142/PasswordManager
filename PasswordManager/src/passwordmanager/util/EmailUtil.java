package passwordmanager.util;

import java.util.Date;

import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class EmailUtil {
	public static void sendEmail(Session session, String senderEmail, String recipientEmail, String subject, String body) {
		try
	    {
	      MimeMessage msg = new MimeMessage(session);
	      //set message headers
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
	    }
	    catch (Exception e) {
	      e.printStackTrace();
	    }
	}
	
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
