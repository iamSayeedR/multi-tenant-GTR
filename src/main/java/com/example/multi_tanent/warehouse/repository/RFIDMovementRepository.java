package com.example.multi_tanent.warehouse.repository;


import com.example.multi_tanent.warehouse.entity.RFIDMovementLogEntity;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RFIDMovementRepository extends JpaRepository<RFIDMovementLogEntity,Long> {

    List<RFIDMovementLogEntity> findByTagCode(String tagCode);
}
