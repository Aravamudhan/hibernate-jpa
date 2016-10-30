package com.amudhan.jpatest.model.simple;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertNotEquals;
import static org.testng.Assert.assertTrue;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.Executors;

import javax.persistence.EntityManager;
import javax.persistence.FlushModeType;
import javax.persistence.PersistenceUnitUtil;
import javax.transaction.UserTransaction;

import org.hibernate.Session;
import org.hibernate.jdbc.Work;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.Test;

import com.amudhan.jpatest.environment.JPASetupTest;

public class SimpleTransitions extends JPASetupTest {

	private Logger logger = LoggerFactory.getLogger(SimpleTransitions.class);

	@Override
	public void configurePersistenceUnit() throws Exception {
		configurePersistenceUnit("SimplePU");
	}

	@Test
	public void makePersistent() throws Exception {
		UserTransaction tx = TRANSACTION_MANAGER.getUserTransaction();
		try {
			EntityManager em;
			tx.begin();
			em = jpaSetup.createEntityManager();
			Item item = new Item();
			em.persist(item);
			/*
			 * The The NAME will be set in an update rather than in the initial
			 * insert. If not null constraint is set on the NAME column, this
			 * will throw an exception.
			 */
			item.setName("ItemOne");
			Long ITEM_ID = item.getId();
			tx.commit();
			em.close();

			tx.begin();
			em = jpaSetup.createEntityManager();
			logger.info(em.find(Item.class, ITEM_ID).toString());
			tx.commit();
			em.close();

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			TRANSACTION_MANAGER.rollback();
		}
	}

	@Test
	public void retrievePersistent() throws Exception {
		UserTransaction tx = TRANSACTION_MANAGER.getUserTransaction();
		try {
			tx.begin();
			EntityManager em = jpaSetup.createEntityManager();
			Item someItem = new Item();
			someItem.setName("Some Item");
			em.persist(someItem);
			tx.commit();
			em.close();
			Long ITEM_ID = someItem.getId();
			{
				tx.begin();
				em = jpaSetup.createEntityManager();
				/*
				 * Hits the database if the item with id ITEM_ID not in the PC
				 * already.
				 */
				Item item = em.find(Item.class, ITEM_ID);
				if (item != null) {
					item.setName("New name");
				}
				tx.commit();// flush, dirty check and SQL update.
				em.close();
			}
			{
				tx.begin();
				em = jpaSetup.createEntityManager();
				Item itemA = em.find(Item.class, ITEM_ID);
				Item itemB = em.find(Item.class, ITEM_ID); // Repeatable read.
				/*
				 * All the statements below evaluate to true. itemA and itemB
				 * both hold the same reference.
				 */
				assertTrue(itemA == itemB);
				assertTrue(itemA.equals(itemB));
				assertTrue(itemA.getId().equals(itemB.getId()));
				tx.commit();
				em.close();
			}
			tx.begin();
			em = jpaSetup.createEntityManager();
			assertEquals(em.find(Item.class, ITEM_ID).getName(), "New name");
			tx.commit();
			em.close();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			TRANSACTION_MANAGER.rollback();
		}
	}

	/* getReference always returns a proxy and does not hit the database.*/
	@Test(expectedExceptions = {org.hibernate.LazyInitializationException.class,
			javax.persistence.EntityNotFoundException.class})
	public void retrievePersistentReference() throws Exception {
		UserTransaction tx = TRANSACTION_MANAGER.getUserTransaction();
		/*
		 * No catch to this try (i.e.) not handling any exception here. This
		 * method expects LazyinitializationException. If that is thrown, and if
		 * we that will be handled in the catch. The method will not be aware
		 * that this was actually thrown. Hence, letting TestNG handle the
		 * exception. Other option would be throw it again explicitly from the
		 * catch.
		 */
		try {
			tx.begin();
			EntityManager em = jpaSetup.createEntityManager();
			Item item = new Item();
			item.setName("ItemName");
			item.setAuctionEnd(LocalDateTime.now().plusDays(2));
			item.setBuyNowPrice(new BigDecimal(1000));
			em.persist(item);
			tx.commit();
			em.close();

			long itemId = item.getId();

			tx.begin();
			em = jpaSetup.createEntityManager();
			/*
			 * This method does not hit the database. No SELECT statement to DB.
			 * EntityManager#getReference method always returns a proxy.
			 */
			Item itemAnother = em.getReference(Item.class, itemId);
			/* There is no item with the id 100. Yet, a proxy is returned.*/
			Item nonItem = em.getReference(Item.class, new Long(100));
			PersistenceUnitUtil persistenceUtil = jpaSetup
					.getEntityManagerFactory().getPersistenceUnitUtil();
			// EntityManager#getReference returns an uninitialized proxy.
			assertFalse(persistenceUtil.isLoaded(itemAnother));
			assertFalse(persistenceUtil.isLoaded(nonItem));
			/*
			 * The proxy reference itemAnother will be initialized when it is
			 * accessed inside the transaction. By calling the
			 * Hibernate#initialize method with the proxy reference, this can be
			 * explicitly initialized.
			 */
			/* The toString method initializes the proxy. */
			// logger.info(itemAnother.toString());
			/* This will throw EntityNotFound exception.
			 * Hibernate hits the database only now.*/
			logger.info(nonItem.toString());
			tx.commit();
			em.close();
			/*
			 * This will throw LazyInitializationException. itemAnother is just
			 * a proxy that is not currently initialized. If it is not
			 * initialized inside the transaction, accessing that proxy
			 * reference will fail.
			 */
			assertEquals(itemAnother.getName(), "ItemName");
		} finally {
			TRANSACTION_MANAGER.rollback();
		}
	}

	/* When the remove is called on an entity, it is transformed to the transient
	 * state from the persistent state, it is removed from the PC
	 * and from the database as well. The row/rows representing the entity
	 * will be deleted. The delete query will be triggered during the commit
	 * or explicit flush.*/
	@Test
	public void makeTransient() throws Exception {
		UserTransaction tx = TRANSACTION_MANAGER.getUserTransaction();
		try {
			tx.begin();
			EntityManager em = jpaSetup.createEntityManager();
			Item itemOne = new Item();
			itemOne.setName("Item One");
			Item itemTwo = new Item();
			itemTwo.setName("Item Two");
			Item itemThree = new Item();
			itemThree.setName("Item Three");
			em.persist(itemOne);
			em.persist(itemTwo);
			em.persist(itemThree);
			tx.commit();
			em.close();
			
			Long itemOneId = itemOne.getId();
			Long itemTwoId = itemTwo.getId();
			Long itemThreeId = itemThree.getId();
			tx.begin();
			em = jpaSetup.createEntityManager();
			Item itemFull = em.find(Item.class, itemOneId);
			/* Proxy is created. */
			Item itemProxy = em.getReference(Item.class, itemTwoId);
			Item itemToPersist = em.find(Item.class, itemThreeId);
			/* Now the item moves from persistent state to the remove state. */
			/*
			 * By default, the entities are removed from the PC. But the entities
			 * retain their identifier values. This is useful in undoing the delete.
			 */
			em.remove(itemFull);
			/*
			 * Since itemProxy is a proxy, this will be initialized with a
			 * SELECT then only moved from persistent to remove. An entity
			 * instance must be fully initialized before state transitions.
			 */
			em.remove(itemProxy);
			em.remove(itemToPersist);
			/* All 3 entities are removed from the PC. */
			assertFalse(em.contains(itemFull));
			assertFalse(em.contains(itemProxy));
			assertFalse(em.contains(itemToPersist));
			/*
			 * The assertNull block should be uncommented after setting the
			 * hibernate.use_identifier_rollback property to true. Otherwise
			 * the identifiers will not be reset. The reset is useful when it
			 * is desirable to make the entities completely transient without
			 * even identifiers values that were already loaded.
			 */
			/*
			 * assertNull(itemFull.getId());
			 * assertNull(itemProxy.getId());
			 * assertNull(itemToPersist.getId());
			 */
			/* Making itemThree persistent again. */
			em.persist(itemToPersist);
			tx.commit();
			em.close();

		} finally {
			TRANSACTION_MANAGER.rollback();
		}
	}
	/* This is useful in multithreaded environments.
	 * If there is a chance that certain values in the PC must always
	 * be in synchrony with the database in a concurrent environment,
	 * refresh must be called. Calling refresh on an entity triggers
	 * SELECT and any changes made to that entity in the transaction
	 * that is not flushed, will be overwritten. */
	@Test
	public void refresh() throws Exception {
		UserTransaction tx = TRANSACTION_MANAGER.getUserTransaction();
		try{
			tx.begin();
			EntityManager em = jpaSetup.createEntityManager();
			Item someItem = new Item();
			someItem.setName("Some item");
			em.persist(someItem);
			tx.commit();
			em.close();
			Long someItemId = someItem.getId();
			tx.begin();
			em = jpaSetup.createEntityManager();
			Item item = em.find(Item.class, someItemId);
			item.setName("Some name");
			
			// Someone updates this row in the database!
            Executors.newSingleThreadExecutor().submit(new Callable<Object>() {
                @Override
                public Object call() throws Exception {
                    UserTransaction tx = TRANSACTION_MANAGER.getUserTransaction();
                    try {
                        tx.begin();
                        EntityManager em = jpaSetup.createEntityManager();

                        Session session = em.unwrap(Session.class);
                        session.doWork(new Work() {
                            @Override
                            public void execute(Connection con) throws SQLException {
                                PreparedStatement ps = con.prepareStatement("update SIMPLE_ITEM set name = ? where ID = ?");
                                ps.setString(1, "Concurrent Update item name");
                                ps.setLong(2, someItemId);

                                /* Alternative: you get an EntityNotFoundException on refresh
                                PreparedStatement ps = con.prepareStatement("delete from ITEM where ID = ?");
                                ps.setLong(1, ITEM_ID);
                                */

                                if (ps.executeUpdate() != 1)
                                    throw new SQLException("ITEM row was not updated");
                            }
                        });

                        tx.commit();
                        em.close();

                    } catch (Exception ex) {
                    	TRANSACTION_MANAGER.rollback();
                        throw new RuntimeException("Concurrent operation failure: " + ex, ex);
                    }
                    return null;
                }
            }).get();
            String oldName = item.getName();
            em.refresh(item);
            assertNotEquals(item.getName(), oldName);
            assertEquals(item.getName(), "Concurrent Update item name");
			tx.commit();
			em.close();
			
		}finally{
			TRANSACTION_MANAGER.rollback();
		}
	}
	
	/* To control when the flush() can be called.*/
	@Test
	public void flushModeType() throws Exception{
		UserTransaction tx = TRANSACTION_MANAGER.getUserTransaction();
		Long itemId;
		try{
			tx.begin();
			EntityManager em = jpaSetup.createEntityManager();
			Item someItem = new Item();
			someItem.setName("Actual name");
			em.persist(someItem);
			tx.commit();
			em.close();
			itemId = someItem.getId();
		}finally{
			TRANSACTION_MANAGER.rollback();
		}
		try{
			tx.begin();
			EntityManager em = jpaSetup.createEntityManager();
			Item item = em.find(Item.class, itemId);
			item.setName("New name");
			/* This disables flushing before commit. Only during commit,
			 * flush is called.*/
			em.setFlushMode(FlushModeType.COMMIT);
			/* Usually Hibernate recognizes that the data has changed and would synchronize
			 * with the memory before querying. Since the FlushModeType is commit, this default
			 * behavior is restricted.*/
			assertEquals(em.createQuery("SELECT i.name from SIMPLE_ITEM i where i.id = :id").
											setParameter("id", itemId).getSingleResult(),"Actual name");
			/* flush is called here. The name "New name" is set here.*/
			tx.commit();
			em.close();
		}finally{
			TRANSACTION_MANAGER.rollback();
		}
	}
	
	/* Inside a transaction even if the find is called in an entity 
	 * multiple times, it will only load from the PC.*/
	@Test
	public void scopeOfIdentity() throws Exception{
		UserTransaction tx = TRANSACTION_MANAGER.getUserTransaction();
		try{
			tx.begin();
			EntityManager em = jpaSetup.createEntityManager();
			Item someItem = new Item();
            someItem.setName("Some Item");
            em.persist(someItem);
			tx.commit();
			em.close();
			Long itemId = someItem.getId();
			
			tx.begin();
			em = jpaSetup.createEntityManager();
			/* a and b are references to the same item in the PC.*/
			Item a = em.find(Item.class, itemId);
			Item b = em.find(Item.class, itemId);
			assertTrue(a==b);
			assertTrue(a.equals(b));
			assertEquals(a.getId(), b.getId());
			tx.commit();
			em.close();

			tx.begin();
			em = jpaSetup.createEntityManager();
			Item c = em.find(Item.class, itemId);
			/* Though conceptually both a and c represent the same object, a is in
			 * the detached state with reference to a heap location not managed by the current PC.
			 * c points to a location in the heap managed by the current PC. Hence they both
			 * are copies of the same object, but in different locations. Since equals method
			 * is not overridden, the equality is compared using memory addresses of the heap.
			 * Only way to tackle this type of situation is to override the equals method.*/
			assertFalse(a==c);
			assertFalse(a.equals(c));
			assertEquals(a.getId(), c.getId());
			tx.commit();
			em.close();
			
			Set<Item> items = new HashSet<Item>();
			items.add(a);
			items.add(b);
			items.add(c);
			assertTrue(items.size()==2);
		}finally{
			TRANSACTION_MANAGER.rollback();
		}
	}

	/* The detach removes an entity from the PC. Any non flushed changes
	 * will not be synchronized with the database.*/
	@Test
	public void detach() throws Exception{
		UserTransaction tx =  TRANSACTION_MANAGER.getUserTransaction();
		try{
			tx.begin();
			EntityManager em = jpaSetup.createEntityManager();
			User user = new User();
			user.setUserName("Awesome guy");
			user.setHomeAddress(new Address("Great street", "12345", "Paris"));
			logger.info("Before persist the id is "+user.getId());
			assertTrue(null == user.getId());
			em.persist(user);
			logger.info("After the persist the id is "+user.getId());
			assertTrue(null != user.getId());
			tx.commit();
			em.close();
			
			Long userId = user.getId();
			tx.begin();
			em = jpaSetup.createEntityManager();
			User detachedUser = em.find(User.class, userId);
			em.detach(detachedUser);
			assertFalse(em.contains(detachedUser));
			tx.commit();
			em.close();
		}finally{
			TRANSACTION_MANAGER.rollback();
		}
	}
	
	/* For a detached entity - Hibernate checks the PC whether the entity
	 * already exists. If it does not, it returns a new entity and copies the state
	 * of the detached entity. The state includes the child entities too.
	 * During flush, an update will be triggered.
	 * For a transient entity - Instead of an update query like in the case
	 * of a detached entity, an insert query is triggered. This newly
	 * returned entity will receive all the properties of the transient instance and 
	 * will be made persistent. The entities supplied to the merge method should be
	 * discarded since they are either transient or detached and wont be tracked
	 * further. Merge creates or updates an entity.*/
	@SuppressWarnings("unused")
	@Test
	public void mergeDetached() throws Exception{
		UserTransaction tx = TRANSACTION_MANAGER.getUserTransaction();
		try{
			tx.begin();
			EntityManager em = jpaSetup.createEntityManager();
			User detachedUser = new User();
			detachedUser.setUserName("Foo name");
			detachedUser.setHomeAddress(new Address("Foo street", "341", "Paris"));
			detachedUser.setBillingAddress(new Address("Bar street", "123", "Berlin"));
			em.persist(detachedUser);
			tx.commit();
			Long userId = detachedUser.getId();
			em.close();
			
			detachedUser.setUserName("Bar name");
			tx.begin();
			em = jpaSetup.createEntityManager(); 
			User mergedUser = em.merge(detachedUser);
			assertTrue(mergedUser.getUserName().equals("Bar name"));
			mergedUser.setUserName("Foo bar name");
			tx.commit();
			em.close();
			
			tx.begin();
			em = jpaSetup.createEntityManager();
			User user = em.find(User.class, userId);
			assertTrue(user.getUserName().equals("Foo bar name"));
			tx.commit();
			em.close();
			
			User transientUser = new User();
			transientUser.setUserName("Awesome guy");
			transientUser.setHomeAddress(new Address("AB street","235","Paris"));
			tx.begin();
			em = jpaSetup.createEntityManager();
			User persistedUser = em.merge(transientUser);
			tx.commit();
			em.close();
		}finally{
			TRANSACTION_MANAGER.rollback();
		}
	}
}
