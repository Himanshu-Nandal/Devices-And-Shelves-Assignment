//package com.assignment1.DevicesAndShelves.Services;
//
//import com.assignment1.DevicesAndShelves.Exceptions.BadRequestException;
//import com.assignment1.DevicesAndShelves.Exceptions.NotFoundException;
//import com.assignment1.DevicesAndShelves.Models.Device;
//import com.assignment1.DevicesAndShelves.Repository.DeviceRepository;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//import java.util.UUID;
//
//@Service
//public class DeviceServiceCo {
//    private static final Logger logger = LoggerFactory.getLogger(DeviceServiceCo.class);
//    private final DeviceRepository deviceRepository;
//
//    @Autowired
//    public DeviceServiceCo(DeviceRepository deviceRepository) {
//        this.deviceRepository = deviceRepository;
//    }
//
//    /**
//     * Create a new device
//     */
//    public Map<String, Object> createDevice(Device device) {
//        logger.info("Creating device: {}", device.getDeviceName());
//
//        // Validate input
//        validateDevice(device);
//
//        // Generate UUID for device
//        device.setDeviceId(UUID.randomUUID().toString());
//        device.setIsDeleted(false);
//
//        // Create device in repository
//        deviceRepository.createDevice(device);
//
//        Map<String, Object> response = new HashMap<>();
//        response.put("success", true);
//        response.put("message", "Device created successfully");
//        response.put("data", device);
//
//        return response;
//    }
//
//    /**
//     * Get device by ID
//     */
//    public Map<String, Object> getDeviceById(String deviceId) {
//        logger.info("Fetching device with ID: {}", deviceId);
//
//        Device device = deviceRepository.findById(deviceId);
//
//        if (device == null) {
//            throw new NotFoundException("Device not found with ID: " + deviceId);
//        }
//
//        Map<String, Object> response = new HashMap<>();
//        response.put("success", true);
//        response.put("data", device);
//
//        return response;
//    }
//
//    /**
//     * Get all devices with pagination and filtering
//     */
//    public Map<String, Object> getAllDevices(int page, int size, String deviceType, String buildingName, Boolean isDeleted) {
//        logger.info("Fetching all devices with filters");
//
//        // Validate pagination parameters
//        if (page < 0) {
//            throw new BadRequestException("Page number cannot be negative");
//        }
//        if (size <= 0 || size > 100) {
//            throw new BadRequestException("Page size must be between 1 and 100");
//        }
//
//        Map<String, Object> result = deviceRepository.findAll(page, size, deviceType, buildingName, isDeleted);
//
//        Map<String, Object> response = new HashMap<>();
//        response.put("success", true);
//        response.put("data", result);
//
//        return response;
//    }
//
//    /**
//     * Update device
//     */
//    public Map<String, Object> updateDevice(String deviceId, Device device) {
//        logger.info("Updating device with ID: {}", deviceId);
//
//        // Check if device exists
//        Device existingDevice = deviceRepository.findById(deviceId);
//        if (existingDevice == null) {
//            throw new NotFoundException("Device not found with ID: " + deviceId);
//        }
//
//        // Validate update data
//        validateDeviceUpdate(device);
//
//        // Update device
//        device.setDeviceId(deviceId);
//        deviceRepository.updateDevice(deviceId, device);
//
//        Map<String, Object> response = new HashMap<>();
//        response.put("success", true);
//        response.put("message", "Device updated successfully");
//        response.put("data", device);
//
//        return response;
//    }
//
//    /**
//     * Soft delete device
//     */
//    public Map<String, Object> deleteDevice(String deviceId) {
//        logger.info("Soft deleting device with ID: {}", deviceId);
//
//        Device device = deviceRepository.findById(deviceId);
//        if (device == null) {
//            throw new NotFoundException("Device not found with ID: " + deviceId);
//        }
//
//        if (device.getIsDeleted()) {
//            throw new BadRequestException("Device is already deleted");
//        }
//
//        deviceRepository.softDelete(deviceId);
//
//        Map<String, Object> response = new HashMap<>();
//        response.put("success", true);
//        response.put("message", "Device deleted successfully");
//
//        return response;
//    }
//
//    /**
//     * Search devices by name or part number
//     */
//    public Map<String, Object> searchDevices(String query) {
//        logger.info("Searching devices with query: {}", query);
//
//        if (query == null || query.trim().isEmpty()) {
//            throw new BadRequestException("Search query cannot be empty");
//        }
//
//        List<Device> devices = deviceRepository.searchDevices(query);
//
//        Map<String, Object> response = new HashMap<>();
//        response.put("success", true);
//        response.put("count", devices.size());
//        response.put("data", devices);
//
//        return response;
//    }
//
//    /**
//     * Get devices by building
//     */
//    public Map<String, Object> getDevicesByBuilding(String buildingName) {
//        logger.info("Fetching devices for building: {}", buildingName);
//
//        if (buildingName == null || buildingName.trim().isEmpty()) {
//            throw new BadRequestException("Building name cannot be empty");
//        }
//
//        List<Device> devices = deviceRepository.findByBuilding(buildingName);
//
//        Map<String, Object> response = new HashMap<>();
//        response.put("success", true);
//        response.put("count", devices.size());
//        response.put("data", devices);
//
//        return response;
//    }
//
//    /**
//     * Get devices by type
//     */
//    public Map<String, Object> getDevicesByType(String deviceType) {
//        logger.info("Fetching devices of type: {}", deviceType);
//
//        if (deviceType == null || deviceType.trim().isEmpty()) {
//            throw new BadRequestException("Device type cannot be empty");
//        }
//
//        List<Device> devices = deviceRepository.findByType(deviceType);
//
//        Map<String, Object> response = new HashMap<>();
//        response.put("success", true);
//        response.put("count", devices.size());
//        response.put("data", devices);
//
//        return response;
//    }
//
//    /**
//     * Restore deleted device
//     */
//    public Map<String, Object> restoreDevice(String deviceId) {
//        logger.info("Restoring device with ID: {}", deviceId);
//
//        Device device = deviceRepository.findById(deviceId);
//        if (device == null) {
//            throw new NotFoundException("Device not found with ID: " + deviceId);
//        }
//
//        if (!device.getIsDeleted()) {
//            throw new BadRequestException("Device is not deleted");
//        }
//
//        deviceRepository.restore(deviceId);
//        device.setIsDeleted(false);
//
//        Map<String, Object> response = new HashMap<>();
//        response.put("success", true);
//        response.put("message", "Device restored successfully");
//        response.put("data", device);
//
//        return response;
//    }
//
//    /**
//     * Get device statistics
//     */
//    public Map<String, Object> getDeviceStats() {
//        logger.info("Fetching device statistics");
//
//        Map<String, Object> stats = deviceRepository.getStatistics();
//
//        Map<String, Object> response = new HashMap<>();
//        response.put("success", true);
//        response.put("data", stats);
//
//        return response;
//    }
//
//    /**
//     * Validate device input
//     */
//    private void validateDevice(Device device) {
//        if (device == null) {
//            throw new BadRequestException("Device cannot be null");
//        }
//
//        if (device.getDeviceName() == null || device.getDeviceName().trim().isEmpty()) {
//            throw new BadRequestException("Device name is required");
//        }
//
//        if (device.getPartNumber() == null || device.getPartNumber().trim().isEmpty()) {
//            throw new BadRequestException("Part number is required");
//        }
//
//        if (device.getBuildingName() == null || device.getBuildingName().trim().isEmpty()) {
//            throw new BadRequestException("Building name is required");
//        }
//
//        if (device.getDeviceType() == null || device.getDeviceType().trim().isEmpty()) {
//            throw new BadRequestException("Device type is required");
//        }
//
//        if (device.getTotalShelfPositions() == null || device.getTotalShelfPositions() <= 0) {
//            throw new BadRequestException("Total shelf positions must be greater than 0");
//        }
//    }
//
//    /**
//     * Validate device update
//     */
//    private void validateDeviceUpdate(Device device) {
//        if (device == null) {
//            throw new BadRequestException("Device update data cannot be null");
//        }
//
//        // Only validate non-null fields for updates
//        if (device.getDeviceName() != null && device.getDeviceName().trim().isEmpty()) {
//            throw new BadRequestException("Device name cannot be empty");
//        }
//
//        if (device.getPartNumber() != null && device.getPartNumber().trim().isEmpty()) {
//            throw new BadRequestException("Part number cannot be empty");
//        }
//
//        if (device.getTotalShelfPositions() != null && device.getTotalShelfPositions() <= 0) {
//            throw new BadRequestException("Total shelf positions must be greater than 0");
//        }
//    }
//
//    public Map<String, Object> searchDeviceById(String deviceId) {
//        return null;
//    }
//}

