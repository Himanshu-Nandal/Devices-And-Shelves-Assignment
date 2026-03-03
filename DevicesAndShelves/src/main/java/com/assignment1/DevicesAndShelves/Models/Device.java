package com.assignment1.DevicesAndShelves.Models;

import lombok.*;
import java.time.ZonedDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Device {
    private String deviceId;           // UUID string we create in service layer

    private String deviceName;
    private String partNumber;
    private String buildingName;
    private String deviceType;
    private Integer totalShelfPositions;

    private String imageUrl;
    private Boolean isDeleted;
    private ZonedDateTime createdAt;   // Audit timestamp - when device was created
    private ZonedDateTime updatedAt;   // Audit timestamp - when device was last modified

    // Helper to map from Neo4j Node using builder pattern
    public static Device from(org.neo4j.driver.types.Node n) {
        return Device.builder()
                .deviceId(n.get("deviceId").asString())
                .deviceName(n.get("deviceName").asString(null))
                .partNumber(n.get("partNumber").asString(null))
                .buildingName(n.get("buildingName").asString(null))
                .deviceType(n.get("deviceType").asString(null))
                .totalShelfPositions(n.get("totalShelfPositions").asInt(0))
                .imageUrl(n.get("imageUrl").asString(null))
                .isDeleted(n.get("isDeleted").asBoolean(false))
                .createdAt(n.get("createdAt").isNull() ? null : n.get("createdAt").asZonedDateTime())
                .updatedAt(n.get("updatedAt").isNull() ? null : n.get("updatedAt").asZonedDateTime())
                .build();
    }
}
