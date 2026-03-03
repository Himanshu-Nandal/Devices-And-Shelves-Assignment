package com.assignment1.DevicesAndShelves.Controllers;

import com.assignment1.DevicesAndShelves.Services.ShelfPositionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/devices/{deviceId}/shelfPositions")
public class ShelfPositionController {
    private final ShelfPositionService shelfPositionService;
    private static final Logger logger = LoggerFactory.getLogger(ShelfPositionController.class);

    public ShelfPositionController(ShelfPositionService shelfPositionService) {
        this.shelfPositionService = shelfPositionService;
    }

    // Update shelf positions from device summary page
    @PutMapping("/update/{shelfPositionId}/{shelfId}")
    public ResponseEntity<Map<String, Object>> updateShelfPositions(@PathVariable String shelfPositionId, @PathVariable String shelfId){
        logger.info("Controller: Updating shelf positions for spId: {}, shelfId{}",shelfPositionId, shelfId);
        shelfPositionService.updateShelfPositions(shelfPositionId, shelfId);
        return ResponseEntity.ok(Map.of(
                "success", true,
                "message", "Shelf position updated successfully"
        ));
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
}
