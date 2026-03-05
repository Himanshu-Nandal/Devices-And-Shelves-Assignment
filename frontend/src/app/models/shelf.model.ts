export interface Shelf {
    shelfId: number;
    shelfName: string;
    partNumber: string;
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