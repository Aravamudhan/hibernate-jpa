package com.amudhan.jpatest.model.querying;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.validation.constraints.NotNull;

@Embeddable
public class Address {
	
	@NotNull
	private String street;
	
	@NotNull
	@Column(length=5)
	private String zipcode;
	
	@NotNull
	private String city;
	
	public Address(){}
	
	public Address(String street, String zipcode, String city){
		this.street = street;
        this.zipcode = zipcode;
        this.city = city;
	}
	
	public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getZipcode() {
        return zipcode;
    }

    public void setZipcode(String zipcode) {
        this.zipcode = zipcode;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }
}
