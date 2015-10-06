package me.zacharilius.servlet;

import com.google.appengine.api.utils.SystemProperty;

import java.io.IOException;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class SendConfirmationEmailServlet
 */
public class SendEmailServlet extends HttpServlet {
       
    private static final Logger Log = Logger.getLogger(SendEmailServlet.class.getName());

    @Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String emailAddress = request.getParameter("emailAddress");
		String subject = request.getParameter("subject");
		String messageText = request.getParameter("message");
		String name = request.getParameter("name");
		
		String myEmail = "zabensley@gmail.com";
		
		Properties props = new Properties();
		
		Session session = Session.getDefaultInstance(props, null);
		String body = messageText + "\n\n FROM: " + name;
		try{
			Message message = new MimeMessage(session);
			InternetAddress from = new InternetAddress(
					String.format("noreply@%s.appspotmail.com", 
							SystemProperty.applicationId.get()), "Zacharilius Portfolio");
			message.setFrom(from);
			message.addRecipient(Message.RecipientType.TO, new InternetAddress(myEmail, ""));
			message.setSubject(subject);
			message.setText(body);
			Transport.send(message);
		} catch(MessagingException e){
			Log.log(Level.WARNING, String.format("Failed to send an email from %s", emailAddress), e);
			throw new RuntimeException(e);
		}
		response.getWriter().append("Served at: ").append(request.getContextPath());
	}

}
