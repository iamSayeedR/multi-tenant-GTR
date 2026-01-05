package com.example.multi_tanent.warehouse.repository;

import com.example.multi_tanent.warehouse.entity.GateOutEntity;
import com.example.multi_tanent.warehouse.entity.GateOutStatus;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GateOutRepository extends JpaRepository<GateOutEntity, Long> {
    Optional<GateOutEntity> findByGateOutNo(String gateOutNo);

    List<GateOutEntity> findByStatus(GateOutStatus status);

    List<GateOutEntity> findByStatusIn(List<GateOutStatus> statuses);
}
