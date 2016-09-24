package com.amudhan.jpatest.model.associations.maps.ternary;

import javax.persistence.Entity;
import javax.persistence.Table;

import com.amudhan.jpatest.model.AbstractUser;

@Entity(name = "ASSOCIATIONS_MANYTOMANY_TERNARY_USER")
@Table(name = "ASSOCIATIONS_MANYTOMANY_TERNARY_USER")
public class User extends AbstractUser {

	public User(){}
	
	public User(String userName){
		this.name = userName;
	}

	@Override
	public String toString() {
		return "User [id=" + id + ", name=" + name + "]";
	}

}
