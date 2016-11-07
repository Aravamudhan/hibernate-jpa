package com.amudhan.jpatest.model.filtering.callback;

import javax.persistence.Entity;

@Entity(name = "FILTERING_CALLBACK_SPECIALUSER")
/*
 * When an instance of SpecialUser is persisted, 
 * the callback in the User entity is called.
 */
public class SpecialUser extends User {

	private int giftPoints;

	public int getGiftPoints() {
		return giftPoints;
	}

	public void setGiftPoints(int giftPoints) {
		this.giftPoints = giftPoints;
	}
	
}
