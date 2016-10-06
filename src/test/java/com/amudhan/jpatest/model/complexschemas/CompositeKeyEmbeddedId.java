package com.amudhan.jpatest.model.complexschemas;

import javax.persistence.EntityManager;
import javax.transaction.UserTransaction;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.amudhan.jpatest.environment.DatabaseProduct;
import com.amudhan.jpatest.environment.JPASetup;
import com.amudhan.jpatest.environment.TransactionManagerTest;
import com.amudhan.jpatest.model.complexschemas.compositekey.embedded.User;
import com.amudhan.jpatest.model.complexschemas.compositekey.embedded.UserId;

public class CompositeKeyEmbeddedId extends TransactionManagerTest{
	
	private Logger logger = LoggerFactory.getLogger(CompositeKeyEmbeddedId.class);
	private JPASetup jpa;
	
	@BeforeClass
	public void beforeMethod() {
		jpa = new JPASetup(DatabaseProduct.H2, "CompositeKeyEmbeddedId");
		jpa.dropSchema();
		jpa.createSchema();
	}
	
	@Test
	public void storeLoadCompositeKeyEmbeddedId() throws Exception {
		UserTransaction tx = TRANSACTION_MANAGER.getUserTransaction();
		try{
			tx.begin();
			EntityManager entityManager = jpa.createEntityManager();
			User user = new User(new UserId("Awesome User","DEPT1234"));
			entityManager.persist(user);
			entityManager.flush();
			tx.commit();
			entityManager.close();
			
			tx.begin();
			EntityManager entityManagerAnother = jpa.createEntityManager();
			User userAnother = entityManagerAnother.find(User.class, user.getId());
			logger.info(userAnother.toString());
			tx.commit();
			entityManagerAnother.close();
			
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			TRANSACTION_MANAGER.rollback();
		}
	}

}
