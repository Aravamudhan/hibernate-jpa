package com.amudhan.jpatest.model.complexschemas.compositekey.mapsid;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity(name = "COMPLEXSCHEMAS_COMPOSITE_MAPSID_DEPARTMENT")
@Table(name = "COMPLEXSCHEMAS_COMPOSITE_MAPSID_DEPARTMENT")
public class Department {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;
	private String name;

	public Department() {
	}

	public Department(String name) {
		this.name = name;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

}
