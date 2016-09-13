package com.amudhan.jpatest.model.associations.onetoone.foreignkeygenerator;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

@Entity( name = "ASSOCIATIONS_ONETOONE_SHAREDPRIMARYKEY_ADDRESS")
@Table( name = "ASSOCIATIONS_ONETOONE_SHAREDPRIMARYKEY_ADDRESS")
public class Address {

	@Id
	@GeneratedValue(generator = "addressKeyGenerator")
	@org.hibernate.annotations.GenericGenerator(
			name = "addressKeyGenerator",
			/* foreign strategy takes the id of the User reference.*/
			strategy = "foreign",
			parameters = 
				@org.hibernate.annotations.Parameter(
						name = "property", value ="user"
				)
			)
	private long id;
	
	@NotNull
	protected String street;
	
	@NotNull
	protected String zipCode;
	
	@NotNull
	protected String city;
	
	@OneToOne(optional = false)
	/* Creates a foreign key reference to Use through the primary key(id) of the Address.*/
	@PrimaryKeyJoinColumn	
	private User user;
	
	public Address(){}
	
	public Address(String street, String zipCode, String city, User user) {
		this.street = street;
		this.zipCode = zipCode;
		this.city = city;
		this.user = user;
	}
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public User getUser() {
		return user;
	}
	public void setUser(User user) {
		this.user = user;
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

	@Override
	public String toString() {
		return "Address [id=" + id + ", street=" + street + ", zipCode="
				+ zipCode + ", city=" + city + "]";
	}
	
}
