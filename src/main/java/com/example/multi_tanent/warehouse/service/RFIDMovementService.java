package com.example.multi_tanent.warehouse.service;

import com.example.multi_tanent.warehouse.entity.InventoryEntity;
import com.example.multi_tanent.warehouse.entity.RFIDMovementLogEntity;
import com.example.multi_tanent.warehouse.entity.RFIDTagEntity;
import com.example.multi_tanent.warehouse.model.RFIDMovementRequest;
import com.example.multi_tanent.warehouse.repository.InventoryRepository;
import com.example.multi_tanent.warehouse.repository.LocationRepository;
import com.example.multi_tanent.warehouse.repository.RFIDMovementRepository;
import com.example.multi_tanent.warehouse.repository.RFIDTagRepository;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class RFIDMovementService {

    private final RFIDMovementRepository repo;
    private final RFIDTagRepository tagRepo;
    private final InventoryRepository inventoryRepo;
    private final LocationRepository locationRepo;

    public RFIDMovementService(
            RFIDMovementRepository repo,
            RFIDTagRepository tagRepo,
            InventoryRepository inventoryRepo,
            LocationRepository locationRepo) {
        this.repo = repo;
        this.tagRepo = tagRepo;
        this.inventoryRepo = inventoryRepo;
        this.locationRepo = locationRepo;
    }

    /**
     * Log RFID movement without auto-updating inventory.
     * Use this when inventory is managed separately (e.g., Gate In/Out).
     */
    public void logMovementOnly(RFIDMovementRequest req) {
        RFIDMovementLogEntity log = new RFIDMovementLogEntity();
        log.setTagCode(req.getTagCode());
        log.setLocationId(req.getLocationId());
        log.setMovementType(req.getMovementType());
        log.setTimestamp(LocalDateTime.now());
        repo.save(log);
    }

    /**
     * Log RFID movement with auto inventory update (for internal movements).
     * This is for MOVE operations within the warehouse.
     */
    public void logMovement(RFIDMovementRequest req) {
        logMovementOnly(req);

        // Only auto-update for MOVE operations
        if ("MOVE".equalsIgnoreCase(req.getMovementType())) {
            autoUpdateStockForMove(req.getTagCode(), req.getLocationId(), 1L);
        }
    }

    /**
     * Auto-update stock for internal MOVE operations.
     * Reduces from old location and adds to new location.
     */
    private void autoUpdateStockForMove(String tagCode, Long newLocationId, Long quantity) {
        RFIDTagEntity tag = tagRepo.findByTagCode(tagCode)
                .orElseThrow(() -> new RuntimeException("RFID tag not registered"));

        Long itemId = tag.getItem().getId();

        // Find all inventory rows for this item
        List<InventoryEntity> invList = inventoryRepo.findByItemId(itemId);

        if (invList.isEmpty()) {
            throw new RuntimeException("No inventory exists for this item");
        }

        // Find old inventory entry (quantity > 0)
        InventoryEntity oldInv = invList.stream()
                .filter(i -> i.getQuantity() > 0)
                .findFirst()
                .orElse(invList.getFirst());

        // Decrement old location quantity
        long oldQty = oldInv.getQuantity();
        if (oldQty > 0) {
            long reduceQty = Math.min(quantity, oldQty);
            oldInv.setQuantity(oldQty - reduceQty);
            inventoryRepo.save(oldInv);
        }

        // Find or create new location inventory entry
        InventoryEntity newInv = inventoryRepo
                .findByLocationIdAndItemId(newLocationId, itemId)
                .orElse(null);

        if (newInv == null) {
            newInv = new InventoryEntity();
            newInv.setItem(tag.getItem());
            newInv.setLocation(
                    locationRepo.findById(newLocationId)
                            .orElseThrow(() -> new RuntimeException("Location not found")));
            newInv.setQuantity(0L);
        }

        // Increment quantity for new location
        newInv.setQuantity(newInv.getQuantity() + quantity);
        inventoryRepo.save(newInv);
    }

    public List<RFIDMovementLogEntity> getLogs() {
        return repo.findAll();
    }
}
