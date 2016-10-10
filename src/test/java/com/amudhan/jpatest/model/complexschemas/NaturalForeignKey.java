package com.amudhan.jpatest.model.complexschemas;

import static org.testng.Assert.assertNotNull;

import javax.persistence.EntityManager;
import javax.transaction.UserTransaction;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.amudhan.jpatest.environment.DatabaseProduct;
import com.amudhan.jpatest.environment.JPASetup;
import com.amudhan.jpatest.environment.TransactionManagerTest;
import com.amudhan.jpatest.model.complexschemas.naturalforeignkey.Item;
import com.amudhan.jpatest.model.complexschemas.naturalforeignkey.User;

public class NaturalForeignKey extends TransactionManagerTest {

	private JPASetup jpa;
	
	@BeforeClass
	public void beforeMethod() {
		jpa = new JPASetup(DatabaseProduct.H2, "NaturalForeignKeyPU");
		jpa.dropSchema();
		jpa.createSchema();
	}
	
	@Test
	public void storeLoad(){
		UserTransaction tx = TRANSACTION_MANAGER.getUserTransaction();
		try{
			tx.begin();
			EntityManager entityManager = jpa.createEntityManager();
			User userOne = new User("1234");
			entityManager.persist(userOne);
			Item itemOne = new Item("GreatItem");
			itemOne.setSeller(userOne);
			entityManager.persist(itemOne);
			tx.commit();
			entityManager.close();
			
			tx.begin();
			EntityManager entityManagerAnother = jpa.createEntityManager();
			User userTwo = entityManagerAnother.find(User.class, userOne.getId());
			assertNotNull(userTwo);
			/* Once an entity is returned by running a query, that entity becomes
			 * part of the persistent context. What ever changes made to that entity
			 * in the transaction is synchronized with the database during flush or 
			 * when the transction ends.*/
			Item itemTwo = (Item)entityManagerAnother.
					createQuery("select i from COMPLEXSCHEMAS_NATURALFOREIGNKEY_ITEM i where i.seller = :s").
					setParameter("s", userTwo).getSingleResult();
			itemTwo.setName("Greatest of items");
			assertNotNull(itemTwo);
			tx.commit();
			entityManagerAnother.close();
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			TRANSACTION_MANAGER.rollback();
		}
	}

}
