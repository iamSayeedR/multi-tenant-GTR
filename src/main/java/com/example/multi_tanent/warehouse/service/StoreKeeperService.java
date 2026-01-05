package com.example.multi_tanent.warehouse.service;

import com.example.multi_tanent.warehouse.entity.StoreKeeperEntity;
import com.example.multi_tanent.warehouse.entity.WarehouseEntity;
import com.example.multi_tanent.warehouse.model.StoreKeeperRequest;
import com.example.multi_tanent.warehouse.model.StoreKeeperResponse;
import com.example.multi_tanent.warehouse.repository.StoreKeeperRepository;
import com.example.multi_tanent.warehouse.repository.WarehouseRepository;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class StoreKeeperService {

    private final StoreKeeperRepository repo;
    private final WarehouseRepository warehouseRepo;

    public StoreKeeperService(StoreKeeperRepository repo, WarehouseRepository warehouseRepo) {
        this.repo = repo;
        this.warehouseRepo = warehouseRepo;
    }

    public StoreKeeperResponse create(StoreKeeperRequest req) {

        StoreKeeperEntity entity = new StoreKeeperEntity();
        entity.setName(req.getName());
        entity.setEmployeeCode(req.getEmployeeId());
        entity.setPhone(req.getContactNumber());
        entity.setEmail(req.getEmail());

        WarehouseEntity wh = warehouseRepo.findById(req.getWarehouseId())
                .orElseThrow(() -> new RuntimeException("Warehouse not found"));

        entity.setWarehouse(wh);

        StoreKeeperEntity saved = repo.save(entity);

        return toResponse(saved);
    }

    public List<StoreKeeperResponse> getAll() {
        return repo.findAll().stream()
                .map(this::toResponse)
                .toList();
    }

    public List<StoreKeeperResponse> list(Long warehouseId) {
        return repo.findByWarehouseId(warehouseId)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    private StoreKeeperResponse toResponse(StoreKeeperEntity e) {
        StoreKeeperResponse r = new StoreKeeperResponse();
        r.setId(e.getId());
        r.setName(e.getName());
        r.setEmployeeId(e.getEmployeeCode());
        r.setContactNumber(e.getPhone());
        r.setEmail(e.getEmail());
        r.setWarehouseId(e.getWarehouse().getId());
        r.setWarehouseName(e.getWarehouse().getName());
        return r;
    }
}