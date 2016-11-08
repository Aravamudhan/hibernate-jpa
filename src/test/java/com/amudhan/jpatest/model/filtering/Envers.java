package com.amudhan.jpatest.model.filtering;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNull;

import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.transaction.UserTransaction;

import org.hibernate.criterion.MatchMode;
import org.hibernate.envers.AuditReader;
import org.hibernate.envers.AuditReaderFactory;
import org.hibernate.envers.DefaultRevisionEntity;
import org.hibernate.envers.RevisionType;
import org.hibernate.envers.query.AuditEntity;
import org.hibernate.envers.query.AuditQuery;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.Test;

import com.amudhan.jpatest.environment.JPASetupTest;
import com.amudhan.jpatest.model.filtering.envers.Item;
import com.amudhan.jpatest.model.filtering.envers.User;

public class Envers extends JPASetupTest {
	
	private Logger logger = LoggerFactory.getLogger(Envers.class);
	
	@Override
    public void configurePersistenceUnit() throws Exception {
        configurePersistenceUnit("FilteringEnversPU");
    }
	
	//@Test
	public void auditLogging() throws Throwable {
		UserTransaction tx = TRANSACTION_MANAGER.getUserTransaction();
		try{
			Long ITEM_ID;
            Long USER_ID;
            {
            	logger.info("Creating an user and item");
                tx.begin();
                EntityManager em = jpaSetup.createEntityManager();

                User user = new User("johndoe");
                em.persist(user);

                Item item = new Item("Foo", user);
                em.persist(item);

                tx.commit();
                em.close();

                ITEM_ID = item.getId();
                USER_ID = user.getId();
            }
            Date TIMESTAMP_CREATE = new Date();
            {
                logger.info("Updating an item");
                tx.begin();
                EntityManager em = jpaSetup.createEntityManager();

                Item item = em.find(Item.class, ITEM_ID);
                item.setName("Bar");
                item.getSeller().setUserName("doejohn");

                tx.commit();
                em.close();
            }
            Date TIMESTAMP_UPDATE = new Date();
            {
            	logger.info("Removing an item");
                tx.begin();
                EntityManager em = jpaSetup.createEntityManager();

                Item item = em.find(Item.class, ITEM_ID);
                em.remove(item);

                tx.commit();
                em.close();
            }
            Date TIMESTAMP_DELETE = new Date();
            /*
             * In the block data is fetched using known time stamps.
             */
            {
            	tx.begin();
            	EntityManager em = jpaSetup.createEntityManager();
            	AuditReader auditReader = AuditReaderFactory.get(em);
            	/*
                 * In the following listing, revision numbers are obtained using
                 * the known time stamps.
                 */
            	Number revisionCreate = auditReader.getRevisionNumberForDate(TIMESTAMP_CREATE);
                Number revisionUpdate = auditReader.getRevisionNumberForDate(TIMESTAMP_UPDATE);
                Number revisionDelete  = auditReader.getRevisionNumberForDate(TIMESTAMP_DELETE);
                logger.info("Create time stamp "+revisionCreate.toString());
                logger.info("Update time stamp "+revisionUpdate.toString());
                logger.info("Delete time stamp "+revisionDelete.toString());
                /*
                 * Using an id and entity type, all its related revisions can be obtained.
                 * Useful for showing a list of historical data.
                 */
                List<Number> itemRevisions = auditReader.getRevisions(Item.class, ITEM_ID);
                assertEquals(itemRevisions.size(), 3);
                logger.info("The list of time stamps for the item id "+ITEM_ID);
                for(Number itemRevision : itemRevisions){
                	logger.info(itemRevision.toString());
                }
                List<Number> userRevisions = auditReader.getRevisions(User.class, USER_ID);
                assertEquals(userRevisions.size(), 2);
                logger.info("The list of time stamps for the user id "+USER_ID);
                for(Number userRevision : userRevisions){
                	logger.info(userRevision.toString());
                }
                em.clear();
                
                /*
                 * In this block time stamp data is considered to be not unknown. 
                 */
                /*
                 * This fetches all the revision numbers associated with an entity.
                 * Revision numbers are incremented from low to high. Higher the
                 * revision number, the latest the version is.
                 */
                {
                	AuditQuery query = auditReader.createQuery().forRevisionsOfEntity(Item.class, false , false);
                    @SuppressWarnings("unchecked")
    				List<Object[]> result = query.getResultList();
                    for(Object[] tuple : result){
                    	Item item = (Item) tuple[0];
                        DefaultRevisionEntity revision = (DefaultRevisionEntity)tuple[1];
                        RevisionType revisionType = (RevisionType)tuple[2];
                        if (revision.getId() == 1) {
                            assertEquals(revisionType, RevisionType.ADD);
                            assertEquals(item.getName(), "Foo");
                        } else if (revision.getId() == 2) {
                            assertEquals(revisionType, RevisionType.MOD);
                            assertEquals(item.getName(), "Bar");
                        } else if (revision.getId() == 3) {
                            assertEquals(revisionType, RevisionType.DEL);
                            assertNull(item);
                        }
                    }
                }
                em.clear();
                {
                	/*
                	 * The find method of AuditReader returns an entity given a revision.
                	 * This entity is not added to the persistent state like the result
                	 * of find from EntityManager API.
                	 */
                	Item item = auditReader.find(Item.class, ITEM_ID, revisionCreate);
                    assertEquals(item.getName(), "Foo");
                    assertEquals(item.getSeller().getUserName(), "johndoe");
                    
                    /*
                     * After the entity item was updated, its version was stored in the
                     * revisionUpdate variable. This method returns the entity state just
                     * after the specified update was performed.
                     */
                    Item modifiedItem = auditReader.find(Item.class, ITEM_ID, revisionUpdate);
                    assertEquals(modifiedItem.getName(), "Bar");
                    assertEquals(modifiedItem.getSeller().getUserName(), "doejohn");
                    
                    /*
                     * There is no item entity in the revisionDelete hence null is returned.
                     */
                    Item deletedItem = auditReader.find(Item.class, ITEM_ID, revisionDelete);
                    assertNull(deletedItem);
                    
                    /*
                     * In this revision user entity was not deleted/modified, hence 
                     * the latest/or nearest revision is returned. 
                     */
                    User user = auditReader.find(User.class, USER_ID, revisionDelete);
                    assertEquals(user.getUserName(), "doejohn");
                }
                em.clear();
                {
                	AuditQuery query = auditReader.createQuery()
                            .forEntitiesAtRevision(Item.class, revisionUpdate);
                	query.add(
                            AuditEntity.property("name").like("Ba", MatchMode.START)
                        );
                	query.add(
                            AuditEntity.relatedId("seller").eq(USER_ID)
                        );
                	query.addOrder(
                            AuditEntity.property("name").desc()
                        );
                	query.setFirstResult(0);
                    query.setMaxResults(10);
                    assertEquals(query.getResultList().size(), 1);
                    Item result = (Item)query.getResultList().get(0);
                    assertEquals(result.getSeller().getUserName(), "doejohn");
                }
                em.clear();
                {
                	AuditQuery query = auditReader.createQuery()
                            .forEntitiesAtRevision(Item.class, revisionUpdate);
                	query.addProjection(
                            AuditEntity.property("name")
                        );
                	assertEquals(query.getResultList().size(), 1);
                    String result = (String)query.getSingleResult();
                    assertEquals(result, "Bar");
                }
                em.clear();
                tx.commit();
                em.close();
            }
            
		}finally {
			TRANSACTION_MANAGER.rollback();
		}
	}
	
	/*
	 * Creates a user and modifies its data twice.
	 */
	@Test
	public void auditTest() throws Throwable {
		UserTransaction tx = TRANSACTION_MANAGER.getUserTransaction();
		try{
			EntityManager em = jpaSetup.createEntityManager();
			Long userId;
			{
				tx.begin();
				logger.info("Creating an user");
                User user = new User("UserNameOne");
                em.persist(user);
                tx.commit();
                userId = user.getId();
			}
			em.clear();
			{
				tx.begin();
				logger.info("Updating an user");
				User user = em.find(User.class, userId);
				user.setUserName("UserNameTwo");
				em.persist(user);
				tx.commit();
			}
			em.clear();
			{
				tx.begin();
				logger.info("Updating an user");
				User user = em.find(User.class, userId);
				user.setUserName("UserNameThree");
				em.persist(user);
				tx.commit();
			}
			em.clear();
			{
				tx.begin();
				AuditReader auditReader = AuditReaderFactory.get(em);
				List<Number> revisions = auditReader.getRevisions(User.class, userId);
				for(Number revision : revisions){
					User user = auditReader.find(User.class, userId, revision);
					logger.info("Revision number : "+revision);
					logger.info("User id "+user.getId()+" User name: "+user.getUserName());
				}
				tx.commit();
			}
			em.close();
		}finally{
			TRANSACTION_MANAGER.rollback();
		}
	}
	
}
