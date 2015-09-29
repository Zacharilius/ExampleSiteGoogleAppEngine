package me.zacharilius.form;

/**
 * Pojo representing a profile form on the client side.
 */
public class ProfileForm {
    /**
     * Any string user wants us to display him/her on this system.
     */
    private String displayName;


    /**
     * profile user's city
     */
    private String city;
    
    /**
     * Profile user's state
     */
    private String state;
    
    private String phoneNumber;
    
    private String pictureUrl;
    
    private ProfileForm () {}

    /**
     * Constructor for ProfileForm, solely for unit test.
     * @param displayName A String for displaying the user on this system.
     * @param notificationEmail An e-mail address for getting notifications from this system.
     */
    public ProfileForm(String displayName, String city, String state, String phoneNumber, String pictureUrl) {
        this.displayName = displayName;
        this.city = city;
        this.state = state;
        this.phoneNumber = phoneNumber;
        this.pictureUrl = pictureUrl;
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getCity(){
    	return city;
    }
    
    public String getState(){
    	return state;
    }
    
	public String getPhoneNumber() {
		return phoneNumber;
	}

	public String getPictureUrl() {
		return pictureUrl;
	}
}
