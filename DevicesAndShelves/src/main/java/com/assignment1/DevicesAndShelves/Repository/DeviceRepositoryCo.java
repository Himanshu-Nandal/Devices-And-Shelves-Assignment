//package com.assignment1.DevicesAndShelves.Repository;
//
//import com.assignment1.DevicesAndShelves.Models.Device;
//import org.neo4j.driver.*;
//import org.neo4j.driver.types.Node;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Repository;
//
//import java.time.LocalDateTime;
//import java.util.*;
//
//@Repository
//public class DeviceRepositoryCo {
//    private final Driver driver;
//    private static final Logger logger = LoggerFactory.getLogger(DeviceRepositoryCo.class);
//
//    @Autowired
//    public DeviceRepositoryCo(Driver driver) {
//        this.driver = driver;
//    }
//
//    /**
//     * Create a new device with shelf positions
//     */
//    public void createDevice(Device device) {
//        try (Session session = driver.session()) {
//            session.executeWrite(tx -> {
//                tx.run("""
//                    CREATE (d:Device {
//                        deviceId: $deviceId,
//                        deviceName: $deviceName,
//                        partNumber: $partNumber,
//                        buildingName: $buildingName,
//                        deviceType: $deviceType,
//                        totalShelfPositions: $totalShelfPositions,
//                        imageUrl: $imageUrl,
//                        isDeleted: false,
//                        createdAt: datetime(),
//                        updatedAt: datetime()
//                    })
//
//                    WITH d
//                    UNWIND range(1, $totalShelfPositions) AS position
//
//                    CREATE (sp:ShelfPosition {
//                        shelfPositionId: randomUUID(),
//                        deviceId: $deviceId,
//                        positionIndex: position,
//                        isOccupied: false,
//                        isDeleted: false,
//                        createdAt: datetime(),
//                        updatedAt: datetime()
//                    })
//
//                    CREATE (d)-[:HAS_POSITION]->(sp)
//                    """, Map.of(
//                        "deviceId", device.getDeviceId(),
//                        "deviceName", device.getDeviceName(),
//                        "partNumber", device.getPartNumber(),
//                        "buildingName", device.getBuildingName(),
//                        "deviceType", device.getDeviceType(),
//                        "totalShelfPositions", device.getTotalShelfPositions(),
//                        "imageUrl", device.getImageUrl() != null ? device.getImageUrl() : ""
//                ));
//                return null;
//            });
//            logger.info("Device created successfully: {}", device.getDeviceId());
//        } catch (Exception e) {
//            logger.error("Error creating device: {}", e.getMessage());
//            throw new RuntimeException("Error creating device", e);
//        }
//    }
//
//    /**
//     * Find device by ID
//     */
//    public Device findById(String deviceId) {
//        try (Session session = driver.session()) {
//            return session.executeRead(tx -> {
//                var result = tx.run("""
//                    MATCH (d:Device {deviceId: $deviceId})
//                    RETURN d
//                    """, Map.of("deviceId", deviceId));
//
//                if (result.hasNext()) {
//                    Node node = result.single().get("d").asNode();
//                    return Device.from(node);
//                }
//                return null;
//            });
//        } catch (Exception e) {
//            logger.error("Error finding device by ID: {}", e.getMessage());
//            throw new RuntimeException("Error finding device", e);
//        }
//    }
//
//    /**
//     * Find all devices with pagination and filtering
//     */
//    public Map<String, Object> findAll(int page, int size, String deviceType, String buildingName, Boolean isDeleted) {
//        try (Session session = driver.session()) {
//            return session.executeRead(tx -> {
//                StringBuilder query = new StringBuilder("MATCH (d:Device) WHERE 1=1 ");
//                Map<String, Object> params = new HashMap<>();
//
//                if (deviceType != null && !deviceType.isEmpty()) {
//                    query.append("AND d.deviceType = $deviceType ");
//                    params.put("deviceType", deviceType);
//                }
//
//                if (buildingName != null && !buildingName.isEmpty()) {
//                    query.append("AND d.buildingName = $buildingName ");
//                    params.put("buildingName", buildingName);
//                }
//
//                if (isDeleted != null) {
//                    query.append("AND d.isDeleted = $isDeleted ");
//                    params.put("isDeleted", isDeleted);
//                }
//
//                // Get total count
//                String countQuery = query.toString() + "RETURN count(d) AS total";
//                var countResult = tx.run(countQuery, params);
//                long totalElements = countResult.single().get("total").asLong();
//
//                // Get paginated results
//                query.append("RETURN d ORDER BY d.createdAt DESC SKIP $skip LIMIT $limit");
//                params.put("skip", page * size);
//                params.put("limit", size);
//
//                var result = tx.run(query.toString(), params);
//                List<Device> devices = new ArrayList<>();
//
//                while (result.hasNext()) {
//                    Node node = result.next().get("d").asNode();
//                    devices.add(Device.from(node));
//                }
//
//                int totalPages = (int) Math.ceil((double) totalElements / size);
//
//                Map<String, Object> response = new HashMap<>();
//                response.put("content", devices);
//                response.put("pageNumber", page);
//                response.put("pageSize", size);
//                response.put("totalElements", totalElements);
//                response.put("totalPages", totalPages);
//                response.put("isFirst", page == 0);
//                response.put("isLast", page >= totalPages - 1);
//                response.put("hasNext", page < totalPages - 1);
//                response.put("hasPrevious", page > 0);
//
//                return response;
//            });
//        } catch (Exception e) {
//            logger.error("Error finding all devices: {}", e.getMessage());
//            throw new RuntimeException("Error finding devices", e);
//        }
//    }
//
//    /**
//     * Update device
//     */
//    public void updateDevice(String deviceId, Device device) {
//        try (Session session = driver.session()) {
//            session.executeWrite(tx -> {
//                StringBuilder query = new StringBuilder("MATCH (d:Device {deviceId: $deviceId}) SET ");
//                Map<String, Object> params = new HashMap<>();
//                params.put("deviceId", deviceId);
//
//                List<String> updates = new ArrayList<>();
//
//                if (device.getDeviceName() != null) {
//                    updates.add("d.deviceName = $deviceName");
//                    params.put("deviceName", device.getDeviceName());
//                }
//                if (device.getPartNumber() != null) {
//                    updates.add("d.partNumber = $partNumber");
//                    params.put("partNumber", device.getPartNumber());
//                }
//                if (device.getBuildingName() != null) {
//                    updates.add("d.buildingName = $buildingName");
//                    params.put("buildingName", device.getBuildingName());
//                }
//                if (device.getDeviceType() != null) {
//                    updates.add("d.deviceType = $deviceType");
//                    params.put("deviceType", device.getDeviceType());
//                }
//                if (device.getImageUrl() != null) {
//                    updates.add("d.imageUrl = $imageUrl");
//                    params.put("imageUrl", device.getImageUrl());
//                }
//
//                updates.add("d.updatedAt = datetime()");
//                query.append(String.join(", ", updates));
//
//                tx.run(query.toString(), params);
//                return null;
//            });
//            logger.info("Device updated successfully: {}", deviceId);
//        } catch (Exception e) {
//            logger.error("Error updating device: {}", e.getMessage());
//            throw new RuntimeException("Error updating device", e);
//        }
//    }
//
//    /**
//     * Soft delete device
//     */
//    public void softDelete(String deviceId) {
//        try (Session session = driver.session()) {
//            session.executeWrite(tx -> {
//                tx.run("""
//                    MATCH (d:Device {deviceId: $deviceId})
//                    SET d.isDeleted = true, d.updatedAt = datetime()
//                    """, Map.of("deviceId", deviceId));
//                return null;
//            });
//            logger.info("Device soft deleted: {}", deviceId);
//        } catch (Exception e) {
//            logger.error("Error soft deleting device: {}", e.getMessage());
//            throw new RuntimeException("Error deleting device", e);
//        }
//    }
//
//    /**
//     * Restore soft-deleted device
//     */
//    public void restore(String deviceId) {
//        try (Session session = driver.session()) {
//            session.executeWrite(tx -> {
//                tx.run("""
//                    MATCH (d:Device {deviceId: $deviceId})
//                    SET d.isDeleted = false, d.updatedAt = datetime()
//                    """, Map.of("deviceId", deviceId));
//                return null;
//            });
//            logger.info("Device restored: {}", deviceId);
//        } catch (Exception e) {
//            logger.error("Error restoring device: {}", e.getMessage());
//            throw new RuntimeException("Error restoring device", e);
//        }
//    }
//
//    /**
//     * Search devices by name or part number
//     */
//    public List<Device> searchDevices(String query) {
//        try (Session session = driver.session()) {
//            return session.executeRead(tx -> {
//                var result = tx.run("""
//                    MATCH (d:Device)
//                    WHERE (d.deviceName CONTAINS $query OR d.partNumber CONTAINS $query)
//                    AND d.isDeleted = false
//                    RETURN d
//                    ORDER BY d.createdAt DESC
//                    """, Map.of("query", query));
//
//                List<Device> devices = new ArrayList<>();
//                while (result.hasNext()) {
//                    Node node = result.next().get("d").asNode();
//                    devices.add(Device.from(node));
//                }
//                return devices;
//            });
//        } catch (Exception e) {
//            logger.error("Error searching devices: {}", e.getMessage());
//            throw new RuntimeException("Error searching devices", e);
//        }
//    }
//
//    /**
//     * Find devices by building
//     */
//    public List<Device> findByBuilding(String buildingName) {
//        try (Session session = driver.session()) {
//            return session.executeRead(tx -> {
//                var result = tx.run("""
//                    MATCH (d:Device {buildingName: $buildingName})
//                    WHERE d.isDeleted = false
//                    RETURN d
//                    ORDER BY d.createdAt DESC
//                    """, Map.of("buildingName", buildingName));
//
//                List<Device> devices = new ArrayList<>();
//                while (result.hasNext()) {
//                    Node node = result.next().get("d").asNode();
//                    devices.add(Device.from(node));
//                }
//                return devices;
//            });
//        } catch (Exception e) {
//            logger.error("Error finding devices by building: {}", e.getMessage());
//            throw new RuntimeException("Error finding devices", e);
//        }
//    }
//
//    /**
//     * Find devices by type
//     */
//    public List<Device> findByType(String deviceType) {
//        try (Session session = driver.session()) {
//            return session.executeRead(tx -> {
//                var result = tx.run("""
//                    MATCH (d:Device {deviceType: $deviceType})
//                    WHERE d.isDeleted = false
//                    RETURN d
//                    ORDER BY d.createdAt DESC
//                    """, Map.of("deviceType", deviceType));
//
//                List<Device> devices = new ArrayList<>();
//                while (result.hasNext()) {
//                    Node node = result.next().get("d").asNode();
//                    devices.add(Device.from(node));
//                }
//                return devices;
//            });
//        } catch (Exception e) {
//            logger.error("Error finding devices by type: {}", e.getMessage());
//            throw new RuntimeException("Error finding devices", e);
//        }
//    }
//
//    /**
//     * Get device statistics
//     */
//    public Map<String, Object> getStatistics() {
//        try (Session session = driver.session()) {
//            return session.executeRead(tx -> {
//                var result = tx.run("""
//                    MATCH (d:Device)
//                    RETURN
//                        count(d) AS totalDevices,
//                        count(CASE WHEN d.isDeleted = false THEN 1 END) AS activeDevices,
//                        count(CASE WHEN d.isDeleted = true THEN 1 END) AS deletedDevices,
//                        collect(DISTINCT d.deviceType) AS deviceTypes,
//                        collect(DISTINCT d.buildingName) AS buildings
//                    """);
//
//                if (result.hasNext()) {
//                    var record = result.single();
//
//                    // Get counts by type
//                    var typeResult = tx.run("""
//                        MATCH (d:Device)
//                        WHERE d.isDeleted = false
//                        RETURN d.deviceType AS type, count(d) AS count
//                        """);
//
//                    Map<String, Long> countByType = new HashMap<>();
//                    while (typeResult.hasNext()) {
//                        var typeRecord = typeResult.next();
//                        countByType.put(typeRecord.get("type").asString(), typeRecord.get("count").asLong());
//                    }
//
//                    // Get counts by building
//                    var buildingResult = tx.run("""
//                        MATCH (d:Device)
//                        WHERE d.isDeleted = false
//                        RETURN d.buildingName AS building, count(d) AS count
//                        """);
//
//                    Map<String, Long> countByBuilding = new HashMap<>();
//                    while (buildingResult.hasNext()) {
//                        var buildingRecord = buildingResult.next();
//                        countByBuilding.put(buildingRecord.get("building").asString(), buildingRecord.get("count").asLong());
//                    }
//
//                    Map<String, Object> stats = new HashMap<>();
//                    stats.put("totalDevices", record.get("totalDevices").asLong());
//                    stats.put("activeDevices", record.get("activeDevices").asLong());
//                    stats.put("deletedDevices", record.get("deletedDevices").asLong());
//                    stats.put("deviceTypes", record.get("deviceTypes").asList());
//                    stats.put("buildings", record.get("buildings").asList());
//                    stats.put("countByType", countByType);
//                    stats.put("countByBuilding", countByBuilding);
//
//                    return stats;
//                }
//
//                return new HashMap<>();
//            });
//        } catch (Exception e) {
//            logger.error("Error getting device statistics: {}", e.getMessage());
//            throw new RuntimeException("Error getting statistics", e);
//        }
//    }
//}
