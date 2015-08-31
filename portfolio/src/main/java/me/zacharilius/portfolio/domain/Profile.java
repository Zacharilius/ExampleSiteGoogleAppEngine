package me.zacharilius.portfolio.domain;

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
    String displayName;
    String mainEmail;
    
    private List<String> conferenceKeysToAttend = new ArrayList<>(0);

    private List<String> sessionKeysWishlist = new ArrayList<>(0);
    // TODO indicate that the userId is to be used in the Entity's key
    @Id String userId;
    
    /**
     * Public constructor for Profile.
     * @param userId The user id, obtained from the email
     * @param displayName Any string user wants us to display him/her on this system.
     * @param mainEmail User's main e-mail address.
     * @param teeShirtSize The User's tee shirt size
     * 
     */
    public Profile (String userId, String displayName, String mainEmail) {
        this.userId = userId;
        this.displayName = displayName;
        this.mainEmail = mainEmail;
    }
    public List<String> getConferenceKeysToAttend(){
    	return ImmutableList.copyOf(conferenceKeysToAttend);
    }

    public void addToConferenceKeysToAttend(String conferenceKey){
    	conferenceKeysToAttend.add(conferenceKey);
    }
    public List<String> getSessionWishlist(){
    	return ImmutableList.copyOf(sessionKeysWishlist);
    }
    public void addSessionWishLislist(String sessionKey){
    	sessionKeysWishlist.add(sessionKey);
    }
    public void unregisterFromConference(String conferenceKey){
    	if(conferenceKeysToAttend.contains(conferenceKey)){
    		conferenceKeysToAttend.remove(conferenceKey);
    	}
    	else{
    		throw new IllegalArgumentException("Invalid conferenceKey: " + conferenceKey);
    	}
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

    /**
     * Just making the default constructor private.
     */
    private Profile() {}
    
    /**
     * Update the Profile with the given displayName and teeShirtSize
     *
     * @param displayName
     * @param teeShirtSize
     */
    public void update(String displayName) {
        if (displayName != null) {
            this.displayName = displayName;
        }
    }

}