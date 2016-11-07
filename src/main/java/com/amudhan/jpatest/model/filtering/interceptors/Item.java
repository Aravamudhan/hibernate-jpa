package com.amudhan.jpatest.model.filtering.interceptors;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;

@Entity(name = "FILTERING_INTERCEPTORS_ITEM")
public class Item implements Auditable {

	@Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    protected Long id;
	
	@NotNull
	private String name;
	
	protected Item() {
    }

    public Item(String name) {
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
