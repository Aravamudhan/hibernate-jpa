package com.amudhan.jpatest.model.filtering.callback;

import javax.persistence.Entity;
import javax.persistence.ExcludeDefaultListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.PostPersist;
import javax.persistence.PrePersist;
import javax.validation.constraints.NotNull;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Entity(name = "FILTERING_CALLBACK_USER")
@ExcludeDefaultListeners
@Inheritance(strategy = InheritanceType.JOINED)
public class User {

	@Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
	
	@NotNull
	private String userName;
	
	public User(){}
	
	public User(String userName){
		this.userName = userName;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}
	
	private static Logger logger = LoggerFactory.getLogger(User.class);
	
	/*
	 * This is called before SQL generation for the entity persistence.
	 * These methods belong to JPA. They do not work for the 
	 * Hibernate's Session API.
	 */
	@PrePersist
	public void notifyBefore(){
		logger.info(this.getUserName() + " is about to be persisted.");
	}
	
	/*
	 * This is called just after the SQL generation of the entity persistence.
	 */
	@PostPersist
    public void notifyAdmin(){
        Mail mail = Mail.INSTANCE;
        logger.info(this.getUserName() +" is persisted.");
        mail.send(
                " In the entity: "
                + this.getClass()
        );
    }

}
