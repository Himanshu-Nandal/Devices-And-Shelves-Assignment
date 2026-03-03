package com.assignment1.DevicesAndShelves.Repository;

import org.neo4j.driver.*;
import org.neo4j.driver.Record;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.Map;

@Repository
public class ShelfPositionRepository {
    private final Driver driver;
    private static final Logger logger = LoggerFactory.getLogger(ShelfPositionRepository.class);

    @Autowired
    public ShelfPositionRepository(Driver driver) {
        this.driver = driver;
    }

    public void createShelfPositions(String deviceId, Integer change, Integer initial) {
        logger.info("Repository: Creating shelf positions for deviceId: {}, change: {}, initial: {}", deviceId, change, initial);
        try (Session session = driver.session()) {
            session.executeWrite(tx -> {
                tx.run("""
                    MATCH (d:Device {deviceId: $deviceId, isDeleted: false})
                    WITH d
                    UNWIND range($initial + 1, $initial + $change) AS position
                    MERGE (sp:ShelfPosition {
                        shelfPositionId: randomUUID(),
                        deviceId: $deviceId,
                        shelfId: "",
                        index: position,
                        isOccupied: false,
                        isDeleted: false,
                        createdAt: datetime(),
                        updatedAt: datetime()
                    })
                    MERGE (d)-[:HAS]->(sp)
                    """, Map.of(
                        "deviceId", deviceId,
                        "change", change,
                        "initial", initial
                    ));
                return null;
            });
        } catch (Exception e) {
            logger.error("Error creating shelf positions in repository: {}", e.getMessage());
            throw new RuntimeException("Error creating shelf positions in repository", e);
        }
    }


    public void deleteShelfPositions(String deviceId, Integer change, Integer initial) {
        logger.info("Repository: Deleting shelf positions for deviceId: {}, change: {}, initial: {}", deviceId, change, initial);
        try (Session session = driver.session()) {
            session.executeWrite(tx -> {
                tx.run("""
                    MATCH (d:Device {deviceId: $deviceId, isDeleted: false})-[:HAS]->(sp:ShelfPosition)
                    WHERE sp.index > $initial - $change AND sp.index <= $initial AND sp.isDeleted = false
                    SET sp.isDeleted = true, sp.updatedAt = datetime()
                    """, Map.of(
                        "deviceId", deviceId,
                        "change", change,
                        "initial", initial
                    ));
                return null;
            });
        } catch (Exception e) {
            logger.error("Error deleting shelf positions in repository: {}", e.getMessage());
            throw new RuntimeException("Error deleting shelf positions in repository", e);
        }
    }

    public void updateShelfPositions(String shelfId, String shelfPositionId) {
        logger.info("Repository: Updating shelf positions for shelfId: {}, shelfPositionId: {}", shelfId, shelfPositionId);
        try (Session session = driver.session()) {
            session.executeWrite(tx -> {
                tx.run("""
                    MATCH (sp:ShelfPosition {shelfPositionId: $shelfPositionId, isDeleted: false})
                    SET sp.isOccupied = true, sp.updatedAt = datetime(), sp.shelfId = $shelfId
                    """, Map.of(
                        "shelfId", shelfId,
                        "shelfPositionId", shelfPositionId
                    ));
                return null;
            });
        } catch (Exception e) {
            logger.error("Error updating shelf positions in repository: {}", e.getMessage());
            throw new RuntimeException("Error updating shelf positions in repository", e);
        }
    }

    public Map<String, Object> getShelfPositionById(String shelfPositionId) {
        logger.info("Repository: Fetching shelf position with id: {}", shelfPositionId);
        try (Session session = driver.session()) {
            return session.executeRead(tx -> {
                Result result = tx.run("""
                        MATCH (sp:ShelfPosition {shelfPositionId: $shelfPositionId, isDeleted: false})
                        RETURN sp {.*, shelfId: sp.shelfId} AS shelfPosition
                        """, Map.of("shelfPositionId", shelfPositionId));
                if (result.hasNext()) {
                    Record record = result.next();
                    logger.info("Shelf position found with ID: {}", shelfPositionId);
                    return record.get("shelfPosition").asMap();
                } else {
                    logger.warn("Shelf position not found with ID: {}", shelfPositionId);
                    return null;
                }
            });
        } catch (Exception e) {
            logger.error("Error fetching shelf position with ID {}: {}", shelfPositionId, e.getMessage());
            throw new RuntimeException("Error fetching shelf position with ID: " + shelfPositionId, e);
        }
    }
}
