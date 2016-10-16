package com.amudhan.jpatest.model.concurrency.versionall;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity(name = "CONCURRENCY_VERSIONALL_ITEM")
/* If no version or timestamp columns are available, this can be
 * achieved by auto versioning. In this way, the current database
 * state is checked with the values that existed last time, when the PC was flushed.
 * Hibernate lists all columns and their last known values in the WHERE clause
 * of the UPDATE query. If any value is modified, Hibernate would detect that.*/
/* If the type is changed to DIRTY instead of ALL, just the modified columns
 * are included in the WHERE clause instead of all the columns.*/
@org.hibernate.annotations.OptimisticLocking(
			type = org.hibernate.annotations.OptimisticLockType.ALL
		)
/* Hibernate generates all the CRUD queries up front.
 * Even update queries are generated for each persistent class when the
 * persistent unit is created. Hibernate creates UPDATE query too, up front.
 * It does that by updating all the available columns of an entity. If
 * there are no changes to a column, that is updated with the old value.
 * If an entity is large, and the updates happen for only few columns it
 * would be costly to generate update query for all the columns.
 * By enabling DynamicUpdate option, only changed columns get referenced
 * in the update queries.*/
@org.hibernate.annotations.DynamicUpdate
public class Item {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	
	private String name;
	
	private String description;
	
	public Item(){}
	
	public Item(String name){
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Long getId() {
		return id;
	}
}
