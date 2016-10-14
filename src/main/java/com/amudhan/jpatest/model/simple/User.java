package com.amudhan.jpatest.model.simple;

import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity(name = "SIMPLE_USER")
public class User {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	
	private String userName;
	
	private Address homeAddress;
	
	@Embedded // Not necessary...
    @AttributeOverrides({
            @AttributeOverride(name = "street",
                    column = @Column(name = "BILLING_STREET")), // NULLable!
            @AttributeOverride(name = "zipcode",
                    column = @Column(name = "BILLING_ZIPCODE", length = 5)),
            @AttributeOverride(name = "city",
                    column = @Column(name = "BILLING_CITY"))
    })
    private Address billingAddress;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public Address getHomeAddress() {
		return homeAddress;
	}

	public void setHomeAddress(Address homeAddress) {
		this.homeAddress = homeAddress;
	}

	public Address getBillingAddress() {
		return billingAddress;
	}

	public void setBillingAddress(Address billingAddress) {
		this.billingAddress = billingAddress;
	}
	
	/* When overriding equals method it is always better to avoid using surrogate primary key.
	 * These keys are assigned by Hibernate once the entity becomes persistent.
	 * Initially the id(the surrogate keys/application generated keys) would be null.
	 * The database identifier(surrogate keys) equality is strongly discouraged.*/
	/* Business keys are a property or a combination of properties that are used to identify
	 * an entity/record uniquely. This could name, Aadhar number, address or a combination of all.
	 * In the equals method, these are properties that must be used. Best candidates for
	 * a business key is properties that are immutable, or change very rarely.*/
	@Override
	public boolean equals(Object other){
		if(this == other) return true;
		/* Check the type using instanceof keyword, not using the getClass method.
		 * The proxies usually are subclasses.*/
		if(other == null || !(other instanceof User) ) return false;
		User otherObject = (User)other;
		/* In Hibernate the reference passed as other might be a proxy.
		 * For proxies, their properties can not be accessed directly.
		 * It is always a best practice to use a getter method anyway.*/
		return this.getUserName().equals(otherObject.getUserName());
	}
	
	@Override
	public int hashCode(){
		return this.getUserName().hashCode();
	}
	
}
