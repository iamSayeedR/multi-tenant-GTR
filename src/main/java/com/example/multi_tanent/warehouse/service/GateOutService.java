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
public class GateOutService {

    private final GateOutRepository gateOutRepository;
    private final GateKeeperRepository gateKeeperRepository;
    private final StoreKeeperRepository storeKeeperRepository;
    private final ItemRepository itemRepository;
    private final LocationRepository locationRepository;
    private final RFIDTagRepository rfidTagRepository;
    private final InventoryService inventoryService;
    private final RFIDMovementService rfidMovementService;
    private final WarehouseRepository warehouseRepository;
    private final GateInRepository gateInRepository;
    private final LocationService locationService;
    private final com.example.multi_tanent.warehouse.mapper.GateOutMapper mapper;

    public GateOutService(
            GateOutRepository gateOutRepository,
            GateKeeperRepository gateKeeperRepository,
            StoreKeeperRepository storeKeeperRepository,
            ItemRepository itemRepository,
            LocationRepository locationRepository,
            RFIDTagRepository rfidTagRepository,
            InventoryService inventoryService,
            RFIDMovementService rfidMovementService,
            WarehouseRepository warehouseRepository,
            GateInRepository gateInRepository,
            LocationService locationService,
            com.example.multi_tanent.warehouse.mapper.GateOutMapper mapper) {
        this.gateOutRepository = gateOutRepository;
        this.gateKeeperRepository = gateKeeperRepository;
        this.storeKeeperRepository = storeKeeperRepository;
        this.itemRepository = itemRepository;
        this.locationRepository = locationRepository;
        this.rfidTagRepository = rfidTagRepository;
        this.inventoryService = inventoryService;
        this.rfidMovementService = rfidMovementService;
        this.warehouseRepository = warehouseRepository;
        this.gateInRepository = gateInRepository;
        this.locationService = locationService;
        this.mapper = mapper;
    }

    @Transactional
    public GateOutResponse createGateOut(GateOutRequest req) {
        // Create gate out entity
        GateOutEntity gateOut = new GateOutEntity();
        gateOut.setGateOutNo(generateGateOutNo());
        gateOut.setTimestamp(LocalDateTime.now());
        gateOut.setVehicleNo(req.getVehicleNo());
        gateOut.setRemarks(req.getRemarks());
        gateOut.setStatus(GateOutStatus.PENDING);

        // Set gate keeper
        GateKeeperEntity gateKeeper = gateKeeperRepository.findById(req.getGateKeeperId())
                .orElseThrow(() -> new RuntimeException("Gate keeper not found"));
        gateOut.setGateKeeper(gateKeeper);

        // Set store keeper if provided
        if (req.getStoreKeeperId() != null) {
            StoreKeeperEntity storeKeeper = storeKeeperRepository.findById(req.getStoreKeeperId())
                    .orElseThrow(() -> new RuntimeException("Store keeper not found"));
            gateOut.setStoreKeeper(storeKeeper);
        }

        // Set destination warehouse for inter-warehouse transfers
        if (req.getDestinationWarehouseId() != null) {
            WarehouseEntity destWarehouse = warehouseRepository.findById(req.getDestinationWarehouseId())
                    .orElseThrow(() -> new RuntimeException("Destination warehouse not found"));
            gateOut.setDestinationWarehouse(destWarehouse);
        }

        // Set customer info for sales deliveries
        gateOut.setCustomerName(req.getCustomerName());
        gateOut.setCustomerAddress(req.getCustomerAddress());

        // Set reference Gate In if provided
        if (req.getReferenceGateInId() != null) {
            GateInEntity refGateIn = gateInRepository.findById(req.getReferenceGateInId())
                    .orElseThrow(() -> new RuntimeException("Reference Gate In not found"));
            gateOut.setReferenceGateIn(refGateIn);
        }

        // Save gate out
        gateOut = gateOutRepository.save(gateOut);

        // Create gate out lines if provided
        if (req.getLines() != null && !req.getLines().isEmpty()) {
            for (GateOutLineRequest lineReq : req.getLines()) {
                GateOutLineEntity line = new GateOutLineEntity();
                line.setGateOut(gateOut);

                ItemEntity item = itemRepository.findById(lineReq.getItemId())
                        .orElseThrow(() -> new RuntimeException("Item not found"));
                line.setItem(item);

                line.setRfidTagCode(lineReq.getRfidTagCode());
                line.setQty(lineReq.getQty());
                line.setReason(lineReq.getReason());

                // Auto-select BIN from parent location (warehouse/zone/floor/rack/bin)
                Long binLocationId = locationService.findAvailableBinInLocation(lineReq.getFromLocationId());

                LocationEntity fromLocation = locationRepository.findById(binLocationId)
                        .orElseThrow(() -> new RuntimeException("BIN location not found"));
                line.setFromLocation(fromLocation);

                gateOut.getLines().add(line);
            }
        }

        gateOut = gateOutRepository.save(gateOut);
        return mapper.toResponse(gateOut);
    }

    @Transactional
    public GateOutResponse attachRFIDTags(Long gateOutId, List<String> tags) {
        GateOutEntity gateOut = gateOutRepository.findById(gateOutId)
                .orElseThrow(() -> new RuntimeException("Gate Out not found"));

        for (String tagCode : tags) {
            // Find the RFID tag
            RFIDTagEntity rfidTag = rfidTagRepository.findByTagCode(tagCode)
                    .orElseThrow(() -> new RuntimeException("RFID tag not found: " + tagCode));

            // Check if line already exists for this item
            boolean lineExists = gateOut.getLines().stream()
                    .anyMatch(line -> line.getItem().getId().equals(rfidTag.getItem().getId()));

            if (!lineExists) {
                // Create new line - need to determine from location
                // For now, we'll throw an error as we need location info
                throw new RuntimeException(
                        "Cannot auto-create line without location info. Please create lines manually.");
            } else {
                // Update existing line
                gateOut.getLines().stream()
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

        gateOut = gateOutRepository.save(gateOut);
        return mapper.toResponse(gateOut);
    }

    @Transactional
    public GateOutResponse confirmGateOut(Long gateOutId) {
        GateOutEntity gateOut = gateOutRepository.findById(gateOutId)
                .orElseThrow(() -> new RuntimeException("Gate Out not found"));

        if (gateOut.getStatus() == GateOutStatus.DISPATCHED) {
            throw new RuntimeException("Gate Out is already dispatched");
        }

        // Reduce inventory for each line (manual update)
        for (GateOutLineEntity line : gateOut.getLines()) {
            inventoryService.reduceStock(
                    line.getFromLocation().getId(),
                    line.getItem().getId(),
                    line.getQty());

            // Log RFID movement WITHOUT auto-update (inventory already reduced above)
            if (line.getRfidTagCode() != null) {
                RFIDMovementRequest movementReq = new RFIDMovementRequest();
                movementReq.setTagCode(line.getRfidTagCode());
                movementReq.setLocationId(line.getFromLocation().getId());
                movementReq.setMovementType("GATE_OUT");
                rfidMovementService.logMovementOnly(movementReq); // ← Changed to logMovementOnly
            }
        }

        // Update status to DISPATCHED
        gateOut.setStatus(GateOutStatus.DISPATCHED);
        gateOut = gateOutRepository.save(gateOut);

        return mapper.toResponse(gateOut);
    }

    public List<GateOutResponse> listGateOuts(List<GateOutStatus> statuses) {
        List<GateOutEntity> gateOuts;

        if (statuses != null && !statuses.isEmpty()) {
            gateOuts = gateOutRepository.findByStatusIn(statuses);
        } else {
            gateOuts = gateOutRepository.findAll();
        }

        return gateOuts.stream()
                .map(mapper::toResponse)
                .collect(Collectors.toList());
    }

    public GateOutResponse getGateOutById(Long id) {
        GateOutEntity gateOut = gateOutRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Gate Out not found"));
        return mapper.toResponse(gateOut);
    }

    /**
     * Generate unique Gate Out number (thread-safe).
     * Uses synchronized to prevent race conditions.
     */
    private synchronized String generateGateOutNo() {
        long count = gateOutRepository.count();
        return "GOUT-" + String.format("%06d", count + 1);
    }
}
