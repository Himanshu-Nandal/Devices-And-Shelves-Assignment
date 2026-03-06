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
    private final ShelfPositionService shelfPositionService;

    @Autowired
    public DeviceService(DeviceRepository deviceRepository, ShelfPositionService shelfPositionService) {
        this.deviceRepository = deviceRepository;
        this.shelfPositionService = shelfPositionService;
    }


    public Map<String, Object> createDevice(Device device) {
        logger.info("Service: Creating device with Name: {}", device.getDeviceName());

        // Validate input
        validateDevice(device);
        if (deviceRepository.getDeviceByName(device.getDeviceName()) != null) {
            throw new BadRequestException("Device with the same name already exists");
        }

        // Generate UUID for device
        try{device.setDeviceId(UUID.randomUUID().toString()); }
        catch (Exception e){
            logger.error("Error generating UUID for device: {}", e.getMessage());
            throw new RuntimeException("Error generating UUID for device", e);
        }

        // Create device in repository
        deviceRepository.createDevice(device);
        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("message", "Device created successfully");
        response.put("content", device);
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

    public Map<String, Object> getDeviceById(String deviceId) {
        logger.info("Service: Fetching device with ID: {}", deviceId);

        // Validate input
        if (deviceId == null || deviceId.isEmpty()) {
            throw new BadRequestException("Device ID is required");
        }

        // Fetch device details from repository
        Device device = deviceRepository.getDeviceById(deviceId);
        if (device == null || device.getIsDeleted()) {
            throw new NotFoundException("Device not found with ID: " + deviceId);
        }

        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("message", "Device found successfully");
        response.put("content", device);
        return response;
    }

    public Map<String, Object> updateDevice(String deviceId, Device device) {
        logger.info("Service: Updating device with ID: {}", deviceId);

        // Validate input
        if (deviceId == null || deviceId.isEmpty()) {
            throw new BadRequestException("Device ID is required");
        }

        // Fetch existing device first
        Device existingDevice = deviceRepository.getDeviceById(deviceId);
        if (existingDevice == null || existingDevice.getIsDeleted()) {
            throw new NotFoundException("Device not found with ID: " + deviceId);
        }

        // Merge updates with existing data (only update non-null fields)
        if (device.getDeviceName() != null && !device.getDeviceName().isEmpty()) {
            existingDevice.setDeviceName(device.getDeviceName());
        }
        if (device.getPartNumber() != null && !device.getPartNumber().isEmpty()) {
            existingDevice.setPartNumber(device.getPartNumber());
        }
        if (device.getBuildingName() != null && !device.getBuildingName().isEmpty()) {
            existingDevice.setBuildingName(device.getBuildingName());
        }
        if (device.getDeviceType() != null && !device.getDeviceType().isEmpty()) {
            existingDevice.setDeviceType(device.getDeviceType());
        }
        if (device.getTotalShelfPositions() != null) {
            if (device.getTotalShelfPositions() <= 0) {
                throw new BadRequestException("Total shelf positions must be greater than 0");
            }
            int changesInShelfPositions = device.getTotalShelfPositions() - existingDevice.getTotalShelfPositions();
            if (changesInShelfPositions > 0) {
                shelfPositionService.createShelfPositions(deviceId, changesInShelfPositions, existingDevice.getTotalShelfPositions());
            } else if (changesInShelfPositions < 0) {
                shelfPositionService.deleteShelfPositions(deviceId, -changesInShelfPositions, existingDevice.getTotalShelfPositions());
            }
            existingDevice.setTotalShelfPositions(device.getTotalShelfPositions());
        }
        if (device.getImageUrl() != null) {
            existingDevice.setImageUrl(device.getImageUrl());
        }

        // Update device details in repository
        Device updatedDevice = deviceRepository.updateDevice(deviceId, existingDevice);

        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("message", "Device updated successfully");
        response.put("content", updatedDevice);
        return response;
    }

//    public Map<String, Object> updateDevice(Device device) {
//        logger.info("Service: Updating device with Name: {}", device.getDeviceName());
//
//        // Validate input
//        validateDevice(device);
//
//        // Update device details in repository
//        String deviceId = device.getDeviceId();
//        Device existingDevice = deviceRepository.getDeviceById(deviceId);
//        Integer changesInShelfPositions = device.getTotalShelfPositions() - existingDevice.getTotalShelfPositions();
//        if (changesInShelfPositions > 0) {
//            shelfPositionService.createShelfPositions(deviceId, changesInShelfPositions, existingDevice.getTotalShelfPositions());
//        } else if (changesInShelfPositions < 0) {
//            shelfPositionService.deleteShelfPositions(deviceId, changesInShelfPositions, existingDevice.getTotalShelfPositions());
//        }
//
//        Device updatedDevice = deviceRepository.updateDevice(deviceId, device);
//
//        if (updatedDevice == null) {
//            throw new NotFoundException("Device not found with Name: " + device.getDeviceName());
//        }
//
//        Map<String, Object> response = new HashMap<>();
//        response.put("success", true);
//        response.put("message", "Device updated successfully");
//        response.put("data", updatedDevice);
//        return response;
//    }

    public Map<String, Object> deleteDevice(String deviceId) {
        logger.info("Service: Soft deleting device with ID: {}", deviceId);

        // Validate input
        if (deviceId == null || deviceId.isEmpty()) {
            throw new BadRequestException("Device ID is required");
        }

        // Soft delete device in repository
        boolean deleted = deviceRepository.softDeleteDevice(deviceId);
        if (!deleted) {
            throw new NotFoundException("Device not found with ID: " + deviceId);
        }

        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("message", "Device deleted successfully");
        return response;
    }

    public Map<String, Object> getDeviceByName(String deviceName) {
        logger.info("Service: Fetching device with Name: {}", deviceName);

        // Validate input
        if (deviceName == null || deviceName.isEmpty()) {
            throw new BadRequestException("Device name is required");
        }

        // Fetch device details from repository
        Device device = deviceRepository.getDeviceByName(deviceName);
        if (device == null || device.getIsDeleted()) {
            throw new NotFoundException("Device not found with name: " + deviceName);
        }

        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("message", "Device found successfully");
        response.put("content", device);
        return response;
    }

    public Map<String, Object> getAllDevices(int page, int size, String search, boolean isDeleted) {
        logger.info("Service: Fetching devices with filters - page: {}, size: {}, deviceId: {}, isDeleted: {}",
                page, size, search, isDeleted);

        // Validate input
        if (page < 1) {
            throw new BadRequestException("Page number must be at least 1");
        }
        if (size <= 0) {
            throw new BadRequestException("Page size must be greater than 0");
        }

        // Fetch devices from repository
        Map<String, Object> record = deviceRepository.getAllDevices(page, size, search, isDeleted);
        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("message", "Devices fetched successfully");
        response.put("content", record.get("devices"));
        response.put("totalElements", record.get("totalCount"));
        response.put("pageNumber", page);
        response.put("pageSize", size);
        return response;
    }
}
