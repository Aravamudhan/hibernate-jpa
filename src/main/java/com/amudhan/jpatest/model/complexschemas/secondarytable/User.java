package com.amudhan.jpatest.model.complexschemas.secondarytable;

import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.SecondaryTable;
import javax.persistence.Table;

@Entity(name = "COMPLEXSCHEMAS_SECONDARYTABLE_USER")
@Table(name = "COMPLEXSCHEMAS_SECONDARYTABLE_USER")
/* If few properties that are part of an entity belong to different table, secondary
 * table annotation is used as the mapping meta data. This says that a secondary
 * table which uses the primary key of the current table, exists.
 * When ever the billingAddress is set, the secondary table is updated.
 * This increases the complexity of the mapping. This method is usually
 * followed for legacy schemas.*/
@SecondaryTable(
			name = "COMPLEXSCHEMAS_SECONDARYTABLE_BILLING_ADDRESS",
			pkJoinColumns = @PrimaryKeyJoinColumn(name = "USER_ID")
		)
public class User {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;
	
	private String userName;
	
	private Address homeAddress;
	
	@AttributeOverrides({
		@AttributeOverride(name = "street",
				column = @Column(table = "COMPLEXSCHEMAS_SECONDARYTABLE_BILLING_ADDRESS", nullable = false)),
		@AttributeOverride(name = "zipCode",
				column = @Column(table = "COMPLEXSCHEMAS_SECONDARYTABLE_BILLING_ADDRESS", nullable = false)),
		@AttributeOverride(name = "city",
				column = @Column(table = "COMPLEXSCHEMAS_SECONDARYTABLE_BILLING_ADDRESS", nullable = false))
	})
	private Address billingAddress;

	public long getId() {
		return id;
	}

	public void setId(long id) {
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
	
	
}
