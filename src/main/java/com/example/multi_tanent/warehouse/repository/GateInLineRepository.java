package com.example.multi_tanent.warehouse.repository;

import com.example.multi_tanent.warehouse.entity.GateInLineEntity;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GateInLineRepository extends JpaRepository<GateInLineEntity, Long> {
    List<GateInLineEntity> findByGateInId(Long gateInId);
}
