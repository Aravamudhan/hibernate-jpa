package com.amudhan.jpatest.model.fetching;

import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;
import static org.testng.Assert.assertNotNull;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import javax.persistence.EntityManager;
import javax.transaction.UserTransaction;

import org.hibernate.Hibernate;
import org.hibernate.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.Test;

import com.amudhan.jpatest.environment.JPASetupTest;
import com.amudhan.jpatest.model.fetching.profile.Bid;
import com.amudhan.jpatest.model.fetching.profile.Item;
import com.amudhan.jpatest.model.fetching.profile.User;
import com.amudhan.jpatest.shared.util.TestData;

public class Profile extends JPASetupTest {
	
	private static Logger logger = LoggerFactory
			.getLogger(Profile.class);

	@Override
	public void configurePersistenceUnit() throws Exception {
		configurePersistenceUnit("FetchingProfilePU");
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

	@Test
	public void fetchWithProfile() throws Exception{
		FetchTestData testData = storeTestData();
		UserTransaction tx = TRANSACTION_MANAGER.getUserTransaction();
		try{
			tx.begin();
			EntityManager em = jpaSetup.createEntityManager();
			Long ITEM_ID = testData.items.getFirstId();
			logger.info("Loading an item with out the fetch profile active.");
			/* Item#seller and Item#bids are lazy loaded.*/
			Item item = em.find(Item.class, ITEM_ID);
            assertFalse(Hibernate.isInitialized(item.getSeller()));
            em.clear();
            
            logger.info("Load item with JOIN_SELLER profile.");
            /* Enabling the profile PROFILE_JOIN_SELLER for this transaction.*/
            em.unwrap(Session.class).enableFetchProfile(Item.PROFILE_JOIN_SELLER);
            item = em.find(Item.class, ITEM_ID);
            em.clear();
            assertNotNull(item.getSeller().getUsername());
            em.clear();
            
            logger.info("Load item with JOIN_BIDS profile.");
            /* Enabling the profile PROFILE_JOIN_BIDS. Now two profiles are active.
             * Item#seller and Item#bids both are fetched according to their profiles.*/
            em.unwrap(Session.class).enableFetchProfile(Item.PROFILE_JOIN_BIDS);
            item = em.find(Item.class, ITEM_ID);
            em.clear();
            assertNotNull(item.getSeller().getUsername());
            assertTrue(item.getBids().size() > 0);
            
			tx.commit();
			em.close();
		}finally{
			TRANSACTION_MANAGER.rollback();
		}
	}

}
