package com.amudhan.jpatest.model.associations.onetomany.jointable;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.amudhan.jpatest.model.AbstractUser;

@Entity(name = "ASSOCIATIONS_ONETOMANY_JOINTABLE_USER")
@Table(name = "ASSOCIATIONS_ONETOMANY_JOINTABLE_USER")
public class User extends AbstractUser {

	/* TODO: Create two entities using default OneToMany, ManyToOne settings.
	 * Observe the behavior. In default mode, foreign key is added in the 
	 * ManyToOne side, and also a new table is created that stores the id
	 * values of both the sides. */
	@OneToMany(mappedBy = "buyer")
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
