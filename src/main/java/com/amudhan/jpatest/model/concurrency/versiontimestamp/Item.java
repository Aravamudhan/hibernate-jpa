package com.amudhan.jpatest.model.concurrency.versiontimestamp;

import java.time.LocalDateTime;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Version;

@Entity(name = "CONCURRENCY_VERSIONTIMESTAMP_ITEM")
public class Item {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	
	@Version
	private LocalDateTime lastUpdated;
	
	private String name;
	
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

	public Long getId() {
		return id;
	}

	public LocalDateTime getLastUpdated() {
		return lastUpdated;
	}
}
