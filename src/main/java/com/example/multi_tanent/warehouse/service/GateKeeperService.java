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
    private final com.example.multi_tanent.warehouse.mapper.GateKeeperMapper mapper;

    public GateKeeperService(GateKeeperRepository repo, WarehouseRepository warehouseRepo,
            com.example.multi_tanent.warehouse.mapper.GateKeeperMapper mapper) {
        this.repo = repo;
        this.warehouseRepo = warehouseRepo;
        this.mapper = mapper;
    }

    public GateKeeperResponse create(GateKeeperRequest req) {

        WarehouseEntity wh = warehouseRepo.findById(req.getWarehouseId())
                .orElseThrow(() -> new RuntimeException("Warehouse not found"));

        GateKeeperEntity entity = mapper.toEntity(req);
        entity.setWarehouse(wh);

        GateKeeperEntity saved = repo.save(entity);

        return mapper.toResponse(saved);
    }

    public List<GateKeeperResponse> list(Long warehouseId) {
        return repo.findByWarehouseId(warehouseId)
                .stream()
                .map(mapper::toResponse)
                .toList();
    }

    public List<GateKeeperResponse> getAll() {
        return repo.findAll().stream().map(mapper::toResponse).toList();
    }
}
