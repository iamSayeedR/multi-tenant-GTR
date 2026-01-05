package com.example.multi_tanent.warehouse.mapper;

import com.example.multi_tanent.warehouse.entity.RequisitionEntity;
import com.example.multi_tanent.warehouse.entity.RequisitionLineEntity;
import com.example.multi_tanent.warehouse.model.RequisitionLineResponse;
import com.example.multi_tanent.warehouse.model.RequisitionResponse;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Component;

@Component
public class RequisitionMapper {

    public RequisitionResponse toResponse(RequisitionEntity entity) {
        if (entity == null) {
            return null;
        }

        RequisitionResponse response = new RequisitionResponse();
        response.setId(entity.getId());
        response.setRequisitionNo(entity.getRequisitionNo());
        response.setRequiredDate(entity.getRequiredDate());
        if (entity.getStatus() != null) {
            response.setStatus(entity.getStatus().name());
        }
        response.setFromDate(entity.getFromDate());
        response.setComment(entity.getComment());
        response.setCustomerOrderId(entity.getCustomerOrderId());
        response.setBasisType(entity.getBasisType());
        response.setBasisId(entity.getBasisId());
        response.setTotalAmount(entity.getTotalAmount());
        response.setCreatedAt(entity.getCreatedAt());
        response.setUpdatedAt(entity.getUpdatedAt());

        if (entity.getEntity() != null) {
            response.setEntityId(entity.getEntity().getId());
            response.setEntityName(entity.getEntity().getName());
        }

        if (entity.getRecipient() != null) {
            response.setRecipientId(entity.getRecipient().getId());
            response.setRecipientName(entity.getRecipient().getName());
        }

        if (entity.getRequestedBy() != null) {
            response.setRequestedById(entity.getRequestedBy().getId());
            response.setRequestedByName(entity.getRequestedBy().getName());
        }

        if (entity.getProject() != null) {
            response.setProjectId(entity.getProject().getId());
            response.setProjectName(entity.getProject().getName());
        }

        if (entity.getProjectTask() != null) {
            response.setProjectTaskId(entity.getProjectTask().getId());
            response.setProjectTaskName(entity.getProjectTask().getName());
        }

        if (entity.getLines() != null) {
            List<RequisitionLineResponse> lineResponses = entity.getLines().stream()
                    .map(this::mapLineToResponse)
                    .collect(Collectors.toList());
            response.setLines(lineResponses);
        }

        return response;
    }

    private RequisitionLineResponse mapLineToResponse(RequisitionLineEntity entity) {
        if (entity == null) {
            return null;
        }

        RequisitionLineResponse response = new RequisitionLineResponse();
        response.setId(entity.getId());
        response.setLineNumber(entity.getLineNumber());
        response.setContent(entity.getContent());
        response.setQuantityTotal(entity.getQuantityTotal());
        response.setQuantityToPurchase(entity.getQuantityToPurchase());
        response.setQuantityToTransfer(entity.getQuantityToTransfer());
        response.setUom(entity.getUom());
        response.setStockBalance(entity.getStockBalance());
        response.setRemainingBudgetQty(entity.getRemainingBudgetQty());
        response.setTransactionContent(entity.getTransactionContent());
        response.setFulfillmentSourceId(entity.getFulfillmentSourceId());
        response.setPrice(entity.getPrice());
        response.setPricesIncludeVat(entity.getPricesIncludeVat());
        response.setVatPercentage(entity.getVatPercentage());
        response.setAmountExclVat(entity.getAmountExclVat());
        response.setVatAmount(entity.getVatAmount());
        response.setTotalAmount(entity.getTotalAmount());

        if (entity.getFulfillmentMethod() != null) {
            response.setFulfillmentMethod(entity.getFulfillmentMethod().name());
        }

        if (entity.getItem() != null) {
            response.setItemId(entity.getItem().getId());
            response.setItemName(entity.getItem().getName());
        }

        if (entity.getService() != null) {
            response.setServiceId(entity.getService().getId());
            response.setServiceName(entity.getService().getName());
        }

        if (entity.getExpenseItem() != null) {
            response.setExpenseItemId(entity.getExpenseItem().getId());
            response.setExpenseItemName(entity.getExpenseItem().getName());
        }

        if (entity.getProjectTask() != null) {
            response.setProjectTaskId(entity.getProjectTask().getId());
            response.setProjectTaskName(entity.getProjectTask().getName());
        }

        return response;
    }
}
