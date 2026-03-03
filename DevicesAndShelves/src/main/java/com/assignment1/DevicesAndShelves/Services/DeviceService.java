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
        logger.info("Service: Creating device with Name: {}", device.getDeviceName());

        // Validate input
        validateDevice(device);

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
        response.put("data", device);
        return response;
    }

    public Map<String, Object> updateDevice(String deviceId, Device device) {
        logger.info("Service: Updating device with ID: {}", deviceId);

        // Validate input
        Device existingDevice = deviceRepository.getDeviceById(deviceId);
        if (deviceId.isEmpty() || device.getDeviceName() == null || device.getDeviceName().isEmpty()) {
            throw new BadRequestException("Device ID is required");
        }
// Not needed as update method is only called on a device when we have the device already opened in the UI, i,e.,
// it already exists. If it doesn't exist, the client should not be able to call the update API in the first place. So this check is redundant.
//        else if (existingDevice == null || existingDevice.getIsDeleted()) {
//            throw new NotFoundException("Device not found with ID: " + deviceId);
//        }
        else if (device.getTotalShelfPositions() != null && device.getTotalShelfPositions() <= 0) {
            throw new BadRequestException("Total shelf positions must be greater than 0");
        }

        // Update device details in repository
        Device updatedDevice = deviceRepository.updateDevice(deviceId, device);
        if (updatedDevice == null) {
            throw new NotFoundException("Device not found with ID: " + deviceId);
        }

        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("message", "Device updated successfully");
        response.put("data", updatedDevice);
        return response;
    }

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
        response.put("data", device);
        return response;
    }
}
