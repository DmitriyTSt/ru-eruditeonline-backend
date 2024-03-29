package ru.erudyt.online.config

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.boot.jdbc.DataSourceBuilder
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Primary
import org.springframework.data.jpa.repository.config.EnableJpaRepositories
import org.springframework.orm.jpa.JpaTransactionManager
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter
import org.springframework.transaction.PlatformTransactionManager
import org.springframework.transaction.annotation.EnableTransactionManagement
import javax.persistence.EntityManagerFactory
import javax.sql.DataSource
import ru.erudyt.online.config.property.DialectSettings

@Configuration
@EnableJpaRepositories(
    entityManagerFactoryRef = "resourceEntityManagerFactory",
    transactionManagerRef = "resourceTransactionManager",
    basePackages = ["ru.erudyt.online.repository.resource"]
)
@EnableTransactionManagement
@EnableConfigurationProperties(DialectSettings::class)
class ResourceDatasourceConfig @Autowired constructor(
    private val dialectSettings: DialectSettings,
) {

    companion object {
        private const val ENTITY_PACKAGE = "ru.erudyt.online.entity.resource"
    }

    @Bean("resourceDataSource")
    @Primary
    @ConfigurationProperties(prefix = "resource.datasource")
    fun resourceDataSource(): DataSource {
        return DataSourceBuilder.create().build()
    }

    @Primary
    @Bean("resourceEntityManagerFactory")
    fun resourceEntityManagerFactory(
        @Qualifier("resourceDataSource") dataSource: DataSource,
    ): LocalContainerEntityManagerFactoryBean {
        return LocalContainerEntityManagerFactoryBean().apply {
            setDataSource(dataSource)
            setPackagesToScan(ENTITY_PACKAGE)
            persistenceUnitName = "resource"
            jpaVendorAdapter = HibernateJpaVendorAdapter()
            setJpaPropertyMap(
                mapOf(
                    "hibernate.dialect" to dialectSettings.resourceDialect,
//                    "hibernate.show_sql" to "true",
                )
            )
        }
    }

    @Primary
    @Bean("resourceTransactionManager")
    fun resourceTransactionManager(
        @Qualifier("resourceEntityManagerFactory") resourceEntityManagerFactory: EntityManagerFactory,
    ): PlatformTransactionManager {
        return JpaTransactionManager(resourceEntityManagerFactory)
    }
}