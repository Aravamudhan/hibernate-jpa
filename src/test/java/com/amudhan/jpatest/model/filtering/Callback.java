package com.amudhan.jpatest.model.filtering;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

import javax.persistence.EntityManager;
import javax.transaction.UserTransaction;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.Test;

import com.amudhan.jpatest.environment.JPASetupTest;
import com.amudhan.jpatest.model.filtering.callback.CurrentUser;
import com.amudhan.jpatest.model.filtering.callback.Item;
import com.amudhan.jpatest.model.filtering.callback.Mail;
import com.amudhan.jpatest.model.filtering.callback.User;

public class Callback extends JPASetupTest {

	private static Logger logger = LoggerFactory.getLogger(Callback.class);
	@Override
    public void configurePersistenceUnit() throws Exception {
        configurePersistenceUnit("FilteringCallbackPU");
    }
	
	@Test
    public void notifyPostPersist() throws Throwable {
		UserTransaction tx = TRANSACTION_MANAGER.getUserTransaction();
		try{
			tx.begin();
			EntityManager em = jpaSetup.createEntityManager();
			{
				User user = new User("Sherlock");
                CurrentUser.INSTANCE.set(user);
                em.persist(user);
                assertEquals(Mail.INSTANCE.size(), 0);
                em.flush();
                assertEquals(Mail.INSTANCE.size(), 1);
                
                for(String mailMessage : Mail.INSTANCE){
                	logger.info(mailMessage);
                }
                Mail.INSTANCE.clear();
                
                Item item = new Item("Some item name", user);
                em.persist(item);
                assertEquals(Mail.INSTANCE.size(), 0);
                em.flush();
                assertEquals(Mail.INSTANCE.size(), 1);
                assertTrue(Mail.INSTANCE.get(0).contains("Sherlock"));
                User userAnother = new User("Watson");
                User userMore = new User("Irene");
                em.persist(userAnother);
                em.persist(userMore);
                em.flush();
                for(String mailMessage : Mail.INSTANCE){
                	logger.info(mailMessage);
                }
                Mail.INSTANCE.clear();
                
                CurrentUser.INSTANCE.set(null);
			}
			em.clear();
		} finally{
			TRANSACTION_MANAGER.rollback();
		}
	}
}
