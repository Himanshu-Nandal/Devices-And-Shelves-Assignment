package com.assignment1.DevicesAndShelves.Services;

import com.assignment1.DevicesAndShelves.Exceptions.BadRequestException;
import com.assignment1.DevicesAndShelves.Repository.ShelfPositionRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class ShelfPositionService {
    private static final Logger logger = LoggerFactory.getLogger(ShelfPositionService.class);
    private final ShelfPositionRepository shelfPositionRepository;

    @Autowired
    public ShelfPositionService(ShelfPositionRepository shelfPositionRepository) {
        this.shelfPositionRepository = shelfPositionRepository;
    }

    public void createShelfPositions(String deviceId, Integer change, Integer initial) {
        logger.info("Service: Creating shelf positions for deviceId: {}, change: {}, initial: {}", deviceId, change, initial);

        if (deviceId == null || deviceId.isEmpty()) {
            throw new BadRequestException("Device ID is required");
        }

        shelfPositionRepository.createShelfPositions(deviceId, change, initial);

    }

    public void deleteShelfPositions(String deviceId, Integer change, Integer initial) {
        logger.info("Service: Deleting shelf positions for deviceId: {}, change: {}, initial: {}", deviceId, change, initial);

        if (deviceId == null || deviceId.isEmpty()) {
            throw new BadRequestException("Device ID is required");
        }

        shelfPositionRepository.deleteShelfPositions(deviceId, change, initial);

    }

    public void updateShelfPositions(String shelfPositionId, String shelfId) {
        logger.info("Service: Updating shelf positions for shelfPositionId: {} with shelfId: {}", shelfPositionId, shelfId);

        if (shelfId == null || shelfId.isEmpty()) {
            throw new BadRequestException("Shelf ID is required");
        }
        if (shelfPositionId == null || shelfPositionId.isEmpty()) {
            throw new BadRequestException("Shelf Position ID is required");
        }

        shelfPositionRepository.updateShelfPositions(shelfId, shelfPositionId);
    }

    public Map<String, Object> getShelfPositionById(String shelfPositionId) {
        logger.info("Service: Fetching shelf position with id: {}", shelfPositionId);
        if (shelfPositionId == null || shelfPositionId.isEmpty()) {
            throw new BadRequestException("Shelf Position ID is required");
        }
        Map<String, Object> response = shelfPositionRepository.getShelfPositionById(shelfPositionId);
        if (response == null || response.isEmpty()) {
            throw new BadRequestException("Shelf Position not found with ID: " + shelfPositionId);
        }
        return response;
    }
}
