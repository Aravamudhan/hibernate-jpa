package com.amudhan.jpatest.model.associations.onetomany.direct;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.amudhan.jpatest.model.AbstractUser;

@Entity(name = "ASSOCIATIONS_ONETOMANY_USER")
@Table(name = "ASSOCIATIONS_ONETOMANY_USER")
public class User extends AbstractUser {

	/* If mappedBy does not exist hibernate creates a new join table, to maintain
	 * the User#boughtItems relationship. Hibernate does not know whether Item#buyer
	 * exists on the other side.*/
	@OneToMany// (mappedBy = "buyer")
	private Set<Item> boughtItems = new HashSet<Item>();

	public Set<Item> getBoughtItems() {
		return boughtItems;
	}

	public void setBoughtItems(Set<Item> boughtItems) {
		this.boughtItems = boughtItems;
	}

	@Override
	public String toString() {
		return "User [id=" + id + ", name=" + name + "]";
	}
	
}
