package com.example.multi_tanent.warehouse.repository;

import com.example.multi_tanent.warehouse.entity.InterLocationTransfer;
import com.example.multi_tanent.warehouse.entity.TransferStatus;
import java.util.Collection;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InterLocationTransferRepository extends JpaRepository<InterLocationTransfer, Long> {
    List<InterLocationTransfer> findByStatusIn(Collection<TransferStatus> statuses);
}
