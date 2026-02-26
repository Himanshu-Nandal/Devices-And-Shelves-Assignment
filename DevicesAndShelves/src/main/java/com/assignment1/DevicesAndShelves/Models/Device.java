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

    // helper to map from Neo4j Node
    public static Device from(org.neo4j.driver.types.Node n) {
        Device d = new Device();
        d.setDeviceId(n.get("deviceId").asString());
        d.setDeviceName(n.get("deviceName").asString(null));
        d.setPartNumber(n.get("partNumber").asString(null));
        d.setBuildingName(n.get("buildingName").asString(null));
        d.setDeviceType(n.get("deviceType").asString(null));
        d.setTotalShelfPositions(n.get("totalShelfPositions").asInt(0));
        d.setImageUrl(n.get("imageUrl").asString(null));
        d.setIsDeleted(n.get("isDeleted").asBoolean(false));

        // Map datetime fields from Neo4j
        if (!n.get("createdAt").isNull()) {
            d.setCreatedAt(n.get("createdAt").asZonedDateTime());
        }
        if (!n.get("updatedAt").isNull()) {
            d.setUpdatedAt(n.get("updatedAt").asZonedDateTime());
        }
        return d;
    }
}
