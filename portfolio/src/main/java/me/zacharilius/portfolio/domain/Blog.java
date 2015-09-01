package me.zacharilius.portfolio.domain;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.google.api.server.spi.config.AnnotationBoolean;
import com.google.api.server.spi.config.ApiResourceProperty;
import com.google.appengine.repackaged.com.google.common.collect.ImmutableList;
import com.google.common.base.Preconditions;
import com.googlecode.objectify.Key;
import com.googlecode.objectify.annotation.Cache;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Index;
import com.googlecode.objectify.annotation.Parent;

import me.zacharilius.portfolio.form.BlogForm;


// TODO indicate that this class is an Entity
@Entity
@Cache
public class Blog {
    // The id is to be used in the Entity's key
    @Id 
    private long id;
    
    
    @Parent
    @ApiResourceProperty(ignored = AnnotationBoolean.TRUE)
    private Key<Profile> bloggerKey;
    
    //The id of the blogger
    private String bloggerId;
    
    // The title of the blog post
    @Index 
    private String title;
    
    // The subtitle of the blog post
    @Index
    private String subTitle;
    
    // The body of the blog post
    private String body;
    
    // The location of an image for the blog post
    private String imageURL;
   
    // The date of the post
    @Index
    private Date date;
    
    /**
     * So default blog constructor is set to private
     */
    private Blog(){}
    
    
    /**
     * Public constructor for Profile.
     * @param userId The user id, obtained from the email
     * @param displayName Any string user wants us to display him/her on this system.
     * @param mainEmail User's main e-mail address.
     * @param teeShirtSize The User's tee shirt size
     * 
     */
    public Blog(final long id, final String bloggerId, final BlogForm blogForm) {
    	Preconditions.checkNotNull(blogForm.getTitle(), "The blog title is required");
    	Preconditions.checkNotNull(blogForm.getSubTitle(), "The blog subtitle is required");
    	Preconditions.checkNotNull(blogForm.getBody(), "The blog body is required");
    	
    	// The blog id
    	this.id = id;
    	
    	// The blogger user id
    	this.bloggerId = bloggerId;
    	
    	// The blogger's key based on the user id
    	this.bloggerKey = Key.create(Profile.class, bloggerId);
    	
    	//The date of the posting
    	Date date = new Date();
    	this.date = date;
    	
    	//Updating the blogForm values;
    	updateWithConferenceForm(blogForm);
}


	/**
	 * @return the id
	 */
	public long getId() {
		return id;
	}


	/**
	 * @return the bloggerKey
	 */
	public Key<Profile> getBloggerKey() {
		return bloggerKey;
	}


	/**
	 * @return the bloggerId
	 */
	public String getBloggerId() {
		return bloggerId;
	}


	/**
	 * @return the title
	 */
	public String getTitle() {
		return title;
	}


	/**
	 * @return the subTitle
	 */
	public String getSubTitle() {
		return subTitle;
	}


	/**
	 * @return the body
	 */
	public String getBody() {
		return body;
	}


	/**
	 * @return the imageURL
	 */
	public String getImageURL() {
		return imageURL;
	}


	private void updateWithConferenceForm(BlogForm blogForm) {
		this.title = blogForm.getTitle();
		this.subTitle = blogForm.getSubTitle();
		this.body = blogForm.getBody();
		this.imageURL = blogForm.getImageURL();	
	}


	public Date getDate() {
		return date;
	}
 }