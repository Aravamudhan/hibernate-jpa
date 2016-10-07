package com.amudhan.jpatest.model.complexschemas.compositekey.embedded;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity(name = "COMPLEXSCHEMAS_COMPOSITEKEY_EMBEDDED_USER")
@Table(name = "COMPLEXSCHEMAS_COMPOSITEKEY_EMBEDDED_USER")
public class User {

	@EmbeddedId
	private UserId id;

	public User() {
	}

	public User(UserId id) {
		this.id = id;
	}

	public UserId getId() {
		return id;
	}

	@Override
	public String toString() {
		return "User [id=" + id + "]";
	}

	public void setId(UserId id) {
		this.id = id;
	}

}
