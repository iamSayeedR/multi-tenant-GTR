package com.example.multi_tanent.warehouse.mapper;

import com.example.multi_tanent.warehouse.entity.ShipmentScheduleEntity;
import com.example.multi_tanent.warehouse.model.ShipmentScheduleResponse;
import java.util.stream.Collectors;
import org.springframework.stereotype.Component;

@Component
public class ShipmentScheduleMapper {

    public ShipmentScheduleResponse toResponse(ShipmentScheduleEntity entity) {
        if (entity == null) {
            return null;
        }

        return ShipmentScheduleResponse.builder()
                .id(entity.getId())
                .scheduleNo(entity.getScheduleNo())
                .expectedArrivalDate(entity.getExpectedArrivalDate())
                .driverName(entity.getDriverName())
                .truckNumber(entity.getTruckNumber())
                .supplierName(entity.getSupplierName())
                .warehouseId(entity.getWarehouse() != null ? entity.getWarehouse().getId() : null)
                .warehouseName(entity.getWarehouse() != null ? entity.getWarehouse().getName() : null)
                .status(entity.getStatus())
                .remarks(entity.getRemarks())
                .createdAt(entity.getCreatedAt())
                .gateInId(entity.getGateIn() != null ? entity.getGateIn().getId() : null)
                .gateInNo(entity.getGateIn() != null ? entity.getGateIn().getGateInNo() : null)
                .lines(entity.getLines() != null ? entity.getLines().stream()
                        .map(line -> ShipmentScheduleResponse.ShipmentScheduleLineResponse.builder()
                                .id(line.getId())
                                .itemId(line.getItem() != null ? line.getItem().getId() : null)
                                .itemName(line.getItem() != null ? line.getItem().getName() : null)
                                .itemCode(line.getItem() != null ? line.getItem().getCode() : null)
                                .expectedQuantity(line.getExpectedQuantity())
                                .build())
                        .collect(Collectors.toList()) : null)
                .build();
    }
}
