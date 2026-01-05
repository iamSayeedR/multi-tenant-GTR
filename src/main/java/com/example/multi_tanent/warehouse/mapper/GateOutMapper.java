package com.example.multi_tanent.warehouse.mapper;

import com.example.multi_tanent.warehouse.entity.GateOutEntity;
import com.example.multi_tanent.warehouse.entity.GateOutLineEntity;
import com.example.multi_tanent.warehouse.model.GateOutLineResponse;
import com.example.multi_tanent.warehouse.model.GateOutResponse;
import java.util.stream.Collectors;
import org.springframework.stereotype.Component;

@Component
public class GateOutMapper {

    public GateOutResponse toResponse(GateOutEntity entity) {
        if (entity == null) {
            return null;
        }

        GateOutResponse response = new GateOutResponse();
        response.setId(entity.getId());
        response.setGateOutNo(entity.getGateOutNo());
        response.setTimestamp(entity.getTimestamp());
        response.setVehicleNo(entity.getVehicleNo());
        response.setRemarks(entity.getRemarks());
        response.setStatus(entity.getStatus());

        if (entity.getGateKeeper() != null) {
            response.setGateKeeperId(entity.getGateKeeper().getId());
            response.setGateKeeperName(entity.getGateKeeper().getName());
        }

        if (entity.getStoreKeeper() != null) {
            response.setStoreKeeperId(entity.getStoreKeeper().getId());
            response.setStoreKeeperName(entity.getStoreKeeper().getName());
        }

        if (entity.getDestinationWarehouse() != null) {
            response.setDestinationWarehouseId(entity.getDestinationWarehouse().getId());
            response.setDestinationWarehouseName(entity.getDestinationWarehouse().getName());
        }

        response.setCustomerName(entity.getCustomerName());
        response.setCustomerAddress(entity.getCustomerAddress());

        if (entity.getReferenceGateIn() != null) {
            response.setReferenceGateInId(entity.getReferenceGateIn().getId());
            response.setReferenceGateInNo(entity.getReferenceGateIn().getGateInNo());
        }

        if (entity.getLines() != null) {
            response.setLines(entity.getLines().stream()
                    .map(this::mapLineToResponse)
                    .collect(Collectors.toList()));
        }

        return response;
    }

    public GateOutLineResponse mapLineToResponse(GateOutLineEntity entity) {
        if (entity == null) {
            return null;
        }

        GateOutLineResponse response = new GateOutLineResponse();
        response.setId(entity.getId());
        response.setItemId(entity.getItem().getId());
        response.setItemName(entity.getItem().getName());
        response.setRfidTagCode(entity.getRfidTagCode());
        response.setQty(entity.getQty());
        response.setReason(entity.getReason());

        if (entity.getFromLocation() != null) {
            response.setFromLocationId(entity.getFromLocation().getId());
            response.setFromLocationName(entity.getFromLocation().getName());
        }

        return response;
    }
}
