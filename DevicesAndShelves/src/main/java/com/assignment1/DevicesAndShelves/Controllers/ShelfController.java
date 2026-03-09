package com.assignment1.DevicesAndShelves.Controllers;

import com.assignment1.DevicesAndShelves.Models.Shelf;
import com.assignment1.DevicesAndShelves.Services.ShelfService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@CrossOrigin(origins = "http://localhost:4200/")
@RestController
@RequestMapping("/api/shelves")
public class ShelfController {
    private final ShelfService shelfService;
    private static final Logger logger = LoggerFactory.getLogger(ShelfController.class);

    @Autowired
    public ShelfController(ShelfService shelfService) {
        this.shelfService = shelfService;
    }

    // CRUD
    @PostMapping("/create")
    public ResponseEntity<Map<String, Object>> createShelf(@RequestBody Shelf shelf) {
        logger.info("Controller: Creating Shelf: {}", shelf.getShelfName());
        Map<String, Object> response = shelfService.createShelf(shelf);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping("/{shelfId}")
    public ResponseEntity<Map<String, Object>> getShelfById(@PathVariable String shelfId) {
        logger.info("Controller: Fetching shelf with id: {}", shelfId);
        return new ResponseEntity<>(shelfService.getShelfById(shelfId), HttpStatus.OK);
    }

    @GetMapping("/search/{shelfName}")
    public ResponseEntity<Map<String, Object>> getShelfByName(@PathVariable String shelfName) {
        logger.info("Controller: Fetching shelf with Name: {}", shelfName);
        return new ResponseEntity<>(shelfService.getShelfByName(shelfName), HttpStatus.OK);
    }

    @PutMapping("/{shelfId}")
    public ResponseEntity<Map<String, Object>> updateShelf(@PathVariable String shelfId, @RequestBody Shelf shelf) {
        logger.info("Controller: Updating shelf with Name: {}", shelf.getShelfName());
        return new ResponseEntity<>(shelfService.updateShelf(shelfId, shelf), HttpStatus.OK);
    }

    @DeleteMapping("/{shelfId}")
    public ResponseEntity<Map<String, Object>> deleteShelf(@PathVariable String shelfId) {
        logger.info("Controller: Deleting shelf with id: {}", shelfId);
        return new ResponseEntity<>(shelfService.deleteShelf(shelfId), HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<Map<String, Object>> getShelfPage(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "6") int size,
            @RequestParam(required = false) String search,
            @RequestParam(defaultValue = "false") boolean isDeleted
    ) {
        logger.info("Controller: Fetching shelves with filters - page: {}, size: {}, search: {}, isDeleted: {}",
                page, size, search, isDeleted);
        return new ResponseEntity<>(shelfService.getShelfPage(page, size, search, isDeleted), HttpStatus.OK);
    }
    
    @GetMapping("/all")
    public ResponseEntity<List<Shelf>> getShelves() {
        logger.info("Controller: Fetching shelves for ");
        List<Shelf> shelves = shelfService.getShelves();
        return new ResponseEntity<>(shelves, HttpStatus.OK);
    }

}
