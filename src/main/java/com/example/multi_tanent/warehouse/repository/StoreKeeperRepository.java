package com.example.multi_tanent.warehouse.repository;

import com.example.multi_tanent.warehouse.entity.StoreKeeperEntity;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StoreKeeperRepository extends JpaRepository<StoreKeeperEntity, Long> {

    List<StoreKeeperEntity> findByWarehouseId(Long warehouseId);
}
