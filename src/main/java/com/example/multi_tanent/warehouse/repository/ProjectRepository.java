package com.example.multi_tanent.warehouse.repository;

import com.example.multi_tanent.warehouse.entity.ProjectEntity;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProjectRepository extends JpaRepository<ProjectEntity, Long> {
    Optional<ProjectEntity> findByCode(String code);

    List<ProjectEntity> findByActiveTrue();

    List<ProjectEntity> findByStatus(String status);
}
