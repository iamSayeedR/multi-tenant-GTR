package com.example.multi_tanent.warehouse.repository;

import com.example.multi_tanent.warehouse.entity.ItemEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository("warehouseItemRepository")
public interface ItemRepository extends JpaRepository<ItemEntity, Long> {
}
