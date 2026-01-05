package com.example.multi_tanent.warehouse.service;

import com.example.multi_tanent.warehouse.entity.InventoryEntity;
import com.example.multi_tanent.warehouse.model.Inventory;
import com.example.multi_tanent.warehouse.model.InventoryResponse;
import com.example.multi_tanent.warehouse.repository.InventoryRepository;
import com.example.multi_tanent.warehouse.repository.ItemRepository;
import com.example.multi_tanent.warehouse.repository.LocationRepository;
import jakarta.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;

@Service("warehouseInventoryService")
public class InventoryService {
    private final InventoryRepository inventoryRepository;

    private final LocationRepository locationRepository;

    private final ItemRepository itemRepository;

    public InventoryService(InventoryRepository inventoryRepository, LocationRepository locationRepository,
            ItemRepository itemRepository) {
        this.inventoryRepository = inventoryRepository;
        this.locationRepository = locationRepository;
        this.itemRepository = itemRepository;
    }

    @Transactional
    public long availableStock(Long locationId, Long itemId) {
        return inventoryRepository.findByLocationIdAndItemId(locationId, itemId)
                .map(InventoryEntity::getQuantity)
                .orElse(0L);
    }

    @Transactional
    public void reduceStock(Long locationId, Long itemId, long qty) {
        var inv = inventoryRepository.findByLocationIdAndItemId(locationId, itemId)
                .orElseGet(() -> {
                    InventoryEntity newInv = new InventoryEntity();
                    newInv.setLocation(locationRepository.findById(locationId).orElseThrow());
                    newInv.setItem(itemRepository.findById(itemId).orElseThrow());
                    newInv.setQuantity(0L);
                    return inventoryRepository.save(newInv);
                });

        if (inv.getQuantity() < qty) {
            throw new IllegalArgumentException("Insufficient stock at source location");
        }

        inv.setQuantity(inv.getQuantity() - qty);
        inventoryRepository.save(inv);
    }

    @Transactional
    public void addStock(Long locationId, Long itemId, long qty) {
        var inv = inventoryRepository.findByLocationIdAndItemId(locationId, itemId)
                .orElseGet(() -> {
                    InventoryEntity newInv = new InventoryEntity();
                    newInv.setLocation(locationRepository.findById(locationId).orElseThrow());
                    newInv.setItem(itemRepository.findById(itemId).orElseThrow());
                    newInv.setQuantity(0L);
                    return inventoryRepository.save(newInv);
                });

        inv.setQuantity(inv.getQuantity() + qty);
        inventoryRepository.save(inv);
    }

    public void uploadInventory(Inventory inventory) {
        var location = locationRepository.findById(inventory.getLocationId())
                .orElseThrow(() -> new RuntimeException("Location not found"));

        var item = itemRepository.findById(inventory.getItemId())
                .orElseThrow(() -> new RuntimeException("Item not found"));

        // check if inventory already exists
        var existing = inventoryRepository.findByLocationIdAndItemId(
                inventory.getLocationId(), inventory.getItemId());

        InventoryEntity inv;

        if (existing.isPresent()) {
            // update stock
            inv = existing.get();
            inv.setQuantity(inv.getQuantity() + inventory.getQuantity());
        } else {
            // create new stock
            inv = new InventoryEntity();
            inv.setLocation(location);
            inv.setItem(item);
            inv.setQuantity(inventory.getQuantity());
        }

        inventoryRepository.save(inv);
    }

    @Transactional
    public void ensureInventoryExists(Long locationId, Long itemId) {
        inventoryRepository.findByLocationIdAndItemId(locationId, itemId)
                .orElseGet(() -> {
                    InventoryEntity inv = new InventoryEntity();
                    inv.setLocation(locationRepository.findById(locationId)
                            .orElseThrow(() -> new IllegalArgumentException("Invalid location")));
                    inv.setItem(itemRepository.findById(itemId)
                            .orElseThrow(() -> new IllegalArgumentException("Invalid item")));
                    inv.setQuantity(0L);
                    return inventoryRepository.save(inv);
                });
    }

    public List<InventoryResponse> getAllInventory() {
        List<InventoryEntity> inventories = inventoryRepository.findAll();
        return inventories.stream()
                .map(inv -> new InventoryResponse(
                        inv.getId(),
                        inv.getLocation().getId(),
                        inv.getLocation().getName(),
                        inv.getItem().getId(),
                        inv.getItem().getName(),
                        inv.getQuantity()))
                .collect(Collectors.toList());
    }

    /**
     * Get total stock for an item across all locations
     */
    public java.math.BigDecimal getTotalStockForItem(Long itemId) {
        List<InventoryEntity> inventories = inventoryRepository.findByItemId(itemId);
        long totalStock = inventories.stream()
                .mapToLong(InventoryEntity::getQuantity)
                .sum();
        return java.math.BigDecimal.valueOf(totalStock);
    }
}
