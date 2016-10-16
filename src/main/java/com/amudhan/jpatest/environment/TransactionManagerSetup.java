package com.amudhan.jpatest.environment;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;
import javax.transaction.Status;
import javax.transaction.UserTransaction;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import bitronix.tm.TransactionManagerServices;
import bitronix.tm.resource.jdbc.PoolingDataSource;

public class TransactionManagerSetup {

	public static final String DATASOURCE_NAME = "dataSourceOne";
	private static final Logger logger = LoggerFactory
			.getLogger(TransactionManagerSetup.class);
	protected final Context context;
	protected final PoolingDataSource dataSource;
	protected final DatabaseProduct databaseProduct;

	public TransactionManagerSetup(DatabaseProduct databaseProduct)
			throws Exception {
		this(databaseProduct, null);
	}

	public TransactionManagerSetup(DatabaseProduct databaseProduct,
			String connectionURL) throws NamingException {
		context = new InitialContext();
		logger.info("Starting database connection pool");
		/*
		 * logger.info("Setting stable unique identifier for transaction recovery"
		 * );
		 * TransactionManagerServices.getConfiguration().setServerId("myServer1234"
		 * );
		 */

		logger.info("Disabling JMX binding of manager in unit tests");
		TransactionManagerServices.getConfiguration().setDisableJmx(true);

		/*
		 * logger.info("Disabling transaction logging for unit tests");
		 * TransactionManagerServices.getConfiguration().setJournal("null");
		 */

		logger.info("Disabling warnings when the database isn't accessed in a transaction");
		TransactionManagerServices.getConfiguration()
				.setWarnAboutZeroResourceTransaction(false);

		logger.info("Creating connection pool");
		dataSource = new PoolingDataSource();
		dataSource.setUniqueName(DATASOURCE_NAME);
		dataSource.setMinPoolSize(1);
		dataSource.setMaxPoolSize(5);
		dataSource.setPreparedStatementCacheSize(10);

		// Our locking/versioning tests assume READ COMMITTED transaction
		// isolation. This is not the default on MySQL InnoDB, so we set
		// it here explicitly.
		dataSource.setIsolationLevel("READ_COMMITTED");

		// Hibernate's SQL schema generator calls connection.setAutoCommit(true)
		// and we use auto-commit mode when the EntityManager is in suspended
		// mode and not joined with a transaction.
		dataSource.setAllowLocalTransactions(true);

		logger.info("Setting up database connection: " + databaseProduct);
		this.databaseProduct = databaseProduct;
		databaseProduct.configuration.configure(dataSource, connectionURL);

		logger.info("Initializing transaction and resource management");
		dataSource.init();
	}

	public Context getNamingContext() {
		return context;
	}

	public UserTransaction getUserTransaction() {
		try {
			return (UserTransaction) getNamingContext().lookup(
					"java:comp/UserTransaction");
		} catch (Exception ex) {
			throw new RuntimeException(ex);
		}
	}

	public DataSource getDataSource() {
		try {
			return (DataSource) getNamingContext().lookup(DATASOURCE_NAME);
		} catch (Exception ex) {
			throw new RuntimeException(ex);
		}
	}

	public void rollback() {
		UserTransaction tx = getUserTransaction();
		try {
			if (tx.getStatus() == Status.STATUS_ACTIVE
					|| tx.getStatus() == Status.STATUS_MARKED_ROLLBACK)
				tx.rollback();
		} catch (Exception ex) {
			System.err
					.println("Rollback of transaction failed, trace follows!");
			ex.printStackTrace(System.err);
		}
	}

	public void stop() throws Exception {
		logger.info("Stopping database connection pool");
		dataSource.close();
		TransactionManagerServices.getTransactionManager().shutdown();
	}

}
