export interface Device {
    deviceId: number;
    deviceName: string;
    partNumber: string;
    buildingName: string;
    deviceType: string;
    totalShelfPositions: number;
    imageUrl: string;
    isDeleted: string;
    createdAt: string;
    updatedAt: string;
}

export interface Page<T> {
    content: T[];
    totalElements: number;
    totalPages: number;
    pageNumber: number;
    pageSize: number;
}