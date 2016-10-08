package com.amudhan.jpatest.model.complexschemas.compositekey.readonly;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity(name = "COMPLEXSCHEMAS_COMPOSITEKEY_READONLY_USER")
@Table(name = "COMPLEXSCHEMAS_COMPOSITEKEY_READONLY_USER")
public class User {

	@EmbeddedId
	private UserId id;

	@ManyToOne
	/*
	 * The use of setting insertable and updatable to false is to let hibernate
	 * know that the same column is referenced somewhere else too and in that
	 * place it is updated, not here. The UserId has departmentId column. That's
	 * where the departmentId column of the User table is updated. Not here in
	 * this relationship, since joinColumn means a column that serves as a foreign
	 * key.
	 */
	@JoinColumn(name = "DEPARTMENTID", insertable = false, updatable = false)
	private Department department;

	private String userType;

	public User() {
	}

	public User(UserId id, String userType) {
		this.id = id;
		this.userType = userType;
	}

	/*
	 * This constructor enforces how the User has to be instantiated by checking
	 * and setting its properties. This is a better approach than MapsId.
	 */
	public User(String userName, Department department, String userType) {
		if (department.getId() == null)
			throw new IllegalStateException("Department is transient: "
					+ department);
		this.id = new UserId(userName, department.getId());
		this.department = department;
		this.userType = userType;
	}

	public UserId getId() {
		return id;
	}

	public void setId(UserId id) {
		this.id = id;
	}

	public Department getDepartment() {
		return department;
	}

	public void setDepartment(Department department) {
		this.department = department;
	}

	public String getUserType() {
		return userType;
	}

	public void setUserType(String userType) {
		this.userType = userType;
	}

	@Override
	public String toString() {
		return "User [id=" + id + " userType = " + userType + "]";
	}

}
