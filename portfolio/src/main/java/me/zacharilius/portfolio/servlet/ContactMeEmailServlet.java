package me.zacharilius.portfolio.servlet;

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

import com.google.appengine.api.utils.SystemProperty;

/**
 * Servlet implementation class ContactMeEmailServlet
 */
public class ContactMeEmailServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public ContactMeEmailServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

    private static final Logger Log = Logger.getLogger(ContactMeEmailServlet.class.getName());

    @Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String email = "zabensley@gmail.com";
		String emailName = request.getParameter("emailName");
		String emailSubject = request.getParameter("emailSubject");
		String emailBody = request.getParameter("emailBody");
		
		Properties props = new Properties();
		
		Session session = Session.getDefaultInstance(props, null);
		String body = "You are being send an email from: \n\n" + emailName + "\n\n";
		try{
			Message message = new MimeMessage(session);
			InternetAddress from = new InternetAddress(
					String.format("noreply@%s.appspotmail.com", 
							SystemProperty.applicationId.get()), "Zacharilius");
			message.setFrom(from);
			message.addRecipient(Message.RecipientType.TO, new InternetAddress(email, ""));
			message.setSubject(emailSubject);
			message.setText(body);
			Transport.send(message);
		} catch(MessagingException e){
			System.out.println("ERROR!!!!");
			Log.log(Level.WARNING, String.format("Failed to send an email to %s", email), e);
			throw new RuntimeException(e);
		}
		response.getWriter().append("Served at: ").append(request.getContextPath());
	}

}
