package me.dabpessoa.service;

import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.context.annotation.*;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.annotation.Resource;
import javax.swing.*;

/**
 * Created by diego.pessoa on 07/03/2017.
 */
@Configuration
@ComponentScan({"me.dabpessoa.*"})
@Lazy(true)
@EnableTransactionManagement
@Development
public class SpringConfigDevelopment {

    @Resource
    private EnvironmentManager environmentManager;

    @Bean
    public String stringTest() {
        System.out.println(environmentManager);
        return "ambiente de development";
    }

    @Bean
    public DriverManagerDataSource dataSource(){
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName(environmentManager.getEnvironmentProperty(EnvironmentManager.SECTION.DEVELOPMENT, "dataSource.postgres.driverClass"));
        dataSource.setUrl(environmentManager.getEnvironmentProperty(EnvironmentManager.SECTION.DEVELOPMENT, "dataSource.postgres.url"));
        dataSource.setUsername(environmentManager.getEnvironmentProperty(EnvironmentManager.SECTION.DEVELOPMENT, "dataSource.postgres.username"));
        dataSource.setPassword(environmentManager.getEnvironmentProperty(EnvironmentManager.SECTION.DEVELOPMENT, "dataSource.postgres.password"));
        return dataSource;
    }

    @Bean
    public PlatformTransactionManager transactionManager() {
        return new DataSourceTransactionManager(dataSource());
    }

    @Bean
    public LocalContainerEntityManagerFactoryBean entityManagerFactory() {
        LocalContainerEntityManagerFactoryBean localContainerEntityManagerFactoryBean = new LocalContainerEntityManagerFactoryBean();
        localContainerEntityManagerFactoryBean.setDataSource(dataSource());
        localContainerEntityManagerFactoryBean.setPersistenceUnitName("postgresPU");
        localContainerEntityManagerFactoryBean.setJpaVendorAdapter(new HibernateJpaVendorAdapter());
        localContainerEntityManagerFactoryBean.setPackagesToScan(new String[]{"me.dabpessoa"});
        localContainerEntityManagerFactoryBean.setJpaProperties(environmentManager.createProperties("hibernate.dialect", "hibernate.show_sql",
                "cache.provider_class", "hibernate.format_sql", "hibernate.generate_statistics", "hibernate.temp.use_jdbc_metadata_defaults"));
        return localContainerEntityManagerFactoryBean;
    }

}
