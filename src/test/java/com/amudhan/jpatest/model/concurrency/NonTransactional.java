package com.amudhan.jpatest.model.concurrency;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;

import java.math.BigDecimal;

import javax.persistence.EntityManager;
import javax.transaction.UserTransaction;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.Test;

import com.amudhan.jpatest.environment.JPASetupTest;
import com.amudhan.jpatest.model.concurrency.version.Item;

public class NonTransactional extends JPASetupTest {

	private static Logger logger = LoggerFactory.getLogger(NonTransactional.class);
	
	@Override
	public void configurePersistenceUnit() throws Exception {
		configurePersistenceUnit("ConcurrencyVersioningPU");
	}

	@Test
	public void autoCommit() throws Exception{
		UserTransaction tx = TRANSACTION_MANAGER.getUserTransaction();
		Long itemId;
		try{
			logger.info("Creating an item");
			tx.begin();
			EntityManager em = jpaSetup.createEntityManager();
			Item item = new Item("Original name");
			item.setBuyNowPrice(new BigDecimal(50));
			em.persist(item);
			tx.commit();
			itemId = item.getId();
			em.close();
		}finally{
			TRANSACTION_MANAGER.rollback();
		}
		{
			logger.info("EntityManager in an unsynchronized persistent context.");
			/* EM is created outside transactions. The PC is in the unsynchronized mode.
			 * The automatic flushing does not happen.*/
			EntityManager em = jpaSetup.createEntityManager();
			logger.info("find an item with the id "+itemId);
			Item item = em.find(Item.class, itemId);
			item.setName("New name");
			/* No flushing here even though a query is executed. This returns a scalar value (i.e.) single
			 * value, this is returned from the database.*/
			logger.info("loading item name using a query");
			assertEquals(em.createQuery("SELECT i.name from CONCURRENCY_VERSION_ITEM i where i.id = :itemId").
						setParameter("itemId", itemId).
						getSingleResult(),"Original name");
			/* The entity is retrieved from the PC.*/
			logger.info("loading an entity using a query.");
			assertEquals(((Item)em.createQuery("SELECT i from CONCURRENCY_VERSION_ITEM i where i.id = :itemId").
					setParameter("itemId", itemId).
					getSingleResult()).getName(),
					"New name");
			/* Any scalar result is returned from the database. Only managed entities are returned from the PC*/
			item.setBuyNowPrice(new BigDecimal(100));
			BigDecimal itemPrice = (BigDecimal)em.createQuery("SELECT i.buyNowPrice from CONCURRENCY_VERSION_ITEM i where i.id = :itemId").
					setParameter("itemId", itemId).
					getSingleResult();
			assertEquals(itemPrice.stripTrailingZeros(), new BigDecimal(50).stripTrailingZeros());
			/* The flushing throws TransactionRequired exception.
			 * If the flushing fails, roll back must be called. Since
			 * there are no transactions, in the unsynchronized mode, updates
			 * can not be performed.
			 * em.flush();
			 * */
			/* PC is refreshed. The entity is loaded from the database.*/
			em.refresh(item);
			assertEquals(item.getName(), "Original name");
			em.close();
		}
		{
			logger.info("Joining EntityManager in a transaction.");
			EntityManager em = jpaSetup.createEntityManager();
			Item item = new Item("New item");
			/* This makes the item entity persistent in the unsynchronized
			 * persistent context. This generates an id by calling a sequence
			 * from the database. This does not insert, as expected, since no flushing
			 * happens on calling persist.*/
			em.persist(item);
			assertNotNull(item.getId());
			tx.begin();
			if(!em.isJoinedToTransaction()){
				em.joinTransaction();
			}
			/* flush is called.*/
			tx.commit();
			em.close();
		}
		
	}
}
