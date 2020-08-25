package teun.demo.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.dao.annotation.PersistenceExceptionTranslationPostProcessor;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.persistence.EntityManagerFactory;
import javax.servlet.annotation.WebServlet;
import javax.sql.DataSource;
import java.util.Properties;

import com.google.common.base.Preconditions;

@Configuration
@EnableTransactionManagement
@PropertySource({"classpath:persistence-h2-${envTarget:local}.properties"})
@ComponentScan({"teun.demo"})
@EnableJpaRepositories(basePackages = "teun.demo.repository")
public class PersistenceJPAConfig {

    @Autowired
    private Environment env;

    public PersistenceJPAConfig() {
        super();
    }

    @Bean
    public LocalContainerEntityManagerFactoryBean entityManagerFactory() {
        final LocalContainerEntityManagerFactoryBean em = new LocalContainerEntityManagerFactoryBean();
        em.setDataSource( dataSource() );
        em.setPackagesToScan( "teun.demo.domain" );

        final HibernateJpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
        em.setJpaVendorAdapter( vendorAdapter );
        em.setJpaProperties( additionalProperties() );

        return em;
    }

    @Bean
    public DataSource dataSource() {
        final DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName( Preconditions.checkNotNull( env.getProperty( "jdbc.driverClassName" ) ) );
        dataSource.setUrl( Preconditions.checkNotNull( env.getProperty( "jdbc.url" ) ) );
        dataSource.setUsername( Preconditions.checkNotNull( env.getProperty( "jdbc.user" ) ) );
        dataSource.setPassword( Preconditions.checkNotNull( env.getProperty( "jdbc.pass" ) ) );

        return dataSource;
    }

    @Bean
    public PlatformTransactionManager transactionManager(final EntityManagerFactory emf ) {
        final JpaTransactionManager transactionManager = new JpaTransactionManager();
        transactionManager.setEntityManagerFactory( emf );
        return transactionManager;
    }

    @Bean
    public PersistenceExceptionTranslationPostProcessor exceptionTranslation() {
        return new PersistenceExceptionTranslationPostProcessor();
    }

    private Properties additionalProperties() {
        final Properties hibernateProperties = new Properties();
        hibernateProperties.setProperty( "hibernate.show_sql", env.getProperty( "hibernate.show_sql" ) );
        hibernateProperties.setProperty( "hibernate.hbm2ddl.auto", env.getProperty( "hibernate.hbm2ddl.auto" ) );
        hibernateProperties.setProperty( "hibernate.dialect", env.getProperty( "hibernate.dialect" ) );
        hibernateProperties.setProperty( "hibernate.cache.use_second_level_cache", env.getProperty( "hibernate.cache.use_second_level_cache" ) );
        hibernateProperties.setProperty( "hibernate.cache.use_query_cache", env.getProperty( "hibernate.cache.use_query_cache" ) );

        return hibernateProperties;
    }

}