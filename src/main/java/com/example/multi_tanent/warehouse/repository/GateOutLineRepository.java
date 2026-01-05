package com.example.multi_tanent.warehouse.repository;

import com.example.multi_tanent.warehouse.entity.GateOutLineEntity;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GateOutLineRepository extends JpaRepository<GateOutLineEntity, Long> {
    List<GateOutLineEntity> findByGateOutId(Long gateOutId);
}
