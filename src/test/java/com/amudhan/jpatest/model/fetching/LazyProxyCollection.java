package com.amudhan.jpatest.model.fetching;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;
import static org.testng.Assert.assertNotEquals;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import javax.persistence.EntityManager;
import javax.persistence.Persistence;
import javax.persistence.PersistenceUtil;
import javax.transaction.UserTransaction;

import org.hibernate.Hibernate;
import org.hibernate.proxy.HibernateProxyHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.Test;

import com.amudhan.jpatest.environment.JPASetupTest;
import com.amudhan.jpatest.model.fetching.proxy.Bid;
import com.amudhan.jpatest.model.fetching.proxy.Category;
import com.amudhan.jpatest.model.fetching.proxy.Item;
import com.amudhan.jpatest.model.fetching.proxy.User;
import com.amudhan.jpatest.shared.util.TestData;

public class LazyProxyCollection extends JPASetupTest {

	private static Logger logger = LoggerFactory
			.getLogger(LazyProxyCollection.class);

	@Override
	public void configurePersistenceUnit() throws Exception {
		configurePersistenceUnit("FetchingProxyPU");
	}

	public FetchTestData storeTestData() throws Exception {
		UserTransaction tx = TRANSACTION_MANAGER.getUserTransaction();
		tx.begin();
		EntityManager em = jpaSetup.createEntityManager();

		Long[] categoryIds = new Long[3];
		Long[] itemIds = new Long[3];
		Long[] userIds = new Long[3];

		User johndoe = new User("johndoe");
		em.persist(johndoe);
		userIds[0] = johndoe.getId();

		User janeroe = new User("janeroe");
		em.persist(janeroe);
		userIds[1] = janeroe.getId();

		User robertdoe = new User("robertdoe");
		em.persist(robertdoe);
		userIds[2] = robertdoe.getId();

		Category category = new Category("Category One");
		em.persist(category);
		categoryIds[0] = category.getId();

		Item item = new Item("Item One", LocalDateTime.now().plusDays(1),
				johndoe);
		em.persist(item);
		itemIds[0] = item.getId();
		category.getItems().add(item);
		item.getCategories().add(category);
		for (int i = 1; i <= 3; i++) {
			Bid bid = new Bid(new BigDecimal(9 + i), item, robertdoe);
			item.getBids().add(bid);
			em.persist(bid);
		}

		category = new Category("Category Two");
		em.persist(category);
		categoryIds[1] = category.getId();

		item = new Item("Item Two", LocalDateTime.now().plusDays(1), johndoe);
		em.persist(item);
		itemIds[1] = item.getId();
		category.getItems().add(item);
		item.getCategories().add(category);
		for (int i = 1; i <= 1; i++) {
			Bid bid = new Bid(new BigDecimal(2 + i), item, janeroe);
			item.getBids().add(bid);
			em.persist(bid);
		}

		item = new Item("Item Three", LocalDateTime.now().plusDays(1), janeroe);
		em.persist(item);
		itemIds[2] = item.getId();
		category.getItems().add(item);
		item.getCategories().add(category);

		category = new Category("Category Three");
		em.persist(category);
		categoryIds[2] = category.getId();

		tx.commit();
		em.close();

		FetchTestData testData = new FetchTestData();
		testData.items = new TestData(itemIds);
		testData.users = new TestData(userIds);
		return testData;
	}

	@Test
	public void lazyEntityProxies() throws Exception {
		FetchTestData testData = storeTestData();
		UserTransaction tx = TRANSACTION_MANAGER.getUserTransaction();
		PersistenceUtil persistenceUtil = Persistence
				.getPersistenceUtil();
		try {
			tx.begin();
			EntityManager em = jpaSetup.createEntityManager();
			Long ITEM_ID = testData.items.getFirstId();
			Long USER_ID = testData.users.getFirstId();
			{
				/* Proxy is returned. No SELECT. */
				Item item = em.getReference(Item.class, ITEM_ID);
				/*
				 * Does not trigger initialization even though this is a field
				 * access. The reason is, id has been generated already and
				 * populated.
				 */
				assertEquals(item.getId(), ITEM_ID);
				/*
				 * item is a proxy and runtime generated. Not the actual Item
				 * class
				 */
				assertNotEquals(item.getClass(), Item.class);
				logger.info("The name of the item proxy's class "
						+ item.getClass().getSimpleName());
				/*
				 * The getClass**Proxy method returns the underlying class of a
				 * proxy.
				 */
				assertEquals(
						HibernateProxyHelper
								.getClassWithoutInitializingProxy(item),
						Item.class);
				assertFalse(persistenceUtil.isLoaded(item));
				assertFalse(persistenceUtil.isLoaded(item, "seller"));
				assertFalse(Hibernate.isInitialized(item));
				Hibernate.initialize(item);
			}
			em.clear();
			{
				Item item = em.find(Item.class, ITEM_ID);
				em.detach(item);
				em.detach(item.getSeller());
				assertTrue(persistenceUtil.isLoaded(item));
				assertTrue(persistenceUtil.isLoaded(item),"name");
				assertFalse(persistenceUtil.isLoaded(item, "seller"));
				/* TODO: Receving could not initialize proxy - no Session.
				 * Why initialize ? Only accessing the ID.*/
				/*assertEquals(item.getSeller().getId(), USER_ID);*/
			}
			em.clear();
			{
                // There is no SQL SELECT in this procedure, only one INSERT!
				logger.info("No SELECT statements. Proxies are created.");
                Item item = em.getReference(Item.class, ITEM_ID);
                User user = em.getReference(User.class, USER_ID);

                Bid newBid = new Bid(new BigDecimal("99.00"));
                newBid.setItem(item);
                newBid.setBidder(user);

                em.persist(newBid);
                // insert into BID values (?, ? ,? , ...)
                logger.info("Insert into the BID table.");
                em.flush();
                em.clear();
                assertEquals(em.find(Bid.class, newBid.getId()).getAmount().compareTo(new BigDecimal("99")), 0);
            }
			tx.commit();
			em.close();
		} finally {
			TRANSACTION_MANAGER.rollback();
		}
	}
}
