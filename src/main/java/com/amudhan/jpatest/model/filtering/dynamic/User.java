package com.amudhan.jpatest.model.filtering.dynamic;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;

@Entity(name = "FILTERING_DYNAMIC_USER")
public class User {

	@Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
	
	@NotNull
    private String username;

    @NotNull
    private int rank = 0;

    @SuppressWarnings("unused")
	private User() {
    }

    public User(String username) {
        this.username = username;
    }
    
    public User(String username, int rank) {
        this.username = username;
        this.rank = rank;
    }

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public int getRank() {
		return rank;
	}

	public void setRank(int rank) {
		this.rank = rank;
	}
    
}
