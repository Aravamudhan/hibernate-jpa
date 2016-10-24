package com.amudhan.jpatest.model.fetching;


import static org.testng.Assert.assertNotNull;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import javax.persistence.EntityManager;
import javax.transaction.UserTransaction;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.Test;

import com.amudhan.jpatest.environment.JPASetupTest;
import com.amudhan.jpatest.model.fetching.batch.Bid;
import com.amudhan.jpatest.model.fetching.batch.Item;
import com.amudhan.jpatest.model.fetching.batch.User;
import com.amudhan.jpatest.shared.util.TestData;

public class Batch extends JPASetupTest {

	private static Logger logger = LoggerFactory
			.getLogger(Batch.class);

	@Override
	public void configurePersistenceUnit() throws Exception {
		configurePersistenceUnit("FetchingBatchPU");
	}
	
	public FetchTestData storeTestData() throws Exception{
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
        
        item = new Item("Item Two", LocalDateTime.now().plusDays(1), robertdoe);
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
        for (int i = 1; i <= 1; i++) {
            Bid bid = new Bid(item, johndoe, new BigDecimal(3 + i));
            item.getBids().add(bid);
            em.persist(bid);
        }
        /* These 3 users are not included in the batch query in the 
         * fetchProxyBatches test since they are not added to the items.
         * Only the proxies available in the persistent context are
         * initialized with the batch queries.*/
        User johnydoe = new User("johnydoe");
        User jimdoe = new User("jimdoe");
        User rosettadoe = new User("rosettadoe");
        em.persist(johnydoe);
        em.persist(jimdoe);
        em.persist(rosettadoe);
        
        tx.commit();
        em.close();
        
		FetchTestData testData = new FetchTestData();
        testData.items = new TestData(itemIds);
        testData.users = new TestData(userIds);
        return testData;
	}
	
	/* Batch fetching is an optimization technique to reduce the 
	 * number of calls to the database. This helps mitigate n+1 problem.
	 * Although this might consume more memory the lesser 
	 * number of trips to the database would make a huge difference. 
	 * The batch fetching algorithm is a blind guess in a sense 
	 * that we would not usually know how many proxies are created 
	 * the PC during a transaction.*/
	@Test
    public void fetchProxyBatches() throws Exception {
        storeTestData();
        UserTransaction tx = TRANSACTION_MANAGER.getUserTransaction();
        try{
        	tx.begin();
        	EntityManager em = jpaSetup.createEntityManager();
        	logger.info("Loading items");
        	@SuppressWarnings({"unchecked" })
			List<Item> items = em.createQuery("select i from FETCHING_BATCH_ITEM i").
										getResultList();
        	logger.info("Loading users in batch.");
        	/* Calling getUserName on the 1st seller proxy of the 1st item initializes
        	 * not just that seller but also other seller proxies. The limit is 10,
        	 * but since only 3 proxies are available in the persistent context, 
        	 * all 3 will be initialized.*/
        	for (Item item : items) {
        		logger.info("Calling getUserName");
                assertNotNull(item.getSeller().getUserName());
            }
        	tx.commit();
        	em.close();
        }finally{
        	TRANSACTION_MANAGER.rollback();
        }
        
	}
	
	@Test
    public void fetchCollectionBatches() throws Exception {
		storeTestData();
        UserTransaction tx = TRANSACTION_MANAGER.getUserTransaction();
        try{
        	tx.begin();
        	EntityManager em = jpaSetup.createEntityManager();
        	logger.info("Loading items");
        	@SuppressWarnings({"unchecked" })
			List<Item> items = em.createQuery("select i from FETCHING_BATCH_ITEM i").
										getResultList();
        	logger.info("Loading bids in batch.");
        	/* Checking the size of first Item#bids loads all bids of
        	 * all other items loaded as proxies in the PC.*/
        	for (Item item : items) {
        		logger.info("Checking the size of bids.");
                assertNotNull(item.getBids().size()>0);
            }
        	tx.commit();
        	em.close();
        }finally{
        	TRANSACTION_MANAGER.rollback();
        }
	}

}
