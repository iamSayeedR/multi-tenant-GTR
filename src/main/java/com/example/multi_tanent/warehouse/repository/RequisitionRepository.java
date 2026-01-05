package com.example.multi_tanent.warehouse.repository;

import com.example.multi_tanent.warehouse.entity.RequisitionEntity;
import com.example.multi_tanent.warehouse.entity.RequisitionStatus;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RequisitionRepository extends JpaRepository<RequisitionEntity, Long> {
    Optional<RequisitionEntity> findByRequisitionNo(String requisitionNo);

    List<RequisitionEntity> findByStatus(RequisitionStatus status);

    List<RequisitionEntity> findByStatusIn(List<RequisitionStatus> statuses);

    List<RequisitionEntity> findByRequestedById(Long employeeId);

    List<RequisitionEntity> findByRecipientId(Long departmentId);

    List<RequisitionEntity> findByProjectId(Long projectId);

    List<RequisitionEntity> findByRequiredDateBetween(LocalDate startDate, LocalDate endDate);

    long count();
}
