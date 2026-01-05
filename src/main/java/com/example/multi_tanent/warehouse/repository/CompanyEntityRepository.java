package com.example.multi_tanent.warehouse.repository;

import com.example.multi_tanent.warehouse.entity.CompanyEntityEntity;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CompanyEntityRepository extends JpaRepository<CompanyEntityEntity, Long> {
    Optional<CompanyEntityEntity> findByCode(String code);

    List<CompanyEntityEntity> findByActiveTrue();
}
