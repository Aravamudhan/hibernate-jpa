package com.amudhan.jpatest.model.associations.onetoone.direct;

import javax.persistence.Entity;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.amudhan.jpatest.model.AbstractUser;

@Entity(name = "ASSOCIATIONS_ONETOONE_USER")
@Table(name = "ASSOCIATIONS_ONETOONE_USER")
/* This entity is just to see what happens for the default mapping with out any options.*/
public class User extends AbstractUser {
	
	/* This triggers default mapping.
	 * The Bid#id is mapped as the foreign key to the Bid table, in the User table.*/
	/* One caveat is Bid has to be persisted first. Hibernate will then automatically, 
	 * assign the bid#id to the bid_id column of the user table.
	 */
	@OneToOne
	private Bid bid = new Bid();

	public Bid getBid() {
		return bid;
	}

	public void setBid(Bid bid) {
		this.bid = bid;
	}
	
}
