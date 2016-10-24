package com.amudhan.jpatest.model.fetching;

import static org.testng.Assert.assertNotNull;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Root;
import javax.transaction.UserTransaction;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.Test;

import com.amudhan.jpatest.environment.JPASetupTest;
import com.amudhan.jpatest.model.fetching.nplusoneselects.Bid;
import com.amudhan.jpatest.model.fetching.nplusoneselects.Item;
import com.amudhan.jpatest.model.fetching.nplusoneselects.User;
import com.amudhan.jpatest.shared.util.TestData;

public class EagerQuery extends JPASetupTest {

	private static Logger logger = LoggerFactory
			.getLogger(EagerQuery.class);

	@Override
	public void configurePersistenceUnit() throws Exception {
		configurePersistenceUnit("FetchingNPlusOneSelectsPU");
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

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Test
	public void fetchUsers() throws Exception {
		storeTestData();
		UserTransaction tx = TRANSACTION_MANAGER.getUserTransaction();
		try {
			tx.begin();
			{
				EntityManager em = jpaSetup.createEntityManager();
				/*
				 * Though the Item#seller is marked for lazy fetching globally,
				 * this is changed dynamically into join fetch.
				 */
				logger.info("Fetching Item and Item#seller using join");
				List<Item> items = em
						.createQuery(
								"select i from FETCHING_NPLUSONESELECTS_ITEM i join fetch i.seller")
						.getResultList();
				logger.info("Closing the PC.");
				em.close();
				/* Item#seller has lazy fetch type. This will fail, if not for the 
				 * 'join fetch' in the query.*/
				for (Item item : items) {
					assertNotNull(item.getSeller().getUserName());
				}
			}
			{
				EntityManager em = jpaSetup.createEntityManager();
				CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();
				CriteriaQuery criteriaQuery = criteriaBuilder.createQuery();
				Root<Item> itemRoot = criteriaQuery.from(Item.class);
				itemRoot.fetch("seller");
				criteriaQuery.select(itemRoot);
				logger.info("Fetching Item and Item#seller using join with criteria query.");
				List<Item> items = em.createQuery(criteriaQuery).getResultList();
				logger.info("Closing the PC.");
				em.close();
				for (Item item : items) {
                    assertNotNull(item.getSeller().getUserName());
                }
				
			}
			tx.commit();
		} finally {
			TRANSACTION_MANAGER.rollback();
		}
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Test
    public void fetchBids() throws Exception {
        storeTestData();
        UserTransaction tx = TRANSACTION_MANAGER.getUserTransaction();
		try {
			tx.begin();
			{
				EntityManager em = jpaSetup.createEntityManager();
				logger.info("Fetching Item and Item#bids using join");
				List<Item> items = em
						.createQuery(
								"select i from FETCHING_NPLUSONESELECTS_ITEM i left join fetch i.bids")
						.getResultList();
				logger.info("Closing the PC.");
				em.close();
				/* Item#bids has lazy fetch type. This will fail, if not for the 
				 * 'left join fetch' in the query. Item to Bid is OneToMany. Items that do not have
				 * bids also are needed, hence left join.*/
				for (Item item : items) {
					assertNotNull(item.getBids().size()>0);
				}
			}
			{
				EntityManager em = jpaSetup.createEntityManager();
				CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();
				CriteriaQuery criteriaQuery = criteriaBuilder.createQuery();
				Root<Item> itemRoot = criteriaQuery.from(Item.class);
				itemRoot.fetch("bids", JoinType.LEFT);
				criteriaQuery.select(itemRoot);
				logger.info("Fetching Item and Item#bids using join with criteria query.");
				List<Item> items = em.createQuery(criteriaQuery).getResultList();
				logger.info("Closing the PC.");
				em.close();
				for (Item item : items) {
                    assertNotNull(item.getBids().size()>0);
                }
				
			}
			tx.commit();
		} finally {
			TRANSACTION_MANAGER.rollback();
		}

        
	}
}
