package com.assignment1.DevicesAndShelves.Services;

import com.assignment1.DevicesAndShelves.Exceptions.BadRequestException;
import com.assignment1.DevicesAndShelves.Exceptions.NotFoundException;
import com.assignment1.DevicesAndShelves.Models.Device;
import com.assignment1.DevicesAndShelves.Repository.DeviceRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class DeviceService {
    private static final Logger logger = LoggerFactory.getLogger(DeviceService.class);
    private final DeviceRepository deviceRepository;

    @Autowired
    public DeviceService(DeviceRepository deviceRepository) {
        this.deviceRepository = deviceRepository;
    }


    public Map<String, Object> createDevice(Device device) {
        logger.info("Creating device: {}", device.getDeviceName());

        // Validate input
        validateDevice(device);

        // Generate UUID for device
        try{device.setDeviceId(UUID.randomUUID().toString()); }
        catch (Exception e){
            logger.error("Error generating UUID for device: {}", e.getMessage());
            throw new RuntimeException("Error generating UUID for device", e);
        }
        try{device.setIsDeleted(false); }
        catch (Exception e){
            logger.error("Error setting isDeleted flag for device: {}", e.getMessage());
            throw new RuntimeException("Error setting isDeleted flag for device", e);
        }

        // Create device in repository
        deviceRepository.createDevice(device);
        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("message", "Device created successfully");
        response.put("data", device);
        return response;

    }

    private void validateDevice(Device device) {

        if (device.getDeviceName() == null || device.getDeviceName().isEmpty()) {
            throw new BadRequestException("Device name is required");
        }

        if (device.getPartNumber() == null || device.getPartNumber().isEmpty()) {
            throw new BadRequestException("Part number is required");
        }

        if (device.getBuildingName() == null || device.getBuildingName().isEmpty()) {
            throw new BadRequestException("Building name is required");
        }

        if (device.getDeviceType() == null || device.getDeviceType().isEmpty()) {
            throw new BadRequestException("Device type is required");
        }

        if (device.getTotalShelfPositions() <= 0) {
            throw new BadRequestException("Number of shelf positions must be greater than 0");
        }

    }
}
