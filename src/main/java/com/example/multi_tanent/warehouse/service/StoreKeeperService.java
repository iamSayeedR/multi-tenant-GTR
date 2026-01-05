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
    private final com.example.multi_tanent.warehouse.mapper.StoreKeeperMapper mapper;

    public StoreKeeperService(StoreKeeperRepository repo, WarehouseRepository warehouseRepo,
            com.example.multi_tanent.warehouse.mapper.StoreKeeperMapper mapper) {
        this.repo = repo;
        this.warehouseRepo = warehouseRepo;
        this.mapper = mapper;
    }

    public StoreKeeperResponse create(StoreKeeperRequest req) {

        StoreKeeperEntity entity = mapper.toEntity(req);

        WarehouseEntity wh = warehouseRepo.findById(req.getWarehouseId())
                .orElseThrow(() -> new RuntimeException("Warehouse not found"));

        entity.setWarehouse(wh);

        StoreKeeperEntity saved = repo.save(entity);

        return mapper.toResponse(saved);
    }

    public List<StoreKeeperResponse> getAll() {
        return repo.findAll().stream()
                .map(mapper::toResponse)
                .toList();
    }

    public List<StoreKeeperResponse> list(Long warehouseId) {
        return repo.findByWarehouseId(warehouseId)
                .stream()
                .map(mapper::toResponse)
                .toList();
    }
}