package com.amudhan.jpatest.model.associations.onetomany.embeddable.jointable;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
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
	@OneToMany
	@JoinTable(
			name = "DELIVERIES",
			joinColumns =
				@JoinColumn(name = "USER_ID"),
			inverseJoinColumns = 
				@JoinColumn(name = "SHIPMENT_ID")
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
