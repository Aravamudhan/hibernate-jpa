package com.amudhan.jpatest.model.complexschemas.naturalprimarykey;

import javax.persistence.Entity;
import javax.persistence.Id;

/* In the legacy tables (composite) natural keys such as name are used extensively as the primary key.*/
@Entity(name = "COMPLEXSCHEMAS_NATURALPRIMARYKEY")
public class User {

	@Id
	private String name;
	
	public User(){}
	
	public User(String name){
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
}
