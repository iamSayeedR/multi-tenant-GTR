package com.example.multi_tanent.warehouse.repository;

import com.example.multi_tanent.warehouse.entity.RFIDTagEntity;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RFIDTagRepository extends JpaRepository<RFIDTagEntity, Long> {

    Optional<RFIDTagEntity> findByTagCode(String tagCode);
}
