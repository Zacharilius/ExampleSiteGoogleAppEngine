package me.zacharilius.form;

import java.util.Date;
import java.util.List;


public class JobForm{
	
    private String title;

    /**
     * The description of the job.
     */
    private String description;


    /**
     * Job descriptor tags
     */
    private List<String> tags;
    
	/**
     * Hours for job
     */
    private double hours;
    
    /**
     * Job pay
     */
    private double pay;
    
    /**
     * Latitude of jobMarker object
     */
    private double latitude;
 
    /**
     * Longitude of jobMarker object
     */
    private double longitude;
    
    /**
     * Needs completed by date
     */
    private Date completionDate;	
    
    private JobForm(){}

    
    
    /**
	 * @param title
	 * @param description
	 * @param tags
	 * @param hours
	 * @param pay
	 * @param completionDate
	 */
	public JobForm(String title, String description, List<String> tags, double hours, double pay, Date completionDate, double latitude, double longitude) {
		this.title = title;
		this.description = description;
		this.tags = tags;
		this.hours = hours;
		this.pay = pay;
		this.completionDate = completionDate;
		this.latitude = latitude;
		this.longitude = longitude;
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
	 * @return the tags
	 */
	public List<String> getTags() {
		return tags;
	}

	/**
	 * @return the hours
	 */
	public double getHours() {
		return hours;
	}

	/**
	 * @return the pay
	 */
	public double getPay() {
		return pay;
	}

	/**
	 * @return the completionDate
	 */
	public Date getCompletionDate() {
		return completionDate;
	}

	


    
    
}
