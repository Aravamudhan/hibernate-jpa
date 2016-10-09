package com.amudhan.jpatest.model.complexschemas.secondarytable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class Address {

	@Column(nullable = false)
	private String street;
	
	@Column(nullable = false)
	private String zipCode;
	
	@Column(nullable = false)
	private String city;
	
	public Address(){}
	
	public Address(String street, String zipCode, String city){
		this.street = street;
		this.zipCode = zipCode;
		this.city = city;
	}

	public String getStreet() {
		return street;
	}

	public void setStreet(String street) {
		this.street = street;
	}

	public String getZipCode() {
		return zipCode;
	}

	public void setZipCode(String zipCode) {
		this.zipCode = zipCode;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}
	
}
