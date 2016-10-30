package com.amudhan.jpatest.model.filtering.cascade;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotNull;

@Entity(name = "FILTERING_CASCADE_USER")
public class User {

	@Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
	
	@NotNull
	private String userName;
	
	@OneToMany(cascade = {
			CascadeType.PERSIST, CascadeType.REFRESH
	})
	@JoinColumn(name="USER_ID", nullable = false)
	private Set<BillingDetails> billingDetails = new HashSet<BillingDetails>();
	
	public User(){}
	
	public User(String userName){
		this.userName = userName;
	}

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

	public Set<BillingDetails> getBillingDetails() {
		return billingDetails;
	}

	public void setBillingDetails(Set<BillingDetails> billingDetails) {
		this.billingDetails = billingDetails;
	}
	
	
}
