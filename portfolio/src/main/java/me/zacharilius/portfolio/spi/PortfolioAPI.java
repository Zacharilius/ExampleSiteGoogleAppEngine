package me.zacharilius.portfolio.spi;

import static me.zacharilius.portfolio.service.OfyService.ofy;
import static me.zacharilius.portfolio.service.OfyService.factory;


import com.google.api.server.spi.config.Api;
import com.google.api.server.spi.config.ApiMethod;
import com.google.api.server.spi.config.ApiMethod.HttpMethod;
import com.google.api.server.spi.response.NotFoundException;
import com.google.api.server.spi.response.UnauthorizedException;
import com.google.appengine.api.users.User;
import com.googlecode.objectify.Key;
import com.googlecode.objectify.cmd.Query;

import me.zacharilius.portfolio.Constants;
import me.zacharilius.portfolio.domain.Blog;
import me.zacharilius.portfolio.domain.HelloGreeting;
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

  public static ArrayList<HelloGreeting> greetings = new ArrayList<HelloGreeting>();

  static {
    greetings.add(new HelloGreeting("hello world!"));
    greetings.add(new HelloGreeting("goodbye world!"));
  }
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
  
/*
   * Returns the most recent blog entry
   */
  @ApiMethod(name = "blog.getMostRecent", path = "blog/newest-post", httpMethod = HttpMethod.GET)
  public List<Blog> getRecentBlog(){
      Query<Blog> query = ofy().load().type(Blog.class).order("-date").limit(1);
      return query.list();
  }
  
  
  
  
  
  
  
  
  
  
  
  
  public HelloGreeting getGreeting(@Named("id") Integer id) throws NotFoundException {
    try {
      return greetings.get(id);
    } catch (IndexOutOfBoundsException e) {
      throw new NotFoundException("Greeting not found with an index: " + id);
    }
  }

  public ArrayList<HelloGreeting> listGreeting() {
    return greetings;
  }

  @ApiMethod(name = "greetings.multiply", httpMethod = "post")
  public HelloGreeting insertGreeting(@Named("times") Integer times, HelloGreeting greeting) {
    HelloGreeting response = new HelloGreeting();
    StringBuilder responseBuilder = new StringBuilder();
    for (int i = 0; i < times; i++) {
      responseBuilder.append(greeting.getMessage());
    }
    response.setMessage(responseBuilder.toString());
    return response;
  }

  @ApiMethod(name = "greetings.authed", path = "hellogreeting/authed")
  public HelloGreeting authedGreeting(User user) {
    HelloGreeting response = new HelloGreeting("hello " + user.getEmail() +"!!!!");
    return response;
  }
}
