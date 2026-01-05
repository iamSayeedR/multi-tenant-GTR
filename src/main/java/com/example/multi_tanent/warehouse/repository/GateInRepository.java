package com.example.multi_tanent.warehouse.repository;

import com.example.multi_tanent.warehouse.entity.GateInEntity;
import com.example.multi_tanent.warehouse.entity.GateInStatus;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GateInRepository extends JpaRepository<GateInEntity, Long> {
    Optional<GateInEntity> findByGateInNo(String gateInNo);

    List<GateInEntity> findByStatus(GateInStatus status);

    List<GateInEntity> findByStatusIn(List<GateInStatus> statuses);
}
