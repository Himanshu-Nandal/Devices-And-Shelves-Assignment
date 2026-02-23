package com.assignment1.DevicesAndShelves.Models;

import lombok.*;
import java.time.OffsetDateTime;

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
    private OffsetDateTime createdAt;
    private OffsetDateTime updatedAt;
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