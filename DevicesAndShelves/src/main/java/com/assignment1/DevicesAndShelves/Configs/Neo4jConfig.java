package com.assignment1.DevicesAndShelves.Configs;

import jakarta.annotation.PreDestroy;
import org.neo4j.driver.AuthTokens;
import org.neo4j.driver.Driver;
import org.neo4j.driver.GraphDatabase;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

// config/Neo4jConfig.java
@Component
@Configuration
public class Neo4jConfig {
    private static final Logger logger = LoggerFactory.getLogger(Neo4jConfig.class);

    @Value("${spring.neo4j.uri}")
    private String uri;

    @Value("${spring.neo4j.authentication.username}")
    private String username;

    @Value("${spring.neo4j.authentication.password}")
    private String password;

    private Driver driver;

    @Bean
    public Driver neo4jDriver() {
        if (driver == null) {
            driver = GraphDatabase.driver(uri, AuthTokens.basic(username, password));
            driver.verifyConnectivity();
            logger.info("Database initialised successfully");
        }
        return driver;
    }

    // OR

    // Problem 1 – Misconfigured property lookups Spring placeholders were passed directly into
    // System.getProperty, so the driver always received literal strings like "${spring.neo4j.uri}",
    // causing authentication failures. Injecting the properties with @Value resolves this while
    // keeping the bean configurable.
//    @Bean
//    public Driver neo4jDriver() {
//        var driver = GraphDatabase.driver(
//                System.getProperty("${spring.neo4j.uri}"), // (1)
//                AuthTokens.basic(
//                        System.getProperty("${spring.neo4j.authentication.username}"), // (2)
//                        System.getProperty("${spring.neo4j.authentication.password}"))
//        );
//        driver.verifyConnectivity();
//        logger.info("Database initialised successfully");
//        return driver;
//    }

    @PreDestroy
    public void closeDriver() {
        if (driver != null) {
            driver.close();
        }
    }

}