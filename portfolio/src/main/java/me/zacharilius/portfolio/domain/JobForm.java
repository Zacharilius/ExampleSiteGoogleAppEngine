package me.zacharilius.portfolio.domain;

import com.googlecode.objectify.annotation.Index;

public class JobForm {
	@Index
	private String title;
	
	
	private String description;
	
	// Must be searchable but how...
	private String address;

	public String getTitle() {
		// TODO Auto-generated method stub
		return title;
	}

	public String getDescription() {
		// TODO Auto-generated method stub
		return description;
	}

	public String getAddress() {
		// TODO Auto-generated method stub
		return address;
	}

}
