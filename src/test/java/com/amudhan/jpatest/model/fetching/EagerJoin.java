package com.amudhan.jpatest.model.fetching;

import static org.testng.Assert.assertTrue;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import javax.persistence.EntityManager;
import javax.persistence.Persistence;
import javax.persistence.PersistenceUtil;
import javax.transaction.UserTransaction;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.Test;

import com.amudhan.jpatest.environment.JPASetupTest;
import com.amudhan.jpatest.model.fetching.eagerjoin.Bid;
import com.amudhan.jpatest.model.fetching.eagerjoin.Item;
import com.amudhan.jpatest.model.fetching.eagerjoin.User;
import com.amudhan.jpatest.shared.util.TestData;

public class EagerJoin extends JPASetupTest {

	private PersistenceUtil persistenceUtil = Persistence.getPersistenceUtil();
	private static Logger logger = LoggerFactory.getLogger(EagerJoin.class);

	@Override
	public void configurePersistenceUnit() throws Exception {
		configurePersistenceUnit("FetchingEagerJoinPU");
	}
	
	public FetchTestData storeTestData() throws Exception {
        UserTransaction tx = TRANSACTION_MANAGER.getUserTransaction();
        tx.begin();
        EntityManager em = jpaSetup.createEntityManager();

        Long[] itemIds = new Long[1];
        Long[] userIds = new Long[2];

        User johndoe = new User("johndoe");
        em.persist(johndoe);
        userIds[0] = johndoe.getId();

        User janeroe = new User("janeroe");
        em.persist(janeroe);
        userIds[1] = janeroe.getId();

        Item item = new Item("Item One", LocalDateTime.now().plusDays(1), johndoe);
        em.persist(item);
        itemIds[0] = item.getId();
        for (int i = 1; i <= 3; i++) {
            Bid bid = new Bid(item, janeroe, new BigDecimal(9 + i));
            item.getBids().add(bid);
            em.persist(bid);
        }
        em.persist(item);

        tx.commit();
        em.close();

        FetchTestData testData = new FetchTestData();
        testData.items = new TestData(itemIds);
        testData.users = new TestData(userIds);
        return testData;
    }
	
	@Test
	public void fetchEagerJoin() throws Exception{
		FetchTestData testData = storeTestData();
		UserTransaction tx = TRANSACTION_MANAGER.getUserTransaction();
		try{
			tx.begin();
			EntityManager em = jpaSetup.createEntityManager();
			Long itemId = testData.items.getFirstId();
			/* Loads the entire object graph.*/
			logger.info("Loading Item with eager fetching.");
			Item item = em.find(Item.class, itemId);
			em.detach(item);
			assertTrue(persistenceUtil.isLoaded(item.getBids()));
			assertTrue(persistenceUtil.isLoaded(item.getSeller()));
			assertTrue(persistenceUtil.isLoaded(item.getSeller().getUsername().equals("johndoe")));
			tx.commit();
			em.close();
		}finally{
			TRANSACTION_MANAGER.rollback();
		}
	}

}
