package me.zacharilius.domain;

import static me.zacharilius.service.OfyService.ofy;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import com.google.api.server.spi.config.AnnotationBoolean;
import com.google.api.server.spi.config.ApiResourceProperty;
import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;
import com.googlecode.objectify.Key;
import com.googlecode.objectify.annotation.Cache;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Index;
import com.googlecode.objectify.annotation.Parent;

import me.zacharilius.form.JobForm;

@Entity
@Cache
public class Job {
    /**
     * The id for the datastore key.
     *
     * We use automatic id assignment for entities of Job class.
     */
    @Id
    private long id;

    /**
     * The title of the job.
     */
    @Index
    private String title;

    /**
     * The description of the job.
     */
    private String description;
    

    /**
     * Holds Profile key as the parent. The key belongs to the person posting the job.
     */
    @Parent
    @ApiResourceProperty(ignored = AnnotationBoolean.TRUE)
    private Key<Profile> profileKey;

    /**
     * The userId of the job poster.
     */
    @ApiResourceProperty(ignored = AnnotationBoolean.TRUE)
    private String posterUserId;

    /**
     * Job descriptor tags
     */
    @Index
    private List<String> tags;
    
    /**
     * The posting date
     */
    @Index
    private Date postDate;
    
    /**
     * Hours for job
     */
    @Index
    private double hours;
    
    /**
     * Job pay
     */
    @Index
    private double pay;
    
    /**
     * Needs completed by date
     */
    @Index
    private Date completionDate;
    
    
    /**
     * Latitude of jobMarker object
     */
    private double latitude;
 
    /**
     * Longitude of jobMarker object
     */
    private double longitude;
    
    /**
     * Just making the default constructor private.
     */
    private Job() {}

    /**
     * 
     * @param id
     * @param posterUserId
     * @param jobForm
     */
    public Job(final long id, final String posterUserId,final JobForm jobForm) {
    	System.out.println(jobForm.getTitle());
    	System.out.println(jobForm.getDescription());
        Preconditions.checkNotNull(jobForm.getTitle(), "The title is required");
        Preconditions.checkNotNull(jobForm.getDescription(), "The description is required");
        this.id = id;
        this.profileKey = Key.create(Profile.class, posterUserId);
        this.posterUserId = posterUserId;
        this.postDate = new Date();
        
        List<String> tags = jobForm.getTags();
        this.tags = tags == null || tags.isEmpty() ? new ArrayList<String>(Arrays.asList("Something Else")) : tags;
        updateJobForm(jobForm);
    }
    /**
     * Helper method 
     * @param jobForm
     */
	private void updateJobForm(JobForm jobForm) {
		this.title = jobForm.getTitle();
		this.description = jobForm.getDescription();
		this.hours = jobForm.getHours();
		this.pay = jobForm.getPay();
		
		// If the user supplied a complete by date. Convert it into date format.
        Date completionDate = jobForm.getCompletionDate();
        this.completionDate = completionDate == null ? null : new Date(completionDate.getTime());
        
        //Adds the posting date.
        this.postDate = new Date();
		this.latitude = jobForm.getLatitude();
		this.longitude = jobForm.getLongitude();
	}
	
    /**
	 * @return the id
	 */
	public long getId() {
		return id;
	}

	/**
	 * @return the title
	 */
	public String getTitle() {
		return title;
	}

	/**
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * @return the profileKey
	 */
	public Key<Profile> getProfileKey() {
		return profileKey;
	}
	public String getWebsafeProfileKey(){
        return profileKey.getString();
	}
	/**
	 * @return the posterUserId
	 */
	public String getPosterUserId() {
		return posterUserId;
	}

	/**
	 * @return the tags
	 */
	public List<String> getTags() {
		return tags == null ? null : ImmutableList.copyOf(tags);
	}

	/**
	 * @return the postDate
	 */
	public Date getPostDate() {
		return postDate;
	}

	/**
	 * @return the jobHours
	 */
	public double getJobHours() {
		return hours;
	}

	/**
	 * @return the jobPay
	 */
	public double getJobPay() {
		return pay;
	}

	/**
	 * @return the completionDate
	 */
	public Date getCompletionDate() {
		return completionDate;
	}
	/**
	 * Gets the job's latitude position
	 * @return latitude
	 */
	public double getLatitude(){
		return latitude;
	}
	
	/**
	 * Gets the job longitude position 
	 * @return longitude
	 */
	public double getLongitude(){
		return longitude;
	}
    // Get a String version of the key
    public String getWebsafeKey() {
        return Key.create(profileKey, Job.class, id).getString();
    }
    
    public String getPosterDisplayName(){
    	Profile poster = ofy().load().key(getProfileKey()).now();
    	if(poster == null){
    		return posterUserId;
    	}
    	return poster.getDisplayName();
    	
    }
}
