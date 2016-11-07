package com.amudhan.jpatest.model.filtering;

import static org.testng.Assert.assertEquals;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.transaction.UserTransaction;

import org.hibernate.Session;
import org.hibernate.engine.spi.SessionImplementor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.Test;

import com.amudhan.jpatest.environment.JPASetupTest;
import com.amudhan.jpatest.model.filtering.interceptors.AuditLogRecord;
import com.amudhan.jpatest.model.filtering.interceptors.Item;
import com.amudhan.jpatest.model.filtering.interceptors.User;

public class AuditLogging extends JPASetupTest{

	private static Logger logger = LoggerFactory
			.getLogger(AuditLogging.class);
	
	@Override
    public void configurePersistenceUnit() throws Exception {
        configurePersistenceUnit("FilteringInterceptorPU");
    }
	
	@Test
    public void writeAuditLog() throws Throwable {
		UserTransaction tx = TRANSACTION_MANAGER.getUserTransaction();
		try{
			Long currentUserId;
			{
				tx.begin();
				EntityManager em = jpaSetup.createEntityManager();
				User currentUser = new User("johndoe");
				em.persist(currentUser);
				tx.commit();
				em.close();
				currentUserId = currentUser.getId();
			}
			EntityManagerFactory emf = jpaSetup.getEntityManagerFactory();
			Map<String, String> properties = new HashMap<String, String>();
			properties.put(org.hibernate.cfg.AvailableSettings.SESSION_SCOPED_INTERCEPTOR,
							AuditLogInterceptor.class.getName());
			EntityManager em = emf.createEntityManager();
			Session session = em.unwrap(Session.class);
			//TODO: Throws ClassCastException. Can not be casted to AuditLogInterceptor
			// from EmptyInterceptor. Check this.
			AuditLogInterceptor interceptor =
					(AuditLogInterceptor) ((SessionImplementor) session).getInterceptor();
			interceptor.setCurrentSession(session);
			interceptor.setCurrentUserId(currentUserId);
			
			tx.begin();
			em.joinTransaction();
			Item item = new Item("Foo");
			em.persist(item);
			tx.commit();
			em.clear();
			
			tx.begin();
            em.joinTransaction();
            List<AuditLogRecord> logs = em.createQuery(
                    "SELECT ar FROM AuditLogRecord ar",
                    AuditLogRecord.class
                ).getResultList();
            assertEquals(logs.size(), 1);
            assertEquals(logs.get(0).getMessage(), "insert");
            assertEquals(logs.get(0).getEntityClass(), Item.class);
            assertEquals(logs.get(0).getEntityId(), item.getId());
            assertEquals(logs.get(0).getUserId(), currentUserId);
            logger.info("Audit log records");
            for(AuditLogRecord log : logs){
            	logger.info(log.toString());
            }
            em.createQuery("delete AuditLogRecord").executeUpdate();
            
            tx.begin();
            em.joinTransaction();
            item = em.find(Item.class, item.getId());
            item.setName("Bar");
            tx.commit();
            em.clear();
            
            tx.begin();
            em.joinTransaction();
            logs = em.createQuery(
                "select lr from AuditLogRecord lr",
                AuditLogRecord.class
            ).getResultList();
            assertEquals(logs.size(), 1);
            assertEquals(logs.get(0).getMessage(), "update");
            assertEquals(logs.get(0).getEntityClass(), Item.class);
            assertEquals(logs.get(0).getEntityId(), item.getId());
            assertEquals(logs.get(0).getUserId(), currentUserId);
            tx.commit();
            em.close();
			
		}finally{
			TRANSACTION_MANAGER.rollback();
		}
	}

}
