package com.assignment1.DevicesAndShelves.Repository;

import com.assignment1.DevicesAndShelves.Models.ShelfPosition;
import org.neo4j.driver.*;
import org.neo4j.driver.Record;
import org.neo4j.driver.types.Node;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;
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
                    SET sp.isDeleted = true, sp.isOccupied = false, sp.shelfId = "", sp.shelfName = "", sp.updatedAt = datetime()
                    
                    WITH sp
                    OPTIONAL MATCH (sp)-[r:HAS]->(s:Shelf)
                    WITH r, s WHERE s IS NOT NULL
                    SET s.shelfPositionId = "", s.updatedAt = datetime()
                    DELETE r
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

    public Map<String, Object> getShelfPositionById(String shelfPositionId) {
        logger.info("Repository: Fetching shelf position with id: {}", shelfPositionId);
        try (Session session = driver.session()) {
            return session.executeRead(tx -> {
                Result result = tx.run("""
                        MATCH (sp:ShelfPosition {shelfPositionId: $shelfPositionId, isDeleted: false})
                        RETURN sp AS shelfPosition
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

    public List<ShelfPosition> getShelfPositionsByDeviceId(String deviceId) {
        logger.info("Repository: Fetching shelf positions for deviceId: {}", deviceId);
        try (Session session = driver.session()) {
            return session.executeRead(tx -> {
                Result result = tx.run("""
                    MATCH (d:Device {deviceId: $deviceId, isDeleted: false})-[:HAS]->(sp:ShelfPosition {isDeleted: false})
                    RETURN sp
                    ORDER BY sp.index ASC
                    """, Map.of(
                            "deviceId", deviceId)
                );
                List<ShelfPosition> shelfPositions = result.list(record -> {
                    Node spNode = record.get("sp").asNode();
                    return ShelfPosition.from(spNode);
                });
                logger.info("Fetched {} shelf positions for deviceId: {}", shelfPositions.size(), deviceId);
                return shelfPositions;
            });
        } catch (Exception e) {
            logger.error("Error fetching shelf positions for deviceId {}: {}", deviceId, e.getMessage());
            throw new RuntimeException("Error fetching shelf positions for deviceId: " + deviceId, e);
        }
    }

    public void deleteShelfPositionById(String shelfPositionId) {
        logger.info("Repository: Deleting shelf position with id: {}", shelfPositionId);
        try (Session session = driver.session()) {
            session.executeWrite(tx -> {
                tx.run("""
                    MATCH (sp:ShelfPosition {shelfPositionId: $shelfPositionId, isDeleted: false})
                    SET sp.isDeleted = true, sp.isOccupied = false, sp.updatedAt = datetime(), sp.shelfId = ""
                    OPTIONAL MATCH (sp)-[r:HAS]->(s:Shelf)
                    WITH r, s WHERE s IS NOT NULL
                    SET s.shelfPositionId = "", s.updatedAt = datetime()
                    DELETE r
                    """, Map.of("shelfPositionId", shelfPositionId));
                return null;
            });
        } catch (Exception e) {
            logger.error("Error deleting shelf position with ID {}: {}", shelfPositionId, e.getMessage());
            throw new RuntimeException("Error deleting shelf position with ID: " + shelfPositionId, e);
        }
    }

    public ShelfPosition updateShelfPositions(String shelfId, String shelfPositionId) {
        logger.info("Repository: Updating shelf positions for shelfId: {}, shelfPositionId: {}", shelfId, shelfPositionId);
        try (Session session = driver.session()) {
            return session.executeWrite(tx -> {
                Result result = tx.run("""
                    MATCH (sp:ShelfPosition {shelfPositionId: $shelfPositionId, isDeleted: false})
                    OPTIONAL MATCH (sp)-[r:HAS]->(old:Shelf)
                    SET old.shelfPositionId = "", old.updatedAt = datetime()
                    DELETE r
                    
                    SET sp.isOccupied = false, sp.shelfId = "", sp.updatedAt = datetime()
                    WITH sp
                    OPTIONAL MATCH (s:Shelf {shelfId: $shelfId, isDeleted: false})
                    
                    WITH sp, s WHERE s IS NOT NULL
                    SET s.shelfPositionId = $shelfPositionId,
                        s.updatedAt = datetime(),
                        sp.isOccupied = true, sp.shelfId = $shelfId,
                        sp.updatedAt = datetime()
                    MERGE (sp)-[:HAS]->(s)
                    
                    RETURN sp
                    """, Map.of(
                        "shelfId", shelfId != null ? shelfId : "",
                        "shelfPositionId", shelfPositionId
                ));

                if (result.hasNext()) {
                    Record record = result.next();
                    logger.info("Shelf position updated with shelfId: {} for shelfPositionId: {}", shelfId, shelfPositionId);
                    System.out.println("RESULT  :    " + record);
                    return ShelfPosition.from(record.get("sp").asNode());
                } else {
                    logger.warn("Shelf position not found or could not be updated for shelfPositionId: {}", shelfPositionId);
                    return null;
                }
            });
        } catch (Exception e) {
            logger.error("Error updating shelf positions in repository: {}", e.getMessage());
            throw new RuntimeException("Error updating shelf positions in repository", e);
        }
    }

    public ShelfPosition updateShelfPosition(String shelfPositionId, ShelfPosition shelfPosition) {

        logger.info("Repository: Updating shelf position with id: {}", shelfPositionId);
        try (Session session = driver.session()) {
            return session.executeWrite(tx -> {
                Result result = tx.run("""
                    MATCH (sp:ShelfPosition {shelfPositionId: $shelfPositionId, isDeleted: false})
                    OPTIONAL MATCH (sp)-[r:HAS]->(old:Shelf)
                    SET old.shelfPositionId = "", old.updatedAt = datetime()
                    DELETE r
                    
                    SET sp.isOccupied = false, sp.shelfId = "", sp.shelfName = "", sp.updatedAt = datetime()
                    WITH sp
                    OPTIONAL MATCH (s:Shelf {shelfId: $shelfId, isDeleted: false})
                    
                    WITH sp, s WHERE s IS NOT NULL
                    SET s.shelfPositionId = $shelfPositionId,
                        s.updatedAt = datetime(),
                        sp.isOccupied = true, sp.shelfId = $shelfId,
                        sp.shelfName = $shelfName, sp.updatedAt = datetime()
                    MERGE (sp)-[:HAS]->(s)
                    
                    RETURN sp
                    """, Map.of(
                        "shelfPositionId", shelfPositionId,
                        "shelfId", shelfPosition.getShelfId() != null ? shelfPosition.getShelfId() : "",
                        "shelfName", shelfPosition.getShelfName() != null ? shelfPosition.getShelfName() : ""
                    ));
                if (result.hasNext()) {
                    Record record = result.next();
                    logger.info("Shelf position updated with ID: {}", shelfPositionId);
                    return ShelfPosition.from(record.get("sp").asNode());
                } else {
                    logger.warn("Shelf position not found or could not be updated for ID: {}", shelfPositionId);
                    return null;
                }
            });
        } catch (Exception e) {
            logger.error("Error updating shelf position with ID {}: {}", shelfPositionId, e.getMessage());
            throw new RuntimeException("Error updating shelf position with ID: " + shelfPositionId, e);
        }
    }
}
