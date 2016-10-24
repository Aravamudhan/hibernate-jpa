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
import static org.testng.Assert.assertTrue;

import com.amudhan.jpatest.environment.JPASetupTest;
import com.amudhan.jpatest.model.fetching.subselect.Bid;
import com.amudhan.jpatest.model.fetching.subselect.Item;
import com.amudhan.jpatest.model.fetching.subselect.User;
import com.amudhan.jpatest.shared.util.TestData;

public class SubSelect extends JPASetupTest {

	private static Logger logger = LoggerFactory
			.getLogger(CartesianProduct.class);

	@Override
	public void configurePersistenceUnit() throws Exception {
		configurePersistenceUnit("FetchingSubselectPU");
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

	/* */
	@SuppressWarnings("unchecked")
	/* In the subselect mode hibernate uses the 1st query that was
	 * executed to retrieve the parent entities to load all the dependent
	 * entities. In the batch, we randomly give a number to initialize certain number
	 * of entities(proxies/lazy collections), but in the subselect it is not random.
	 * If 3 items are loaded, the same query is used to load all the Item#bids related
	 * to those 3 items. This helps mitigate n+1 problem in better way than batch select.*/
	@Test
    public void fetchCollectionSubselect() throws Exception {
        storeTestData();
        UserTransaction tx = TRANSACTION_MANAGER.getUserTransaction();
        try{
        	tx.begin();
        	EntityManager em = jpaSetup.createEntityManager();
        	logger.info("Loading items");
        	/* This query is used to load Item#bids.*/
			List<Item> items = em.createQuery("select i from FETCHING_SUBSELECT_ITEM i").
										getResultList();
        	logger.info("Loading bids with subselect.");
        	/* SELECT * FROM BIDS WHERE ITEM_ID IN( SELECT ID FROM ITEM). 
        	 * If any specific item had been loaded, this generated query in the "IN"
        	 * clause would be modified to SELECT ID FROM ITEM WHERE ID = :ITEM_ID*/
        	for (Item item : items) {
        		logger.info("Checking the size of bids.");
                assertNotNull(item.getBids().size()>0);
            }
        	em.clear();
        	items = em.createQuery("select i from FETCHING_SUBSELECT_ITEM i").
					getResultList();
        	logger.info("Checking the Item#bids's size of the 1st item.");
        	/* Only accessing the 1st Item#bids but it loads all the available
        	 * bids in the database.*/
        	assertTrue(items.iterator().next().getBids().size() > 0);
        	tx.commit();
        	em.close();
        }finally{
        	TRANSACTION_MANAGER.rollback();
        }
        
	}
}
