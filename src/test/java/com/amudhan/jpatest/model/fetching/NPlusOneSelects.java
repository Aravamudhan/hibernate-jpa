package com.amudhan.jpatest.model.fetching;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertTrue;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import javax.persistence.EntityManager;
import javax.transaction.UserTransaction;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.Test;

import com.amudhan.jpatest.environment.JPASetupTest;
import com.amudhan.jpatest.model.fetching.nplusoneselects.Bid;
import com.amudhan.jpatest.model.fetching.nplusoneselects.Item;
import com.amudhan.jpatest.model.fetching.nplusoneselects.User;
import com.amudhan.jpatest.shared.FetchTestLoadEventListener;
import com.amudhan.jpatest.shared.util.TestData;

public class NPlusOneSelects extends JPASetupTest{

	private static Logger logger = LoggerFactory
			.getLogger(NPlusOneSelects.class);
	private FetchTestLoadEventListener loadEventListener;
	
	@Override
    public void configurePersistenceUnit() throws Exception {
        configurePersistenceUnit("FetchingNPlusOneSelectsPU");
    }
	
	@Override
    public void afterJPABootstrap() throws Exception {
        loadEventListener = new FetchTestLoadEventListener(jpaSetup.getEntityManagerFactory());
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

		Item item = new Item("Item One", LocalDateTime.now().plusDays(1),
				johndoe);
		em.persist(item);
		itemIds[0] = item.getId();
		for (int i = 1; i <= 3; i++) {
			Bid bid = new Bid(new BigDecimal(9 + i), item, robertdoe);
			item.getBids().add(bid);
			em.persist(bid);
		}

		item = new Item("Item Two", LocalDateTime.now().plusDays(1), johndoe);
		em.persist(item);
		itemIds[1] = item.getId();
		for (int i = 1; i <= 1; i++) {
			Bid bid = new Bid(new BigDecimal(2 + i), item, janeroe);
			item.getBids().add(bid);
			em.persist(bid);
		}

		item = new Item("Item Three", LocalDateTime.now().plusDays(1), janeroe);
		em.persist(item);
		itemIds[2] = item.getId();
		for (int i = 1; i <= 1; i++) {
            Bid bid = new Bid(new BigDecimal(3 + i), item, johndoe);
            item.getBids().add(bid);
            em.persist(bid);
        }
		
		tx.commit();
		em.close();

		FetchTestData testData = new FetchTestData();
		testData.items = new TestData(itemIds);
		testData.users = new TestData(userIds);
		return testData;
	}

	/* This demonstrates n+1 selection problem.
	 * Here there are 3 items. All 3 items are loaded with one SELECT.
	 * When loading seller of each item, for each seller, there will be
	 * a SELECT. If there are 100 items, and every one has a seller,
	 * there will be 101 SELECT statements. 1 for 100 items and 100 for
	 * 100 sellers. This is inefficient strategy. This happens with lazily
	 * loaded collections.*/
	@Test
	public void fetchUser() throws Exception{
		storeTestData();
		loadEventListener.reset();
		UserTransaction tx = TRANSACTION_MANAGER.getUserTransaction();
		try{
			tx.begin();
			EntityManager em = jpaSetup.createEntityManager();
			logger.info("Loading item");
			@SuppressWarnings("unchecked")
			List<Item> items = em.createQuery("select i from FETCHING_NPLUSONESELECTS_ITEM i").getResultList();
			assertEquals(loadEventListener.getLoadCount(Item.class), 3);
            assertEquals(loadEventListener.getLoadCount(User.class), 0);
            for (Item item : items) {
                // Each seller has to be loaded with an additional SELECT
            	logger.info("Loading Item#seller");
                assertNotNull(item.getSeller().getUserName());
                // select * from USERS where ID = ?
            }
            assertEquals(loadEventListener.getLoadCount(User.class), 2);
			tx.commit();
			em.close();
		}finally{
			TRANSACTION_MANAGER.rollback();
		}
	}
	
	@Test
	public void fetchBids() throws Exception{
		storeTestData();
		loadEventListener.reset();
		UserTransaction tx = TRANSACTION_MANAGER.getUserTransaction();
		try{
			tx.begin();
			EntityManager em = jpaSetup.createEntityManager();
			logger.info("Loading item");
			@SuppressWarnings("unchecked")
			List<Item> items = em.createQuery("select i from FETCHING_NPLUSONESELECTS_ITEM i").getResultList();
			assertEquals(loadEventListener.getLoadCount(Item.class), 3);
            assertEquals(loadEventListener.getLoadCount(User.class), 0);
            for (Item item : items) {
            	logger.info("Checking the size of each Item#bids");
            	logger.info("Loading Item#bids");
            	assertTrue(item.getBids().size() > 0);
            }
            assertEquals(loadEventListener.getLoadCount(Bid.class), 5);
			tx.commit();
			em.close();
		}finally{
			TRANSACTION_MANAGER.rollback();
		}
	}
	
	
}
