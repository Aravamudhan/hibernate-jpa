package com.amudhan.jpatest.model.complexschemas.compositekey.readonly;

import java.io.Serializable;

import javax.persistence.Embeddable;

/* This serves as a composite key to the User entity.*/
@Embeddable
public class UserId implements Serializable {

	private static final long serialVersionUID = 1L;

	private String userName;
	private Long departmentId;

	public UserId() {
	}

	public UserId(String userName, Long departmentId) {
		this.userName = userName;
		this.departmentId = departmentId;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public Long getdepartmentId() {
		return departmentId;
	}

	public void setdepartmentId(Long departmentId) {
		this.departmentId = departmentId;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime
				* result
				+ ((departmentId == null) ? 0 : departmentId.hashCode());
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
		if (departmentId == null) {
			if (other.departmentId != null)
				return false;
		} else if (!departmentId.equals(other.departmentId))
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
		return "UserId [userName=" + userName + ", departmentId=" + departmentId + "]";
	}
}
