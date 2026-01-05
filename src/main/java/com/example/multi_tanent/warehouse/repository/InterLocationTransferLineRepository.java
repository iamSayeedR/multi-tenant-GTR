package com.example.multi_tanent.warehouse.repository;

import com.example.multi_tanent.warehouse.entity.InterLocationTransferLine;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InterLocationTransferLineRepository extends JpaRepository<InterLocationTransferLine, Long> {
    List<InterLocationTransferLine> findByTransferId(Long transferId);
}
