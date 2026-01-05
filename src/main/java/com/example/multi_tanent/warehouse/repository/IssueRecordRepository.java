package com.example.multi_tanent.warehouse.repository;

import com.example.multi_tanent.warehouse.entity.IssueRecord;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IssueRecordRepository extends JpaRepository<IssueRecord, Long> {
    List<IssueRecord> findByLineIdOrderByDateAsc(Long lineId);
}
