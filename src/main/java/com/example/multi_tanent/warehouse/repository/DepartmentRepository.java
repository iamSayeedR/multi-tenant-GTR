package com.example.multi_tanent.warehouse.repository;

import com.example.multi_tanent.warehouse.entity.DepartmentEntity;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DepartmentRepository extends JpaRepository<DepartmentEntity, Long> {
    Optional<DepartmentEntity> findByCode(String code);

    List<DepartmentEntity> findByActiveTrue();

    List<DepartmentEntity> findByType(String type);
}
