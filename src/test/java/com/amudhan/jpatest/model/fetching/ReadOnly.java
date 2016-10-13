	package com.amudhan.jpatest.model.fetching;

import static org.testng.Assert.assertNotEquals;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import javax.persistence.EntityManager;
import javax.transaction.UserTransaction;

import org.hibernate.Session;
import org.testng.annotations.Test;

import com.amudhan.jpatest.environment.JPASetupTest;
import com.amudhan.jpatest.model.fetching.readonly.Bid;
import com.amudhan.jpatest.model.fetching.readonly.Item;
import com.amudhan.jpatest.model.fetching.readonly.User;
import com.amudhan.jpatest.shared.util.TestData;

/* This class contains tests that demonstrate how the entities in a PC
 * can be set to read only mode. This restricts any modifications to those
 * entities. Deletion is allowed.*/
public class ReadOnly extends JPASetupTest{

	@Override
	public void configurePersistenceUnit() throws Exception{
		configurePersistenceUnit("FetchingReadOnly");
	}
	
	public FetchTestData storeTestData() throws Exception{
		FetchTestData testData = new FetchTestData();
		UserTransaction tx = TRANSACTION_MANAGER.getUserTransaction();
		try{
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
			
			testData.items = new TestData(itemIds);
			testData.users = new TestData(userIds);
			
		}catch(Exception e){
			e.printStackTrace();
		}
		return testData;
	}

	@Test
	public void immutableEntity() throws Exception{
		FetchTestData testData = storeTestData();
		UserTransaction tx = TRANSACTION_MANAGER.getUserTransaction();
		try{
			tx.begin();
			EntityManager em = jpaSetup.createEntityManager();
			long itemId = testData.items.getFirstId();
			Item itemOne = em.find(Item.class, itemId);
			for(Bid bid : itemOne.getBids()){
				/* This has no effect since the entity is Immutable*/
				bid.setAmount(new BigDecimal(55));
			}
			tx.commit();
			em.close();
			
			tx.begin();
			em = jpaSetup.createEntityManager();
			Item itemTwo = em.find(Item.class, testData.items.getFirstId());
			for(Bid bid : itemTwo.getBids()){
				assertNotEquals(bid.getAmount(), new BigDecimal(55));
			}
			tx.commit();
			em.close();
		}finally{
			TRANSACTION_MANAGER.rollback();
		}
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes", "deprecation" })
	@Test
	public void selectiveReadOnly() throws Exception{
		FetchTestData testData = storeTestData();
		UserTransaction tx = TRANSACTION_MANAGER.getUserTransaction();
		try{
			tx.begin();
			EntityManager em = jpaSetup.createEntityManager();
			em.unwrap(Session.class).setDefaultReadOnly(true);
			long itemId = testData.items.getFirstId();
			{
				Item item = em.find(Item.class, itemId);
				item.setItemName("New Name");
				/*No dirty checking and update.*/
				em.flush();
				em.clear();
			}
			{
				Item item =  em.find(Item.class, itemId);
				assertNotEquals(item.getItemName(),"New Name");
				em.clear();
			}
			{
				Item item =  em.find(Item.class, itemId);
				em.unwrap(Session.class).setReadOnly(item, true);
				item.setItemName("Another new name");
				em.flush();
				em.clear();
			}
			{
				Item item =  em.find(Item.class, itemId);
				assertNotEquals(item.getItemName(),"Another new name");
				em.clear();
			}
			{
				org.hibernate.Query query= em.unwrap(Session.class).createQuery("SELECT i from FETCHING_READONLY_ITEM i");
				List<Item> items = query.setReadOnly(true).list();
				for(Item item : items){
					item.setItemName("New name");
				}
				em.flush();
			}
			{
				List<Item> items = em.createQuery("SELECT i from FETCHING_READONLY_ITEM i").
						setHint(org.hibernate.annotations.QueryHints.READ_ONLY, true).
						getResultList();
				for(Item item : items){
					item.setItemName("New name");
				}
				em.flush();
			}
			{
				em.clear();
				Item item = em.find(Item.class, itemId);
				assertNotEquals(item.getItemName(), "New name");
			}
			tx.commit();
			em.close();
		}finally{
			TRANSACTION_MANAGER.rollback();
		}
		
	}
	
}
