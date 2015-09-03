package me.zacharilius.portfolio.domain;

import com.google.api.server.spi.config.AnnotationBoolean;
import com.google.api.server.spi.config.ApiResourceProperty;
import com.googlecode.objectify.Key;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Index;
import com.googlecode.objectify.annotation.Parent;

/**
 * A google appengine Entity for Job Postings for the Odd-Job application
 * @author zacharilius
 *
 */
public class Job{
	
	// The unique identifier for each job posting
	@Id
	private long id;
	
	// Identifies each job posting as a child of the poster
	@Parent
    @ApiResourceProperty(ignored = AnnotationBoolean.TRUE)
	private String profileId;
	
	// The key of the poster
	private Key<Profile> profileKey;
	
	@Index
	private String title;
	
	
	private String description;
	
	// Must be searchable but how...
	private String address;
	
	//Map location
	
	
	public Job(long id, String profileId, JobForm jobForm){
		//Add precondition checks
		
		this.id = id;
		this.profileId = profileId;
		this.profileKey = Key.create(Profile.class, profileId);
		
		updateWithJobForm(jobForm);
	}


	private void updateWithJobForm(JobForm jobForm) {
		this.title = jobForm.getTitle();
		this.description = jobForm.getDescription();
		this.address = jobForm.getAddress();
		
	}
	
	
	
	
	
}
