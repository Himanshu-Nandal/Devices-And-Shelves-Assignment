package com.assignment1.DevicesAndShelves.Services;

import com.assignment1.DevicesAndShelves.Exceptions.BadRequestException;
import com.assignment1.DevicesAndShelves.Exceptions.NotFoundException;
import com.assignment1.DevicesAndShelves.Models.Shelf;
import com.assignment1.DevicesAndShelves.Repository.ShelfRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
public class ShelfService {
    private static final Logger logger = LoggerFactory.getLogger(ShelfService.class);
    private final ShelfRepository shelfRepository;

    @Autowired
    public ShelfService(ShelfRepository shelfRepository) {
        this.shelfRepository = shelfRepository;
    }

    public Map<String, Object> createShelf(Shelf shelf) {
        logger.info("Service: Creating shelf in service: {}", shelf.getShelfName());

        //validate input
        if (shelf.getShelfName() == null || shelf.getShelfName().isEmpty()) {
            throw new BadRequestException("Shelf name is required");
        }
        if (shelf.getPartNumber() == null || shelf.getPartNumber().isEmpty()) {
            throw new BadRequestException("Part number is required");
        }
        if(shelfRepository.getShelfByName(shelf.getShelfName()) != null){
            throw new BadRequestException("Shelf with the same name already exists");
        }

        // Generate UUID for shelf
        shelf.setShelfId(UUID.randomUUID().toString());

        // Create shelf in repository
        shelfRepository.createShelf(shelf);
        return Map.of(
                "success", true,
                "message", "Shelf created successfully",
                "content", shelf
        );
    }

    public Map<String, Object> getShelfById(String shelfId) {
        logger.info("Service: Fetching shelf with id: {}", shelfId);

        //validate input
        if (shelfId == null || shelfId.isEmpty()) {
            throw new BadRequestException("Shelf ID is required");
        }

        // Fetch shelf from repository
        Shelf shelf = shelfRepository.getShelfById(shelfId);
        if (shelf == null) {
            throw new NotFoundException("Shelf not found with ID: " + shelfId);
        }

        return Map.of(
                "success", true,
                "message", "Shelf fetched successfully",
                "content", shelf
        );
    }


    public Map<String, Object> getShelfByName(String shelfName) {
        logger.info("Service: Fetching shelf with name: {}", shelfName);

        //validate input
        if (shelfName == null || shelfName.isEmpty()) {
            throw new BadRequestException("Shelf name is required");
        }

        // Fetch shelf from repository
        Shelf shelf = shelfRepository.getShelfByName(shelfName);
        if (shelf == null) {
            throw new NotFoundException("Shelf not found with name: " + shelfName);
        }

        return Map.of(
                "success", true,
                "message", "Shelf fetched successfully",
                "content", shelf
        );
    }

    public Map<String, Object> updateShelf(String shelfId, Shelf shelf) {
        logger.info("Service: Updating shelf with id: {}", shelfId);

        //validate input
        if (shelfId == null || shelfId.isEmpty()) {
            throw new BadRequestException("Shelf ID is required");
        }
        if (shelf == null || shelf.getShelfName() == null || shelf.getShelfName().isEmpty()) {
            throw new BadRequestException("Shelf with Shelf Name is required");
        }

        // Set the shelfId from the path variable onto the shelf object
        shelf.setShelfId(shelfId);

        // Update shelf in repository
        Shelf updatedShelf = shelfRepository.updateShelf(shelf);
        if (updatedShelf == null) {
            throw new NotFoundException("Shelf not found with ID: " + shelfId);
        }
        return Map.of(
                "success", true,
                "message", "Shelf updated successfully",
                "content", updatedShelf
        );
    }

    public Map<String, Object> deleteShelf(String shelfId) {
        logger.info("Service: Deleting shelf with id: {}", shelfId);

        //validate input
        if (shelfId == null || shelfId.isEmpty()) {
            throw new BadRequestException("Shelf ID is required");
        }

        // Delete shelf in repository
        shelfRepository.deleteShelf(shelfId);
        return Map.of(
                "success", true,
                "message", "Shelf deleted successfully"
        );
    }

    public Map<String, Object> getShelfPage(int page, int size, String search, boolean isDeleted) {
        logger.info("Service: Fetching shelves with filters - page: {}, size: {}, search: {}, isDeleted: {}",
                page, size, search, isDeleted);

        //validate input
        if (page < 1) {
            throw new BadRequestException("Page number cannot be negative");
        }
        if (size <= 0) {
            throw new BadRequestException("Page size must be greater than zero");
        }

        // Fetch shelves from repository
        Map<String, Object> response = shelfRepository.getShelfPage(page, size, search, isDeleted);
        return Map.of(
                "success", true,
                "message", "Shelves fetched successfully",
                "content", response.get("shelves"),
                "totalElements", response.get("totalCount"),
                "pageNumber", page,
                "pageSize", size
        );
    }

    public List<Shelf> getShelves() {
        logger.info("Service: Fetching shelves ");

        // Fetch shelves from repository
        return shelfRepository.getShelves();
    }
}
