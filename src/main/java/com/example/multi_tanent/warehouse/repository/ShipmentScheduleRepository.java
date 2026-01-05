package com.example.multi_tanent.warehouse.repository;

import com.example.multi_tanent.warehouse.entity.ShipmentScheduleEntity;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ShipmentScheduleRepository extends JpaRepository<ShipmentScheduleEntity, Long> {

    Optional<ShipmentScheduleEntity> findByScheduleNo(String scheduleNo);

    List<ShipmentScheduleEntity> findByStatus(String status);

    List<ShipmentScheduleEntity> findByExpectedArrivalDate(LocalDate date);

    List<ShipmentScheduleEntity> findByWarehouseId(Long warehouseId);
}
