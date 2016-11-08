package com.amudhan.jpatest.model.filtering.envers;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;

import org.hibernate.envers.Audited;

@Entity(name = "FILTERING_ENVERS_USER")
@Audited
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

}
