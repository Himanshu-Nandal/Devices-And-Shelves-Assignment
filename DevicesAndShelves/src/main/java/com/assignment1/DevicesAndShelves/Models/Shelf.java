package com.assignment1.DevicesAndShelves.Models;

import lombok.*;
import java.time.ZonedDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Shelf {
    private String shelfId;           // UUID string we create in service layer
    private String shelfName;
    private String partNumber;
    private String imageUrl;
    private Boolean isDeleted;
    private ZonedDateTime createdAt;
    private ZonedDateTime updatedAt;

    // Helper to map from Neo4j Node using builder pattern
    public static Shelf from(org.neo4j.driver.types.Node n) {
        return Shelf.builder()
                .shelfId(n.get("shelfId").asString())
                .shelfName(n.get("shelfName").asString(null))
                .partNumber(n.get("partNumber").asString(null))
                .imageUrl(n.get("imageUrl").asString(null))
                .isDeleted(n.get("isDeleted").asBoolean(false))
                .createdAt(n.get("createdAt").isNull() ? null : n.get("createdAt").asZonedDateTime())
                .updatedAt(n.get("updatedAt").isNull() ? null : n.get("updatedAt").asZonedDateTime())
                .build();
    }
}
/*
* shelfId: string (UUID) unique
shelfName: string
partNumber: string
imageUrl?: string
isDeleted: boolean (default false)
createdAt: datetime
updatedAt: datetime
* */
