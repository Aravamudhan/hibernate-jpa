package com.amudhan.jpatest.model.fetching;

import static org.testng.Assert.assertEquals;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import javax.persistence.EntityManager;
import javax.transaction.UserTransaction;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.Test;

import com.amudhan.jpatest.environment.JPASetupTest;
import com.amudhan.jpatest.model.fetching.cartesianproduct.Bid;
import com.amudhan.jpatest.model.fetching.cartesianproduct.Item;
import com.amudhan.jpatest.model.fetching.cartesianproduct.User;
import com.amudhan.jpatest.shared.FetchTestLoadEventListener;
import com.amudhan.jpatest.shared.util.TestData;

public class CartesianProduct extends JPASetupTest {

	private static Logger logger = LoggerFactory
			.getLogger(CartesianProduct.class);
	private FetchTestLoadEventListener loadEventListener;

	@Override
	public void configurePersistenceUnit() throws Exception {
		configurePersistenceUnit("FetchingCartesianProductPU");
	}

	@Override
	public void afterJPABootstrap() throws Exception {
		loadEventListener = new FetchTestLoadEventListener(
				jpaSetup.getEntityManagerFactory());
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
        item.getImages().add("foo.jpg");
        item.getImages().add("bar.jpg");
        item.getImages().add("baz.jpg");
        em.persist(item);
        itemIds[0] = item.getId();
        for (int i = 1; i <= 3; i++) {
            Bid bid = new Bid(new BigDecimal(9 + i), item);
            item.getBids().add(bid);
            em.persist(bid);
        }

        item = new Item("Item Two", LocalDateTime.now().plusDays(1), johndoe);
        item.getImages().add("a.jpg");
        item.getImages().add("b.jpg");
        em.persist(item);
        itemIds[1] = item.getId();
        for (int i = 1; i <= 1; i++) {
            Bid bid = new Bid(new BigDecimal(2 + i), item);
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
	/* The cartesian product occurs when there are N rows
	 * in the tableOne and M rows in the tableTwo. The result
	 * would contain NM rows when there are no common attributes
	 * among them. The cartesian product problem occurs with multiple left outer
	 * joins between item, bids and images. There is a possibility of
	 * duplicate items from the SQL query result. Hibernate removes
	 * the duplicate items mostly. But this can not be avoided in
	 * the SQL queries.*/
	@Test
	public void fetchCollections() throws Exception{
		FetchTestData testData = storeTestData();
        loadEventListener.reset();
        UserTransaction tx = TRANSACTION_MANAGER.getUserTransaction();
        try{
        	tx.begin();
        	EntityManager em = jpaSetup.createEntityManager();
        	Long itemId = testData.items.getFirstId();
        	logger.info("Loading an item with eager loading");
        	Item item = em.find(Item.class, itemId);
        	assertEquals(loadEventListener.getLoadCount(Item.class), 1);
            assertEquals(loadEventListener.getLoadCount(Bid.class), 3);
        	em.detach(item);
        	assertEquals(item.getImages().size(), 3);
            assertEquals(item.getBids().size(), 3);
        	tx.commit();
        	em.close();
        }finally{
        	TRANSACTION_MANAGER.rollback();
        }
	}

}
