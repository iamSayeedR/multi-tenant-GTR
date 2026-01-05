package com.example.multi_tanent.warehouse.repository;

import com.example.multi_tanent.warehouse.entity.ExpenseItemEntity;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ExpenseItemRepository extends JpaRepository<ExpenseItemEntity, Long> {
    Optional<ExpenseItemEntity> findByCode(String code);

    List<ExpenseItemEntity> findByActiveTrue();
}
