package com.amudhan.jpatest.model.fetching;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

import java.time.LocalDateTime;

import javax.persistence.EntityManager;
import javax.persistence.Persistence;
import javax.persistence.PersistenceUtil;
import javax.transaction.UserTransaction;

import org.hibernate.Hibernate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.Test;

import com.amudhan.jpatest.environment.JPASetupTest;
import com.amudhan.jpatest.model.fetching.interception.Item;
import com.amudhan.jpatest.model.fetching.interception.User;
import com.amudhan.jpatest.shared.util.TestData;

public class LazyInterception extends JPASetupTest {

	private static Logger logger = LoggerFactory.getLogger(LazyProxyCollection.class);

	private PersistenceUtil persistenceUtil = Persistence.getPersistenceUtil();

	@Override
	public void configurePersistenceUnit() throws Exception {
		configurePersistenceUnit("FetchingInterceptionPU");
	}

	public FetchTestData storeTestData() throws Exception {
		UserTransaction tx = TRANSACTION_MANAGER.getUserTransaction();
		tx.begin();
		EntityManager em = jpaSetup.createEntityManager();
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

		Item item = new Item("Item One", LocalDateTime.now().plusDays(1), johndoe, "Some description.");
		em.persist(item);
		itemIds[0] = item.getId();

		item = new Item("Item Two", LocalDateTime.now().plusDays(1), johndoe, "Some description.");
		em.persist(item);
		itemIds[1] = item.getId();

		item = new Item("Item Three", LocalDateTime.now().plusDays(2), janeroe, "Some description.");
		em.persist(item);
		itemIds[2] = item.getId();

		tx.commit();
		em.close();

		FetchTestData testData = new FetchTestData();
		testData.items = new TestData(itemIds);
		testData.users = new TestData(userIds);
		return testData;
	}

	@Test
	public void noUserProxy() throws Exception {
		FetchTestData testData = storeTestData();
		UserTransaction tx = TRANSACTION_MANAGER.getUserTransaction();
		Long itemId = testData.items.getFirstId();
		Long userId = testData.users.getFirstId();
		try {
			tx.begin();
			EntityManager em = jpaSetup.createEntityManager();
			/*
			 * Proxies are disabled. Fully initialized user will be returned
			 * from the database.
			 */
			{
				logger.info("Get the fully intialized entity with proxy-disabled option.");
				User user = em.getReference(User.class, userId);
				assertTrue(persistenceUtil.isLoaded(user));
				assertTrue(Hibernate.isInitialized(user));
			}
			em.clear();
			{
				logger.info("Get an item entity.");
				Item item = em.find(Item.class, itemId);
				/* This triggers a SELECT*/
				assertEquals(item.getSeller().getId(), userId);
			}
			tx.commit();
			em.close();
		} finally {
			TRANSACTION_MANAGER.rollback();
		}
	}
	
	@Test
	public void lazyBasic() throws Exception {
		FetchTestData testData = storeTestData();
		Long itemId = testData.items.getFirstId();
		UserTransaction tx = TRANSACTION_MANAGER.getUserTransaction();
		try{
			tx.begin();
			EntityManager em = jpaSetup.createEntityManager();
			logger.info("Loading item");
			Item item = em.find(Item.class, itemId);
			logger.info("Accessing description property. This is lazy loaded.");
			assertTrue(item.getDescription().length()>0);
			tx.commit();
			em.close();
		}finally{
			TRANSACTION_MANAGER.rollback();
		}
	}

}
