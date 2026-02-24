package com.assignment1.DevicesAndShelves.Configs;

import lombok.Value;
import org.neo4j.driver.AuthTokens;
import org.neo4j.driver.Driver;
import org.neo4j.driver.GraphDatabase;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

// config/Neo4jConfig.java
@Configuration
public class Neo4jConfig {
    final private String uri = "${NEO4J_URI}";
    final private String username = "${NEO4J_USERNAME}";
    final private String password = "${NEO4J_PASSWORD}";
//    @Value("${neo4j.password}") private String password;

    @Bean(destroyMethod = "close")
    public Driver neo4jDriver() {
        try(org.neo4j.driver.Driver Driver = GraphDatabase.driver(uri, AuthTokens.basic(username, password))){
            return Driver;
        }
    }

    // OR

//    public Driver neo4jDriver() {
//        try (var driver = GraphDatabase.driver(
//                System.getProperty("NEO4J_URI"), // (1)
//                AuthTokens.basic(
//                        System.getProperty("NEO4J_USERNAME"), // (2)
//                        System.getProperty("NEO4J_PASSWORD"))
//        )
//        ) {
//            return driver;
//        }
//    }
}