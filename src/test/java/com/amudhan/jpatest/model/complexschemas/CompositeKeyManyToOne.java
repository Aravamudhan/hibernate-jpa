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
import com.amudhan.jpatest.model.complexschemas.compositekey.manytoone.Item;
import com.amudhan.jpatest.model.complexschemas.compositekey.manytoone.User;
import com.amudhan.jpatest.model.complexschemas.compositekey.manytoone.UserId;

public class CompositeKeyManyToOne extends TransactionManagerTest {

	private JPASetup jpa;
	private Logger logger = LoggerFactory.getLogger(CompositeKeyReadOnly.class);

	@BeforeClass
	void configurePersistentUnit() {
		jpa = new JPASetup(DatabaseProduct.H2, "CompositeKeyManyToOne");
		jpa.dropSchema();
		jpa.createSchema();
	}
	
	@Test
	public void compositeKeyJoinColumns(){
		UserTransaction tx = TRANSACTION_MANAGER.getUserTransaction();
		try{
			tx.begin();
			EntityManager entityManager = jpa.createEntityManager();
			UserId id = new UserId("James","123");
			User seller = new User(id);
			entityManager.persist(seller);
			Item item = new Item("SellingItem");
			item.setSeller(seller);
			entityManager.persist(item);
			tx.commit();
			entityManager.close();
			
			tx.begin();
			EntityManager anotherEntityManager = jpa.createEntityManager();
			User user = anotherEntityManager.find(User.class, id);
			logger.info(user.toString());
			tx.commit();
			anotherEntityManager.close();
			
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			TRANSACTION_MANAGER.rollback();
		}
	}

}
