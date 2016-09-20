package com.amudhan.jpatest.model.associations.onetomany.embeddable;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotNull;

@Embeddable
public class Address {

	@NotNull
	@Column(nullable = false)
	private String street;
	@NotNull
	@Column(nullable = false)
	private String zipCode;
	@NotNull
	@Column(nullable = false)
	private String city;
	/* This can not have mappedBy since Address is an embeddable.
	 * Embeddables can have only one parent and its reference can not be shared.
	 * Even though Address can reference Shipment, the reverse is not possible.*/
	@OneToMany
	/* User and Address have the same id.
	 * This adds a foreign key reference of the User to Shipment.*/
	@JoinColumn(
			name = "DELIVERY_ADDRESS_USER_ID",
			nullable = false
	)
	private Set<Shipment> deliveries = new HashSet<Shipment>();
	
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
	public Set<Shipment> getDeliveries() {
		return deliveries;
	}
	public void setDeliveries(Set<Shipment> deliveries) {
		this.deliveries = deliveries;
	}
	@Override
	public String toString() {
		return "Address [street=" + street + ", zipCode=" + zipCode + ", city="
				+ city + "]";
	}
	
}
