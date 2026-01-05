package com.example.multi_tanent.warehouse.mapper;

import com.example.multi_tanent.warehouse.entity.InterLocationTransfer;
import com.example.multi_tanent.warehouse.entity.InterLocationTransferLine;
import com.example.multi_tanent.warehouse.model.TransferResponse;
import java.util.stream.Collectors;
import org.springframework.stereotype.Component;

@Component
public class TransferMapper {

    public TransferResponse toResponse(InterLocationTransfer entity) {
        if (entity == null) {
            return null;
        }

        return TransferResponse.builder()
                .id(entity.getId())
                .issueNo(entity.getIssueNo())
                .issueDate(entity.getIssueDate())
                .fromLocation(entity.getFromLocation() != null ? entity.getFromLocation().getName() : null)
                .toLocation(entity.getToLocation() != null ? entity.getToLocation().getName() : null)
                .fromRFIDTag(entity.getFromRFIDTag())
                .toRFIDTag(entity.getToRFIDTag())
                .status(entity.getStatus())
                .lines(entity.getLines() != null ? entity.getLines().stream()
                        .map(this::mapLineToResponse)
                        .collect(Collectors.toList()) : null)
                .build();
    }

    private TransferResponse.Line mapLineToResponse(InterLocationTransferLine entity) {
        if (entity == null) {
            return null;
        }

        return TransferResponse.Line.builder()
                .lineId(entity.getId())
                .itemCode(entity.getItem() != null ? entity.getItem().getCode() : null)
                .itemName(entity.getItem() != null ? entity.getItem().getName() : null)
                .requisitionQty(entity.getRequisitionQty())
                .issuedQty(entity.getIssuedQty())
                .ratePerUnit(entity.getRatePerUnit())
                .vatPercent(entity.getVatPercent())
                .build();
    }
}
