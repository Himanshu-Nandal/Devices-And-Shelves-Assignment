//package com.assignment1.DevicesAndShelves.Controllers;
//
//import com.assignment1.DevicesAndShelves.Models.Device;
//import com.assignment1.DevicesAndShelves.Services.DeviceService;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.*;
//
//import java.util.Map;
//
//@RestController
//@RequestMapping("/api/devices")
//public class DeviceControllerCo {
//    private final DeviceService deviceService;
//    private final Logger logger = LoggerFactory.getLogger(DeviceControllerCo.class);
//
//    @Autowired
//    public DeviceControllerCo(DeviceService deviceService) {
//        this.deviceService = deviceService;
//    }
//
//    /**
//     * Create a new device
//     * POST /api/devices
//     * Request Body: Device object
//     * Response: Created device with deviceId
//     */
//    @PostMapping
//    public ResponseEntity<Map<String, Object>> createDevice(@RequestBody Device device) {
//        logger.info("Creating device: {}", device.getDeviceName());
//        Map<String, Object> response = deviceService.createDevice(device);
//        return new ResponseEntity<>(response, HttpStatus.CREATED);
//    }
//
//    /**
//     * Get device by ID
//     * GET /api/devices/{deviceId}
//     * Response: Device object
//     */
//    @GetMapping("/{deviceId}")
//    public ResponseEntity<Map<String, Object>> getDeviceById(@PathVariable String deviceId) {
//        logger.info("Fetching device with ID: {}", deviceId);
//        Map<String, Object> response = deviceService.getDeviceById(deviceId);
//        return new ResponseEntity<>(response, HttpStatus.OK);
//    }
//
//    /**
//     * Get all devices with pagination and filtering
//     * GET /api/devices?page=0&size=10&deviceType=RACK&buildingName=Building1&isDeleted=false
//     * Query Parameters:
//     * - page: Page number (default: 0)
//     * - size: Page size (default: 10)
//     * - deviceType: Filter by device type (optional)
//     * - buildingName: Filter by building name (optional)
//     * - isDeleted: Filter by deletion status (default: false)
//     * Response: Paginated list of devices
//     */
//    @GetMapping
//    public ResponseEntity<Map<String, Object>> getAllDevices(
//            @RequestParam(defaultValue = "0") int page,
//            @RequestParam(defaultValue = "10") int size,
//            @RequestParam(required = false) String deviceType,
//            @RequestParam(required = false) String buildingName,
//            @RequestParam(defaultValue = "false") Boolean isDeleted) {
//        logger.info("Fetching devices - page: {}, size: {}, type: {}, building: {}, deleted: {}",
//                    page, size, deviceType, buildingName, isDeleted);
//        Map<String, Object> response = deviceService.getAllDevices(page, size, deviceType, buildingName, isDeleted);
//        return new ResponseEntity<>(response, HttpStatus.OK);
//    }
//
//    /**
//     * Update device by ID
//     * PUT /api/devices/{deviceId}
//     * Request Body: Device object with updated fields
//     * Response: Updated device
//     */
//    @PutMapping("/{deviceId}")
//    public ResponseEntity<Map<String, Object>> updateDevice(
//            @PathVariable String deviceId,
//            @RequestBody Device device) {
//        logger.info("Updating device with ID: {}", deviceId);
//        Map<String, Object> response = deviceService.updateDevice(deviceId, device);
//        return new ResponseEntity<>(response, HttpStatus.OK);
//    }
//
//    /**
//     * Soft delete device by ID
//     * DELETE /api/devices/{deviceId}
//     * Response: Success message
//     */
//    @DeleteMapping("/{deviceId}")
//    public ResponseEntity<Map<String, Object>> deleteDevice(@PathVariable String deviceId) {
//        logger.info("Soft deleting device with ID: {}", deviceId);
//        Map<String, Object> response = deviceService.deleteDevice(deviceId);
//        return new ResponseEntity<>(response, HttpStatus.OK);
//    }
//
//    /**
//     * Search devices by name or part number
//     * GET /api/devices/search?query=rack
//     * Query Parameters:
//     * - query: Search term (searches in deviceName and partNumber)
//     * Response: List of matching devices
//     */
//    @GetMapping("/search")
//    public ResponseEntity<Map<String, Object>> searchDevices(@RequestParam String query) {
//        logger.info("Searching devices with query: {}", query);
//        Map<String, Object> response = deviceService.searchDevices(query);
//        return new ResponseEntity<>(response, HttpStatus.OK);
//    }
//
//    /**
//     * Get devices by building name
//     * GET /api/devices/building/{buildingName}
//     * Response: List of devices in the specified building
//     */
//    @GetMapping("/building/{buildingName}")
//    public ResponseEntity<Map<String, Object>> getDevicesByBuilding(@PathVariable String buildingName) {
//        logger.info("Fetching devices for building: {}", buildingName);
//        Map<String, Object> response = deviceService.getDevicesByBuilding(buildingName);
//        return new ResponseEntity<>(response, HttpStatus.OK);
//    }
//
//    /**
//     * Get devices by type
//     * GET /api/devices/type/{deviceType}
//     * Response: List of devices of the specified type
//     */
//    @GetMapping("/type/{deviceType}")
//    public ResponseEntity<Map<String, Object>> getDevicesByType(@PathVariable String deviceType) {
//        logger.info("Fetching devices of type: {}", deviceType);
//        Map<String, Object> response = deviceService.getDevicesByType(deviceType);
//        return new ResponseEntity<>(response, HttpStatus.OK);
//    }
//
//    /**
//     * Restore a soft-deleted device
//     * PATCH /api/devices/{deviceId}/restore
//     * Response: Restored device
//     */
//    @PatchMapping("/{deviceId}/restore")
//    public ResponseEntity<Map<String, Object>> restoreDevice(@PathVariable String deviceId) {
//        logger.info("Restoring device with ID: {}", deviceId);
//        Map<String, Object> response = deviceService.restoreDevice(deviceId);
//        return new ResponseEntity<>(response, HttpStatus.OK);
//    }
//
//    /**
//     * Get device statistics
//     * GET /api/devices/stats
//     * Response: Statistics about devices (total count, by type, by building, etc.)
//     */
//    @GetMapping("/stats")
//    public ResponseEntity<Map<String, Object>> getDeviceStats() {
//        logger.info("Fetching device statistics");
//        Map<String, Object> response = deviceService.getDeviceStats();
//        return new ResponseEntity<>(response, HttpStatus.OK);
//    }
//}
