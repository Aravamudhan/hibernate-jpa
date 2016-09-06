package com.amudhan.jpatest.model.associations.onetomany.orphanremoval;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.OneToMany;

import com.amudhan.jpatest.model.AbstractUser;

public class User extends AbstractUser {
	
	@OneToMany(mappedBy = "bidder")
	private Set<Bid> bids = new HashSet<Bid>();

	public Set<Bid> getBids() {
		return bids;
	}

	public void setBids(Set<Bid> bids) {
		this.bids = bids;
	}

}
