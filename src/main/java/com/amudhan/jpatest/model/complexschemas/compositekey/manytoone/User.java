package com.amudhan.jpatest.model.complexschemas.compositekey.manytoone;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity(name = "COMPLEXSCHEMAS_COMPOSITEKEY_MANYTOONE_USER")
@Table(name = "COMPLEXSCHEMAS_COMPOSITEKEY_MANYTOONE_USER")
public class User {

	@EmbeddedId
	private UserId id;

	public User() {
	}

	public User(UserId id){
		this.id = id;
	}
	
	public UserId getId() {
		return id;
	}

	public void setId(UserId id) {
		this.id = id;
	}

	@Override
	public String toString() {
		return "User [id=" + id +"]";
	}

}
