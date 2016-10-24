package com.amudhan.jpatest.model.fetching;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import javax.persistence.EntityManager;
import javax.transaction.UserTransaction;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.Test;

import com.amudhan.jpatest.environment.JPASetupTest;
import com.amudhan.jpatest.model.fetching.eagerselect.Bid;
import com.amudhan.jpatest.model.fetching.eagerselect.Item;
import com.amudhan.jpatest.model.fetching.eagerselect.User;
import com.amudhan.jpatest.shared.util.TestData;

public class EagerSelect extends JPASetupTest{

	private static Logger logger = LoggerFactory
			.getLogger(EagerSelect.class);

	@Override
	public void configurePersistenceUnit() throws Exception {
		configurePersistenceUnit("FetchingEagerSelectPU");
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

        Item item = new Item("Item One", LocalDateTime.now().plusDays(1), johndoe);
        em.persist(item);
        itemIds[0] = item.getId();
        for (int i = 1; i <= 3; i++) {
        	Bid bid = new Bid(item, robertdoe, new BigDecimal(9 + i));
            item.getBids().add(bid);
            em.persist(bid);
        }

        item = new Item("Item Two", LocalDateTime.now().plusDays(1), johndoe);
        em.persist(item);
        itemIds[1] = item.getId();
        for (int i = 1; i <= 1; i++) {
        	Bid bid = new Bid(item, janeroe, new BigDecimal(2 + i));
            item.getBids().add(bid);
            em.persist(bid);
        }

        item = new Item("Item Three", LocalDateTime.now().plusDays(2), janeroe);
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
    public void fetchEagerSelect() throws Exception {
		FetchTestData testData = storeTestData();
        UserTransaction tx = TRANSACTION_MANAGER.getUserTransaction();
        try{
        	tx.begin();
        	EntityManager em = jpaSetup.createEntityManager();
        	Long itemId = testData.items.getFirstId();
        	logger.info("Loading an item with eager select");
        	logger.info("Loading Item#seller and Item#bids with separate SELECT.");
        	/*This loads Item#seller and Item#bids with separate SELECT statements instead of joins*/
        	Item item = em.find(Item.class, itemId);
        	logger.info("Detaching the item");
        	em.detach(item);
        	/* Though the item alone is loaded separately, seller and bids are
        	 * not loaded lazily. This is verified by checking their statuses
        	 * in the detached state of the item.*/
            assertEquals(item.getBids().size(), 3);
            assertNotNull(item.getBids().iterator().next().getAmount());
            assertEquals(item.getSeller().getUsername(), "johndoe");
        	tx.commit();
        	em.close();
        }finally{
        	TRANSACTION_MANAGER.rollback();
        }
	}

}
