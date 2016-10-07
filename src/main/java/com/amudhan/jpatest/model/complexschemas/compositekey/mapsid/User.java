package com.amudhan.jpatest.model.complexschemas.compositekey.mapsid;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.MapsId;
import javax.persistence.Table;

@Entity(name = "COMPLEXSCHEMAS_COMPOSITEKEY_MAPSID_USER")
@Table(name = "COMPLEXSCHEMAS_COMPOSITEKEY_MAPSID_USER")
public class User {

	@EmbeddedId
	private UserId id;

	@ManyToOne
	/* Signifies that the departmentNumber field in the UserId will be
	 * receiving the id generated when a Department is saved.*/
	@MapsId("departmentNumber")
	private Department department;
	
	private String userType; 

	public User() {
	}

	public User(UserId id, String userType) {
		this.id = id;
		this.userType = userType;
	}

	public UserId getId() {
		return id;
	}

	public void setId(UserId id) {
		this.id = id;
	}

	public Department getDepartment() {
		return department;
	}

	public void setDepartment(Department department) {
		this.department = department;
	}
	
	public String getUserType() {
		return userType;
	}

	public void setUserType(String userType) {
		this.userType = userType;
	}

	@Override
	public String toString() {
		return "User [id=" + id + " userType = "+userType+"]";
	}

}
