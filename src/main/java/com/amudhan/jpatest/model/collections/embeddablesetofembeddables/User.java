package com.amudhan.jpatest.model.collections.embeddablesetofembeddables;

import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.amudhan.jpatest.model.AbstractUser;

@Entity(name = "COLLECTIONS_EMBEDDABLESETOFEMBEDDABLES_USER")
@Table(name = "COLLECTIONS_EMBEDDABLESETOFEMBEDDABLES_USER")
public class User extends AbstractUser {

	private String userName;
	
	@Embedded
	private Address address;

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public Address getAddress() {
		return address;
	}

	public void setAddress(Address address) {
		this.address = address;
	}
}
