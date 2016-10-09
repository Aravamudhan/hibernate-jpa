package com.amudhan.jpatest.model.complexschemas;

import javax.persistence.EntityManager;
import javax.transaction.UserTransaction;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.amudhan.jpatest.environment.DatabaseProduct;
import com.amudhan.jpatest.environment.JPASetup;
import com.amudhan.jpatest.environment.TransactionManagerTest;
import com.amudhan.jpatest.model.complexschemas.secondarytable.Address;
import com.amudhan.jpatest.model.complexschemas.secondarytable.User;

public class SecondaryTable extends TransactionManagerTest {
	
	private JPASetup jpa;
	
	@BeforeClass
	public void beforeMethod() {
		jpa = new JPASetup(DatabaseProduct.H2, "SecondaryTablePU");
		jpa.dropSchema();
		jpa.createSchema();
	}
	
	@Test
	public void storeLoad(){
		UserTransaction tx = TRANSACTION_MANAGER.getUserTransaction();
		try{
			tx.begin();
			EntityManager entityManager = jpa.createEntityManager();
			User user = new User();
			user.setUserName("Great user");
			Address homeAddress = new Address("Some Street 123", "12345", "Some City");
			Address billingAddress = new Address("Office street", "125", "Some City");
			user.setHomeAddress(homeAddress);
			/* This will insert the data into the BILLING_ADDRESS table.*/
			user.setBillingAddress(billingAddress);
			entityManager.persist(user);
			tx.commit();
			entityManager.close();
			
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			TRANSACTION_MANAGER.rollback();
		}
	}


}
