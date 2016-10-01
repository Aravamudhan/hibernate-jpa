package com.amudhan.jpatest.model.helloWorldJPA;

import java.time.LocalDateTime;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.transaction.UserTransaction;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.Test;

import static org.testng.Assert.assertEquals;

import com.amudhan.jpatest.environment.TransactionManagerTest;
import com.amudhan.jpatest.model.helloworld.Message;


public class HelloWorldJPA extends TransactionManagerTest{
  
  private static Logger logger = LoggerFactory.getLogger(HelloWorldJPA.class);
  
  @Test
  public void storeLoadMessage(){
	  EntityManagerFactory entityManagerFactory = 
	    Persistence.createEntityManagerFactory("HelloWorldPU");
	  try{
		{
          UserTransaction tx  = TRANSACTION_MANAGER.getUserTransaction();
          tx.begin();
          EntityManager entityManager = entityManagerFactory.createEntityManager();
          Message message = new Message();
          message.setText("Helloworld @"+LocalDateTime.now());
          entityManager.persist(message);
          tx.commit();
          entityManager.close();
		}
		{
		  UserTransaction tx  = TRANSACTION_MANAGER.getUserTransaction();
	      tx.begin();
	      EntityManager entityManager = entityManagerFactory.createEntityManager();
	      @SuppressWarnings("unchecked")
		List<Message> messages = entityManager.createQuery("select m from HELLOWORLD_MESSAGE m").getResultList();
	      for(Message message : messages){
	    	  logger.info(message.toString());
	      }
          assertEquals(messages.size(), 1);
          tx.commit();
          entityManager.close();
		}
		  
	  }catch(Exception e){
	    logger.info("Exception inside the transaction "+e);
	  } finally {
		  
	  }
  }
}
