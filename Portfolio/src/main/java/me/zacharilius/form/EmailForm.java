package me.zacharilius.form;

import java.util.Date;
import java.util.List;


public class EmailForm{
	
    private String name;

    private String emailAddress;

    private String message;

    private String subject;
    
    
    private EmailForm(){}


	/**
	 * @param name
	 * @param emailAddress
	 * @param message
	 * @param subject
	 */
	public EmailForm(String name, String emailAddress, String message, String subject) {
		super();
		this.name = name;
		this.emailAddress = emailAddress;
		this.message = message;
		this.subject = subject;
	}


	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}


	/**
	 * @return the emailAddress
	 */
	public String getEmailAddress() {
		return emailAddress;
	}


	/**
	 * @return the message
	 */
	public String getMessage() {
		return message;
	}


	/**
	 * @return the subject
	 */
	public String getSubject() {
		return subject;
	}
}
