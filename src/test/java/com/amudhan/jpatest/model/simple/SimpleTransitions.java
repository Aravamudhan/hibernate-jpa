package com.amudhan.jpatest.model.simple;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.time.LocalDateTime;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceUnitUtil;
import javax.transaction.UserTransaction;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.amudhan.jpatest.environment.JPASetupTest;

public class SimpleTransitions extends JPASetupTest {

	private Logger logger = LoggerFactory.getLogger(SimpleTransitions.class);
	
	@Override
    public void configurePersistenceUnit() throws Exception {
        configurePersistenceUnit("SimplePU");
    }
	
	@BeforeMethod
	public void beforeMethod(Method method){
		logger.info("**************************************");
		logger.info(method.getName());
		logger.info("**************************************");
	}
	
	@Test
	public void makePersistent() throws Exception{
		UserTransaction tx = TRANSACTION_MANAGER.getUserTransaction();
		try{
			EntityManager em;
			tx.begin();
			em = jpaSetup.createEntityManager();
			Item item = new Item();
			em.persist(item);
			/* The The NAME will be set in an update rather than in the initial insert.
			 * If not null constraint is set on the NAME column, this will throw
			 * an exception.*/
			item.setName("ItemOne");
			Long ITEM_ID = item.getId();
			tx.commit();
			em.close();
			
			tx.begin();
			em = jpaSetup.createEntityManager();
			logger.info(em.find(Item.class, ITEM_ID).toString());
			tx.commit();
			em.close();
			
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			TRANSACTION_MANAGER.rollback();
		}
	}
	
	@Test
	public void retrievePersistent() throws Exception{
		UserTransaction tx = TRANSACTION_MANAGER.getUserTransaction();
		try{
			tx.begin();
			EntityManager em = jpaSetup.createEntityManager();
			Item someItem = new Item();
			someItem.setName("Some Item");
			em.persist(someItem);
			tx.commit();
			em.close();
			Long ITEM_ID = someItem.getId();
			{
				tx.begin();
				em = jpaSetup.createEntityManager();
				//Hits the database if the item with id ITEM_ID not in the PC already.
				Item item = em.find(Item.class, ITEM_ID);
				if(item != null){
					item.setName("New name");
				}
				tx.commit();//flush, dirty check and SQL update.
				em.close();
			}
			{
				tx.begin();
				em = jpaSetup.createEntityManager();
				Item itemA = em.find(Item.class, ITEM_ID);
				Item itemB = em.find(Item.class, ITEM_ID); //Repeatable read.
				/* All the statements below evaluate to true.
				 * itemA and itemB both hold the same reference.*/
				assertTrue(itemA == itemB);
				assertTrue(itemA.equals(itemB));
				assertTrue(itemA.getId().equals(itemB.getId()));
				tx.commit();
				em.close();
			}
			tx.begin();
			em = jpaSetup.createEntityManager();
			assertEquals(em.find(Item.class, ITEM_ID).getName(), "New name");
			tx.commit();
			em.close();
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			TRANSACTION_MANAGER.rollback();
		}
	}
	
	@Test(expectedExceptions = org.hibernate.LazyInitializationException.class)
	public void retrievePersistentReference() throws Exception{
		UserTransaction tx = TRANSACTION_MANAGER.getUserTransaction();
		try{
			tx.begin();
			EntityManager em = jpaSetup.createEntityManager();
			Item item = new Item();
			item.setName("ItemName");
			item.setAuctionEnd(LocalDateTime.now().plusDays(2));
			item.setBuyNowPrice(new BigDecimal(1000));
			em.persist(item);
			tx.commit();
			em.close();
			
			long itemId = item.getId();
			
			tx.begin();
			em = jpaSetup.createEntityManager();
			/* This method does not hit the database. 
			 * EntityManager#getReference method always returns a proxy.*/
			Item itemAnother = em.getReference(Item.class, itemId);
			PersistenceUnitUtil persistenceUtil = 
					jpaSetup.getEntityManagerFactory().getPersistenceUnitUtil();
			//EntityManager#getReference returns an uninitialized proxy.
			assertFalse(persistenceUtil.isLoaded(itemAnother));
			/* The proxy reference itemAnother will be initialized
			 * when it is accessed inside the transaction.
			 * By calling the Hibernate#initialize method with the proxy reference,
			 * this can be explicitly initialized.*/
			/* The toString method initializes the proxy.*/
			//logger.info(itemAnother.toString());
			tx.commit();
			em.close();
			/* This will throw LazyInitializationException. itemAnother is just a proxy
			 * that is not currently initialized. If it is not initialized inside the transaction,
			 * accessing that proxy reference will fail.*/
			assertEquals(itemAnother.getName(),"ItemName");
		}finally{
			TRANSACTION_MANAGER.rollback();
		}
	}
	
}
