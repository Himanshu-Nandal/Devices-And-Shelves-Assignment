package com.assignment1.DevicesAndShelves.Services;

import com.assignment1.DevicesAndShelves.Exceptions.BadRequestException;
import com.assignment1.DevicesAndShelves.Exceptions.NotFoundException;
import com.assignment1.DevicesAndShelves.Models.Shelf;
import com.assignment1.DevicesAndShelves.Repository.ShelfRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class ShelfService {
    private static final Logger logger = LoggerFactory.getLogger(ShelfService.class);
    private final ShelfRepository shelfRepository;

    public ShelfService(ShelfRepository shelfRepository) {
        this.shelfRepository = shelfRepository;
    }

    public Map<String, Object> createShelf(Shelf shelf) {
        logger.info("Creating shelf in service: {}", shelf.getShelfName());

        //validate input
        if (shelf.getShelfName() == null || shelf.getShelfName().isEmpty()) {
            throw new BadRequestException("Shelf name is required");
        }
        if (shelf.getPartNumber() == null || shelf.getPartNumber().isEmpty()) {
            throw new BadRequestException("Part number is required");
        }

        // Create shelf in repository
        shelfRepository.createShelf(shelf);
        return Map.of(
                "success", true,
                "message", "Shelf created successfully",
                "data", shelf
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
                "data", shelf
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
                "data", shelf
        );
    }

    public Map<String, Object> updateShelf(Shelf shelf) {
        logger.info("Service: Updating shelf with id: {}", shelf);

        //validate input
        if (shelf == null || shelf.getShelfName().isEmpty()) {
            throw new BadRequestException("Shelf with Shelf Name is required");
        }

        // Update shelf in repository
        Shelf shelfFound = shelfRepository.updateShelf(shelf);
        return Map.of(
                "success", true,
                "message", "Shelf updated successfully",
                "data", shelf
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
}
