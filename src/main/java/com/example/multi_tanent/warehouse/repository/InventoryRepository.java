package com.example.multi_tanent.warehouse.repository;

import com.example.multi_tanent.warehouse.entity.InventoryEntity;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository("warehouseInventoryRepository")
public interface InventoryRepository extends JpaRepository<InventoryEntity, Long> {
    Optional<InventoryEntity> findByLocationIdAndItemId(Long locationId, Long itemId);

    List<InventoryEntity> findByLocationId(Long locationId);

    List<InventoryEntity> findByItemId(Long itemId);
}
