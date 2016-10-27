package com.amudhan.jpatest.model.fetching;

import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityGraph;
import javax.persistence.EntityManager;
import javax.persistence.Persistence;
import javax.persistence.PersistenceUtil;
import javax.persistence.Subgraph;
import javax.transaction.UserTransaction;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.Test;

import com.amudhan.jpatest.environment.JPASetupTest;
import com.amudhan.jpatest.model.fetching.fetchloadgraph.Bid;
import com.amudhan.jpatest.model.fetching.fetchloadgraph.Bid_;
import com.amudhan.jpatest.model.fetching.fetchloadgraph.Item;
import com.amudhan.jpatest.model.fetching.fetchloadgraph.Item_;
import com.amudhan.jpatest.model.fetching.fetchloadgraph.User;
import com.amudhan.jpatest.shared.FetchTestLoadEventListener;
import com.amudhan.jpatest.shared.util.TestData;

public class FetchLoadGraph extends JPASetupTest {

	private FetchTestLoadEventListener loadEventListener;
	private static Logger logger = LoggerFactory
			.getLogger(FetchLoadGraph.class);

	@Override
	public void configurePersistenceUnit() throws Exception {
		configurePersistenceUnit("FetchingFetchLoadGraphPU");
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
        Long[] bidIds = new Long[3];
		
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
            Bid bid = new Bid(item, robertdoe, new BigDecimal(9 + i));
            item.getBids().add(bid);
            em.persist(bid);
            bidIds[i - 1] = bid.getId();
        }
        
        item = new Item("Item Two", LocalDateTime.now().plusDays(1), johndoe);
		em.persist(item);
		itemIds[1] = item.getId();
		for (int i = 1; i <= 1; i++) {
			Bid bid = new Bid(item, janeroe, new BigDecimal(2 + i));
			item.getBids().add(bid);
			em.persist(bid);
		}

		item = new Item("Item Three", LocalDateTime.now().plusDays(1), janeroe);
		em.persist(item);
		itemIds[2] = item.getId();
		for (int i = 1; i <= 1; i++) {
            Bid bid = new Bid(item, johndoe, new BigDecimal(3 + i));
            item.getBids().add(bid);
            em.persist(bid);
        }

		tx.commit();
		em.close();
		
		FetchTestData testData = new FetchTestData();
		testData.items = new TestData(itemIds);
		testData.users = new TestData(userIds);
		testData.bids = new TestData(bidIds);
		return testData;

	}
	
	@Test
    public void loadItem() throws Exception {
		FetchTestData testData = storeTestData();
        long ITEM_ID = testData.items.getFirstId();
        PersistenceUtil persistenceUtil = Persistence.getPersistenceUtil();
        loadEventListener.reset();
        {
        	UserTransaction tx = TRANSACTION_MANAGER.getUserTransaction();
        	try{
        		tx.begin();
        		EntityManager em = jpaSetup.createEntityManager();
        		Map<String, Object> properties = new HashMap<>();
        		/* 
        		 * This graph has no parameters, hence loads the graph according
        		 * to the fetch plan/type of each parameter. The default name of
        		 * a load graph without name is the entity's name.
        		 */
        		/*
        		 * The hint is loadgraph. Another type of hint is fetchgraph. In the fetchgraph
        		 * any attribute node that is not part of the graph will be lazy and what ever is
        		 * added to the graph will be eager. In loadgraph, whatever is added will be 
        		 * eager, whatever is not added in the graph will be loaded according to its fetch type.
        		 * fetchgraph hint ignores the FetchType settings.
        		 */
        		properties.put("javax.persistence.loadgraph", em.getEntityGraph("FETCHING_FETCHLOADGRAPH_ITEM"));
        		logger.info("Loading an item with the default EntityGraph using the Item name.");
        		Item item = em.find(Item.class, ITEM_ID, properties);
        		assertTrue(persistenceUtil.isLoaded(item));
                assertTrue(persistenceUtil.isLoaded(item, "name"));
                assertTrue(persistenceUtil.isLoaded(item, "auctionEnd"));
                assertFalse(persistenceUtil.isLoaded(item, "seller"));
                assertFalse(persistenceUtil.isLoaded(item, "bids"));
        		tx.commit();
        		em.close();
        	}finally{
        		TRANSACTION_MANAGER.rollback();
        	}
        }
        {
        	UserTransaction tx = TRANSACTION_MANAGER.getUserTransaction();
        	try{
        		tx.begin();
        		EntityManager em = jpaSetup.createEntityManager();
        		EntityGraph<Item> itemGraph = em.createEntityGraph(Item.class);
        		Map<String, Object> properties = new HashMap<>();
        		properties.put("javax.persistence.loadgraph", itemGraph);
        		logger.info("Loading an item with the default EntityGraph using the Item class.");
        		Item item = em.find(Item.class, ITEM_ID, properties);
        		assertTrue(persistenceUtil.isLoaded(item));
                assertTrue(persistenceUtil.isLoaded(item, "name"));
                assertTrue(persistenceUtil.isLoaded(item, "auctionEnd"));
                assertFalse(persistenceUtil.isLoaded(item, "seller"));
                assertFalse(persistenceUtil.isLoaded(item, "bids"));
        		tx.commit();
        		em.close();
        	}finally{
        		TRANSACTION_MANAGER.rollback();
        	}
        }
	}
	
	@SuppressWarnings("unchecked")
	@Test
	public void loadItemSeller() throws Exception{
		FetchTestData testData = storeTestData();
        long ITEM_ID = testData.items.getFirstId();
        PersistenceUtil persistenceUtil = Persistence.getPersistenceUtil();
        loadEventListener.reset();
		{
        	UserTransaction tx = TRANSACTION_MANAGER.getUserTransaction();
        	try{
        		tx.begin();
        		EntityManager em = jpaSetup.createEntityManager();
        		Map<String, Object> properties = new HashMap<>();
        		properties.put("javax.persistence.loadgraph", em.getEntityGraph("ItemSeller"));
        		logger.info("Loading an item with itemSeller entity graph. Loads seller too.");
        		Item item = em.find(Item.class, ITEM_ID, properties);
        		assertTrue(persistenceUtil.isLoaded(item));
                assertTrue(persistenceUtil.isLoaded(item, "name"));
                assertTrue(persistenceUtil.isLoaded(item, "auctionEnd"));
                assertTrue(persistenceUtil.isLoaded(item, "seller"));
                assertFalse(persistenceUtil.isLoaded(item, "bids"));
        		tx.commit();
        		em.close();
        	}finally{
        		TRANSACTION_MANAGER.rollback();
        	}
        }
		{
        	UserTransaction tx = TRANSACTION_MANAGER.getUserTransaction();
        	try{
        		tx.begin();
        		EntityManager em = jpaSetup.createEntityManager();
        		EntityGraph<Item> itemGraph = em.createEntityGraph(Item.class);
        		/*
        		 * Using static meta model. This promotes type safety. Instead
        		 * of using string to represent property names/meta data of an
        		 * entity, the meta data itself can directly be used using
        		 * the generated meta model.
        		 */
        		/*
        		 * Instead of using annotations in the entities, the graph can
        		 * be dynamically added. By using addAttributeNodes method,
        		 * an entire graph can be built dynamically.
        		 */
        		itemGraph.addAttributeNodes(Item_.seller);
        		Map<String, Object> properties = new HashMap<>();
        		properties.put("javax.persistence.loadgraph", itemGraph);
        		logger.info("Loading an item with seller as the attribute node added dynamically.");
        		Item item = em.find(Item.class, ITEM_ID, properties);
        		assertTrue(persistenceUtil.isLoaded(item));
                assertTrue(persistenceUtil.isLoaded(item, "name"));
                assertTrue(persistenceUtil.isLoaded(item, "auctionEnd"));
                assertTrue(persistenceUtil.isLoaded(item, "seller"));
                assertFalse(persistenceUtil.isLoaded(item, "bids"));
        		tx.commit();
        		em.close();
        	}finally{
        		TRANSACTION_MANAGER.rollback();
        	}
		}
		{
			UserTransaction tx = TRANSACTION_MANAGER.getUserTransaction();
        	try{
        		tx.begin();
        		EntityManager em = jpaSetup.createEntityManager();
        		EntityGraph<Item> itemGraph = em.createEntityGraph(Item.class);
        		itemGraph.addAttributeNodes(Item_.seller);
        		Map<String, Object> properties = new HashMap<>();
        		properties.put("javax.persistence.loadgraph", itemGraph);
        		logger.info("Loading all the items with Item_.seller as the attribute node.");
        		List<Item> items = em.createQuery("SELECT i from FETCHING_FETCHLOADGRAPH_ITEM i").
        								setHint("javax.persistence.loadgraph", itemGraph).getResultList();
        		assertTrue(items.size()==3);
        		for(Item item : items){
        			assertTrue(persistenceUtil.isLoaded(item));
                    assertTrue(persistenceUtil.isLoaded(item, "name"));
                    assertTrue(persistenceUtil.isLoaded(item, "auctionEnd"));
                    assertTrue(persistenceUtil.isLoaded(item, "seller"));
                    assertFalse(persistenceUtil.isLoaded(item, "bids"));
        		}
        		tx.commit();
        		em.close();
        	}finally{
        		TRANSACTION_MANAGER.rollback();
        	}
		}
	}
	
	@SuppressWarnings("unchecked")
	@Test
    public void loadBidBidderItemSellerBids() throws Exception {
		FetchTestData testData = storeTestData();
        long BID_ID = testData.bids.getFirstId();
        PersistenceUtil persistenceUtil = Persistence.getPersistenceUtil();
        loadEventListener.reset();
		{
        	UserTransaction tx = TRANSACTION_MANAGER.getUserTransaction();
        	try{
        		tx.begin();
        		EntityManager em = jpaSetup.createEntityManager();
        		Map<String, Object> properties = new HashMap<>();
        		properties.put("javax.persistence.loadgraph", em.getEntityGraph("BidBidderItemSellerBids"));
        		logger.info("Loading a bid using entity graph annotations.");
        		Bid bid = em.find(Bid.class, BID_ID, properties);
        		assertTrue(persistenceUtil.isLoaded(bid));
        		/*
        		 * The meta data model can be used to specify the properties.
        		 * This is cleaner.
        		 */
                assertTrue(persistenceUtil.isLoaded(bid, Bid_.amount.toString()));
                assertTrue(persistenceUtil.isLoaded(bid, Bid_.bidder.toString()));
                assertTrue(persistenceUtil.isLoaded(bid, "item"));
                assertTrue(persistenceUtil.isLoaded(bid.getItem(), "seller"));
                assertTrue(persistenceUtil.isLoaded(bid.getItem().getSeller(), "userName"));
                assertTrue(persistenceUtil.isLoaded(bid.getItem(), "bids"));
        		tx.commit();
        		em.close();
        	}finally{
        		TRANSACTION_MANAGER.rollback();
        	}
        }
		{                                                                                                   
			UserTransaction tx = TRANSACTION_MANAGER.getUserTransaction();                                  
			try{                                                                                            
				tx.begin();                                                                                 
				EntityManager em = jpaSetup.createEntityManager();
				
				EntityGraph<Bid> bidGraph = em.createEntityGraph(Bid.class);
				bidGraph.addAttributeNodes(Bid_.bidder, Bid_.item);
				/*
				 * Creating a subgraph.
				 */
				Subgraph<Item> itemGraph = bidGraph.addSubgraph(Bid_.item);
				itemGraph.addAttributeNodes(Item_.seller, Item_.bids);
				
				Map<String, Object> properties = new HashMap<>();
				properties.put("javax.persistence.loadgraph", bidGraph);
				logger.info("Loading a bid using entity graph API.");                               
				Bid bid = em.find(Bid.class, BID_ID, properties);                                           
				assertTrue(persistenceUtil.isLoaded(bid));                                                  
		        assertTrue(persistenceUtil.isLoaded(bid, "amount"));                          
		        assertTrue(persistenceUtil.isLoaded(bid, "bidder"));                          
		        assertTrue(persistenceUtil.isLoaded(bid, "item"));                                          
		        assertTrue(persistenceUtil.isLoaded(bid.getItem(), "seller"));                              
		        assertTrue(persistenceUtil.isLoaded(bid.getItem().getSeller(), "userName"));                
		        assertTrue(persistenceUtil.isLoaded(bid.getItem(), "bids"));                                
				tx.commit();                                                                                
				em.close();                                                                                 
			}finally{                                                                                       
				TRANSACTION_MANAGER.rollback();                                                             
			}                                                                                               
		}                                                                                                   
	}
}
