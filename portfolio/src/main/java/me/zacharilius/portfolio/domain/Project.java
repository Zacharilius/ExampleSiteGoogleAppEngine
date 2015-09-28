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
public class Project {
    // The id is to be used in the Entity's key
    @Id 
    private long id;
    
    
    @Parent
    @ApiResourceProperty(ignored = AnnotationBoolean.TRUE)
    private Key<Profile> projectKey;
    
    //The id of the creator
    private String userId;
    
    // The title of the project
    @Index 
    private String title;
    
    
    // Describes the project
    private String description;
    
    // The location of an image of the project working;
    private String imageURL;
   
    // The date of the post
    @Index
    private Date date;
    
    // Technologies used for the project
    @Index
    private List<String> technologiesUsed = new ArrayList<>(0);    
    /**
     * So default blog constructor is set to private
     */
    private Project(){}
    
    
    /**
     * Public constructor for Profile.
     * @param userId The user id, obtained from the email
     * @param displayName Any string user wants us to display him/her on this system.
     * @param mainEmail User's main e-mail address.
     * @param teeShirtSize The User's tee shirt size
     * 
     */
    public Project(final long id, final String bloggerId, final BlogForm blogForm) {
    	Preconditions.checkNotNull(blogForm.getTitle(), "The blog title is required");
    	Preconditions.checkNotNull(blogForm.getSubTitle(), "The blog subtitle is required");
    	Preconditions.checkNotNull(blogForm.getBody(), "The blog body is required");

}


}

	