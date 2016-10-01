package com.amudhan.jpatest.environment;

import bitronix.tm.resource.jdbc.PoolingDataSource;

public enum DatabaseProduct {
  H2(
    new DataSourceConfiguration() {
	@Override
	  public void configure(PoolingDataSource ds, String connectionURL) {
	    ds.setClassName("org.h2.jdbcx.JdbcDataSource");
        ds.getDriverProperties().put("URL", connectionURL != null ? connectionURL : "jdbc:h2:~/test");
        ds.getDriverProperties().put("user", "-user");
      }
	},
	org.hibernate.dialect.H2Dialect.class.getName()
  );
  public DataSourceConfiguration configuration;
  public String hibernateDialect;
  private DatabaseProduct(DataSourceConfiguration configuration, String hibernateDialect) {
    this.configuration = configuration;
	this.hibernateDialect = hibernateDialect;
  }
  public interface DataSourceConfiguration {
    void configure(PoolingDataSource ds, String connectionURL);
  }
}
