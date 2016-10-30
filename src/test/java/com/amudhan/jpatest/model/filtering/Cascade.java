package com.amudhan.jpatest.model.filtering;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.concurrent.Callable;
import java.util.concurrent.Executors;

import javax.persistence.EntityManager;
import javax.transaction.UserTransaction;

import org.hibernate.Session;
import org.hibernate.jdbc.Work;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.Test;

import com.amudhan.jpatest.environment.JPASetupTest;
import com.amudhan.jpatest.model.filtering.cascade.BankAccount;
import com.amudhan.jpatest.model.filtering.cascade.Bid;
import com.amudhan.jpatest.model.filtering.cascade.BillingDetails;
import com.amudhan.jpatest.model.filtering.cascade.CreditCard;
import com.amudhan.jpatest.model.filtering.cascade.Image;
import com.amudhan.jpatest.model.filtering.cascade.Item;
import com.amudhan.jpatest.model.filtering.cascade.User;

public class Cascade extends JPASetupTest{

	private static Logger logger = LoggerFactory
			.getLogger(Cascade.class);

	@Override
	public void configurePersistenceUnit() throws Exception {
		configurePersistenceUnit("FilteringCascadePU");
	}
	
	/*
	 * A new bid is added to the Item#bids in the detached state and
	 * the item is later merged.
	 */
	@Test
	public void detachMerge() throws Throwable{
		UserTransaction tx = TRANSACTION_MANAGER.getUserTransaction();
		try{
			tx.begin();
			EntityManager em = jpaSetup.createEntityManager();
			Long itemId;
			{
				logger.info("Creating an user, an item, two bids");
				User user = new User("John");
				em.persist(user);
				
				Item item = new Item("Some item", user);
				em.persist(item);
				itemId = item.getId();
				
				Bid firstBid = new Bid(new BigDecimal("99.0"), item);
				item.getBids().add(firstBid);
				em.persist(firstBid);
				
				Bid secondBid = new Bid(new BigDecimal("100.0"), item);
				item.getBids().add(secondBid);
                em.persist(secondBid);
                
                logger.info("Flushing the PC");
                em.flush();
			}
			logger.info("Clearing the PC");
			em.clear();
			{
				logger.info("Loading the item with id: "+itemId);
				Item item = em.find(Item.class, itemId);
				
				logger.info("Calling size method of bids collection. Initializes the bids.");
				assertEquals(item.getBids().size(), 2);
				
				logger.info("Detaching the item");
				em.detach(item);
				
				/*
				 * Adding a new bid in the detached state.
				 */
				item.getBids().add(new Bid(new BigDecimal("101.00"), item));
				item.setName("New item name");
				
				/*
				 * This loads the item and bids from the database.
				 * Hibernate checks the PC for the entity, when it is not found
				 * hits the database. Here Hibernate ignores the FetchType.LAZY
				 * annotation when merging. Even if the bids were uninitialized
				 * before detaching,they are initialized during merging
				 * anyway. This is due to the CascadeType.MERGE.
				 */
				logger.info("Merging the item.");
				Item mergedItem = em.merge(item);
				for (Bid b : mergedItem.getBids()) {
	                assertNotNull(b.getId());
	            }
				logger.info("Changes are updated");
				em.flush();
				em.clear();
			}
			tx.commit();
			em.close();
		}finally{
			TRANSACTION_MANAGER.rollback();
		}
	}
	
	/*
	 * Adding a new image to the Item#images when the item was detached and
	 * later merging the item with out the CascadeType.MERGE enabled for Item#images. 
	 */
	@Test(expectedExceptions = {org.hibernate.exception.ConstraintViolationException.class})
	public void noCascadeMerge() throws Throwable {
		UserTransaction tx = TRANSACTION_MANAGER.getUserTransaction();
		try{
			tx.begin();
			EntityManager em = jpaSetup.createEntityManager();
			Long itemId;
			{
				logger.info("Creating an user, an item, two images");
				User user = new User("John");
				em.persist(user);
				
				Item item = new Item("Some item", user);
				em.persist(item);
				itemId = item.getId();
				
				em.persist(new Image("ImageOne","/image1.jpg", item));
				em.persist(new Image("ImageTwo","/image2.jpg", item));
                logger.info("Flushing the PC");
                em.flush();
			}
			logger.info("Clearing the PC");
			em.clear();
			{
				logger.info("Loading the item with id: "+itemId);
				Item item = em.find(Item.class, itemId);
				
				logger.info("Initializing images.");
				assertEquals(item.getImages().size(), 2);
				
				logger.info("Detaching the item");
				em.detach(item);
				
				/*
				 * Adding a new image in the detached state. During the merge
				 * Hibernate will check in the PC for this new image. It does
				 * not exist in the PC. Since there is no id for this image,
				 * it is not loaded from the database too. Hence a new image
				 * with empty values is created and assigned to the mergedItem entity.
				 * Without the cascade MERGE option, this new image will be essentially
				 * lost. The new image that is created only contains the auto
				 * generated id.
				 */
				item.getImages().add(new Image("ImageThree","/image3.jpg", item));
				item.setName("New item name");
				
				logger.info("Merging the item.");
				Item mergedItem = em.merge(item);
				logger.info(" The new item name "+ item.getName());
				for(Image image : mergedItem.getImages()){
					logger.info(image.toString());
				}
				logger.info("Changes are updated");
				/*
				 * ConstraintViolation exception is thrown here. Since the cascade type
				 * for Item#images is not MERGE, the new image added to the item entity 
				 * is not copied to the mergedItem entity. Hence the Image#item value
				 * is null.
				 */
				em.flush();
				em.clear();
			}
			tx.commit();
			em.close();
		}catch(Exception e){
			throw unwrapCauseOfType(e, org.hibernate.exception.ConstraintViolationException.class);
		}finally{
			TRANSACTION_MANAGER.rollback();
		}
	}
	/*
	 * For a transaction that is long running, the data gets stale frequently.
	 * To synchronize the PC with the database, refresh needs to be called.
	 * Unless the refresh on a particular entity is cascaded to its children too,
	 * the child entities are not refreshed and stay stale. 
	 */
	@Test
	public void refresh() throws Exception{
		UserTransaction tx = TRANSACTION_MANAGER.getUserTransaction();
		try{
			tx.begin();
			EntityManager em = jpaSetup.createEntityManager();
			Long userId;
			Long creditCardId = null;
			{
				User user = new User("johndoe");
                user.getBillingDetails().add(
                    new CreditCard("John Doe", "1234567890", "11", "2020")
                );
                user.getBillingDetails().add(
                    new BankAccount("John Doe", "45678", "Some Bank", "1234")
                );
                em.persist(user);
                em.flush();
                
                userId = user.getId();
                for (BillingDetails bd : user.getBillingDetails()) {
                    if (bd instanceof CreditCard)
                    	creditCardId = bd.getId();
                }
                assertNotNull(creditCardId);
			}
			tx.commit();
			logger.info("Creation of entities is completed.");
			em.close();
			
			tx.begin();
			em = jpaSetup.createEntityManager();
			User user = em.find(User.class, userId);
			assertEquals(user.getBillingDetails().size(), 2);
            for (BillingDetails bd : user.getBillingDetails()) {
                assertEquals(bd.getOwner(), "John Doe");
            }
            final Long someUserId = userId;
            Executors.newSingleThreadExecutor().submit(new Callable<Object>() {
				@Override
				public Object call() throws Exception {
					UserTransaction tx = TRANSACTION_MANAGER.getUserTransaction();
					try{
						tx.begin();
						EntityManager em = jpaSetup.createEntityManager();
						em.unwrap(Session.class).doWork(new Work(){
							@Override
							public void execute(Connection connection)
									throws SQLException {
								PreparedStatement ps;
								ps = connection.prepareStatement(
	                                    "update FILTERING_CASCADE_BILLINGDETAILS set OWNER = ? where USER_ID = ?"
	                                );
								ps.setString(1, "Doe John");
                                ps.setLong(2, someUserId);
                                logger.info("Changing the owner name.");
                                ps.executeUpdate();
							}
							
						});
						tx.commit();
						em.close();
					}catch(Exception ex){
						ex.printStackTrace();
					}
					return null;
				}
            }).get();
            logger.info("Calling refresh on the user");
            /*
             * Has the cascade on refresh not been set, then the billingDetails
             * collection would not have been initialized with the new values, in this
             * case it is the new owner name. 
             */
            em.refresh(user);
            logger.info("User name "+ user.getUserName());
            for (BillingDetails bd : user.getBillingDetails()) {
            	logger.info("For the billing method id "+bd.getId()+" owner name "+bd.getOwner());
                assertEquals(bd.getOwner(), "Doe John");
            }
            tx.commit();
            em.close();
            
		}finally{
			TRANSACTION_MANAGER.rollback();
		}
	}
	
	

}
