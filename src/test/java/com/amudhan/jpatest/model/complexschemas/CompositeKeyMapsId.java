package com.amudhan.jpatest.model.complexschemas;

import javax.persistence.EntityManager;
import javax.transaction.UserTransaction;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.amudhan.jpatest.environment.DatabaseProduct;
import com.amudhan.jpatest.environment.JPASetup;
import com.amudhan.jpatest.environment.TransactionManagerTest;
import com.amudhan.jpatest.model.complexschemas.compositekey.mapsid.Department;
import com.amudhan.jpatest.model.complexschemas.compositekey.mapsid.User;
import com.amudhan.jpatest.model.complexschemas.compositekey.mapsid.UserId;

public class CompositeKeyMapsId extends TransactionManagerTest {

	private Logger logger = LoggerFactory.getLogger(CompositeKeyEmbeddedId.class);
	private JPASetup jpa;
	
	@BeforeClass
	public void beforeMethod() {
		jpa = new JPASetup(DatabaseProduct.H2, "CompositeKeyMapsId");
		jpa.dropSchema();
		jpa.createSchema();
	}
	
	@Test
	public void storeLoadWithCompsiteKeyMapsId() throws Exception{
		UserTransaction tx = TRANSACTION_MANAGER.getUserTransaction();
		try{
			tx.begin();
			EntityManager entityManager = jpa.createEntityManager();
			Department department = new Department("Greate department");
			entityManager.persist(department);
			/* Since the departmentNumber of UserId is a @MapsId, hibernate
			 * manages that using the department added to User. null here does not
			 * throw ConstraintViolationException because of that. Hibernate
			 * will ignore what ever value that would be set for departmentNumber.*/
			User user = new User(new UserId("Awesome User", null), "Buyer");
			user.setDepartment(department);
			entityManager.persist(user);
			tx.commit();
			entityManager.close();
			
			tx.begin();
			EntityManager entityManagerAnother = jpa.createEntityManager();
			User userAnother = entityManagerAnother.find(User.class, user.getId());
			logger.info(userAnother.toString());
			tx.commit();
			entityManagerAnother.close();
			
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			TRANSACTION_MANAGER.rollback();
		}
	}
}
