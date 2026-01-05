package com.example.multi_tanent.warehouse.service;

import com.example.multi_tanent.warehouse.entity.*;
import com.example.multi_tanent.warehouse.model.*;
import com.example.multi_tanent.warehouse.repository.*;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class StorageAssignmentService {

    private final StorageAssignmentRepository storageAssignmentRepository;
    private final GateInLineRepository gateInLineRepository;
    private final StoreKeeperRepository storeKeeperRepository;
    private final LocationRepository locationRepository;
    private final LocationService locationService;
    private final InventoryService inventoryService;
    private final com.example.multi_tanent.warehouse.mapper.StorageAssignmentMapper mapper;

    public StorageAssignmentService(
            StorageAssignmentRepository storageAssignmentRepository,
            GateInLineRepository gateInLineRepository,
            StoreKeeperRepository storeKeeperRepository,
            LocationRepository locationRepository,
            LocationService locationService,
            InventoryService inventoryService,
            com.example.multi_tanent.warehouse.mapper.StorageAssignmentMapper mapper) {
        this.storageAssignmentRepository = storageAssignmentRepository;
        this.gateInLineRepository = gateInLineRepository;
        this.storeKeeperRepository = storeKeeperRepository;
        this.locationRepository = locationRepository;
        this.locationService = locationService;
        this.inventoryService = inventoryService;
        this.mapper = mapper;
    }

    @Transactional
    public StorageAssignmentResponse createAssignment(StorageAssignmentRequest request) {
        // Validate gate in line
        GateInLineEntity gateInLine = gateInLineRepository.findById(request.getGateInLineId())
                .orElseThrow(() -> new IllegalArgumentException("Gate In line not found"));

        // Auto-select BIN from target location (can be warehouse/zone/floor/rack)
        Long binLocationId = locationService.findAvailableBinInLocation(request.getTargetLocationId());
        LocationEntity targetLocation = locationRepository.findById(binLocationId)
                .orElseThrow(() -> new IllegalArgumentException("BIN location not found"));

        // Find the floor for this BIN
        LocationEntity floor = findFloorForLocation(targetLocation);

        // Auto-assign storekeeper if not specified
        StoreKeeperEntity storeKeeper;
        if (request.getAssignedToId() != null) {
            // Manual assignment
            storeKeeper = storeKeeperRepository.findById(request.getAssignedToId())
                    .orElseThrow(() -> new IllegalArgumentException("StoreKeeper not found"));
        } else {
            // Smart auto-assignment: find available storekeeper on this floor
            storeKeeper = findAvailableStorekeeper(floor);
            if (storeKeeper == null) {
                throw new IllegalArgumentException(
                        "No available storekeepers found for floor: " +
                                (floor != null ? floor.getName() : "Unknown"));
            }
        }

        // Create assignment
        StorageAssignmentEntity assignment = new StorageAssignmentEntity();
        assignment.setAssignmentNo(generateAssignmentNo());
        assignment.setGateInLine(gateInLine);
        assignment.setAssignedTo(storeKeeper);
        assignment.setTargetLocation(targetLocation);
        assignment.setQuantityToStore(request.getQuantityToStore());
        assignment.setStatus("ASSIGNED");

        // Update storekeeper workload
        storeKeeper.setCurrentWorkload(storeKeeper.getCurrentWorkload() + 1);
        if (storeKeeper.getCurrentWorkload() >= 5) { // Max 5 assignments
            storeKeeper.setIsAvailable(false);
        }
        storeKeeperRepository.save(storeKeeper);

        assignment = storageAssignmentRepository.save(assignment);
        return mapper.toResponse(assignment);
    }

    @Transactional
    public StorageAssignmentResponse completeAssignment(Long assignmentId, CompleteStorageRequest request) {
        StorageAssignmentEntity assignment = storageAssignmentRepository.findById(assignmentId)
                .orElseThrow(() -> new IllegalArgumentException("Assignment not found"));

        if (!"ASSIGNED".equals(assignment.getStatus())) {
            throw new IllegalArgumentException("Assignment is not in ASSIGNED status");
        }

        // Update inventory - add stock to target location
        inventoryService.addStock(
                assignment.getTargetLocation().getId(),
                assignment.getGateInLine().getItem().getId(),
                assignment.getQuantityToStore());

        // Update assignment status
        assignment.setStatus("COMPLETED");
        assignment.setCompletedAt(LocalDateTime.now());
        assignment.setCompletionRemarks(request.getCompletionRemarks());

        // Decrease storekeeper workload
        StoreKeeperEntity storeKeeper = assignment.getAssignedTo();
        storeKeeper.setCurrentWorkload(Math.max(0, storeKeeper.getCurrentWorkload() - 1));
        if (storeKeeper.getCurrentWorkload() < 5) {
            storeKeeper.setIsAvailable(true);
        }
        storeKeeperRepository.save(storeKeeper);

        assignment = storageAssignmentRepository.save(assignment);
        return mapper.toResponse(assignment);
    }

    @Transactional(readOnly = true)
    public List<StorageAssignmentResponse> listAssignments(String status, Long storeKeeperId) {
        List<StorageAssignmentEntity> assignments;

        if (storeKeeperId != null) {
            assignments = storageAssignmentRepository.findByAssignedToId(storeKeeperId);
        } else if (status != null && !status.isEmpty()) {
            assignments = storageAssignmentRepository.findByStatus(status);
        } else {
            assignments = storageAssignmentRepository.findAll();
        }

        return assignments.stream()
                .map(mapper::toResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public StorageAssignmentResponse getAssignmentById(Long id) {
        StorageAssignmentEntity assignment = storageAssignmentRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Assignment not found"));
        return mapper.toResponse(assignment);
    }

    @Transactional(readOnly = true)
    public List<StoreKeeperEntity> getAvailableStorekeepers(Long floorId) {
        // For now, return all storekeepers
        // In future, filter by floor/availability
        return storeKeeperRepository.findAll();
    }

    private String generateAssignmentNo() {
        long count = storageAssignmentRepository.count();
        return "ASG-" + String.format("%06d", count + 1);
    }

    /**
     * Find the floor location for a given BIN location by traversing up the
     * hierarchy
     */
    private LocationEntity findFloorForLocation(LocationEntity location) {
        LocationEntity current = location;

        while (current != null) {
            if ("FLOOR".equalsIgnoreCase(current.getLocationType())) {
                return current;
            }
            if (current.getParentLocationId() != null) {
                current = locationRepository.findById(current.getParentLocationId()).orElse(null);
            } else {
                break;
            }
        }

        return null; // No floor found in hierarchy
    }

    /**
     * Find an available storekeeper assigned to the given floor
     * Priority: lowest workload first
     */
    private StoreKeeperEntity findAvailableStorekeeper(LocationEntity floor) {
        if (floor == null) {
            // No floor specified, find any available storekeeper
            return storeKeeperRepository.findAll().stream()
                    .filter(sk -> "STOREKEEPER".equals(sk.getRole()))
                    .filter(StoreKeeperEntity::getIsAvailable)
                    .min((sk1, sk2) -> Integer.compare(sk1.getCurrentWorkload(), sk2.getCurrentWorkload()))
                    .orElse(null);
        }

        // Find storekeepers assigned to this floor
        List<StoreKeeperEntity> floorStorekeepers = storeKeeperRepository.findAll().stream()
                .filter(sk -> "STOREKEEPER".equals(sk.getRole()))
                .filter(sk -> sk.getAssignedFloor() != null && sk.getAssignedFloor().getId().equals(floor.getId()))
                .filter(StoreKeeperEntity::getIsAvailable)
                .sorted((sk1, sk2) -> Integer.compare(sk1.getCurrentWorkload(), sk2.getCurrentWorkload()))
                .toList();

        if (!floorStorekeepers.isEmpty()) {
            return floorStorekeepers.get(0); // Return storekeeper with lowest workload
        }

        // No available storekeeper on this floor, try any available storekeeper
        return storeKeeperRepository.findAll().stream()
                .filter(sk -> StoreKeeperRole.STOREKEEPER.equals(sk.getRole()))
                .filter(StoreKeeperEntity::getIsAvailable)
                .min((sk1, sk2) -> Integer.compare(sk1.getCurrentWorkload(), sk2.getCurrentWorkload()))
                .orElse(null);
    }
}
