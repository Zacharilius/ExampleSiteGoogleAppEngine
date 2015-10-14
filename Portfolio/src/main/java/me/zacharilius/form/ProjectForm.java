package me.zacharilius.form;

import java.util.List;

import com.google.common.collect.ImmutableList;
import com.googlecode.objectify.annotation.Index;

/**
 * Pojo representing a profile form on the client side.
 */
public class ProjectForm {
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
    
    /**
     * Topics related to this conference.
     */
    @Index
    private List<technologiesList> technologies;

    
    private ProjectForm(){}
    
	/**
	 * @param title
	 * @param description
	 * @param githubURL
	 * @param pageURL
	 * @param codepenURL
	 * @param technologies
	 */
	public ProjectForm(String title, String description, String githubURL, String pageURL, String codepenURL,
			List<technologiesList> technologies) {
		super();
		this.title = title;
		this.description = description;
		this.githubURL = githubURL;
		this.pageURL = pageURL;
		this.codepenURL = codepenURL;
		this.technologies = technologies == null ? null : ImmutableList.copyOf(technologies);
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
		return technologies;
	}
	public static enum technologiesList {
    	JAVASCRIPT,
        ANGULARJS,
        HTML5,
        HTML,
        JQUERY,
        GOOGLE_MAPS_API,
        EXPRESSJS,
        JSON,
        GOOGLE_ENDPOINTS_API,
        JAVA,
        CSS,
        CSS3,
        OAUTH_2
    }
}
