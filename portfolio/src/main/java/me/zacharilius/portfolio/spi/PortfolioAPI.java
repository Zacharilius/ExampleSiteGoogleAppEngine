package me.zacharilius.portfolio.spi;

import static me.zacharilius.portfolio.service.OfyService.ofy;
import static me.zacharilius.portfolio.service.OfyService.factory;


import com.google.api.server.spi.config.Api;
import com.google.api.server.spi.config.ApiMethod;
import com.google.api.server.spi.config.ApiMethod.HttpMethod;
import com.google.api.server.spi.response.NotFoundException;
import com.google.api.server.spi.response.UnauthorizedException;
import com.google.appengine.api.taskqueue.Queue;
import com.google.appengine.api.taskqueue.QueueFactory;
import com.google.appengine.api.taskqueue.TaskOptions;
import com.google.appengine.api.users.User;
import com.googlecode.objectify.Key;
import com.googlecode.objectify.cmd.Query;
import com.googlecode.objectify.Work;

import me.zacharilius.portfolio.Constants;
import me.zacharilius.portfolio.domain.Blog;
import me.zacharilius.portfolio.domain.Profile;
import me.zacharilius.portfolio.form.BlogForm;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Named;

/**
 * Defines v1 of the portfolio API, which provides simple "greeting" methods.
 */
@Api(
    name = "portfolio",
    version = "v1",
    scopes = {Constants.EMAIL_SCOPE},
    clientIds = {Constants.WEB_CLIENT_ID, Constants.ANDROID_CLIENT_ID, Constants.IOS_CLIENT_ID, Constants.API_EXPLORER_CLIENT_ID },
    audiences = {Constants.ANDROID_AUDIENCE}
)
public class PortfolioAPI {


  /*
   * Creates a new blog entry
   */
  @ApiMethod(name = "blog.create", path = "blog/write-new", httpMethod = HttpMethod.POST)
  public Blog createBlog(final User user, BlogForm blogForm)
          throws UnauthorizedException {
	     // TODO 2
      // If the user is not logged in, throw an UnauthorizedException
      if (user == null) {
          throw new UnauthorizedException("Authorization required");
      }

      // Get the user id and create the key
      final String userId = user.getUserId();
      Key<Profile> profileKey = Key.create(Profile.class, userId);
      
      // Create a key for the blog using the profile ancestor. Get its id
      final Key<Blog> blogKey = factory().allocateId(profileKey, Blog.class);
      final long blogId = blogKey.getId();
      
      // Get the profile from the datastore
      Profile profile = getProfileFromUser(user);
	
      // Create a new blog POJO and save it.
      Blog blog = new Blog(blogId, userId, blogForm);
      ofy().save().entities(profile, blog).now();
      
      return blog;
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
                  extractDefaultDisplayNameFromEmail(email), email);
      }
      return profile;
  }
  
  
  /*
   * Get the display name from the user's email. For example, if the email is
   * example@example.com, then the display name becomes "example."
   */
  private static String extractDefaultDisplayNameFromEmail(String email) {
      return email == null ? null : email.substring(0, email.indexOf("@"));
  }
  
  /**
   * Returns the most recent blog entry
   */
  @ApiMethod(name = "blog.getMostRecent", path = "blog/newest-post", httpMethod = HttpMethod.GET)
  public List<Blog> getRecentBlog(){
      Query<Blog> query = ofy().load().type(Blog.class).order("-date").limit(1);
      return query.list();
  }
  

	/**
	 * Endpoint for the contact-me form in portfolio
	 * @param user The user object of the logged in user
	 * @return 
	 * @throws UnauthorizedException
	 */  	
  	@ApiMethod(name="portfolio.sendEmail",  httpMethod = HttpMethod.POST)
  	public WrappedBoolean sendEmail(final User user, @Named("emailName") final String emailName, 
  			@Named("emailSubject") final String emailSubject, @Named("emailBody") final String emailBody)
  	        throws UnauthorizedException {
  		System.out.println("emailName: " + emailName);
  		if (user == null) {
  			throw new UnauthorizedException("Authorization required");
  	    }
        final String userId = user.getUserId();
        Key<Profile> profileKey = Key.create(Profile.class, userId);
        final Queue queue = QueueFactory.getDefaultQueue();
        
        // Start transaction
        WrappedBoolean result = ofy().transact(new Work<WrappedBoolean>() {
            @Override
            public WrappedBoolean run() {
                try {
                    Profile profile = getProfileFromUser(user);
                    if(profile == null){
                    	return new WrappedBoolean(false, "Not registered user");
                    }
                	queue.add(ofy().getTransaction(),
                		TaskOptions.Builder.withUrl("/tasks/send_me_an_email")
                		.param("email",  profile.getMainEmail())
                		.param("emailName", emailName)
                		.param("emailBody", emailBody)
                		.param("emailSubject", emailSubject));
                	
                	return new WrappedBoolean(true, "Your email has been sent");
                }catch(Exception e){
                	return new WrappedBoolean(false);
                }
            }
        });
        return result;
  	}
  	 /**
     * Just a wrapper for Boolean.
     * We need this wrapped Boolean because endpoints functions must return
     * an object instance, they can't return a Type class such as
     * String or Integer or Boolean
     */
    public static class WrappedBoolean {

        private final Boolean result;
        private final String reason;

        public WrappedBoolean(Boolean result) {
            this.result = result;
            this.reason = "";
        }

        public WrappedBoolean(Boolean result, String reason) {
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
  
    
    
	/**
	 * Endpoint for the contact-me form in portfolio
	 * @param user The user object of the logged in user
	 * @return 
	 * @throws UnauthorizedException
	 */  	
  	@ApiMethod(name="portfolio.sendEmail",  httpMethod = HttpMethod.POST)
  	public void myClass(){
  		
  	}
  	
    
    
    
    
    
    
    
    
    
}
