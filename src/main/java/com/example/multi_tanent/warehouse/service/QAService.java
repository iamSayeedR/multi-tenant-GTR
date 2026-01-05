package com.example.multi_tanent.warehouse.service;

import com.example.multi_tanent.warehouse.entity.GateInEntity;
import com.example.multi_tanent.warehouse.entity.QARecordEntity;
import com.example.multi_tanent.warehouse.entity.StoreKeeperEntity;
import com.example.multi_tanent.warehouse.model.QARequest;
import com.example.multi_tanent.warehouse.model.QAResponse;
import com.example.multi_tanent.warehouse.repository.GateInRepository;
import com.example.multi_tanent.warehouse.repository.QARecordRepository;
import com.example.multi_tanent.warehouse.repository.StoreKeeperRepository;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class QAService {

    private final QARecordRepository qaRecordRepository;
    private final GateInRepository gateInRepository;
    private final StoreKeeperRepository storeKeeperRepository;

    public QAService(
            QARecordRepository qaRecordRepository,
            GateInRepository gateInRepository,
            StoreKeeperRepository storeKeeperRepository) {
        this.qaRecordRepository = qaRecordRepository;
        this.gateInRepository = gateInRepository;
        this.storeKeeperRepository = storeKeeperRepository;
    }

    @Transactional
    public QAResponse performQA(QARequest request) {
        // Check if QA already exists for this gate in
        if (qaRecordRepository.existsByGateInId(request.getGateInId())) {
            throw new IllegalArgumentException("QA already performed for this Gate In");
        }

        // Validate gate in
        GateInEntity gateIn = gateInRepository.findById(request.getGateInId())
                .orElseThrow(() -> new IllegalArgumentException("Gate In not found"));

        // Validate storekeeper
        StoreKeeperEntity storeKeeper = storeKeeperRepository.findById(request.getInspectedById())
                .orElseThrow(() -> new IllegalArgumentException("StoreKeeper not found"));

        // Validate ratings
        if (request.getDriverRating() < 1 || request.getDriverRating() > 5) {
            throw new IllegalArgumentException("Driver rating must be between 1 and 5");
        }
        if (request.getItemQualityRating() < 1 || request.getItemQualityRating() > 5) {
            throw new IllegalArgumentException("Item quality rating must be between 1 and 5");
        }

        // Create QA record
        QARecordEntity qaRecord = new QARecordEntity();
        qaRecord.setGateIn(gateIn);
        qaRecord.setDriverRating(request.getDriverRating());
        qaRecord.setItemQualityRating(request.getItemQualityRating());
        qaRecord.setQaRemarks(request.getQaRemarks());
        qaRecord.setInspectedBy(storeKeeper);

        qaRecord = qaRecordRepository.save(qaRecord);
        return mapToResponse(qaRecord);
    }

    @Transactional(readOnly = true)
    public QAResponse getQAByGateInId(Long gateInId) {
        QARecordEntity qaRecord = qaRecordRepository.findByGateInId(gateInId)
                .orElseThrow(() -> new IllegalArgumentException("QA record not found for this Gate In"));
        return mapToResponse(qaRecord);
    }

    @Transactional(readOnly = true)
    public List<QAResponse> getAllQARecords() {
        return qaRecordRepository.findAll().stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    private QAResponse mapToResponse(QARecordEntity entity) {
        return QAResponse.builder()
                .id(entity.getId())
                .gateInId(entity.getGateIn().getId())
                .gateInNo(entity.getGateIn().getGateInNo())
                .driverRating(entity.getDriverRating())
                .itemQualityRating(entity.getItemQualityRating())
                .qaRemarks(entity.getQaRemarks())
                .inspectedById(entity.getInspectedBy().getId())
                .inspectedByName(entity.getInspectedBy().getName())
                .inspectionDate(entity.getInspectionDate())
                .build();
    }
}
