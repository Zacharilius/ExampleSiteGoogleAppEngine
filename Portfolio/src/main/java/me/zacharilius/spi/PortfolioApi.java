package me.zacharilius.spi;

import static me.zacharilius.service.OfyService.factory;
import static me.zacharilius.service.OfyService.ofy;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

import com.google.api.server.spi.config.Api;
import com.google.api.server.spi.config.ApiMethod;
import com.google.api.server.spi.config.ApiMethod.HttpMethod;
import com.google.api.server.spi.config.Named;
import com.google.api.server.spi.response.ConflictException;
import com.google.api.server.spi.response.ForbiddenException;
import com.google.api.server.spi.response.NotFoundException;
import com.google.api.server.spi.response.UnauthorizedException;
import com.google.appengine.api.memcache.MemcacheService;
import com.google.appengine.api.memcache.MemcacheServiceFactory;
import com.google.appengine.api.taskqueue.Queue;
import com.google.appengine.api.taskqueue.QueueFactory;
import com.google.appengine.api.taskqueue.TaskOptions;
import com.google.appengine.api.users.User;
import com.googlecode.objectify.Key;
import com.googlecode.objectify.Work;
import com.googlecode.objectify.cmd.Query;

import me.zacharilius.Constants;
import me.zacharilius.domain.Announcement;
import me.zacharilius.domain.Job;
import me.zacharilius.domain.Profile;
import me.zacharilius.form.EmailForm;
import me.zacharilius.form.JobForm;
import me.zacharilius.form.ProfileForm;
/**
 * Defines odd-job APIs.
 */
@Api(name = "portfolio", version = "v1", scopes = { Constants.EMAIL_SCOPE }, clientIds = {
        Constants.WEB_CLIENT_ID, Constants.API_EXPLORER_CLIENT_ID }, description = "API for the my portfolio  Backend application.")
public class PortfolioApi {

    /*
     * Get the display name from the user's email. For example, if the email is
     * lemoncake@example.com, then the display name becomes "lemoncake."
     */
    private static String extractDefaultDisplayNameFromEmail(String email) {
        return email == null ? null : email.substring(0, email.indexOf("@"));
    }

    /**
     * Creates or updates a Profile object associated with the given user
     * object.
     *
     * @param user
     *            A User object injected by the cloud endpoints.
     * @param profileForm
     *            A ProfileForm object sent from the client form.
     * @return Profile object just created.
     * @throws UnauthorizedException
     *             when the User object is null.
     */

    // Declare this method as a method available externally through Endpoints
    @ApiMethod(name = "saveProfile", path = "profile", httpMethod = HttpMethod.POST)
    // The request that invokes this method should provide data that
    // conforms to the fields defined in ProfileForm
    public Profile saveProfile(final User user, ProfileForm profileForm)
            throws UnauthorizedException {

        // If the user is not logged in, throw an UnauthorizedException
        if (user == null) {
            throw new UnauthorizedException("Authorization required");
        }

        // Get the userId and mainEmail
        String mainEmail = user.getEmail();
        String userId = user.getUserId();

        // Get the displayName, city, state, & phone number sent by the request.
        String displayName = profileForm.getDisplayName();
        String phoneNumber = profileForm.getPhoneNumber();
        String city = profileForm.getCity();
        String state = profileForm.getState();
        String pictureUrl = profileForm.getPictureUrl();
        
        // Get the Profile from the datastore if it exists
        // otherwise create a new one
        Profile profile = ofy().load().key(Key.create(Profile.class, userId))
                .now();

        if (profile == null) {
            // Populate the displayName with default values
            // if not sent in the request
            if (displayName == null) {
                displayName = extractDefaultDisplayNameFromEmail(user
                        .getEmail());
            }

            // Now create a new Profile entity
            profile = new Profile(userId, displayName, mainEmail, city, state, phoneNumber, pictureUrl);
        } else {
            // The Profile entity already exists
            // Update the Profile entity
            profile.update(displayName, city, state, phoneNumber, pictureUrl);
        }

        // Save the entity in the datastore
        ofy().save().entity(profile).now();
        // Return the profile
        return profile;
    }

    /**
     * Returns a Profile object associated with the given user object. The cloud
     * endpoints system automatically inject the User object.
     *
     * @param user
     *            A User object injected by the cloud endpoints.
     * @return Profile object.
     * @throws UnauthorizedException
     *             when the User object is null.
     */
    @ApiMethod(name = "getProfile", path = "profile", httpMethod = HttpMethod.GET)
    public Profile getProfile(final User user) throws UnauthorizedException {
        if (user == null) {
            throw new UnauthorizedException("Authorization required");
        }

        // load the Profile Entity
        String userId = user.getUserId();
        Key key = Key.create(Profile.class, userId);

        Profile profile = (Profile) ofy().load().key(key).now();
        return profile;
    }
    
    /**
     * Returns a Profile object associated with the given user object. The cloud
     * endpoints system automatically inject the User object.
     *
     * @param user
     *            A User object injected by the cloud endpoints.
     * @return Profile object.
     * @throws UnauthorizedException
     *             when the User object is null.
     */
    @ApiMethod(
    		name = "getProfileDetail", 
    		path = "profile/{websafeProfileKey}", 
    		httpMethod = HttpMethod.GET
    		)
    public Profile getProfileDetail(final User user, 
    		@Named("websafeProfileKey") final String websafeProfileKey) throws UnauthorizedException {
        System.out.println("Pow");
    	if (user == null) {
            throw new UnauthorizedException("Authorization required to view profiles");
        }
        System.out.println("\n\nwebsafeProfileKey" + websafeProfileKey + "\n\n");
        // load the Profile Entity from the websafeProfileKey
        String userId = user.getUserId();
        Key key = Key.create(websafeProfileKey);

        Profile profile = (Profile) ofy().load().key(key).now();
        return profile;
    }   
    
    
    
    
    /**
     * Gets the Profile entity for the current user
     * or creates it if it doesn't exist
     * @param user
     * @return user's Profile
     */
    private static Profile getProfileFromUser(User user) {
        // First fetch the user's Profile from the datastore.
        Profile profile = ofy().load().key(
                Key.create(Profile.class, user.getUserId())).now();
        if (profile == null) {
            // Create a new Profile if it doesn't exist.
            // Use default displayName and teeShirtSize
            String email = user.getEmail();

            profile = new Profile(user.getUserId(),
                    extractDefaultDisplayNameFromEmail(email), email, null, null, null, null);
        }
        return profile;
    }


 /**
     * Just a wrapper for Boolean.
     * We need this wrapped Boolean because endpoints functions must return
     * an object instance, they can't return a Type class such as
     * String or Integer or Boolean
     */
    public static class WrappedResult {

        private final Boolean result;
        private final String reason;

        public WrappedResult(Boolean result) {
            this.result = result;
            this.reason = "";
        }

        public WrappedResult(Boolean result, String reason) {
            this.result = result;
            this.reason = reason;
        }

        public Boolean getResult() {
            return result;
        }

        public String getReason() {
            return reason;
        }
    }

 
    @ApiMethod(
    		name = "getAnnouncement",
    		path = "announcement",
    		httpMethod = HttpMethod.GET
    )
    public Announcement getAnnouncement(){
    	MemcacheService memcacheService = MemcacheServiceFactory.getMemcacheService();
    	Object message = memcacheService.get(Constants.MEMCACHE_ANNOUNCEMENTS_KEY);
    	if(message != null){
    		return new Announcement(message.toString());
    	}
    	return null;
    }
    
    
 
/**
 * Creates a new Job object and stores it to the datastore.
 *
 * @param user A user who invokes this method, null when the user is not signed in.
 * @param jobForm A JobForm object representing user's inputs.
 * @return A newly created Job Object.
 * @throws UnauthorizedException when the user is not signed in.
 * @throws ConflictException 
 */
@ApiMethod(name = "sendEmail", path = "sendEmail", httpMethod = HttpMethod.POST)
public EmailForm sendEmail(final EmailForm emailForm)
    throws UnauthorizedException, ConflictException {

	if(emailForm.getEmailAddress() == null){
        throw new ConflictException("You must enter an email address");
    }
	if(emailForm.getMessage() == null){
        throw new ConflictException("You must enter a message");
	}
	if(emailForm.getName() == null){
        throw new ConflictException("You must enter a name");
	}	
	if(emailForm.getSubject() == null){
        throw new ConflictException("You must enter an email subject");
	}    
	
	final Queue queue = QueueFactory.getDefaultQueue();
    

    // Start transactions
    EmailForm email = ofy().transact(new Work<EmailForm>(){
    	@Override
    	public EmailForm run(){
            
            queue.add(ofy().getTransaction(),
            		TaskOptions.Builder.withUrl("/tasks/send_email")
            		.param("emailAddress",  emailForm.getEmailAddress())
            		.param("message", emailForm.getMessage())
            		.param("subject", emailForm.getSubject())
            		.param("name", emailForm.getName()));
            return emailForm;
    	}
    }); 
    return email;
}
/**
 * Queries the datastore for all jobs the user created
 *
 * @return A list of job objects
 * @throws UnauthorizedException when the user is not signed in.
 */
@ApiMethod(
        name = "getJobsCreated",
        path = "getJobsCreated",
        httpMethod = HttpMethod.POST
)
public List<Job> getJobsCreated(final User user)throws UnauthorizedException {
	if (user == null) {
		throw new UnauthorizedException("Authorization required");
	}
	
	System.out.println("getJobsCreated()");
	String userId = user.getUserId();
    Key key = Key.create(Profile.class, userId);
    
    Query<Job> query = ofy().load().type(Job.class).ancestor(key);
	return query.list();
}
/**
 * Queries the datastore for all jobs. sorts by posting date;
 *
 * @return A list of all job objects
 * @throws UnauthorizedException when the user is not signed in.
 */
@ApiMethod(
        name = "getAllJobsCreated",
        path = "getAllJobsCreated",
        httpMethod = HttpMethod.POST
)
public List<Job> getAllJobsCreated()throws UnauthorizedException {
    Query<Job> query = ofy().load().type(Job.class).order("postDate");
	return query.list();
}
/**
 * Returns a Conference object with the given conferenceId.
 *
 * @param websafeConferenceKey The String representation of the Conference Key.
 * @return a Conference object with the given conferenceId.
 * @throws NotFoundException when there is no Conference with the given conferenceId.
 */
@ApiMethod(
        name = "getJob",
        path = "job/{websafeJobKey}",
        httpMethod = HttpMethod.GET
)
public Job getJob(
        @Named("websafeJobKey") final String websafeJobKey)
        throws NotFoundException {
    Key<Job> jobKey = Key.create(websafeJobKey);
    Job job = ofy().load().key(jobKey).now();
    if (job == null) {
        throw new NotFoundException("No Conference found with key: " + websafeJobKey);
    }
    return job;
}
}
