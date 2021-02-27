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
}
