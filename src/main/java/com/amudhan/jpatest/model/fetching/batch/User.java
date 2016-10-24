package com.amudhan.jpatest.model.fetching.batch;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity(name="FETCHING_BATCH_USER")
/* With lazy loading hibernate fetches data on demand.
 * If many parent entities are loaded, this will trigger
 * many SQL queries with multiple trips to the database. 
 * The batch fetching algorithm optimizes this by initializing
 * several proxies at one go with the same SELECT.
 * This annotation asks the hibernate to load up to 10 User
 * proxies if one User has to be loaded.*/
@org.hibernate.annotations.BatchSize(size = 10)
public class User {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	
	private String userName;

	public User(){
		
	}
	
	public User(String userName) {
		this.userName = userName;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public Long getId() {
		return id;
	}

	@SuppressWarnings("unused")
	private void setId(Long id) {
		this.id = id;
	}

	
}
