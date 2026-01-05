package com.example.multi_tanent.warehouse.repository;

import com.example.multi_tanent.warehouse.entity.ServiceEntity;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ServiceRepository extends JpaRepository<ServiceEntity, Long> {
    Optional<ServiceEntity> findByCode(String code);

    List<ServiceEntity> findByActiveTrue();

    List<ServiceEntity> findByCategory(String category);
}
