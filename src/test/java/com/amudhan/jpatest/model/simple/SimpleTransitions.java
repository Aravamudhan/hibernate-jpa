package com.amudhan.jpatest.model.simple;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.concurrent.Callable;
import java.util.concurrent.Executors;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceUnitUtil;
import javax.transaction.UserTransaction;

import org.hibernate.Session;
import org.hibernate.jdbc.Work;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.AfterMethod;
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
	public void beforeMethod(Method method) {
		logger.info("**************************************");
		logger.info(method.getName() + " has started");
		logger.info("**************************************");
	}

	@AfterMethod
	public void afterMethod(Method method) {
		logger.info("**************************************");
		logger.info(method.getName() + " has ended");
		logger.info("**************************************");
	}

	@Test
	public void makePersistent() throws Exception {
		UserTransaction tx = TRANSACTION_MANAGER.getUserTransaction();
		try {
			EntityManager em;
			tx.begin();
			em = jpaSetup.createEntityManager();
			Item item = new Item();
			em.persist(item);
			/*
			 * The The NAME will be set in an update rather than in the initial
			 * insert. If not null constraint is set on the NAME column, this
			 * will throw an exception.
			 */
			item.setName("ItemOne");
			Long ITEM_ID = item.getId();
			tx.commit();
			em.close();

			tx.begin();
			em = jpaSetup.createEntityManager();
			logger.info(em.find(Item.class, ITEM_ID).toString());
			tx.commit();
			em.close();

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			TRANSACTION_MANAGER.rollback();
		}
	}

	@Test
	public void retrievePersistent() throws Exception {
		UserTransaction tx = TRANSACTION_MANAGER.getUserTransaction();
		try {
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
				/*
				 * Hits the database if the item with id ITEM_ID not in the PC
				 * already.
				 */
				Item item = em.find(Item.class, ITEM_ID);
				if (item != null) {
					item.setName("New name");
				}
				tx.commit();// flush, dirty check and SQL update.
				em.close();
			}
			{
				tx.begin();
				em = jpaSetup.createEntityManager();
				Item itemA = em.find(Item.class, ITEM_ID);
				Item itemB = em.find(Item.class, ITEM_ID); // Repeatable read.
				/*
				 * All the statements below evaluate to true. itemA and itemB
				 * both hold the same reference.
				 */
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
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			TRANSACTION_MANAGER.rollback();
		}
	}

	@Test(expectedExceptions = org.hibernate.LazyInitializationException.class)
	public void retrievePersistentReference() throws Exception {
		UserTransaction tx = TRANSACTION_MANAGER.getUserTransaction();
		/*
		 * No catch to this try (i.e.) not handling any exception here. This
		 * method expects LazyinitializationException. If that is thrown, and if
		 * we that will be handled in the catch. The method will not be aware
		 * that this was actually thrown. Hence, letting TestNG handle the
		 * exception. Other option would be throw it again explicitly from the
		 * catch.
		 */
		try {
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
			/*
			 * This method does not hit the database. No SELECT statement to DB.
			 * EntityManager#getReference method always returns a proxy.
			 */
			Item itemAnother = em.getReference(Item.class, itemId);
			PersistenceUnitUtil persistenceUtil = jpaSetup
					.getEntityManagerFactory().getPersistenceUnitUtil();
			// EntityManager#getReference returns an uninitialized proxy.
			assertFalse(persistenceUtil.isLoaded(itemAnother));
			/*
			 * The proxy reference itemAnother will be initialized when it is
			 * accessed inside the transaction. By calling the
			 * Hibernate#initialize method with the proxy reference, this can be
			 * explicitly initialized.
			 */
			/* The toString method initializes the proxy. */
			// logger.info(itemAnother.toString());
			tx.commit();
			em.close();
			/*
			 * This will throw LazyInitializationException. itemAnother is just
			 * a proxy that is not currently initialized. If it is not
			 * initialized inside the transaction, accessing that proxy
			 * reference will fail.
			 */
			assertEquals(itemAnother.getName(), "ItemName");
		} finally {
			TRANSACTION_MANAGER.rollback();
		}
	}

	@Test
	public void makeTransient() throws Exception {
		UserTransaction tx = TRANSACTION_MANAGER.getUserTransaction();
		try {
			tx.begin();
			EntityManager em = jpaSetup.createEntityManager();
			Item itemOne = new Item();
			itemOne.setName("Item One");
			Item itemTwo = new Item();
			itemTwo.setName("Item Two");
			Item itemThree = new Item();
			itemThree.setName("Item Three");
			em.persist(itemOne);
			em.persist(itemTwo);
			em.persist(itemThree);
			tx.commit();
			em.close();
			Long itemOneId = itemOne.getId();
			Long itemTwoId = itemTwo.getId();
			Long itemThreeId = itemThree.getId();
			tx.begin();
			em = jpaSetup.createEntityManager();
			Item itemFull = em.find(Item.class, itemOneId);
			/* Proxy is created. */
			Item itemProxy = em.getReference(Item.class, itemTwoId);
			Item itemToPersist = em.find(Item.class, itemThreeId);
			/* Now the item moves from persistent state to the remove state. */
			/*
			 * By default, the entities are removed from the PC. But the entities
			 * retain their identifier values. This is useful in undoing the delete.
			 */
			em.remove(itemFull);
			/*
			 * Since itemProxy is a proxy, this will be initialized with a
			 * SELECT then only moved from persistent to remove. An entity
			 * instance must be fully initialized before state transitions.
			 */
			em.remove(itemProxy);
			em.remove(itemToPersist);
			/* All 3 entities are removed from the PC. */
			assertFalse(em.contains(itemFull));
			assertFalse(em.contains(itemProxy));
			assertFalse(em.contains(itemToPersist));
			/*
			 * The assertNull block should be uncommented after setting the
			 * hibernate.use_identifier_rollback property to true. Otherwise
			 * the identifiers will not be reset. The reset is useful when it
			 * is desirable to make the entities completely transient without
			 * even identifiers values that were already loaded.
			 */
			/*
			 * assertNull(itemFull.getId());
			 * assertNull(itemProxy.getId());
			 * assertNull(itemToPersist.getId());
			 */
			/* Making itemThree persistent again. */
			em.persist(itemToPersist);
			tx.commit();
			em.close();

		} finally {
			TRANSACTION_MANAGER.rollback();
		}
	}
	/* This is useful in multithreaded environments.
	 * If there is a chance that certain values in the PC must always
	 * be in synchrony with the database in a concurrent environment,
	 * refresh must be called. Calling refresh on an entity triggers
	 * SELECT and any changes made to that entity in the transaction
	 * that is not flushed, will be overwritten. */
	@Test
	public void refresh() throws Exception {
		UserTransaction tx = TRANSACTION_MANAGER.getUserTransaction();
		try{
			tx.begin();
			EntityManager em = jpaSetup.createEntityManager();
			Item someItem = new Item();
			someItem.setName("Some item");
			em.persist(someItem);
			tx.commit();
			em.close();
			Long someItemId = someItem.getId();
			tx.begin();
			em = jpaSetup.createEntityManager();
			Item item = em.find(Item.class, someItemId);
			item.setName("Some name");
			
			// Someone updates this row in the database!
            Executors.newSingleThreadExecutor().submit(new Callable<Object>() {
                @Override
                public Object call() throws Exception {
                    UserTransaction tx = TRANSACTION_MANAGER.getUserTransaction();
                    try {
                        tx.begin();
                        EntityManager em = jpaSetup.createEntityManager();

                        Session session = em.unwrap(Session.class);
                        session.doWork(new Work() {
                            @Override
                            public void execute(Connection con) throws SQLException {
                                PreparedStatement ps = con.prepareStatement("update SIMPLE_ITEM set name = ? where ID = ?");
                                ps.setString(1, "Concurrent Update item name");
                                ps.setLong(2, someItemId);

                                /* Alternative: you get an EntityNotFoundException on refresh
                                PreparedStatement ps = con.prepareStatement("delete from ITEM where ID = ?");
                                ps.setLong(1, ITEM_ID);
                                */

                                if (ps.executeUpdate() != 1)
                                    throw new SQLException("ITEM row was not updated");
                            }
                        });

                        tx.commit();
                        em.close();

                    } catch (Exception ex) {
                    	TRANSACTION_MANAGER.rollback();
                        throw new RuntimeException("Concurrent operation failure: " + ex, ex);
                    }
                    return null;
                }
            }).get();
            String oldName = item.getName();
            em.refresh(item);
            assertNotEquals(item.getName(), oldName);
            assertEquals(item.getName(), "Concurrent Update item name");
			tx.commit();
			em.close();
			
		}finally{
			TRANSACTION_MANAGER.rollback();
		}
	}

}
