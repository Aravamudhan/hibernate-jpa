package com.amudhan.jpatest.model.concurrency;

import static org.testng.Assert.assertEquals;

import java.util.concurrent.Callable;
import java.util.concurrent.Executors;

import javax.persistence.EntityManager;
import javax.persistence.OptimisticLockException;
import javax.transaction.UserTransaction;

import org.testng.annotations.Test;

import com.amudhan.jpatest.environment.JPASetupTest;
import com.amudhan.jpatest.model.concurrency.version.Item;

public class Versioning extends JPASetupTest {

	@Override
	public void configurePersistenceUnit() throws Exception {
		configurePersistenceUnit("ConcurrencyVersioningPU");
	}

	/*
	 * Say two users are executing an unit of work on an item at the same time.
	 * 1st user makes changes, flushes, updating the version number to 1. If the
	 * 2nd user tries to flush his changes, his transaction would throw an
	 * exception. Because that transaction would not be able to find an item in
	 * version 0, which was the case when the transaction started. Hibernate
	 * updates the version number of an entity, when it finds the entity dirty
	 * (i.e.) its values be it simple types or complex types, have changed.
	 */
	/*
	 * If several applications share a single database without all using the
	 * versioning algorithm of Hibernate, concurrency problems will arise. In
	 * that case, this method should be avoided.
	 */
	@Test(expectedExceptions = OptimisticLockException.class)
	public void firstCommitWins() throws Throwable {
		UserTransaction tx = TRANSACTION_MANAGER.getUserTransaction();
		try {
			tx.begin();
			EntityManager em = jpaSetup.createEntityManager();
			/* When the item is created it's version value is 0. */
			Item someItem = new Item("First item name");
			em.persist(someItem);
			tx.commit();
			em.close();
			final Long itemId = someItem.getId();

			tx.begin();
			em = jpaSetup.createEntityManager();
			Item item = em.find(Item.class, itemId);
			assertEquals(item.getVersion(), 0);
			item.setName("New name");
			/* Concurrently creating an another transaction. */
			Executors.newSingleThreadExecutor().submit(new Callable<Object>() {
				@Override
				public Object call() throws Exception {
					UserTransaction tx = TRANSACTION_MANAGER
							.getUserTransaction();
					try {
						tx.begin();
						EntityManager em = jpaSetup.createEntityManager();
						Item concurrentItem = em.find(Item.class, itemId);
						assertEquals(concurrentItem.getVersion(), 0);
						concurrentItem.setName("Second item name");
						em.persist(concurrentItem);
						/*
						 * PC is flushed and version is updated to 1. UPDATE
						 * ITEM set NAME = ?, VERSION = 1 where ID = ? and
						 * VERSION = 0. This will succeed. Now the item with the
						 * id 'itemId' and VERSION = 1 is in the database.
						 */
						tx.commit();
						em.close();
					} catch (Exception e) {
						/* This should not happen. This commit should win. */
						TRANSACTION_MANAGER.rollback();
						throw new RuntimeException(
								"Concurrent operation failure: " + e);
					}
					return null;
				}

			}).get();
			/*
			 * Here the PC is flushed. It will identify that the item is dirty.
			 * It will update the version number and trigger a query for an item
			 * with the id 'itemId' and version = 0. But there is no item in
			 * that version, since the item with the id 'itemId' and version =
			 * 0, is now updated to version = 1.
			 */
			/*
			 * UPDATE ITEM set NAME = ?, VERSION = 1 where ID = ? and VERSION =
			 * 0
			 */
			tx.commit();
			em.close();

		} catch (Exception ex) {
			throw unwrapCauseOfType(ex, OptimisticLockException.class);
		} finally {
			TRANSACTION_MANAGER.rollback();
		}
	}

}
