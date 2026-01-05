package com.example.multi_tanent.warehouse.service;

import com.example.multi_tanent.warehouse.entity.*;
import com.example.multi_tanent.warehouse.entity.LocationEntity;
import com.example.multi_tanent.warehouse.model.*;
import com.example.multi_tanent.warehouse.repository.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TransferService {

        private final LocationRepository locationRepository;
        private final ItemRepository itemRepository;
        private final InterLocationTransferRepository transferRepository;
        private final InterLocationTransferLineRepository lineRepository;
        private final IssueRecordRepository issueRecordRepository;
        private final InventoryRepository inventoryRepository;
        private final InventoryService inventoryService;
        private final LocationService locationService;

        public TransferService(LocationRepository locationRepository,
                        ItemRepository itemRepository,
                        InterLocationTransferRepository transferRepository,
                        InterLocationTransferLineRepository lineRepository,
                        IssueRecordRepository issueRecordRepository,
                        InventoryRepository inventoryRepository,
                        InventoryService inventoryService,
                        LocationService locationService) {

                this.locationRepository = locationRepository;
                this.itemRepository = itemRepository;
                this.transferRepository = transferRepository;
                this.lineRepository = lineRepository;
                this.issueRecordRepository = issueRecordRepository;
                this.inventoryRepository = inventoryRepository;
                this.inventoryService = inventoryService;
                this.locationService = locationService;
        }

        // -----------------------------------------------------------------------
        // 1. CREATE TRANSFER
        // -----------------------------------------------------------------------

        @Transactional
        public TransferResponse createTransfer(TransferRequest req) {

                // Auto-select BINs from parent locations (warehouse/zone/floor/rack/bin)
                Long fromBinId = locationService.findAvailableBinInLocation(req.getFromLocationId());
                Long toBinId = locationService.findAvailableBinInLocation(req.getToLocationId());

                LocationEntity from = locationRepository.findById(fromBinId)
                                .orElseThrow(() -> new IllegalArgumentException("From BIN location not found"));

                LocationEntity to = locationRepository.findById(toBinId)
                                .orElseThrow(() -> new IllegalArgumentException("To BIN location not found"));

                InterLocationTransfer t = new InterLocationTransfer();
                t.setIssueNo("WHT" + System.currentTimeMillis());
                t.setIssueDate(LocalDate.now());
                t.setFromLocation(from);
                t.setToLocation(to);
                t.setRemarks(req.getRemarks());

                // NEW RFID fields
                t.setFromRFIDTag(req.getFromRFIDTag());
                t.setToRFIDTag(req.getToRFIDTag());

                for (TransferLineRequest l : req.getLines()) {

                        ItemEntity item = itemRepository.findById(l.getItemId())
                                        .orElseThrow(() -> new IllegalArgumentException("Invalid item"));

                        // Make sure inventory rows exist
                        inventoryService.ensureInventoryExists(from.getId(), item.getId());
                        inventoryService.ensureInventoryExists(to.getId(), item.getId());

                        InterLocationTransferLine line = new InterLocationTransferLine();
                        line.setTransfer(t);
                        line.setItem(item);
                        line.setRequisitionQty(l.getRequisitionQty());
                        line.setRatePerUnit(
                                        l.getRatePerUnit() != null ? l.getRatePerUnit() : item.getDefaultRate());
                        line.setVatPercent(
                                        l.getVatPercent() != null ? l.getVatPercent() : item.getDefaultVatPercent());

                        t.getLines().add(line);
                }

                transferRepository.save(t);
                return toResponse(t);
        }

        @Transactional(readOnly = true)
        public List<TransferResponse> listTransfers(List<TransferStatus> statuses) {

                List<InterLocationTransfer> list;

                if (statuses == null || statuses.isEmpty()) {
                        list = transferRepository.findAll();
                } else {
                        list = transferRepository.findByStatusIn(statuses);
                }

                return list.stream()
                                .map(this::toResponse)
                                .toList();
        }

        @Transactional(readOnly = true)
        public TransferResponse getTransferById(Long id) {

                InterLocationTransfer t = transferRepository.findById(id)
                                .orElseThrow(() -> new IllegalArgumentException("Transfer not found"));

                return toResponse(t);
        }

        @Transactional(readOnly = true)
        public List<IssueRecordResponse> history(Long transferId, Long lineId) {

                InterLocationTransferLine line = lineRepository.findById(lineId)
                                .orElseThrow(() -> new IllegalArgumentException("Line not found"));

                if (!line.getTransfer().getId().equals(transferId)) {
                        throw new IllegalArgumentException("Line does not belong to transfer");
                }

                return issueRecordRepository.findByLineIdOrderByDateAsc(lineId)
                                .stream()
                                .map(r -> IssueRecordResponse.builder()
                                                .id(r.getId())
                                                .date(r.getDate())
                                                .qty(r.getQty())
                                                .ratePerUnit(r.getRatePerUnit())
                                                .vatPercent(r.getVatPercent())
                                                .amount(r.getAmount())
                                                .toLocation(line.getTransfer().getToLocation().getName())
                                                .build())
                                .toList();
        }

        // -----------------------------------------------------------------------
        // 2. ISSUE STOCK
        // -----------------------------------------------------------------------
        @Transactional
        public IssueRecordResponse issue(Long transferId, Long lineId, IssueRequest req) {

                InterLocationTransfer t = transferRepository.findById(transferId)
                                .orElseThrow(() -> new IllegalArgumentException("Transfer not found"));

                InterLocationTransferLine line = lineRepository.findById(lineId)
                                .orElseThrow(() -> new IllegalArgumentException("Line not found"));

                if (!line.getTransfer().getId().equals(transferId)) {
                        throw new IllegalArgumentException("Line does not belong to transfer");
                }

                long reqQty = line.getRequisitionQty() == null ? 0 : line.getRequisitionQty();
                long issuedQty = line.getIssuedQty() == null ? 0 : line.getIssuedQty();
                long remaining = reqQty - issuedQty;

                if (req.getQuantity() > remaining) {
                        throw new IllegalArgumentException("Quantity exceeds remaining requisition");
                }

                inventoryService.ensureInventoryExists(
                                t.getFromLocation().getId(),
                                line.getItem().getId());

                long available = inventoryService.availableStock(
                                t.getFromLocation().getId(),
                                line.getItem().getId());

                if (available < req.getQuantity()) {
                        throw new IllegalArgumentException("Insufficient stock");
                }

                // Reduce stock at source
                inventoryService.reduceStock(
                                t.getFromLocation().getId(),
                                line.getItem().getId(),
                                req.getQuantity());

                // Increase stock at destination
                inventoryService.addStock(
                                t.getToLocation().getId(),
                                line.getItem().getId(),
                                req.getQuantity());

                // Update line
                line.setIssuedQty(issuedQty + req.getQuantity());
                lineRepository.save(line);

                // Save issue record
                IssueRecord rec = new IssueRecord();
                rec.setLine(line);
                rec.setDate(LocalDate.now());
                rec.setQty(req.getQuantity());

                BigDecimal rate = req.getRatePerUnit() != null ? req.getRatePerUnit() : line.getRatePerUnit();
                BigDecimal vat = req.getVatPercent() != null ? req.getVatPercent() : line.getVatPercent();

                rec.setRatePerUnit(rate);
                rec.setVatPercent(vat);
                rec.setAmount(rate.multiply(BigDecimal.valueOf(req.getQuantity())));

                issueRecordRepository.save(rec);

                // Update transfer RFID if provided
                if (req.getFromRFIDTag() != null)
                        t.setFromRFIDTag(req.getFromRFIDTag());
                if (req.getToRFIDTag() != null)
                        t.setToRFIDTag(req.getToRFIDTag());

                // Update transfer status
                boolean allZero = t.getLines().stream()
                                .allMatch(l -> (l.getIssuedQty() == null ? 0 : l.getIssuedQty()) == 0);

                boolean allFull = t.getLines().stream()
                                .allMatch(l -> {
                                        long i = l.getIssuedQty() == null ? 0 : l.getIssuedQty();
                                        long r = l.getRequisitionQty() == null ? 0 : l.getRequisitionQty();
                                        return i == r;
                                });

                if (allFull)
                        t.setStatus(TransferStatus.ISSUED);
                else if (allZero)
                        t.setStatus(TransferStatus.PENDING);
                else
                        t.setStatus(TransferStatus.PARTIALLY_ISSUED);

                transferRepository.save(t);

                return IssueRecordResponse.builder()
                                .id(rec.getId())
                                .date(rec.getDate())
                                .qty(rec.getQty())
                                .amount(rec.getAmount())
                                .ratePerUnit(rec.getRatePerUnit())
                                .vatPercent(rec.getVatPercent())
                                .toLocation(t.getToLocation().getName())
                                .build();
        }

        // -----------------------------------------------------------------------
        // Convert entity to response
        // -----------------------------------------------------------------------
        private TransferResponse toResponse(InterLocationTransfer t) {
                return TransferResponse.builder()
                                .id(t.getId())
                                .issueNo(t.getIssueNo())
                                .issueDate(t.getIssueDate())

                                .fromLocation(t.getFromLocation().getName())
                                .toLocation(t.getToLocation().getName())

                                .fromRFIDTag(t.getFromRFIDTag())
                                .toRFIDTag(t.getToRFIDTag())

                                .status(t.getStatus())

                                .lines(
                                                t.getLines().stream().map(l -> TransferResponse.Line.builder()
                                                                .lineId(l.getId())
                                                                .itemCode(l.getItem().getCode())
                                                                .itemName(l.getItem().getName())
                                                                .requisitionQty(l.getRequisitionQty())
                                                                .issuedQty(l.getIssuedQty())
                                                                .ratePerUnit(l.getRatePerUnit())
                                                                .vatPercent(l.getVatPercent())
                                                                .build()).toList())
                                .build();
        }
}
