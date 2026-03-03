package com.assignment1.DevicesAndShelves.Repository;

import com.assignment1.DevicesAndShelves.Models.Shelf;
import org.neo4j.driver.Driver;
import org.neo4j.driver.Record;
import org.neo4j.driver.Result;
import org.neo4j.driver.Session;
import org.neo4j.driver.types.Node;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.Map;

@Repository
public class ShelfRepository {
    private static final Logger logger = LoggerFactory.getLogger(ShelfRepository.class);
    private final Driver driver;

    @Autowired
    public ShelfRepository(Driver driver) {
        this.driver = driver;
    }

    public void createShelf(Shelf shelf) {
        try (Session session = driver.session()) {
            session.executeWrite(tx -> {
                tx.run("""
                    MERGE (s:Shelf {
                        shelfId: $shelfId,
                        shelfName: $shelfName,
                        partNumber: $partNumber,
                        imageUrl: $imageUrl,
                        isDeleted: false,
                        createdAt: datetime(),
                        updatedAt: datetime()
                    })
                    """, Map.of(
                    "shelfId", shelf.getShelfId(),
                    "shelfName", shelf.getShelfName(),
                    "partNumber", shelf.getPartNumber(),
                    "imageUrl", shelf.getImageUrl() != null ? shelf.getImageUrl() : ""
                ));
                return null;
            });
        } catch (Exception e) {
            logger.error("Error creating shelf in repository: {}", e.getMessage());
            throw new RuntimeException("Error creating shelf in repository", e);
        }
    }

    public Shelf getShelfById(String shelfId) {
        logger.info("Repository: Fetching shelf with ID: {}", shelfId);
        try(Session session = driver.session()){
            return session.executeRead(tx -> {
                Result result = tx.run("""
                    MATCH (s:Shelf {shelfId: $shelfId, isDeleted: false})
                    RETURN s
                    """, Map.of(
                    "shelfId", shelfId
                ));
                if (result.hasNext()) {
                    Record record = result.next();
                    Node shelfNode = record.get("s").asNode();
                    logger.info("Shelf fetched successfully with shelfId: {}", shelfId);
                    return Shelf.from(shelfNode);
                } else {
                    logger.warn("Shelf not found with ID: {}", shelfId);
                    return null;
                }
            });
        } catch (Exception e){
                logger.error("Error fetching shelf with ID {}: {}", shelfId, e.getMessage());
                throw new RuntimeException("Error fetching shelf with ID: " + shelfId, e);
        }
    }

    public Shelf getShelfByName(String shelfName) {
        logger.info("Repository: Fetching shelf with name: {}", shelfName);
        try(Session session = driver.session()){
            return session.executeRead(tx -> {
                Result result = tx.run("""
                    MATCH (s:Shelf {shelfName: $shelfName, isDeleted: false})
                    RETURN s
                    """, Map.of(
                    "shelfName", shelfName
                ));
                if (result.hasNext()) {
                    Record record = result.next();
                    Node shelfNode = record.get("s").asNode();
                    logger.info("Shelf fetched successfully with name: {}", shelfName);
                    return Shelf.from(shelfNode);
                } else {
                    logger.warn("Shelf not found with name: {}", shelfName);
                    return null;
                }
            });
        } catch (Exception e){
            logger.error("Error fetching shelf with name {}: {}", shelfName, e.getMessage());
            throw new RuntimeException("Error fetching shelf with name: " + shelfName, e);
        }
    }

    public Shelf updateShelf(Shelf shelf) {
        logger.info("Repository: Updating shelf: {}", shelf.getShelfId());
        try(Session session = driver.session()){
            return session.executeWrite(tx -> {
                Result result = tx.run("""
                    MATCH (s:Shelf {shelfId: $shelfId, isDeleted: false})
                    SET s.shelfName = $shelfName,
                        s.partNumber = $partNumber,
                        s.imageUrl = $imageUrl,
                        s.updatedAt = datetime()
                    RETURN s
                    """, Map.of(
                        "shelfId", shelf.getShelfId(),
                        "shelfName", shelf.getShelfName(),
                        "partNumber", shelf.getPartNumber(),
                        "imageUrl", shelf.getImageUrl() != null ? shelf.getImageUrl() : ""
                ));
                if (result.hasNext()) {
                    Record record = result.next();
                    Node shelfNode = record.get("s").asNode();
                    logger.info("Shelf updated successfully with shelfId: {}", shelf.getShelfId());
                    return Shelf.from(shelfNode);
                } else {
                    logger.warn("Shelf not found for update with shelfId: {}", shelf.getShelfId());
                    return null;
                }
            });
        } catch (Exception e) {
            logger.error("Error updating shelf with ID {}: {}", shelf.getShelfId(), e.getMessage());
            throw new RuntimeException("Error updating shelf with ID: " + shelf.getShelfId(), e);
        }
    }

    public void deleteShelf(String shelfId) {
        logger.info("Repository: Deleting shelf with ID: {}", shelfId);
        try(Session session = driver.session()){
            session.executeWrite(tx -> {
                tx.run("""
                    MATCH (s:Shelf {shelfId: $shelfId, isDeleted: false})
                    OPTIONAL MATCH (sp:ShelfPosition)-[r:HAS]->(s)
                    SET s.isDeleted = true, s.updatedAt = datetime()
                    DELETE r
                    """, Map.of(
                    "shelfId", shelfId
                ));
                return null;
            });
            logger.info("Shelf deleted successfully with ID: {}", shelfId);
        } catch (Exception e) {
            logger.error("Error deleting shelf with ID {}: {}", shelfId, e.getMessage());
            throw new RuntimeException("Error deleting shelf with ID: " + shelfId, e);
        }
    }
}
