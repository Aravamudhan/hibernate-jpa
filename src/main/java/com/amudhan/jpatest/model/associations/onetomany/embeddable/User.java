package com.amudhan.jpatest.model.associations.onetomany.embeddable;

import javax.persistence.Entity;
import javax.persistence.Table;

import com.amudhan.jpatest.model.AbstractUser;

@Entity(name = "ASSOCIATIONS_ONETOMANY_EMBEDDABLE_USER")
@Table(name = "ASSOCIATIONS_ONETOMANY_EMBEDDABLE_USER")
public class User extends AbstractUser {

	private Address shippingAddress;

	public Address getShippingAddress() {
		return shippingAddress;
	}

	public void setShippingAddress(Address shippingAddress) {
		this.shippingAddress = shippingAddress;
	}

	@Override
	public String toString() {
		return "User [id=" + id + ", name=" + name + "]";
	}
	
}
