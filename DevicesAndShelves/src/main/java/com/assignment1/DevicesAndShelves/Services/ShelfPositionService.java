package com.assignment1.DevicesAndShelves.Services;

import com.assignment1.DevicesAndShelves.Exceptions.BadRequestException;
import com.assignment1.DevicesAndShelves.Repository.ShelfPositionRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class ShelfPositionService {
    private static final Logger logger = LoggerFactory.getLogger(ShelfPositionService.class);
    private final ShelfPositionRepository shelfPositionRepository;

    public ShelfPositionService(ShelfPositionRepository shelfPositionRepository) {
        this.shelfPositionRepository = shelfPositionRepository;
    }

    public Object createShelfPositions(String deviceId, Integer totalShelfPositions) {
        logger.info("Service: Creating shelf positions for deviceId: {}, totalShelfPositions: {}", deviceId, totalShelfPositions);

        // Validate input
        if (deviceId == null || deviceId.isEmpty()) {
            throw new BadRequestException("Device ID is required");
        }
        if (totalShelfPositions == null || totalShelfPositions <= 0) {
            throw new BadRequestException("Total shelf positions must be a positive integer");
        }

        // Create shelf positions in repository

    }
}
