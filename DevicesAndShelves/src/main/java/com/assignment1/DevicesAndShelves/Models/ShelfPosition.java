package com.assignment1.DevicesAndShelves.Models;

import lombok.*;

import java.time.OffsetDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ShelfPosition {
    private String shelfPositionId;           // UUID string we create in service layer
    private String deviceId;
    private Integer index;
    private Boolean isDeleted;
    private OffsetDateTime createdAt;
    private OffsetDateTime updatedAt;


}
/*
* shelfPositionId: string (UUID) unique
index: int (1..n within a device)
deviceId: string (device’s unique identifier as required)
isDeleted: boolean (default false)
createdAt: datetime
updatedAt: datetime
* */
