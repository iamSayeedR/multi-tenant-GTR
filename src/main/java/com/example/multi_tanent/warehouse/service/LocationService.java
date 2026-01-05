package com.example.multi_tanent.warehouse.service;

import com.example.multi_tanent.warehouse.entity.InventoryEntity;
import com.example.multi_tanent.warehouse.entity.ItemEntity;
import com.example.multi_tanent.warehouse.entity.LocationEntity;
import com.example.multi_tanent.warehouse.model.Location;
import com.example.multi_tanent.warehouse.model.LocationNode;
import com.example.multi_tanent.warehouse.repository.InventoryRepository;
import com.example.multi_tanent.warehouse.repository.ItemRepository;
import com.example.multi_tanent.warehouse.repository.LocationRepository;
import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class LocationService {

    private final LocationRepository locationRepository;

    private final ItemRepository itemRepository;

    private final InventoryRepository inventoryRepository;

    private final com.example.multi_tanent.warehouse.mapper.LocationMapper locationMapper;

    public LocationService(LocationRepository locationRepository, ItemRepository itemRepository,
            InventoryRepository inventoryRepository,
            com.example.multi_tanent.warehouse.mapper.LocationMapper locationMapper) {
        this.locationRepository = locationRepository;
        this.itemRepository = itemRepository;
        this.inventoryRepository = inventoryRepository;
        this.locationMapper = locationMapper;
    }

    public void uploadLocation(Location location) {

        LocationEntity loc = new LocationEntity();
        loc.setName(location.getName());
        loc.setLocationType(location.getType());

        loc.setParentLocationId(location.getParentId());
        loc.setWarehouseId(location.getWarehouseId());

        // Auto-generate code if not provided or empty
        if (location.getCode() == null || location.getCode().trim().isEmpty()) {
            loc.setCode(generateLocationCode(location));
        } else {
            loc.setCode(location.getCode());
        }

        loc.setDescription(location.getDescription());

        // Save new location
        LocationEntity saved = locationRepository.save(loc);

        // Only create inventory for BIN-type locations
        if ("BIN".equalsIgnoreCase(location.getType())) {
            List<ItemEntity> items = itemRepository.findAll();

            for (ItemEntity item : items) {
                InventoryEntity inv = new InventoryEntity();
                inv.setLocation(saved);
                inv.setItem(item);
                inv.setQuantity(0L);
                inventoryRepository.save(inv);
            }
        }
    }

    @Transactional(readOnly = true)
    public List<Location> findAll() {
        List<LocationEntity> entities = locationRepository.findAll();
        return entities.stream()
                .map(locationMapper::toDto)
                .toList();
    }

    public List<Location> getChildren(Long parentId) {
        return locationRepository.findByParentLocationId(parentId)
                .stream()
                .map(locationMapper::toDto)
                .toList();
    }

    public List<Location> getByWarehouse(Long warehouseId) {
        return locationRepository.findByWarehouseId(warehouseId)
                .stream()
                .map(locationMapper::toDto)
                .toList();
    }

    public List<Location> getByType(String type) {
        return locationRepository.findByLocationType(type)
                .stream()
                .map(locationMapper::toDto)
                .toList();
    }

    public List<LocationNode> getLocationTree(Long warehouseId) {
        List<LocationEntity> all = locationRepository.findByWarehouseId(warehouseId);

        return all.stream()
                .filter(l -> l.getParentLocationId() == null)
                .map(l -> buildNode(l, all))
                .toList();
    }

    private LocationNode buildNode(LocationEntity current, List<LocationEntity> all) {
        LocationNode node = new LocationNode(current);

        List<LocationNode> children = all.stream()
                .filter(l -> current.getId().equals(l.getParentLocationId()))
                .map(l -> buildNode(l, all))
                .toList();

        node.setChildren(children);
        return node;
    }

    /**
     * Generate location code based on parent hierarchy and location type
     */
    private String generateLocationCode(Location location) {
        StringBuilder code = new StringBuilder();

        // Get parent code if exists
        if (location.getParentId() != null) {
            LocationEntity parent = locationRepository.findById(location.getParentId())
                    .orElse(null);
            if (parent != null && parent.getCode() != null) {
                code.append(parent.getCode()).append("-");
            }
        }

        // Generate abbreviation based on location type and name
        String abbreviation = generateAbbreviation(location.getType(), location.getName());
        code.append(abbreviation);

        return code.toString();
    }

    /**
     * Generate abbreviation based on location type and name
     */
    private String generateAbbreviation(String locationType, String name) {
        if (locationType == null || name == null) {
            return name != null ? name.toUpperCase() : "LOC";
        }

        String type = locationType.toUpperCase();
        String cleanName = name.trim();

        switch (type) {
            case "WAREHOUSE":
                // Use first 3 letters of warehouse name
                return cleanName.length() >= 3
                        ? cleanName.substring(0, 3).toUpperCase()
                        : cleanName.toUpperCase();

            case "ZONE":
                // Use "ZONE-" prefix + name
                return "ZONE-" + cleanName.toUpperCase();

            case "FLOOR":
                // Extract number from name (e.g., "Floor 1" -> "F1")
                String floorNum = cleanName.replaceAll("[^0-9]", "");
                return "F" + (floorNum.isEmpty() ? "1" : floorNum);

            case "RACK":
                // Extract identifier (e.g., "Rack R1" -> "R1")
                String rackId = cleanName.replaceAll("(?i)rack\\s*", "").trim();
                return rackId.isEmpty() ? "R1" : rackId.toUpperCase();

            case "BIN":
                // Extract identifier (e.g., "Bin B01" -> "B01")
                String binId = cleanName.replaceAll("(?i)bin\\s*", "").trim();
                return binId.isEmpty() ? "B01" : binId.toUpperCase();

            default:
                return cleanName.toUpperCase();
        }
    }

    /**
     * Validate that location is a BIN type for inventory operations
     */
    public void validateLocationForInventory(Long locationId) {
        LocationEntity location = locationRepository.findById(locationId)
                .orElseThrow(() -> new IllegalArgumentException("Location not found with id: " + locationId));

        if (!"BIN".equalsIgnoreCase(location.getLocationType())) {
            throw new IllegalArgumentException("Products can only be stored in BIN locations");
        }
    }

    /**
     * Find an available BIN location within the given parent location hierarchy.
     * If the location is already a BIN, returns it.
     * Otherwise, searches for BINs under the parent hierarchy.
     * 
     * @param locationId - Can be warehouse, zone, floor, rack, or bin
     * @return BIN location ID
     * @throws IllegalArgumentException if location not found or no BINs available
     */
    public Long findAvailableBinInLocation(Long locationId) {
        LocationEntity location = locationRepository.findById(locationId)
                .orElseThrow(() -> new IllegalArgumentException("Location not found with id: " + locationId));

        // If already a BIN, return it
        if ("BIN".equalsIgnoreCase(location.getLocationType())) {
            return locationId;
        }

        // Find all BINs under this parent
        List<LocationEntity> bins = findAllBinsUnderParent(locationId);

        if (bins.isEmpty()) {
            throw new IllegalArgumentException(
                    "No BIN locations found under " + location.getName() +
                            " (" + location.getLocationType() + ")");
        }

        // Return first available BIN (sorted by ID)
        return bins.get(0).getId();
    }

    /**
     * Recursively find all BIN locations under a parent location
     */
    private List<LocationEntity> findAllBinsUnderParent(Long parentId) {
        List<LocationEntity> allBins = new ArrayList<>();
        List<LocationEntity> children = locationRepository.findByParentLocationId(parentId);

        for (LocationEntity child : children) {
            if ("BIN".equalsIgnoreCase(child.getLocationType())) {
                allBins.add(child);
            } else {
                // Recursively search children
                allBins.addAll(findAllBinsUnderParent(child.getId()));
            }
        }

        return allBins;
    }
}
