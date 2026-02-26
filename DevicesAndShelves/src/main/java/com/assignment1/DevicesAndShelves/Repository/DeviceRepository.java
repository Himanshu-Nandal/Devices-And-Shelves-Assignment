package com.assignment1.DevicesAndShelves.Repository;

import com.assignment1.DevicesAndShelves.Models.Device;
import org.neo4j.driver.*;
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
                    CREATE (d:Device {
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
                    
                    CREATE (sp:ShelfPosition {
                        shelfPositionId: randomUUID(),
                        deviceId: $deviceId,
                        positionIndex: position,
                        isOccupied: false,
                        isDeleted: false,
                        createdAt: datetime(),
                        updatedAt: datetime()
                    })
                    
                    CREATE (d)-[:HAS]->(sp)
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
}