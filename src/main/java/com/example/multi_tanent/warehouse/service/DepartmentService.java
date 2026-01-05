package com.example.multi_tanent.warehouse.service;

import com.example.multi_tanent.warehouse.entity.DepartmentEntity;
import com.example.multi_tanent.warehouse.entity.WarehouseEntity;
import com.example.multi_tanent.warehouse.mapper.DepartmentMapper;
import com.example.multi_tanent.warehouse.model.DepartmentRequest;
import com.example.multi_tanent.warehouse.model.DepartmentResponse;
import com.example.multi_tanent.warehouse.repository.DepartmentRepository;
import com.example.multi_tanent.warehouse.repository.WarehouseRepository;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class DepartmentService {

    private final DepartmentRepository departmentRepository;
    private final WarehouseRepository warehouseRepository;
    private final DepartmentMapper departmentMapper;

    public DepartmentService(DepartmentRepository departmentRepository,
            WarehouseRepository warehouseRepository,
            DepartmentMapper departmentMapper) {
        this.departmentRepository = departmentRepository;
        this.warehouseRepository = warehouseRepository;
        this.departmentMapper = departmentMapper;
    }

    @Transactional
    public DepartmentResponse create(DepartmentRequest request) {
        DepartmentEntity department = departmentMapper.toEntity(request);

        if (request.warehouseId() != null) {
            WarehouseEntity warehouse = warehouseRepository.findById(request.warehouseId())
                    .orElseThrow(() -> new RuntimeException("Warehouse not found with id: " + request.warehouseId()));
            department.setWarehouse(warehouse);
        }

        DepartmentEntity saved = departmentRepository.save(department);
        return departmentMapper.toResponse(saved);
    }

    @Transactional(readOnly = true)
    public List<DepartmentResponse> listAll() {
        return departmentRepository.findByActiveTrue()
                .stream()
                .map(departmentMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public DepartmentResponse getById(Long id) {
        DepartmentEntity department = departmentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Department not found with id: " + id));
        return departmentMapper.toResponse(department);
    }

}
