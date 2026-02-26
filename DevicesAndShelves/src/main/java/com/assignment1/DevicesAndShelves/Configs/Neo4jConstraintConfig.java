package com.assignment1.DevicesAndShelves.Configs;

import jakarta.annotation.PostConstruct;
import org.neo4j.driver.Driver;
import org.neo4j.driver.Session;
import org.springframework.stereotype.Component;

@Component  // Why @Component? Because Spring will: Detect it automatically->Create object at startup->Inject Driver->Run @PostConstruct
public class Neo4jConstraintConfig {
    private final Driver driver;

    public Neo4jConstraintConfig(Driver driver) {
        this.driver = driver;
    }

    @PostConstruct  // This runs automatically after bean initialization.
    public void createConstraints() {

        try (Session session = driver.session()) {

            session.executeWrite(tx -> {
                tx.run("""
                        CREATE CONSTRAINT device_id IF NOT EXISTS
                        FOR (d:Device)
                        REQUIRE d.id IS UNIQUE
                        """);
                // using IF NOT EXISTS makes it not fail when constraints already exist.
                tx.run("""
                        CREATE CONSTRAINT shelf_id IF NOT EXISTS
                        FOR (s:Shelf)
                        REQUIRE s.id IS UNIQUE
                        """);

                tx.run("""
                        CREATE CONSTRAINT shelf_position_id IF NOT EXISTS
                        FOR (sp:ShelfPosition)
                        REQUIRE sp.id IS UNIQUE
                        """);

                return null;
            });

            System.out.println("Neo4j constraints created successfully.");

        } catch (Exception e) {
            System.err.println("Failed to create constraints: " + e.getMessage());
        }
    }
}
