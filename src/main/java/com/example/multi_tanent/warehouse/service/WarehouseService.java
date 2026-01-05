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

    public WarehouseService(WarehouseRepository repo,
            com.example.multi_tanent.spersusers.repository.TenantRepository tenantRepo) {
        this.repo = repo;
        this.tenantRepo = tenantRepo;
    }

    public WarehouseResponse create(WarehouseRequest request) {

        WarehouseEntity w = new WarehouseEntity();
        w.setName(request.getName());
        w.setAddress(request.getAddress());
        w.setActive(request.getActive());

        if (request.getTenantId() != null) {
            w.setTenant(tenantRepo.findById(request.getTenantId())
                    .orElseThrow(() -> new RuntimeException("Tenant not found")));
        } else {
            // Fallback for testing: use first available tenant
            w.setTenant(tenantRepo.findAll().stream().findFirst()
                    .orElseThrow(() -> new RuntimeException("No tenant found. Please create a tenant first.")));
        }

        WarehouseEntity saved = repo.save(w);

        WarehouseResponse res = new WarehouseResponse();
        res.setId(saved.getId());
        res.setName(saved.getName());
        res.setAddress(saved.getAddress());
        res.setActive(saved.getActive());

        return res;
    }

    public List<WarehouseResponse> getAll() {
        return repo.findAll().stream().map(w -> {
            WarehouseResponse r = new WarehouseResponse();
            r.setId(w.getId());
            r.setName(w.getName());
            r.setAddress(w.getAddress());
            r.setActive(w.getActive());
            return r;
        }).toList();
    }
}
