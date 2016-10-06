package com.amudhan.jpatest.model.complexschemas;

import javax.persistence.EntityManager;
import javax.transaction.UserTransaction;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import static org.testng.Assert.assertNotNull;

import com.amudhan.jpatest.environment.DatabaseProduct;
import com.amudhan.jpatest.environment.JPASetup;
import com.amudhan.jpatest.environment.TransactionManagerTest;
import com.amudhan.jpatest.model.complexschemas.naturalprimarykey.User;

public class NaturalPrimaryKey extends TransactionManagerTest{

	private JPASetup jpa;
	private Logger logger = LoggerFactory.getLogger(NaturalPrimaryKey.class);
	
	@BeforeClass
	public void beforeMethod() {
		jpa = new JPASetup(DatabaseProduct.H2, "NaturalPrimaryKeyPU");
		jpa.dropSchema();
		jpa.createSchema();
	}
	
	@Test
	public void storeLoad(){
		UserTransaction tx = TRANSACTION_MANAGER.getUserTransaction();
		try{
			tx.begin();
			EntityManager entityManager = jpa.createEntityManager();
			User userOne = new User("Awesome user");
			entityManager.persist(userOne);
			tx.commit();
			entityManager.close();
			
			tx.begin();
			EntityManager entityManagerAnother = jpa.createEntityManager();
			User userTwo = entityManagerAnother.find(User.class, "Awesome user");
			assertNotNull(userTwo);
			tx.commit();
			entityManagerAnother.close();
		}catch(Exception e){
			logger.info(" Exception at storeLoad"+e);
		}finally{
			TRANSACTION_MANAGER.rollback();
		}
	}
}
