package com.amudhan.jpatest.model.concurrency;

import static org.testng.Assert.assertEquals;

import java.math.BigDecimal;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.Executors;

import javax.persistence.EntityManager;
import javax.persistence.LockModeType;
import javax.persistence.NoResultException;
import javax.persistence.OptimisticLockException;
import javax.transaction.UserTransaction;

import org.testng.annotations.Test;

import com.amudhan.jpatest.environment.JPASetupTest;
import com.amudhan.jpatest.model.concurrency.version.Bid;
import com.amudhan.jpatest.model.concurrency.version.Category;
import com.amudhan.jpatest.model.concurrency.version.InvalidBidException;
import com.amudhan.jpatest.model.concurrency.version.Item;
import com.amudhan.jpatest.shared.util.TestData;

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

	@SuppressWarnings({ "unchecked", "deprecation" })
	@Test(expectedExceptions = org.hibernate.OptimisticLockException.class)
	public void manualVersionChecking() throws Throwable {
		final ConcurrencyTestData testData = storeCategoriesAndItems();
		Long[] categoryIds = testData.categories.identifiers;
		UserTransaction tx = TRANSACTION_MANAGER.getUserTransaction();
		try {
			tx.begin();
			EntityManager em = jpaSetup.createEntityManager();
			BigDecimal totalPrice = new BigDecimal(0);
			/*
			 * OPTIMISTIC lock mode triggers a SELECT query at the time of
			 * flushing and compares the state of the database item values when
			 * items were loaded at first. Simply put, OPTIMISTIC LOCK mode
			 * issues a version check upon a transaction commit.
			 */
			for (Long categoryId : categoryIds) {
				List<Item> items = em
						.createQuery(
								"SELECT i from CONCURRENCY_VERSION_ITEM i where i.category.id = :catId")
						.setLockMode(LockModeType.OPTIMISTIC)
						.setParameter("catId", categoryId).getResultList();
				for (Item item : items) {
					totalPrice = totalPrice.add(item.getBuyNowPrice());
				}
				if (categoryId.equals(testData.categories.getFirstId())) {
					Executors.newSingleThreadExecutor()
							.submit(new Callable<Object>() {

								@Override
								public Object call() throws Exception {
									UserTransaction tx = TRANSACTION_MANAGER
											.getUserTransaction();
									try {
										tx.begin();
										EntityManager em = jpaSetup
												.createEntityManager();
										List<Item> items = em
												.createQuery(
														"SELECT i from CONCURRENCY_VERSION_ITEM i where i.category.id = :catId")
												.setParameter(
														"catId",
														testData.categories
																.getFirstId())
												.getResultList();
										Category lastCategory = em
												.getReference(Category.class,
														testData.categories
																.getLastId());
										items.iterator().next()
												.setCategory(lastCategory);
										tx.commit();
										em.close();
									} catch (Exception e) {
										throw new RuntimeException(
												"Concurrent operation failure "
														+ e);
									}
									return null;
								}

							}).get();
				}
			}
			tx.commit();
			em.close();
		} catch (Exception e) {
			throw unwrapCauseOfType(e, OptimisticLockException.class);
		} finally {
			TRANSACTION_MANAGER.rollback();
		}
	}

	@Test(expectedExceptions = org.hibernate.StaleObjectStateException.class)
	public void forcedIncrement() throws Throwable {
		final TestData testData = storeItemAndBids();
		Long itemIds = testData.getFirstId();
		UserTransaction tx = TRANSACTION_MANAGER.getUserTransaction();
		try {
			tx.begin();
			EntityManager em = jpaSetup.createEntityManager();
			Item item = em.find(Item.class, itemIds,
					LockModeType.OPTIMISTIC_FORCE_INCREMENT);
			Bid highestBid = queryHighestBid(em, item);
			Executors.newSingleThreadExecutor().submit(new Callable<Object>() {
				@Override
				public Object call() throws Exception {
					UserTransaction tx = TRANSACTION_MANAGER
							.getUserTransaction();
					try {
						tx.begin();
						EntityManager em = jpaSetup.createEntityManager();
						Item concurrentItem = em.find(Item.class,
								testData.getFirstId(),
								LockModeType.OPTIMISTIC_FORCE_INCREMENT);
						Bid highestBid = queryHighestBid(em, item);
						try {
                            Bid newBid = new Bid(
                                new BigDecimal("44.44"),
                                item,
                                highestBid
                            );
                            em.persist(newBid);
                        } catch (InvalidBidException ex) {
                        	ex.printStackTrace();
                        }
						em.persist(concurrentItem);
						tx.commit();
						em.close();
					} catch (Exception e) {
						TRANSACTION_MANAGER.rollback();
						throw new RuntimeException(
								"Concurrent operation failure: " + e);
					}
					return null;
				}

			}).get();
			try{
				@SuppressWarnings("unused")
				Bid newBid = new Bid(
	                    new BigDecimal("44.44"),
	                    item,
	                    highestBid
	                );
			}catch(InvalidBidException ex){
				ex.printStackTrace();
			}
			tx.commit();
			em.close();
		} catch (Exception e) {
			throw unwrapCauseOfType(e,
					org.hibernate.StaleObjectStateException.class);
		} finally {
			TRANSACTION_MANAGER.rollback();
		}
	}

	/*------------Test data here---------------*/
	class ConcurrencyTestData {
		TestData categories;
		TestData items;
	}

	public ConcurrencyTestData storeCategoriesAndItems() throws Exception {
		UserTransaction tx = TRANSACTION_MANAGER.getUserTransaction();
		ConcurrencyTestData testData = new ConcurrencyTestData();
		try {
			tx.begin();
			EntityManager em = jpaSetup.createEntityManager();
			testData.categories = new TestData(new Long[3]);
			testData.items = new TestData(new Long[5]);
			for (int i = 0; i < testData.categories.identifiers.length; i++) {
				Category category = new Category();
				category.setName("Category " + i + 1);
				em.persist(category);
				testData.categories.identifiers[i] = category.getId();
				for (int j = 0; j < testData.categories.identifiers.length; j++) {
					Item item = new Item("Item " + j + 1);
					item.setCategory(category);
					item.setBuyNowPrice(new BigDecimal(10 + j));
					em.persist(item);
					testData.items.identifiers[i + j] = item.getId();
				}
			}
			tx.commit();
			em.close();
		} finally {
			TRANSACTION_MANAGER.rollback();
		}
		return testData;
	}

	public TestData storeItemAndBids() throws Exception {
		UserTransaction tx = TRANSACTION_MANAGER.getUserTransaction();
		tx.begin();
		EntityManager em = jpaSetup.createEntityManager();
		Long[] ids = new Long[1];
		Item item = new Item("Some Item");
		em.persist(item);
		ids[0] = item.getId();
		for (int i = 0; i < 3; i++) {
			Bid bid = new Bid(new BigDecimal(10 + i), item);
			em.persist(bid);
		}
		tx.commit();
		em.close();
		return new TestData(ids);
	}

	protected Bid queryHighestBid(EntityManager em, Item item) {
		try {
			return (Bid) em
					.createQuery(
							"select b from CONCURRENCY_VERSION_BID b"
									+ " where b.item = :itm"
									+ " order by b.amount desc")
					.setParameter("itm", item).setMaxResults(1)
					.getSingleResult();
		} catch (NoResultException ex) {
			return null;
		}
	}

}
