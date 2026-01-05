package com.example.multi_tanent.warehouse.service;

import com.example.multi_tanent.warehouse.entity.GateKeeperEntity;
import com.example.multi_tanent.warehouse.entity.WarehouseEntity;
import com.example.multi_tanent.warehouse.model.GateKeeperRequest;
import com.example.multi_tanent.warehouse.model.GateKeeperResponse;
import com.example.multi_tanent.warehouse.repository.GateKeeperRepository;
import com.example.multi_tanent.warehouse.repository.WarehouseRepository;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class GateKeeperService {

    private final GateKeeperRepository repo;
    private final WarehouseRepository warehouseRepo;

    public GateKeeperService(GateKeeperRepository repo, WarehouseRepository warehouseRepo) {
        this.repo = repo;
        this.warehouseRepo = warehouseRepo;
    }

    public GateKeeperResponse create(GateKeeperRequest req) {

        WarehouseEntity wh = warehouseRepo.findById(req.getWarehouseId())
                .orElseThrow(() -> new RuntimeException("Warehouse not found"));

        GateKeeperEntity entity = new GateKeeperEntity();
        entity.setName(req.getName());
        entity.setEmployeeCode(req.getEmployeeCode());
        entity.setPhone(req.getPhone());
        entity.setWarehouse(wh);

        GateKeeperEntity saved = repo.save(entity);

        return toResponse(saved);
    }

    public List<GateKeeperResponse> list(Long warehouseId) {
        return repo.findByWarehouseId(warehouseId)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    public List<GateKeeperResponse> getAll() {
        return repo.findAll().stream().map(this::toResponse).toList();
    }

    private GateKeeperResponse toResponse(GateKeeperEntity e) {
        GateKeeperResponse r = new GateKeeperResponse();
        r.setId(e.getId());
        r.setName(e.getName());
        r.setEmployeeCode(e.getEmployeeCode());
        r.setPhone(e.getPhone());
        r.setWarehouseId(e.getWarehouse().getId());
        r.setWarehouseName(e.getWarehouse().getName());
        return r;
    }
}
