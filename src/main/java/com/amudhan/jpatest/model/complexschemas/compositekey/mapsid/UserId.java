package com.amudhan.jpatest.model.complexschemas.compositekey.mapsid;

import java.io.Serializable;

import javax.persistence.Embeddable;

/* This serves as a composite key to the User entity.*/
@Embeddable
public class UserId implements Serializable {

	private static final long serialVersionUID = 1L;

	private String userName;
	private Long departmentNumber;

	public UserId() {
	}

	/* The departmentNumber will be ignored by Hibernate since the User
	 * entity has a ManyToOne relationship with Department with "departmentNumber"
	 * in the MapsId annotation. This is called derived identifier mapping.
	 * This means that a Department entity must be saved before a User and
	 * the id generated for that Department will be used as the value for the
	 * departmentNumber field in the composite key.*/
	public UserId(String userName, Long departmentNumber) {
		this.userName = userName;
		this.departmentNumber = departmentNumber;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public Long getDepartmentNumber() {
		return departmentNumber;
	}

	public void setDepartmentNumber(Long departmentNumber) {
		this.departmentNumber = departmentNumber;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime
				* result
				+ ((departmentNumber == null) ? 0 : departmentNumber.hashCode());
		result = prime * result
				+ ((userName == null) ? 0 : userName.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		UserId other = (UserId) obj;
		if (departmentNumber == null) {
			if (other.departmentNumber != null)
				return false;
		} else if (!departmentNumber.equals(other.departmentNumber))
			return false;
		if (userName == null) {
			if (other.userName != null)
				return false;
		} else if (!userName.equals(other.userName))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "UserId [userName=" + userName + ", departmentNumber=" + departmentNumber + "]";
	}
}
