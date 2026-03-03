package com.assignment1.DevicesAndShelves.Repository;

import com.assignment1.DevicesAndShelves.Models.Device;
import org.neo4j.driver.*;
import org.neo4j.driver.Record;
import org.neo4j.driver.types.Node;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.*;

@Repository
public class DeviceRepository {
    private final Driver driver;
    private static final Logger logger = LoggerFactory.getLogger(DeviceRepository.class);

    @Autowired
    public DeviceRepository(Driver driver) {
        this.driver = driver;
    }


    public void createDevice(Device device) {
        try(Session session = driver.session()){
            session.executeWrite(tx -> {
                tx.run("""
                    MERGE (d:Device {
                        deviceId: $deviceId,
                        deviceName: $deviceName,
                        partNumber: $partNumber,
                        buildingName: $buildingName,
                        deviceType: $deviceType,
                        totalShelfPositions: $totalShelfPositions,
                        imageUrl: $imageUrl,
                        isDeleted: false,
                        createdAt: datetime(),
                        updatedAt: datetime()
                    })
                    
                    WITH d
                    UNWIND range(1, $totalShelfPositions) AS position
                    
                    MERGE (sp:ShelfPosition {
                        shelfPositionId: randomUUID(),
                        deviceId: $deviceId,
                        shelfId: ""
                        index: position,
                        isOccupied: false,
                        isDeleted: false,
                        createdAt: datetime(),
                        updatedAt: datetime()
                    })
                    
                    MERGE (d)-[:HAS]->(sp)
                    """, Map.of(
                        "deviceId", device.getDeviceId(),
                        "deviceName", device.getDeviceName(),
                        "partNumber", device.getPartNumber(),
                        "buildingName", device.getBuildingName(),
                        "deviceType", device.getDeviceType(),
                        "totalShelfPositions", device.getTotalShelfPositions(),
                        "imageUrl", device.getImageUrl() != null ? device.getImageUrl() : ""
                ));
                return null;
            });
            logger.info("Device created successfully: {}", device.getDeviceId());
        } catch (Exception e) {
            logger.error("Error creating device: {}", e.getMessage());
            throw new RuntimeException("Error creating device", e);
        }
    }

    public Device getDeviceById(String deviceId) {
        try(Session session = driver.session()) {
            return session.executeRead(tx -> {
                Result result = tx.run("""
                        MATCH (d:Device {deviceId: $deviceId, isDeleted: false})
                        OPTIONAL MATCH (d)-[HAS]->(sp:ShelfPosition {isDeleted: false})
                        RETURN d, collect(sp) AS shelfPositions
                        
                        """, Map.of(
                        "deviceId", deviceId
                ));
                if (result.hasNext()) {
                    Record record = result.next();
                    Node deviceNode = record.get("d").asNode();
                    logger.info("Device fetched successfully with deviceId: {}", deviceId);
                    return Device.from(deviceNode);
                } else {
                    logger.warn("Device not found with ID: {}", deviceId);
                    return null;
                }
            });
        } catch (Exception e) {
            logger.error("Error fetching device with ID {}: {}", deviceId, e.getMessage());
            throw new RuntimeException("Error fetching device with ID: " + deviceId, e);
        }
    }

    public Device updateDevice(String deviceId, Device device) {
        try(Session session = driver.session()) {
            return session.executeWrite(tx -> {
                Result result = tx.run("""
                        MATCH (d:Device {deviceId: $deviceId, isDeleted: false})
                        SET d.deviceName = $deviceName,
                            d.partNumber = $partNumber,
                            d.buildingName = $buildingName,
                            d.deviceType = $deviceType,
                            d.totalShelfPositions = $totalShelfPositions,
                            d.imageUrl = $imageUrl,
                            d.updatedAt = datetime()
                        RETURN d
                        """, Map.of(
                        "deviceId", deviceId,
                        "deviceName", device.getDeviceName(),
                        "partNumber", device.getPartNumber(),
                        "buildingName", device.getBuildingName(),
                        "deviceType", device.getDeviceType(),
                        "totalShelfPositions", device.getTotalShelfPositions(),
                        "imageUrl", device.getImageUrl() != null ? device.getImageUrl() : ""
                ));
                if (result.hasNext()) {
                    Record record = result.next();
                    Node deviceNode = record.get("d").asNode();
                    logger.info("Device updated successfully with ID: {}", deviceId);
                    return Device.from(deviceNode);
                } else {
                    logger.warn("Device not found for update with ID: {}", deviceId);
                    return null;
                }
            });
        } catch (Exception e) {
            logger.error("Error updating device with ID {}: {}", deviceId, e.getMessage());
            throw new RuntimeException("Error updating device with ID: " + deviceId, e);
        }
    }

    public boolean softDeleteDevice(String deviceId) {
        try(Session session = driver.session()){
            return session.executeWrite(tx -> {
                Result result = tx.run("""
                        MATCH (d:Device {deviceId: $deviceId, isDeleted: false})
                        SET d.isDeleted = true, d.updatedAt = datetime()
                        WITH d
                        OPTIONAL MATCH (d)-[:HAS]->(sp:ShelfPosition {isDeleted: false})
                        SET sp.isDeleted = true, sp.updatedAt = datetime()
                        WITH d
                        OPTIONAL MATCH (d)-[:HAS]->(:ShelfPosition)-[r:HAS]->(s:Shelf {isDeleted: false})
                        DELETE r
                        RETURN d
                        """, Map.of(
                        "deviceId", deviceId
                ));
                if (result.hasNext()) {
                    logger.info("Device and its shelf positions soft deleted and respective shelves released successfully with ID: {}", deviceId);
                    return true;
                } else {
                    logger.warn("Device not found for deletion with ID: {}", deviceId);
                    return false;
                }
            });
        } catch (Exception e) {
            logger.error("Error soft deleting device with ID {}: {}", deviceId, e.getMessage());
            throw new RuntimeException("Error soft deleting device with ID: " + deviceId, e);
        }
    }

    public Device getDeviceByName(String deviceName) {
        try(Session session = driver.session()) {
            return session.executeRead(tx -> {
                Result result = tx.run("""
                        MATCH (d:Device {deviceName: $deviceName, isDeleted: false})
                        OPTIONAL MATCH (d)-[HAS]->(sp:ShelfPosition {isDeleted: false})
                        RETURN d, collect(sp) AS shelfPositions
                        
                        """, Map.of(
                        "deviceName", deviceName
                ));
                if (result.hasNext()) {
                    Record record = result.next();
                    Node deviceNode = record.get("d").asNode();
                    logger.info("Device fetched successfully with deviceName: {}", deviceName);
                    return Device.from(deviceNode);
                } else {
                    logger.warn("Device not found with Name: {}", deviceName);
                    return null;
                }
            });
        } catch (Exception e) {
            logger.error("Error fetching device with Name {}: {}", deviceName, e.getMessage());
            throw new RuntimeException("Error fetching device with Name: " + deviceName, e);
        }
    }
}
// OPTIONAL MATCH (d)-[HAS]->(sp:ShelfPosition {isDeleted: false})
// , collect(sp) AS shelfPositions