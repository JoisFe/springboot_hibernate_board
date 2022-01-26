package com.example.board.config.hibernate;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.hibernate5.HibernateTransactionManager;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;

import javax.sql.DataSource;
import java.util.Properties;

@Configuration
public class DBConfiguration {
    @Autowired
    GlobalPropertySource globalPropertySource;

    @Bean
    public DataSource getDataSource() {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();

        dataSource.setDriverClassName(globalPropertySource.getDriver());
        dataSource.setUrl(globalPropertySource.getUrl());
        dataSource.setUsername(globalPropertySource.getUsername());
        dataSource.setPassword(globalPropertySource.getPassword());

        System.out.println("## getDataSource: " + dataSource);
        return dataSource;
    }

    @Bean
    public static SessionFactory getSessionFactory(DataSource dataSource) throws Exception {
        Properties properties = new Properties();
        // See: application.properties
        properties.put("hibernate.dialect", "org.hibernate.dialect.MySQL5Dialect");
        properties.put("hibernate.show_sql", true);
        properties.put("current_session_context_class", "thread");
        properties.put("hibernate.format_sql", true);
        properties.put("hibernate.hbm2ddl.auto", "update");
        LocalSessionFactoryBean factoryBean = new LocalSessionFactoryBean();
        factoryBean.setDataSource(dataSource);
        factoryBean.setHibernateProperties(properties);
        // Package contain entity classes
        factoryBean.setPackagesToScan("com.example.board.domain.*");
        factoryBean.afterPropertiesSet();
        //
        SessionFactory sf = factoryBean.getObject();
        System.out.println("## getSessionFactory: " + sf);
        return sf;
    }

    @Bean
    public HibernateTransactionManager transactionManager(SessionFactory sessionFactory) {
        HibernateTransactionManager transactionManager = new HibernateTransactionManager(sessionFactory);
        transactionManager.setDataSource(getDataSource());
        transactionManager.setHibernateManagedSession(false);
        return transactionManager;
    }
}
