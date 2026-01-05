package com.example.multi_tanent.warehouse.service;

import com.example.multi_tanent.warehouse.entity.*;
import com.example.multi_tanent.warehouse.model.*;
import com.example.multi_tanent.warehouse.repository.*;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ShipmentScheduleService {

    private final ShipmentScheduleRepository shipmentScheduleRepository;
    private final WarehouseRepository warehouseRepository;
    private final ItemRepository itemRepository;
    private final com.example.multi_tanent.warehouse.mapper.ShipmentScheduleMapper mapper;

    public ShipmentScheduleService(
            ShipmentScheduleRepository shipmentScheduleRepository,
            WarehouseRepository warehouseRepository,
            ItemRepository itemRepository,
            com.example.multi_tanent.warehouse.mapper.ShipmentScheduleMapper mapper) {
        this.shipmentScheduleRepository = shipmentScheduleRepository;
        this.warehouseRepository = warehouseRepository;
        this.itemRepository = itemRepository;
        this.mapper = mapper;
    }

    @Transactional
    public ShipmentScheduleResponse createSchedule(ShipmentScheduleRequest request) {
        // Validate warehouse
        WarehouseEntity warehouse = warehouseRepository.findById(request.getWarehouseId())
                .orElseThrow(() -> new IllegalArgumentException("Warehouse not found"));

        // Create shipment schedule
        ShipmentScheduleEntity schedule = new ShipmentScheduleEntity();
        schedule.setScheduleNo(generateScheduleNo());
        schedule.setExpectedArrivalDate(request.getExpectedArrivalDate());
        schedule.setDriverName(request.getDriverName());
        schedule.setTruckNumber(request.getTruckNumber());
        schedule.setSupplierName(request.getSupplierName());
        schedule.setWarehouse(warehouse);
        schedule.setRemarks(request.getRemarks());
        schedule.setStatus("SCHEDULED");

        // Add lines
        if (request.getLines() != null) {
            for (ShipmentScheduleRequest.ShipmentScheduleLineRequest lineReq : request.getLines()) {
                ItemEntity item = itemRepository.findById(lineReq.getItemId())
                        .orElseThrow(() -> new IllegalArgumentException("Item not found: " + lineReq.getItemId()));

                ShipmentScheduleLineEntity line = new ShipmentScheduleLineEntity();
                line.setShipmentSchedule(schedule);
                line.setItem(item);
                line.setExpectedQuantity(lineReq.getExpectedQuantity());

                schedule.getLines().add(line);
            }
        }

        schedule = shipmentScheduleRepository.save(schedule);
        return mapper.toResponse(schedule);
    }

    @Transactional(readOnly = true)
    public List<ShipmentScheduleResponse> listSchedules(String status) {
        List<ShipmentScheduleEntity> schedules;

        if (status != null && !status.isEmpty()) {
            schedules = shipmentScheduleRepository.findByStatus(status);
        } else {
            schedules = shipmentScheduleRepository.findAll();
        }

        return schedules.stream()
                .map(mapper::toResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public ShipmentScheduleResponse getScheduleById(Long id) {
        ShipmentScheduleEntity schedule = shipmentScheduleRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Schedule not found"));
        return mapper.toResponse(schedule);
    }

    @Transactional
    public ShipmentScheduleResponse linkGateIn(Long scheduleId, Long gateInId) {
        ShipmentScheduleEntity schedule = shipmentScheduleRepository.findById(scheduleId)
                .orElseThrow(() -> new IllegalArgumentException("Schedule not found"));

        // This will be set when gate in is created
        // For now, just update status
        schedule.setStatus("ARRIVED");
        schedule = shipmentScheduleRepository.save(schedule);

        return mapper.toResponse(schedule);
    }

    private String generateScheduleNo() {
        long count = shipmentScheduleRepository.count();
        return "SCH-" + String.format("%06d", count + 1);
    }
}
