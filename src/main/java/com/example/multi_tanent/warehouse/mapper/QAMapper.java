package com.example.multi_tanent.warehouse.mapper;

import com.example.multi_tanent.warehouse.entity.QARecordEntity;
import com.example.multi_tanent.warehouse.model.QAResponse;
import org.springframework.stereotype.Component;

@Component
public class QAMapper {

    public QAResponse toResponse(QARecordEntity entity) {
        if (entity == null) {
            return null;
        }

        return QAResponse.builder()
                .id(entity.getId())
                .gateInId(entity.getGateIn() != null ? entity.getGateIn().getId() : null)
                .gateInNo(entity.getGateIn() != null ? entity.getGateIn().getGateInNo() : null)
                .driverRating(entity.getDriverRating())
                .itemQualityRating(entity.getItemQualityRating())
                .qaRemarks(entity.getQaRemarks())
                .inspectedById(entity.getInspectedBy() != null ? entity.getInspectedBy().getId() : null)
                .inspectedByName(entity.getInspectedBy() != null ? entity.getInspectedBy().getName() : null)
                .inspectionDate(entity.getInspectionDate())
                .build();
    }
}
