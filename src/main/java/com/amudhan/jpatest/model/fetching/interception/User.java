package com.amudhan.jpatest.model.fetching.interception;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;

@Entity(name = "FETCHING_INTERCEPTION_USER")
@org.hibernate.annotations.Proxy(lazy = false)
public class User {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	@NotNull
	protected String userName;

	public User() {
	}

	public User(String userName) {
		this.userName = userName;
	}

	public Long getId() {
		return id;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}
}
