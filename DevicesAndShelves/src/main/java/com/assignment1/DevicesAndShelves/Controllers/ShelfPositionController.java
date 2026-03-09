package com.assignment1.DevicesAndShelves.Controllers;

import com.assignment1.DevicesAndShelves.Models.ShelfPosition;
import com.assignment1.DevicesAndShelves.Services.ShelfPositionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@CrossOrigin(origins = "http://localhost:4200/")
@RestController
@RequestMapping("/api/devices/shelfPositions")
public class ShelfPositionController {
    private final ShelfPositionService shelfPositionService;
    private static final Logger logger = LoggerFactory.getLogger(ShelfPositionController.class);

    public ShelfPositionController(ShelfPositionService shelfPositionService) {
        this.shelfPositionService = shelfPositionService;
    }

    // get properties of shelf position by clicking on the shelf position
    @GetMapping("/{shelfPositionId}")
    public ResponseEntity<Map<String, Object>> getShelfPositionById(@PathVariable String shelfPositionId) {
        logger.info("Controller: Fetching shelf position with id: {}", shelfPositionId);
        Map<String, Object> record = shelfPositionService.getShelfPositionById(shelfPositionId);
        return ResponseEntity.ok(Map.of(
                "success", true,
                "message", "Shelf position fetched successfully",
                "data", record
        ));
    }

    //delete shelf position when it is deleted from the device summary page
    @DeleteMapping("/delete/{shelfPositionId}")
    public ResponseEntity<Map<String, Object>> deleteShelfPositionById(@PathVariable String shelfPositionId){
        logger.info("Controller: Deleting shelf positions for spId: {}", shelfPositionId);
        shelfPositionService.deleteShelfPositionById(shelfPositionId);
        return ResponseEntity.ok(Map.of(
                "success", true,
                "message", "Shelf position deleted successfully"
        ));
    }


    // Update shelf positions from device summary page
    @PutMapping("/update/{shelfPositionId}/deprecated")
    public ResponseEntity<Map<String, Object>> updateShelfPositions(@PathVariable String shelfPositionId, @RequestParam(defaultValue = "") String shelfId) {
        logger.info("Controller: Updating shelf positions for spId: {}, shelfId{}",shelfPositionId, shelfId);
        ShelfPosition updatedSp = shelfPositionService.updateShelfPositions(shelfPositionId, shelfId);
        return ResponseEntity.ok(Map.of(
                "success", true,
                "message", "Shelf position updated successfully",
                "content", updatedSp
        ));
    }


    // Update shelf positions from device summary page
    @PutMapping("/update/{shelfPositionId}")
    public ResponseEntity<Map<String, Object>> updateShelfPosition(@PathVariable String shelfPositionId, @RequestBody ShelfPosition shelfPosition) {
        logger.info("Controller: Updating shelf positions for spId: {}",shelfPositionId);
        ShelfPosition updatedSp = shelfPositionService.updateShelfPosition(shelfPositionId, shelfPosition);
        return ResponseEntity.ok(Map.of(
                "success", true,
                "message", "Shelf position updated successfully",
                "content", updatedSp
        ));
    }
}
