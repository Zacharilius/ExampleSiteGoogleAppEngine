package me.zacharilius.portfolio.form;

import com.google.api.server.spi.config.AnnotationBoolean;
import com.google.api.server.spi.config.ApiResourceProperty;
import com.googlecode.objectify.Key;
import com.googlecode.objectify.annotation.Index;
import com.googlecode.objectify.annotation.Parent;

import me.zacharilius.portfolio.domain.Profile;

/**
 * Pojo representing a profile form on the client side.
 */
public class JobForm {
    
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

    private JobForm () {}

    /**
     * Constructor for BlogForm, solely for unit test.
     * @param  
     * @param S 
     */
    public JobForm(String title, String subTitle, String body, String imageURL) {
    	this.title = title;
    	this.subTitle = title;
    	this.body = body;
    	this.imageURL = imageURL;
    }

	public String getTitle() {
		return title;
	}

	public String getSubTitle() {
		return subTitle;
	}

	public String getBody() {
		return body;
	}

	public String getImageURL() {
		return imageURL;
	}

	public String getDescription() {
		// TODO Auto-generated method stub
		return null;
	}

	public String getAddress() {
		// TODO Auto-generated method stub
		return null;
	}
}
