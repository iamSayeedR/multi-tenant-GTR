package com.example.multi_tanent.warehouse.service;

import com.example.multi_tanent.warehouse.entity.*;
import com.example.multi_tanent.warehouse.model.*;
import com.example.multi_tanent.warehouse.repository.*;
import jakarta.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;

@Service
public class GateInService {

    private final GateInRepository gateInRepository;
    private final GateInLineRepository gateInLineRepository;
    private final GateKeeperRepository gateKeeperRepository;
    private final StoreKeeperRepository storeKeeperRepository;
    private final ItemRepository itemRepository;
    private final LocationRepository locationRepository;
    private final RFIDTagRepository rfidTagRepository;
    private final InventoryService inventoryService;
    private final RFIDMovementService rfidMovementService;
    private final WarehouseRepository warehouseRepository;
    private final GateOutRepository gateOutRepository;

    public GateInService(
            GateInRepository gateInRepository,
            GateInLineRepository gateInLineRepository,
            GateKeeperRepository gateKeeperRepository,
            StoreKeeperRepository storeKeeperRepository,
            ItemRepository itemRepository,
            LocationRepository locationRepository,
            RFIDTagRepository rfidTagRepository,
            InventoryService inventoryService,
            RFIDMovementService rfidMovementService,
            WarehouseRepository warehouseRepository,
            GateOutRepository gateOutRepository) {
        this.gateInRepository = gateInRepository;
        this.gateInLineRepository = gateInLineRepository;
        this.gateKeeperRepository = gateKeeperRepository;
        this.storeKeeperRepository = storeKeeperRepository;
        this.itemRepository = itemRepository;
        this.locationRepository = locationRepository;
        this.rfidTagRepository = rfidTagRepository;
        this.inventoryService = inventoryService;
        this.rfidMovementService = rfidMovementService;
        this.warehouseRepository = warehouseRepository;
        this.gateOutRepository = gateOutRepository;
    }

    @Transactional
    public GateInResponse createGateIn(GateInRequest req) {
        // Create gate in entity
        GateInEntity gateIn = new GateInEntity();
        gateIn.setGateInNo(generateGateInNo());
        gateIn.setTimestamp(LocalDateTime.now());
        gateIn.setVehicleNo(req.getVehicleNo());
        gateIn.setRemarks(req.getRemarks());
        gateIn.setStatus(GateInStatus.PENDING);

        // Set gate-keeper
        GateKeeperEntity gateKeeper = gateKeeperRepository.findById(req.getGateKeeperId())
                .orElseThrow(() -> new RuntimeException("Gate keeper not found"));
        gateIn.setGateKeeper(gateKeeper);

        // Set store-keeper if provided
        if (req.getStoreKeeperId() != null) {
            StoreKeeperEntity storeKeeper = storeKeeperRepository.findById(req.getStoreKeeperId())
                    .orElseThrow(() -> new RuntimeException("Store keeper not found"));
            gateIn.setStoreKeeper(storeKeeper);
        }

        // Set source warehouse for inter-warehouse transfers
        if (req.getSourceWarehouseId() != null) {
            WarehouseEntity sourceWarehouse = warehouseRepository.findById(req.getSourceWarehouseId())
                    .orElseThrow(() -> new RuntimeException("Source warehouse not found"));
            gateIn.setSourceWarehouse(sourceWarehouse);
        }

        // Set supplier info for purchases
        gateIn.setSupplierName(req.getSupplierName());
        gateIn.setSupplierAddress(req.getSupplierAddress());

        // Set reference Gate Out if provided
        if (req.getReferenceGateOutId() != null) {
            GateOutEntity refGateOut = gateOutRepository.findById(req.getReferenceGateOutId())
                    .orElseThrow(() -> new RuntimeException("Reference Gate Out not found"));
            gateIn.setReferenceGateOut(refGateOut);
        }

        // Save gate in
        gateIn = gateInRepository.save(gateIn);

        // Create gate in lines if provided
        if (req.getLines() != null && !req.getLines().isEmpty()) {
            for (GateInLineRequest lineReq : req.getLines()) {
                GateInLineEntity line = new GateInLineEntity();
                line.setGateIn(gateIn);

                ItemEntity item = itemRepository.findById(lineReq.getItemId())
                        .orElseThrow(() -> new RuntimeException("Item not found"));
                line.setItem(item);

                line.setRfidTagCode(lineReq.getRfidTagCode());
                line.setQty(lineReq.getQty());

                // CORRECTED: Store at warehouse level only - specific BIN assigned during
                // storage assignment
                if (lineReq.getToLocationId() != null) {
                    LocationEntity toLocation = locationRepository.findById(lineReq.getToLocationId())
                            .orElseThrow(() -> new RuntimeException("Location not found"));

                    // Validate it's a WAREHOUSE-level location
                    if (!"WAREHOUSE".equalsIgnoreCase(toLocation.getLocationType())) {
                        throw new RuntimeException(
                                "Gate In location must be a WAREHOUSE. Specific bin assignment happens during storage assignment by StoreKeeper Manager.");
                    }

                    line.setToLocation(toLocation);
                }

                gateIn.getLines().add(line);
            }
        }

        gateIn = gateInRepository.save(gateIn);
        return mapToResponse(gateIn);
    }

    @Transactional
    public GateInResponse attachRFIDTags(Long gateInId, List<String> tags) {
        GateInEntity gateIn = gateInRepository.findById(gateInId)
                .orElseThrow(() -> new RuntimeException("Gate In not found"));

        for (String tagCode : tags) {
            // Find the RFID tag
            RFIDTagEntity rfidTag = rfidTagRepository.findByTagCode(tagCode)
                    .orElseThrow(() -> new RuntimeException("RFID tag not found: " + tagCode));

            // Check if line already exists for this item
            boolean lineExists = gateIn.getLines().stream()
                    .anyMatch(line -> line.getItem().getId().equals(rfidTag.getItem().getId()));

            if (!lineExists) {
                // Create new line
                GateInLineEntity line = new GateInLineEntity();
                line.setGateIn(gateIn);
                line.setItem(rfidTag.getItem());
                line.setRfidTagCode(tagCode);
                line.setQty(1L);
                gateIn.getLines().add(line);
            } else {
                // Update existing line
                gateIn.getLines().stream()
                        .filter(line -> line.getItem().getId().equals(rfidTag.getItem().getId()))
                        .findFirst()
                        .ifPresent(line -> {
                            line.setQty(line.getQty() + 1);
                            if (line.getRfidTagCode() == null) {
                                line.setRfidTagCode(tagCode);
                            }
                        });
            }
        }

        gateIn = gateInRepository.save(gateIn);
        return mapToResponse(gateIn);
    }

    @Transactional
    public GateInResponse confirmGateIn(Long gateInId) {
        GateInEntity gateIn = gateInRepository.findById(gateInId)
                .orElseThrow(() -> new RuntimeException("Gate In not found"));

        if (gateIn.getStatus() != GateInStatus.PENDING) {
            throw new RuntimeException("Gate In is not in PENDING status");
        }

        // Update status to RECEIVED
        gateIn.setStatus(GateInStatus.RECEIVED);
        gateIn = gateInRepository.save(gateIn);

        return mapToResponse(gateIn);
    }

    @Transactional
    public GateInResponse assignLocation(Long gateInId, AssignLocationRequest req) {
        GateInEntity gateIn = gateInRepository.findById(gateInId)
                .orElseThrow(() -> new RuntimeException("Gate In not found"));

        // Find the line
        GateInLineEntity line = gateIn.getLines().stream()
                .filter(l -> l.getId().equals(req.getLineId()))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Gate In line not found"));

        // Set to location
        LocationEntity toLocation = locationRepository.findById(req.getToLocationId())
                .orElseThrow(() -> new RuntimeException("Location not found"));
        line.setToLocation(toLocation);

        // Add stock to inventory (manual update)
        inventoryService.addStock(req.getToLocationId(), line.getItem().getId(), line.getQty());

        // Log RFID movement WITHOUT auto-update (inventory already updated above)
        if (line.getRfidTagCode() != null) {
            RFIDMovementRequest movementReq = new RFIDMovementRequest();
            movementReq.setTagCode(line.getRfidTagCode());
            movementReq.setLocationId(req.getToLocationId());
            movementReq.setMovementType("GATE_IN");
            rfidMovementService.logMovementOnly(movementReq);
        }

        // Check if all lines have been assigned locations
        boolean allAssigned = gateIn.getLines().stream()
                .allMatch(l -> l.getToLocation() != null);

        if (allAssigned) {
            gateIn.setStatus(GateInStatus.COMPLETED);
        }

        gateIn = gateInRepository.save(gateIn);
        return mapToResponse(gateIn);
    }

    public List<GateInResponse> listGateIns(List<GateInStatus> statuses) {
        List<GateInEntity> gateIns;

        if (statuses != null && !statuses.isEmpty()) {
            gateIns = gateInRepository.findByStatusIn(statuses);
        } else {
            gateIns = gateInRepository.findAll();
        }

        return gateIns.stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    public GateInResponse getGateInById(Long id) {
        GateInEntity gateIn = gateInRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Gate In not found"));
        return mapToResponse(gateIn);
    }

    /**
     * Generate unique Gate In number (thread-safe).
     * Uses synchronized to prevent race conditions.
     */
    private synchronized String generateGateInNo() {
        long count = gateInRepository.count();
        return "GIN-" + String.format("%06d", count + 1);
    }

    private GateInResponse mapToResponse(GateInEntity entity) {
//        GateInResponse response = new GateInResponse();
//        response.setId(entity.getId());
//        response.setGateInNo(entity.getGateInNo());
//        response.setTimestamp(entity.getTimestamp());
//        response.setVehicleNo(entity.getVehicleNo());
//        response.setRemarks(entity.getRemarks());
//        response.setStatus(entity.getStatus());

//        if (entity.getGateKeeper() != null) {
//            response.setGateKeeperId(entity.getGateKeeper().getId());
//            response.setGateKeeperName(entity.getGateKeeper().getName());
//        }
//
//        if (entity.getStoreKeeper() != null) {
//            response.setStoreKeeperId(entity.getStoreKeeper().getId());
//            response.setStoreKeeperName(entity.getStoreKeeper().getName());
//        }
//
//        // Map source warehouse
//        if (entity.getSourceWarehouse() != null) {
//            response.setSourceWarehouseId(entity.getSourceWarehouse().getId());
//            response.setSourceWarehouseName(entity.getSourceWarehouse().getName());
//        }
//
//        // Map supplier info
//        response.setSupplierName(entity.getSupplierName());
//        response.setSupplierAddress(entity.getSupplierAddress());
//
//        // Map reference Gate Out
//        if (entity.getReferenceGateOut() != null) {
//            response.setReferenceGateOutId(entity.getReferenceGateOut().getId());
//            response.setReferenceGateOutNo(entity.getReferenceGateOut().getGateOutNo());
//        }
//
//        List<GateInLineResponse> lineResponses = entity.getLines().stream()
//                .map(this::mapLineToResponse)
//                .collect(Collectors.toList());
//        response.setLines(lineResponses);

        return GateInResponse.builder().build();
    }

    private GateInLineResponse mapLineToResponse(GateInLineEntity entity) {
        GateInLineResponse response = new GateInLineResponse();
        response.setId(entity.getId());
        response.setItemId(entity.getItem().getId());
        response.setItemName(entity.getItem().getName());
        response.setRfidTagCode(entity.getRfidTagCode());
        response.setQty(entity.getQty());

        // Only toLocation is relevant for Gate In
        if (entity.getToLocation() != null) {
            response.setToLocationId(entity.getToLocation().getId());
            response.setToLocationName(entity.getToLocation().getName());
        }

        return response;
    }
}
