package com.amudhan.jpatest.model.complexschemas.naturalforeignkey;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

@Entity(name = "COMPLEXSCHEMAS_NATURALFOREIGNKEY_USER")
@Table(name = "COMPLEXSCHEMAS_NATURALFOREIGNKEY_USER")
public class User implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 943850432095519844L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	
	@NotNull
	@Column(unique = true)
	private String customerNumber;
	
	public User(){}
	
	public User(String customerNumber){
		this.customerNumber = customerNumber;
	}
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getCustomerNumber() {
		return customerNumber;
	}

	public void setCustomerNumber(String customerNumber) {
		this.customerNumber = customerNumber;
	}
	
}
