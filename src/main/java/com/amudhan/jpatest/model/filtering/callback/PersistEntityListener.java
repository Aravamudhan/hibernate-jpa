package com.amudhan.jpatest.model.filtering.callback;

import javax.persistence.PostPersist;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PersistEntityListener {
	
	private static Logger logger = LoggerFactory.getLogger(PersistEntityListener.class);
	
	@PostPersist
	public void notifyAdmin(Object entity){
		User currentUser = CurrentUser.INSTANCE.get();
		Mail mail = Mail.INSTANCE;
		if(entity instanceof Item){
			Item item = (Item)entity;
			logger.info("Persisted the item: "+item.getName());
		}
		mail.send(
	            "Entity instance persisted by "
	                + currentUser.getUserName()
	                + " in the PersistentEntityListener: "
	                + entity.getClass()
	        );
	}
}
