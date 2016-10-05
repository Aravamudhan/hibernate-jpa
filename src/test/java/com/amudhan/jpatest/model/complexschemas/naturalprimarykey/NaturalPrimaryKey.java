package com.amudhan.jpatest.model.complexschemas.naturalprimarykey;

import javax.transaction.UserTransaction;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.Test;

import com.amudhan.jpatest.environment.JPASetup;
import com.amudhan.jpatest.environment.TransactionManagerTest;

public class NaturalPrimaryKey extends TransactionManagerTest{

	private JPASetup jpa;
	private Logger logger = LoggerFactory.getLogger(NaturalPrimaryKey.class);
	
	@Test
	public void storeLoad(){
		UserTransaction tx = TRANSACTION_MANAGER.getUserTransaction();
	}
}
