package me.zacharilius.domain;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.google.common.collect.ImmutableList;
import com.googlecode.objectify.annotation.Cache;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Index;

import me.zacharilius.form.ProjectForm;
import me.zacharilius.form.ProjectForm.technologiesList;


// TODO indicate that this class is an Entity
@Entity
@Cache
public class Project {
	
    /**
     * The id for the datastore key.
     *
     * We use automatic id assignment for entities of Conference class.
     */
    @Id
    private long id;
    


    /**
     * The name of the project.
     */
    @Index
    private String title;

    /**
     * The description of the project.
     */
    private String description;

    private String githubURL;
    
    private String pageURL;
    
    private String codepenURL;
    
    private Date date;
    
    /**
     * Topics related to this conference.
     */
    @Index
    private List<technologiesList> technologies;
    
    
    private Project(){}

    

	/**
	 * @param id
	 * @param title
	 * @param description
	 * @param githubURL
	 * @param pageURL
	 * @param codepenURL
	 * @param technologies
	 */
	public Project(long id, ProjectForm projectForm) {
		this.id = id;
		this.date = new Date();
		this.title = projectForm.getTitle();
		this.description = projectForm.getDescription();
		this.githubURL = projectForm.getGithubURL();
		this.pageURL = projectForm.getPageURL();
		this.codepenURL = projectForm.getCodepenURL();
		
		technologies = projectForm.getTechnologies();
		this.technologies = technologies == null || technologies.isEmpty() ? null : projectForm.getTechnologies();
	}

	/**
	 * 
	 * @return
	 */
	public Date getDate(){
		return date;
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
	 * @return the githubURL
	 */
	public String getGithubURL() {
		return githubURL;
	}


	/**
	 * @return the pageURL
	 */
	public String getPageURL() {
		return pageURL;
	}


	/**
	 * @return the codepenURL
	 */
	public String getCodepenURL() {
		return codepenURL;
	}


	/**
	 * @return the technologies
	 */
	public List<technologiesList> getTechnologies() {
        return technologies == null ? null : ImmutableList.copyOf(technologies);	
	}
}
