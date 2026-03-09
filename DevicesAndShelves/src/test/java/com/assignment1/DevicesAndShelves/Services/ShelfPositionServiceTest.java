package com.assignment1.DevicesAndShelves.Services;

import com.assignment1.DevicesAndShelves.Models.ShelfPosition;
import com.assignment1.DevicesAndShelves.Repository.ShelfPositionRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.junit.jupiter.api.Assertions.*;

import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ShelfPositionServiceTest {

    @Mock
    ShelfPositionRepository shelfPositionRepository;

    @InjectMocks
    ShelfPositionService shelfPositionService;

    @Test
    void createShelfPositionsTest() {
        shelfPositionService.createShelfPositions("device123", 5, 0);
    }

    @Test
    void deleteShelfPositionsTest() {
    }

    @Test
    void updateShelfPositionsTest() {
        String shelfPositionId = "shelfPosition123";
        String shelfId = "shelf456";

        ShelfPosition dummySp = new ShelfPosition();
        dummySp.setShelfPositionId(shelfPositionId);
        dummySp.setShelfId(shelfId);
        when(shelfPositionRepository.updateShelfPositions(shelfId, shelfPositionId)).thenReturn(dummySp);

        ShelfPosition  updatedShelfPosition = shelfPositionService.updateShelfPositions(shelfPositionId, shelfId);

        assertNotNull(updatedShelfPosition);
        assertDoesNotThrow(() -> shelfPositionService.updateShelfPositions(shelfPositionId, shelfId));
        assertEquals(shelfPositionId, updatedShelfPosition.getShelfPositionId());
        assertEquals(shelfId, updatedShelfPosition.getShelfId());
    }

    @Test
    void getShelfPositionByIdTest() {
    }

    @Test
    void getShelfPositionsByDeviceIdTest() {
    }

    @Test
    void deleteShelfPositionByIdTest() {
    }

    @Test
    void updateShelfPositionTest() {
    }
}