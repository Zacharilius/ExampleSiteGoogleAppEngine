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
import me.zacharilius.form.MessageForm;

@Entity
@Cache
public class Message {
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
    private String body;
    

    /**
     * Holds Profile key as the parent. The key belongs to the person posting the job.
     */
    @Parent
    @ApiResourceProperty(ignored = AnnotationBoolean.TRUE)
    private Key<Job> jobKey;

    /**
     * The userId of the job poster.
     */
    @ApiResourceProperty(ignored = AnnotationBoolean.TRUE)
    private String jobId;


    
    /**
     * The posting date
     */
    @Index
    private Date sentDate;
    

  
    /**
     * Just making the default constructor private.
     */
    private Message() {}

    /**
     * 
     * @param id
     * @param posterUserId
     * @param jobForm
     */
    public Message(final long id, final String jobId,final MessageForm messageForm) {
        Preconditions.checkNotNull(messageForm.getTitle(), "The title is required");
        Preconditions.checkNotNull(messageForm.getBody(), "The description is required");

        updateMessageForm(messageForm);
    }
    /**
     * Helper method 
     * @param jobForm
     */
	private void updateMessageForm(MessageForm messageForm) {
		
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

}
