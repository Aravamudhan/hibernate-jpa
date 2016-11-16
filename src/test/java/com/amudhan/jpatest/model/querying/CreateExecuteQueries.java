package com.amudhan.jpatest.model.querying;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Parameter;
import javax.persistence.Query;
import javax.persistence.TemporalType;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.ParameterExpression;
import javax.persistence.criteria.Root;
import javax.transaction.UserTransaction;

import org.hibernate.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.Test;

import com.amudhan.jpatest.shared.util.CalendarUtil;

public class CreateExecuteQueries extends QueryingTest {

	private static Logger logger = LoggerFactory
			.getLogger(CreateExecuteQueries.class);

	@Test
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public void createQueries() throws Exception {
		storeTestData();
		UserTransaction tx = TRANSACTION_MANAGER.getUserTransaction();
		try {
			tx.begin();
			EntityManager em = jpaSetup.createEntityManager();
			{
				logger.info("Query");
				/*
				 * In JPQL the entity names, the property names are case
				 * sensitive. During the refactoring care must be taken to check
				 * if any JPQL queries are affected due to the change in the
				 * entity names.
				 */
				Query query = em.createQuery("select i from QUERYING_ITEM i");
				assertEquals(query.getResultList().size(), 3);
			}
			{
				logger.info("CriteriaBuilder, CriteriaQuery, Seleting item");
				CriteriaBuilder cb = em.getCriteriaBuilder();
				CriteriaQuery criteria = cb.createQuery();
				criteria.select(criteria.from(Item.class));

				Query query = em.createQuery(criteria);
				assertEquals(query.getResultList().size(), 3);
			}
			{
				logger.info("Native query");
				Query query = em
						.createNativeQuery("select * from QUERYING_ITEM");
				assertEquals(query.getResultList().size(), 3);
			}
			tx.commit();
			em.close();

		} finally {
			TRANSACTION_MANAGER.rollback();
		}
	}

	@Test
	public void createTypedQueries() throws Exception {
		TestDataCategoriesItems testData = storeTestData();
		Long ITEM_ID = testData.items.getFirstId();
		UserTransaction tx = TRANSACTION_MANAGER.getUserTransaction();
		try {
			tx.begin();
			EntityManager em = jpaSetup.createEntityManager();
			{
				logger.info("Query and set parameter with id :" + ITEM_ID);
				Query query = em.createQuery(
						"select i from QUERYING_ITEM i where i.id = :id")
						.setParameter("id", ITEM_ID);
				Item result = (Item) query.getSingleResult();
				assertEquals(result.getId(), ITEM_ID);
			}
			{
				logger.info("TypedQuery");
				/*
				 * This does not need cast the result list from the Object to an
				 * entity, since the entity type is mentioned during the
				 * creation of a query.
				 */
				TypedQuery<Item> query = em.createQuery(
						"select i from QUERYING_ITEM i where i.id = :id",
						Item.class).setParameter("id", ITEM_ID);
				Item result = query.getSingleResult();
				assertEquals(result.getId(), ITEM_ID);
			}
			{
				logger.info("CriteriaBuilder, CriteriaQuery, Seleting item with TypedQuery");
				CriteriaBuilder cb = em.getCriteriaBuilder();
				CriteriaQuery<Item> criteria = cb.createQuery(Item.class);
				Root<Item> i = criteria.from(Item.class);
				criteria.select(i).where(cb.equal(i.get("id"), ITEM_ID));
				TypedQuery<Item> query = em.createQuery(criteria);
				Item result = query.getSingleResult();
				assertEquals(result.getId(), ITEM_ID);
			}
			tx.commit();
			em.close();
		} finally {
			TRANSACTION_MANAGER.rollback();
		}
	}

	@SuppressWarnings({ "rawtypes", "deprecation" })
	@Test
	public void createHibernateQueries() throws Exception {
		TestDataCategoriesItems testData = storeTestData();
		Long ITEM_ID = testData.items.getFirstId();
		UserTransaction tx = TRANSACTION_MANAGER.getUserTransaction();
		try {
			tx.begin();
			EntityManager em = jpaSetup.createEntityManager();
			{
				Session session = em.unwrap(Session.class);
				logger.info("Hibernate query");
				org.hibernate.query.Query query = session
						.createQuery("select i from QUERYING_ITEM i");
				assertEquals(query.getResultList().size(), 3);
			}
			{
				Session session = em.unwrap(Session.class);
				logger.info("Hibernate SQLQuery");
				org.hibernate.SQLQuery query = session.createSQLQuery(
						"select {i.*} from QUERYING_ITEM {i}").addEntity("i",
						Item.class);
				assertEquals(query.list().size(), 3);
			}
			{
				Session session = em.unwrap(Session.class);
				logger.info("Session#criteriaQuery");
				org.hibernate.Criteria query = session
						.createCriteria(Item.class);
				query.add(org.hibernate.criterion.Restrictions
						.eq("id", ITEM_ID));
				Item result = (Item) query.uniqueResult();
				assertEquals(result.getId(), ITEM_ID);

			}
			tx.commit();
			em.close();
		} finally {
			TRANSACTION_MANAGER.rollback();
		}
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Test
	public void paramterBinding() throws Exception {
		TestDataCategoriesItems testData = storeTestData();
		UserTransaction tx = TRANSACTION_MANAGER.getUserTransaction();
		try {
			tx.begin();
			EntityManager em = jpaSetup.createEntityManager();
			List<Item> items;
			{
				
				/**
				 * NEVER DO THIS. This leads to SQL Injection. An attacker could
				 * enter a SQL query instead of a searchString. This user
				 * entered query will be executed directly in the database.
				 */
				 
				String searchString = getValueEnteredByUser();
				Query query = em
						.createQuery("select i from QUERYING_ITEM where i.name = '"
								+ searchString + "'");
				items = query.getResultList();
				assertEquals(items.size(), 0);
			}
			{
				String searchString = "Foo";
				Query query = em.createQuery(
						"select i from QUERYING_ITEM i where i.name = :itemName")
						.setParameter("itemName", searchString);
				for (Parameter<?> parameter : query.getParameters()) {
					assertTrue(query.isBound(parameter));
				}
				items = query.getResultList();
				assertEquals(items.size(), 1);
			}
			{ // Temporal parameter
				Date tomorrowDate = // ...
				CalendarUtil.TOMORROW.getTime();

				Query query = em.createQuery(
						"select i from QUERYING_ITEM i where i.auctionEnd > :endDate")
						.setParameter("endDate", tomorrowDate,
								TemporalType.TIMESTAMP);

				items = query.getResultList();
				assertEquals(items.size(), 1);
			}
			{ // Entity parameter
				Item someItem = // ...
				em.find(Item.class, testData.items.getFirstId());

				Query query = em.createQuery(
						"select b from Bid b where b.item = :item")
						.setParameter("item", someItem);

				items = query.getResultList();
				assertEquals(items.size(), 3);
			}
			{
				String searchString = // ...
				"Foo";

				CriteriaBuilder cb = em.getCriteriaBuilder();

				CriteriaQuery criteria = cb.createQuery();
				Root<Item> i = criteria.from(Item.class);

				Query query = em
						.createQuery(
								criteria.select(i).where(
										cb.equal(i.get("name"), cb.parameter(
												String.class, "itemName"))))
						.setParameter("itemName", searchString);

				items = query.getResultList();
				assertEquals(items.size(), 1);
			}
			{
				String searchString = // ...
				"Foo";

				CriteriaBuilder cb = em.getCriteriaBuilder();

				CriteriaQuery criteria = cb.createQuery(Item.class);
				Root<Item> i = criteria.from(Item.class);

				ParameterExpression<String> itemNameParameter = cb
						.parameter(String.class);

				Query query = em.createQuery(
						criteria.select(i).where(
								cb.equal(i.get("name"), itemNameParameter)))
						.setParameter(itemNameParameter, searchString);

				items = query.getResultList();
				assertEquals(items.size(), 1);
			}
			{ // Positional parameters
				String searchString = "B%";
				Date tomorrowDate = CalendarUtil.TOMORROW.getTime();

				Query query = em
						.createQuery("select i from QUERYING_ITEM i where i.name like ?1 and i.auctionEnd > ?2");
				query.setParameter(1, searchString);
				query.setParameter(2, tomorrowDate, TemporalType.TIMESTAMP);

				items = query.getResultList();
				assertEquals(items.size(), 1);
			}
			tx.commit();
		} finally {
			TRANSACTION_MANAGER.rollback();
		}
	}

	private String getValueEnteredByUser() {
		return "ALWAYS FILTER VALUES ENTERED BY USERS!";
	}

	@SuppressWarnings({ "unchecked", "deprecation", "rawtypes" })
	@Test
	public void pagination() throws Exception {
		storeTestData();

		UserTransaction tx = TRANSACTION_MANAGER.getUserTransaction();
		try {
			tx.begin();
			EntityManager em = jpaSetup.createEntityManager();

			List<Item> items;
			{ // Limiting result rows
				Query query = em.createQuery("select i from QUERYING_ITEM i");
				query.setFirstResult(40).setMaxResults(10);

				items = query.getResultList();
				assertEquals(items.size(), 0);
			}
			{ // Rewrite SQL
				Query query = em
						.createNativeQuery("select * from QUERYING_ITEM");
				query.setFirstResult(40).setMaxResults(10);

				items = query.getResultList();
				assertEquals(items.size(), 0);
			}
			{ // Getting total count with a cursor
				Query query = em.createQuery("select i from QUERYING_ITEM i");

				// Unwrap the Hibernate API to use scrollable cursors.
				org.hibernate.Query hibernateQuery = query.unwrap(
						org.hibernate.jpa.HibernateQuery.class)
						.getHibernateQuery();
				/*
				 * Execute the query with a database cursor; this does not
				 * retrieve the result set into memory.
				 */
				org.hibernate.ScrollableResults cursor = hibernateQuery
						.scroll(org.hibernate.ScrollMode.SCROLL_INSENSITIVE);
				/*
				 * Jump to the last row of the result in the database, then get
				 * the row number. Since row numbers are zero-based, add one to
				 * get the total count of rows.
				 */
				cursor.last();
				int count = cursor.getRowNumber() + 1;
				// You must close the database cursor.
				cursor.close();
				/*
				 * Now execute the query again and retrieve an arbitrary page of
				 * data.
				 */
				query.setFirstResult(40).setMaxResults(10);
				assertEquals(count, 3);
				items = query.getResultList();
				assertEquals(items.size(), 0);
			}

			tx.commit();
			em.close();
		} finally {
			TRANSACTION_MANAGER.rollback();
		}
	}
}
