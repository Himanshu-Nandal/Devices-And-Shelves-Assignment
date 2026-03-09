package com.assignment1.DevicesAndShelves.Repository;

import com.assignment1.DevicesAndShelves.Exceptions.NotFoundException;
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

import java.util.List;
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
                        shelfPositionId: $shelfPositionId,
                        isDeleted: false,
                        createdAt: datetime(),
                        updatedAt: datetime()
                    })
                    
                    WITH s
                    OPTIONAL MATCH (sp:ShelfPosition {shelfPositionId: $shelfPositionId})
                    SET sp.shelfId = s.shelfId, sp.updatedAt = datetime()
                    MERGE (sp)-[:HAS]->(s)
                    """, Map.of(
                    "shelfId", shelf.getShelfId(),
                    "shelfName", shelf.getShelfName(),
                    "partNumber", shelf.getPartNumber(),
                    "imageUrl", shelf.getImageUrl() != null ? shelf.getImageUrl() : "",
                    "shelfPositionId", shelf.getShelfPositionId() != null ? shelf.getShelfPositionId() : ""
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
                    OPTIONAL MATCH (sp:ShelfPosition {shelfPositionId: $shelfPositionId})-[r:HAS]->(s)
                    SET sp.shelfId = "", sp.updatedAt = datetime()
                    DELETE r
                    
                    WITH s
                    SET s.shelfName = $shelfName,
                        s.partNumber = $partNumber,
                        s.imageUrl = $imageUrl,
                        s.shelfPositionId = $shelfPositionId,
                        s.updatedAt = datetime()
                    
                    WITH s
                    OPTIONAL MATCH (sp2:ShelfPosition {shelfPositionId: $shelfPositionId})
                    SET sp2.shelfId = s.shelfId, sp.updatedAt = datetime()
                    MERGE (sp2)-[:HAS]->(s)
                    
                    RETURN s
                    """, Map.of(
                        "shelfId", shelf.getShelfId(),
                        "shelfName", shelf.getShelfName(),
                        "partNumber", shelf.getPartNumber(),
                        "imageUrl", shelf.getImageUrl() != null ? shelf.getImageUrl() : "",
                        "shelfPositionId", shelf.getShelfPositionId() != null ? shelf.getShelfPositionId() : ""
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
                    SET s.isDeleted = true, s.updatedAt = datetime(), s.shelfPositionId = "",
                        sp.shelfId = "", sp.isOccupied = false, sp.updatedAt = datetime()
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

    public Map<String, Object> getShelfPage(int page, int size, String search, boolean isDeleted) {
        logger.info("Repository: Fetching shelves with filters - page: {}, size: {}, search: {}, isDeleted: {}",
                page, size, search, isDeleted);
        try(Session session = driver.session()){
            return session.executeRead(tx -> {
                Result countresult = tx.run("""
                    MATCH (s:Shelf)
                    WHERE s.isDeleted = $isDeleted
                    AND ($search = ""
                    OR toUpper(s.shelfName) CONTAINS toUpper($search)
                    OR toUpper(s.shelfId) = toUpper($search))
                    RETURN count(s) AS totalCount
                    """, Map.of(
                    "isDeleted", isDeleted,
                    "search", search == null ? "" : search
                ));
                int totalCount = countresult.single().get("totalCount").asInt();

                Result result = tx.run("""
                    MATCH (s:Shelf)
                    WHERE s.isDeleted = $isDeleted
                    AND ($search = ""
                    OR toUpper(s.shelfName) CONTAINS toUpper($search)
                    OR toUpper(s.shelfId) = toUpper($search))
                    RETURN s
                    ORDER BY s.createdAt DESC
                    SKIP $skip
                    LIMIT $limit
                    """, Map.of(
                    "isDeleted", isDeleted,
                    "search", search== null ? "" : search,
                    "skip", (page-1) * size,
                    "limit", size
                ));
                List<Shelf> shelves = result.list(record -> Shelf.from(record.get("s").asNode()));
                logger.info("Shelves fetched successfully with filters - page: {}, size: {}, search: {}, isDeleted: {}",
                        page, size, search, isDeleted);
                return Map.of(
                        "shelves", shelves,
                        "totalCount", totalCount
                );
            });
        } catch (Exception e) {
            logger.error("Error fetching shelves with filters - page: {}, size: {}, search: {}, isDeleted: {}: {}",
                    page, size, search, isDeleted, e.getMessage());
            throw new NotFoundException("Error fetching shelves with filters" + e);
        }
    }

    public List<Shelf> getShelves() {
        logger.info("Repository: Fetching shelves}");
        try(Session session = driver.session()){
            return session.executeRead(tx -> {
                Result result = tx.run("""
                    MATCH (s:Shelf {isDeleted: false, shelfPositionId: ""})
                    RETURN DISTINCT s
                    """);
                List<Shelf> shelves = result.list(record -> Shelf.from(record.get("s").asNode()));
                logger.info("Fetched {} shelves ", shelves.size());
                return shelves;
            });
        } catch (Exception e) {
            logger.error("Error fetching shelves for deviceId {}: {}", "deviceId", e.getMessage());
            throw new RuntimeException("Error fetching shelves for deviceId: " + "deviceId", e);
        }
    }
}
