package com.example.multi_tanent.warehouse.repository;

import com.example.multi_tanent.warehouse.entity.WarehouseEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WarehouseRepository extends JpaRepository<WarehouseEntity, Long> { }
