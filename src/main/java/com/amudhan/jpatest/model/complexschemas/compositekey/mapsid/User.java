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
	@MapsId("departmentId")
	private Department department;

	public User() {
	}

	public User(UserId id) {
		this.id = id;
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
	
	@Override
	public String toString() {
		return "User [id=" + id + "]";
	}

}
