export interface Shelf {
    shelfId: string;
    shelfName: string;
    partNumber: string;
    imageUrl: string;
    shelfPositionId: string;
//     isDeleted: string;
    createdAt: string;
    updatedAt: string;
}

// export interface Page<T> {
//     content: T[];
//     totalElements: number;
//     totalPages: number;
//     pageNumber: number;
//     pageSize: number;
// }