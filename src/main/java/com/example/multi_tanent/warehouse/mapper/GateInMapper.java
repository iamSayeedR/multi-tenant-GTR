package com.example.multi_tanent.warehouse.mapper;

import com.example.multi_tanent.warehouse.entity.GateInEntity;
import com.example.multi_tanent.warehouse.entity.GateInLineEntity;
import com.example.multi_tanent.warehouse.model.GateInLineResponse;
import com.example.multi_tanent.warehouse.model.GateInResponse;
import java.util.stream.Collectors;
import org.springframework.stereotype.Component;

@Component
public class GateInMapper {

    public GateInResponse toResponse(GateInEntity entity) {
        if (entity == null) {
            return null;
        }

        GateInResponse response = new GateInResponse();
        response.setId(entity.getId());
        response.setGateInNo(entity.getGateInNo());
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

        if (entity.getSourceWarehouse() != null) {
            response.setSourceWarehouseId(entity.getSourceWarehouse().getId());
            response.setSourceWarehouseName(entity.getSourceWarehouse().getName());
        }

        response.setSupplierName(entity.getSupplierName());
        response.setSupplierAddress(entity.getSupplierAddress());

        if (entity.getReferenceGateOut() != null) {
            response.setReferenceGateOutId(entity.getReferenceGateOut().getId());
            response.setReferenceGateOutNo(entity.getReferenceGateOut().getGateOutNo());
        }

        if (entity.getLines() != null) {
            response.setLines(entity.getLines().stream()
                    .map(this::mapLineToResponse)
                    .collect(Collectors.toList()));
        }

        return response;
    }

    public GateInLineResponse mapLineToResponse(GateInLineEntity entity) {
        if (entity == null) {
            return null;
        }

        GateInLineResponse response = new GateInLineResponse();
        response.setId(entity.getId());
        response.setItemId(entity.getItem().getId());
        response.setItemName(entity.getItem().getName());
        response.setRfidTagCode(entity.getRfidTagCode());
        response.setQty(entity.getQty());

        if (entity.getToLocation() != null) {
            response.setToLocationId(entity.getToLocation().getId());
            response.setToLocationName(entity.getToLocation().getName());
        }

        return response;
    }
}
