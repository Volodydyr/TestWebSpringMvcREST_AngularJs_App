package com.portal.config;

import static org.hibernate.cfg.AvailableSettings.HBM2DDL_AUTO;

import java.net.*;
import java.util.Properties;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.*;
import org.springframework.core.env.Environment;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.rest.core.config.RepositoryRestConfiguration;
import org.springframework.data.rest.webmvc.config.RepositoryRestMvcConfiguration;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.jpa.*;
import org.springframework.orm.jpa.vendor.*;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@EnableJpaRepositories(basePackages = { "com.portal.repository" })
@EnableTransactionManagement
@ComponentScan(basePackages = "com.portal", excludeFilters = {
    @ComponentScan.Filter(value = Controller.class, type = FilterType.ANNOTATION),
    @ComponentScan.Filter(value = Configuration.class, type = FilterType.ANNOTATION)

})
@PropertySource(value = { "classpath:application.properties" })
public class AppConfig extends RepositoryRestMvcConfiguration {
  
  @Override
  protected void configureRepositoryRestConfiguration(RepositoryRestConfiguration config) {
    super.configureRepositoryRestConfiguration(config);
    try {
      config.setBaseUri(new URI("/api"));
    } catch (URISyntaxException e) {
      e.printStackTrace();
    }
  }

  @Autowired
  private Environment environment;

  @Bean
  public DataSource dataSource() {
    DriverManagerDataSource dataSource = new DriverManagerDataSource();
    dataSource.setDriverClassName(environment.getRequiredProperty("jdbc.driverClassName"));
    dataSource.setUrl(environment.getRequiredProperty("jdbc.url"));
    dataSource.setUsername(environment.getRequiredProperty("jdbc.username"));
    dataSource.setPassword(environment.getRequiredProperty("jdbc.password"));
    return dataSource;
  }

  @Bean
  public JpaVendorAdapter jpaVendorAdapter() {
    HibernateJpaVendorAdapter adapter = new HibernateJpaVendorAdapter();
    adapter.setShowSql(true);
    adapter.setGenerateDdl(true);
    adapter.setDatabase(Database.MYSQL);
    return adapter;
  }

  @Bean
  public LocalContainerEntityManagerFactoryBean entityManagerFactory() throws ClassNotFoundException {
    LocalContainerEntityManagerFactoryBean factoryBean = new LocalContainerEntityManagerFactoryBean();
    factoryBean.setDataSource(dataSource());
    factoryBean.setPackagesToScan("com.portal.model");
    factoryBean.setJpaVendorAdapter(jpaVendorAdapter());
    factoryBean.setJpaProperties(jpaProperties());

    return factoryBean;
  }

  @Bean
  public JpaTransactionManager transactionManager() throws ClassNotFoundException {
    JpaTransactionManager transactionManager = new JpaTransactionManager();
    transactionManager.setEntityManagerFactory(entityManagerFactory().getObject());

    return transactionManager;
  }

  @Bean
  public Properties jpaProperties() {
    Properties properties = new Properties();
//    properties.put(HBM2DDL_AUTO, "create-drop");
    properties.put("hibernate.dialect", environment.getRequiredProperty("hibernate.dialect"));
    properties.put("hibernate.show_sql", environment.getRequiredProperty("hibernate.show_sql"));
    properties.put("hibernate.format_sql", environment.getRequiredProperty("hibernate.format_sql"));
    return properties;
  }
}
