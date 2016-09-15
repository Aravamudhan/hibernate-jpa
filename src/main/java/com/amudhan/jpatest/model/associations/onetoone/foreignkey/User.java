package com.amudhan.jpatest.model.associations.onetoone.foreignkey;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.amudhan.jpatest.model.AbstractUser;

@Entity(name = "ASSOCIATIONS_ONETOONE_FOREIGNKEY_USER")
@Table(name = "ASSOCIATIONS_ONETOONE_FOREIGNKEY_USER")
public class User extends AbstractUser {

	@OneToOne(
			fetch = FetchType.LAZY,
			/* Since Address is marked as unique, only one field in the 
			 * SHIPPING_ID column can have null assigned to it. Hence
			 * it makes sense to make it non optional rather than optional.
			 * */
			optional = false,
			cascade = CascadeType.PERSIST
	)
	/* JoinColumn means foreign key.
	 * This defaults to SHIPPINGADDRESS_ID.
	 * This creates(or expects) a column named SHIPPINGADDRESS_ID in the User table, and 
	 * is assigned the id value of the Address entity.*/
	/* OneToOne using foreign key association should be the default mapping methodology,
	 * except where the association is optional.*/
	@JoinColumn(unique = true)
	private Address shippingAddress;

	public Address getShippingAddress() {
		return shippingAddress;
	}
	public void setShippingAddress(Address shippingAddress) {
		this.shippingAddress = shippingAddress;
	}
}
