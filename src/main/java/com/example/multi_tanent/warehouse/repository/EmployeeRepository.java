package com.example.multi_tanent.warehouse.repository;

import com.example.multi_tanent.warehouse.entity.EmployeeEntity;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EmployeeRepository extends JpaRepository<EmployeeEntity, Long> {
    Optional<EmployeeEntity> findByEmployeeCode(String employeeCode);

    List<EmployeeEntity> findByActiveTrue();

    List<EmployeeEntity> findByDepartmentId(Long departmentId);
}
