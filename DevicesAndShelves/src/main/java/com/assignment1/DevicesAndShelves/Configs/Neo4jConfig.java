package com.assignment1.DevicesAndShelves.Configs;

import lombok.Value;
import org.neo4j.driver.AuthTokens;
import org.neo4j.driver.GraphDatabase;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.sql.Driver;

// config/Neo4jConfig.java
@Configuration
public class Neo4jConfig {
    @Value("${NEO4J_URI}") private String uri;
    @Value("${NEO4J_USERNAME}") private String username;
    @Value("${NEO4J_PASSWORD}") private String password;
//    @Value("${neo4j.password}") private String password;

    @Bean(destroyMethod = "close")
    public Driver neo4jDriver() {
        return GraphDatabase.driver(uri, AuthTokens.basic(username, password));
    }
}