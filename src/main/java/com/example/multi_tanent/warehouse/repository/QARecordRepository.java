package com.example.multi_tanent.warehouse.repository;

import com.example.multi_tanent.warehouse.entity.QARecordEntity;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface QARecordRepository extends JpaRepository<QARecordEntity, Long> {

    Optional<QARecordEntity> findByGateInId(Long gateInId);

    boolean existsByGateInId(Long gateInId);
}
