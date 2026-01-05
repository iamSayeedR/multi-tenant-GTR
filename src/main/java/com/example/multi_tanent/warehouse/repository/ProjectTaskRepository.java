package com.example.multi_tanent.warehouse.repository;

import com.example.multi_tanent.warehouse.entity.ProjectTaskEntity;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProjectTaskRepository extends JpaRepository<ProjectTaskEntity, Long> {
    List<ProjectTaskEntity> findByProjectId(Long projectId);

    List<ProjectTaskEntity> findByStatus(String status);
}
