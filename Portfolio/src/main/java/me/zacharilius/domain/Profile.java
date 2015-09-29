package me.zacharilius.domain;

import java.util.ArrayList;
import java.util.List;

import com.google.appengine.repackaged.com.google.common.collect.ImmutableList;
import com.googlecode.objectify.annotation.Cache;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;


// TODO indicate that this class is an Entity
@Entity
@Cache
public class Profile {
	
    private String displayName;
    private String mainEmail;
    private String city;
    private String state;
    private String phoneNumber;
    private String pictureUrl;
    
    // TODO indicate that the userId is to be used in the Entity's key
    @Id String userId;
    
    /**
     * Public constructor for Profile.
     * @param userId The user id, obtained from the email
     * @param displayName Any string user wants us to display him/her on this system.
     * @param mainEmail User's main e-mail address.
     * 
     */
    public Profile (String userId, String displayName, String mainEmail, String city, String state, String phoneNumber, String pictureUrl) {
        this.userId = userId;
        this.displayName = displayName;
        this.mainEmail = mainEmail;
        this.city = city;
        this.state = state;
        this.phoneNumber = phoneNumber;
        this.pictureUrl = pictureUrl;
    }
    
    public String getPictureUrl(){
    	return pictureUrl;
    }
    
    public String getDisplayName() {
        return displayName;
    }

    public String getMainEmail() {
        return mainEmail;
    }


    public String getUserId() {
        return userId;
    }
    
    public String getPhoneNumber(){
    	return phoneNumber;
    }
    
    public String getCity(){
    	return city;
    }
    
    public String getState(){
    	return state;
    }

    /**
     * Just making the default constructor private.
     */
    private Profile() {}
    
    /**
     * Update the Profile with the given displayName
     *
     * @param displayName
     * @param teeShirtSize
     */
    public void update(String displayName, String city, String state, String phoneNumber, String pictureUrl) {
        if (displayName != null) {
            this.displayName = displayName;
        }
        if (city != null) {
            this.city = city;
        }
        if (state != null) {
            this.state = state;
        }    
        if (phoneNumber != null) {
            this.phoneNumber = phoneNumber;
        }    
        if (pictureUrl != null){
        	this.pictureUrl = pictureUrl;
        }
    }

}
