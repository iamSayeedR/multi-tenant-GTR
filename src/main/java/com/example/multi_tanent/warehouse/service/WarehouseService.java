package com.example.multi_tanent.warehouse.service;

import com.example.multi_tanent.warehouse.entity.WarehouseEntity;
import com.example.multi_tanent.warehouse.model.WarehouseRequest;
import com.example.multi_tanent.warehouse.model.WarehouseResponse;
import com.example.multi_tanent.warehouse.repository.WarehouseRepository;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class WarehouseService {

    private final WarehouseRepository repo;
    private final com.example.multi_tanent.spersusers.repository.TenantRepository tenantRepo;
    private final com.example.multi_tanent.warehouse.mapper.WarehouseMapper mapper;

    public WarehouseService(WarehouseRepository repo,
            com.example.multi_tanent.spersusers.repository.TenantRepository tenantRepo,
            com.example.multi_tanent.warehouse.mapper.WarehouseMapper mapper) {
        this.repo = repo;
        this.tenantRepo = tenantRepo;
        this.mapper = mapper;
    }

    public WarehouseResponse create(WarehouseRequest request) {

        WarehouseEntity w = mapper.toEntity(request);

        if (request.getTenantId() != null) {
            w.setTenant(tenantRepo.findById(request.getTenantId())
                    .orElseThrow(() -> new RuntimeException("Tenant not found")));
        } else {
            // Fallback for testing: use first available tenant
            w.setTenant(tenantRepo.findAll().stream().findFirst()
                    .orElseThrow(() -> new RuntimeException("No tenant found. Please create a tenant first.")));
        }

        WarehouseEntity saved = repo.save(w);
        return mapper.toResponse(saved);
    }

    public List<WarehouseResponse> getAll() {
        return repo.findAll().stream()
                .map(mapper::toResponse)
                .toList();
    }
}
