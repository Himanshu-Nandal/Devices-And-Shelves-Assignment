package com.assignment1.DevicesAndShelves.Controllers;

import com.assignment1.DevicesAndShelves.Models.Device;
import com.assignment1.DevicesAndShelves.Services.DeviceService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/devices")
public class DeviceController {
    private final DeviceService deviceService;
    private static final Logger logger = LoggerFactory.getLogger(DeviceController.class);

    public DeviceController(DeviceService deviceService) {
        this.deviceService = deviceService;
    }

    // Creating new device by clicking a button on the client, which will open a form to fill the details of the device and then submit to create the device
    @PostMapping("/create")
    public ResponseEntity<Map<String, Object>> createDevice(@RequestBody Device device) {
        logger.info("Creating device: {}", device.getDeviceName());
        Map<String, Object> response = deviceService.createDevice(device);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

//    // Get the device details by clicking on the device on the client, which will show the details of the device and the shelf positions in that device
//    @GetMapping("/{deviceId}")
//    public ResponseEntity<Map<String, Object>> getDeviceById(@PathVariable String deviceId) {
//        logger.info("Fetching device with ID: {}", deviceId);
//        Map<String, Object> response = deviceService.getDeviceById(deviceId);
//        return new ResponseEntity<>(response, HttpStatus.OK);
//    }
//
//    // for pagination of devices on landing page of the client
//    @GetMapping
//    public ResponseEntity<Map<String, Object>> getAllDevices(
//            @RequestParam(defaultValue = "0") int page,
//            @RequestParam(defaultValue = "10") int size,
//            @RequestParam(required = false) String deviceType,
//            @RequestParam(required = false) String buildingName,
//            @RequestParam(defaultValue = "false") boolean isDeleted
//    ) {
//        logger.info("Fetching devices with filters - page: {}, size: {}, deviceType: {}, buildingName: {}, isDeleted: {}",
//                page, size, deviceType, buildingName, isDeleted);
//        Map<String, Object> response = deviceService.getAllDevices(page, size, deviceType, buildingName, isDeleted);
//        return new ResponseEntity<>(response, HttpStatus.OK);
//    }
//
//    // To update a device's details (except deviceId)
//    @PutMapping("/{deviceId}")
//    public ResponseEntity<Map<String, Object>> updateDevice(@PathVariable String deviceId, @RequestBody Device device) {
//        logger.info("Updating device with ID: {}", deviceId);
//        Map<String, Object> response = deviceService.updateDevice(deviceId, device);
//        return new ResponseEntity<>(response, HttpStatus.OK);
//    }
//
//    // Soft delete a device by setting isDeleted to true
//    @DeleteMapping("/{deviceId}")
//    public ResponseEntity<Map<String, Object>> deleteDevice(@PathVariable String deviceId) {
//        logger.info("Soft deleting device with ID: {}", deviceId);
//        Map<String, Object> response = deviceService.deleteDevice(deviceId);
//        return new ResponseEntity<>(response, HttpStatus.OK);
//    }
//
//    // Get the device from searching deviceId on search bar
//    @GetMapping("/search/{deviceId}")
//    public ResponseEntity<Map<String, Object>> searchDeviceById(@PathVariable String deviceId) {
//        logger.info("Searching device with ID: {}", deviceId);
//        Map<String, Object> response = deviceService.searchDeviceById(deviceId);
//        return new ResponseEntity<>(response, HttpStatus.OK);
//    }
//
//    // Get all the devices in the building
//    @GetMapping("/building/{buildingName}")
//    public ResponseEntity<Map<String, Object>> getDevicesByBuilding(@PathVariable String buildingName) {
//        logger.info("Fetching devices for building: {}", buildingName);
//        Map<String, Object> response = deviceService.getDevicesByBuilding(buildingName);
//        return new ResponseEntity<>(response, HttpStatus.OK);
//    }
//
//    // Get all the devices of a particular type (e.g., rack, cabinet, etc.)
//    @GetMapping("/type/{deviceType}")
//    public ResponseEntity<Map<String, Object>> getDevicesByType(@PathVariable String deviceType) {
//        logger.info("Fetching devices of type: {}", deviceType);
//        Map<String, Object> response = deviceService.getDevicesByType(deviceType);
//        return new ResponseEntity<>(response, HttpStatus.OK);
//    }

}
