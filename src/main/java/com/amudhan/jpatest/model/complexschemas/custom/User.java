package com.amudhan.jpatest.model.complexschemas.custom;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

@Entity(name = "COMPLEXSCHEMAS_CUSTOM_USER")
/*
 * This is an example for column constraints. userName has constraint on its
 * value. For email, constraint has been enforced by the creation of EMAIL
 * domain.
 */
/* By using @UniqueConstraints, an "unique constraint" can span multiple columns. */
@Table(
		name = "COMPLEXSCHEMAS_CUSTOM_USER",
		uniqueConstraints = @UniqueConstraint(
				name = "UNIQUE_USERNAME_EMAIL",
				columnNames = {"USERNAME","EMAIL"}))
public class User {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	protected long id;

	@Column(columnDefinition = "varchar(15) not null unique" + " check (not substring(lower(USERNAME), 0, 5)='admin')")
	private String userName;

	@Column(nullable = false, unique = true, columnDefinition = "EMAIL_ADDRESS(255)")
	private String email;
	
	public User(){}
	
	public User(String userName, String email){
		this.userName = userName;
		this.email = email;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	@Override
	public String toString() {
		return "User [email=" + email + ", id=" + id + ", name=" + userName + "]";
	}
}
