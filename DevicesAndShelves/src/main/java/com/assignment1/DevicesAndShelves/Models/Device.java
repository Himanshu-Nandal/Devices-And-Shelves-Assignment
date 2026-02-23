package com.assignment1.DevicesAndShelves.Models;

import lombok.*;
import java.time.OffsetDateTime;

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
    private OffsetDateTime createdAt;
    private OffsetDateTime updatedAt;

    // helper to map from Neo4j Node (if you want)
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
        // createdAt/updatedAt can be read as LocalDateTime if you store as datetime()
        return d;
    }
}

/*
* deviceId: string (UUID) unique
deviceName: string
partNumber: string
buildingName: string
deviceType: string
totalShelfPositions: int
imageUrl?: string
isDeleted: boolean (default false)  ← soft delete
createdAt: datetime
updatedAt: datetime
* */