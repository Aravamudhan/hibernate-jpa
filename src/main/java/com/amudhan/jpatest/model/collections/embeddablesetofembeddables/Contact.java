package com.amudhan.jpatest.model.collections.embeddablesetofembeddables;

import javax.persistence.Embeddable;

@Embeddable
public class Contact {

	private String name;
	
	public Contact(){}
	
	public Contact(String name){
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
}
