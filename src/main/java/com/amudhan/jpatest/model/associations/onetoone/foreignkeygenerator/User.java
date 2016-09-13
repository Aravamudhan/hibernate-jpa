package com.amudhan.jpatest.model.associations.onetoone.foreignkeygenerator;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity( name = "ASSOCIATIONS_ONETOONE_FOREIGNKEYGENERATOR_USER")
@Table( name = "ASSOCIATIONS_ONETOONE_FOREIGNKEYGENERATOR_USER")
public class User {

	@Id
	@GeneratedValue (generator = "ID_GENERATOR")
	private long id;
	private String name;
	@OneToOne(cascade = CascadeType.PERSIST , mappedBy = "user")
	private Address shippingAddress;
	
	public User(){}
	
	public User(String name){
		this.name = name;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Address getShippingAddress() {
		return shippingAddress;
	}

	public void setShippingAddress(Address shippingAddress) {
		this.shippingAddress = shippingAddress;
	}
	
	@Override
	public String toString() {
		return "User [id=" + id + ", name=" + name +" ] ";
	}
	
}
