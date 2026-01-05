package com.example.multi_tanent.warehouse.repository;

import com.example.multi_tanent.warehouse.entity.StorageAssignmentEntity;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StorageAssignmentRepository extends JpaRepository<StorageAssignmentEntity, Long> {

    Optional<StorageAssignmentEntity> findByAssignmentNo(String assignmentNo);

    List<StorageAssignmentEntity> findByAssignedToId(Long storeKeeperId);

    List<StorageAssignmentEntity> findByStatus(String status);

    List<StorageAssignmentEntity> findByGateInLineId(Long gateInLineId);
}
