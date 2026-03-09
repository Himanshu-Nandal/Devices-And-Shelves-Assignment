export interface Device {
    deviceId: string;
    deviceName: string;
    partNumber: string;
    buildingName: string;
    deviceType: string;
    totalShelfPositions: number;
    imageUrl: string;
    // isDeleted: string;
    createdAt: string;
    updatedAt: string;
}

export interface ShelfPosition {
    shelfPositionId: string;
    deviceId: string;
    shelfId: string;
    shelfName: string;
    index: number;
    isOccupied: boolean;
    isDeleted: string;
    createdAt: string;
    updatedAt: string;
}

export interface Page<T> {
    success: boolean;
    message: string;
    content: T[];
    totalElements: number;
    pageNumber: number;
    pageSize: number;
}