package com.example.multi_tanent.warehouse.service;

import com.example.multi_tanent.warehouse.entity.*;
import com.example.multi_tanent.warehouse.model.*;
import com.example.multi_tanent.warehouse.repository.*;
import jakarta.transaction.Transactional;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;

@Service
public class RequisitionService {

    private final RequisitionRepository requisitionRepository;
    private final DepartmentRepository departmentRepository;
    private final EmployeeRepository employeeRepository;
    private final CompanyEntityRepository companyEntityRepository;
    private final ProjectRepository projectRepository;
    private final ProjectTaskRepository projectTaskRepository;
    private final ItemRepository itemRepository;
    private final ServiceRepository serviceRepository;
    private final ExpenseItemRepository expenseItemRepository;
    private final InventoryService inventoryService;

    public RequisitionService(
            RequisitionRepository requisitionRepository,
            DepartmentRepository departmentRepository,
            EmployeeRepository employeeRepository,
            CompanyEntityRepository companyEntityRepository,
            ProjectRepository projectRepository,
            ProjectTaskRepository projectTaskRepository,
            ItemRepository itemRepository,
            ServiceRepository serviceRepository,
            ExpenseItemRepository expenseItemRepository,
            InventoryService inventoryService) {
        this.requisitionRepository = requisitionRepository;
        this.departmentRepository = departmentRepository;
        this.employeeRepository = employeeRepository;
        this.companyEntityRepository = companyEntityRepository;
        this.projectRepository = projectRepository;
        this.projectTaskRepository = projectTaskRepository;
        this.itemRepository = itemRepository;
        this.serviceRepository = serviceRepository;
        this.expenseItemRepository = expenseItemRepository;
        this.inventoryService = inventoryService;
    }

    @Transactional
    public RequisitionResponse createRequisition(RequisitionRequest request) {
        RequisitionEntity requisition = new RequisitionEntity();

        // Generate requisition number
        requisition.setRequisitionNo(generateRequisitionNo());

        // Set basic fields
        requisition.setRequiredDate(request.getRequiredDate());
        requisition.setFromDate(request.getFromDate() != null ? request.getFromDate() : LocalDate.now());
        requisition.setComment(request.getComment());
        requisition.setCustomerOrderId(request.getCustomerOrderId());
        requisition.setBasisType(request.getBasisType());
        requisition.setBasisId(request.getBasisId());

        // Set relationships
        if (request.getEntityId() != null) {
            CompanyEntityEntity entity = companyEntityRepository.findById(request.getEntityId())
                    .orElseThrow(() -> new RuntimeException("Company entity not found"));
            requisition.setEntity(entity);
        }

        DepartmentEntity recipient = departmentRepository.findById(request.getRecipientId())
                .orElseThrow(() -> new RuntimeException("Recipient department not found"));
        requisition.setRecipient(recipient);

        EmployeeEntity requestedBy = employeeRepository.findById(request.getRequestedById())
                .orElseThrow(() -> new RuntimeException("Employee not found"));
        requisition.setRequestedBy(requestedBy);

        if (request.getProjectId() != null) {
            ProjectEntity project = projectRepository.findById(request.getProjectId())
                    .orElseThrow(() -> new RuntimeException("Project not found"));
            requisition.setProject(project);
        }

        if (request.getProjectTaskId() != null) {
            ProjectTaskEntity projectTask = projectTaskRepository.findById(request.getProjectTaskId())
                    .orElseThrow(() -> new RuntimeException("Project task not found"));
            requisition.setProjectTask(projectTask);
        }

        // Save requisition first to get ID
        requisition = requisitionRepository.save(requisition);

        // Add lines
        if (request.getLines() != null && !request.getLines().isEmpty()) {
            int lineNumber = 1;
            for (RequisitionLineRequest lineReq : request.getLines()) {
                RequisitionLineEntity line = createLine(requisition, lineReq, lineNumber++);
                requisition.getLines().add(line);
            }
        }

        // Calculate total amount
        calculateTotalAmount(requisition);

        requisition = requisitionRepository.save(requisition);
        return mapToResponse(requisition);
    }

    private RequisitionLineEntity createLine(RequisitionEntity requisition, RequisitionLineRequest lineReq,
            int lineNumber) {
        RequisitionLineEntity line = new RequisitionLineEntity();
        line.setRequisition(requisition);
        line.setLineNumber(lineNumber);
        line.setContent(lineReq.getContent());
        line.setQuantityTotal(lineReq.getQuantityTotal());
        line.setQuantityToPurchase(
                lineReq.getQuantityToPurchase() != null ? lineReq.getQuantityToPurchase() : BigDecimal.ZERO);
        line.setQuantityToTransfer(
                lineReq.getQuantityToTransfer() != null ? lineReq.getQuantityToTransfer() : BigDecimal.ZERO);
        line.setUom(lineReq.getUom());
        line.setTransactionContent(lineReq.getTransactionContent());
        line.setFulfillmentSourceId(lineReq.getFulfillmentSourceId());
        line.setPrice(lineReq.getPrice());
        line.setPricesIncludeVat(lineReq.getPricesIncludeVat() != null ? lineReq.getPricesIncludeVat() : false);
        line.setVatPercentage(lineReq.getVatPercentage() != null ? lineReq.getVatPercentage() : BigDecimal.ZERO);

        // Set fulfillment method
        if (lineReq.getFulfillmentMethod() != null) {
            line.setFulfillmentMethod(FulfillmentMethod.valueOf(lineReq.getFulfillmentMethod()));
        }

        // Set item or service
        if (lineReq.getItemId() != null) {
            ItemEntity item = itemRepository.findById(lineReq.getItemId())
                    .orElseThrow(() -> new RuntimeException("Item not found"));
            line.setItem(item);

            // Get stock balance
            BigDecimal stockBalance = inventoryService.getTotalStockForItem(lineReq.getItemId());
            line.setStockBalance(stockBalance);
        }

        if (lineReq.getServiceId() != null) {
            ServiceEntity service = serviceRepository.findById(lineReq.getServiceId())
                    .orElseThrow(() -> new RuntimeException("Service not found"));
            line.setService(service);
        }

        if (lineReq.getExpenseItemId() != null) {
            ExpenseItemEntity expenseItem = expenseItemRepository.findById(lineReq.getExpenseItemId())
                    .orElseThrow(() -> new RuntimeException("Expense item not found"));
            line.setExpenseItem(expenseItem);
        }

        if (lineReq.getProjectTaskId() != null) {
            ProjectTaskEntity projectTask = projectTaskRepository.findById(lineReq.getProjectTaskId())
                    .orElseThrow(() -> new RuntimeException("Project task not found"));
            line.setProjectTask(projectTask);
        }

        // Calculate amounts
        line.calculateAmounts();

        return line;
    }

    private void calculateTotalAmount(RequisitionEntity requisition) {
        BigDecimal total = requisition.getLines().stream()
                .map(RequisitionLineEntity::getTotalAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        requisition.setTotalAmount(total);
    }

    @Transactional
    public RequisitionResponse submitForProcessing(Long id) {
        RequisitionEntity requisition = requisitionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Requisition not found"));

        if (requisition.getStatus() != RequisitionStatus.DRAFT) {
            throw new RuntimeException("Only draft requisitions can be submitted");
        }

        requisition.setStatus(RequisitionStatus.WAITING_FOR_PROCESSING);
        requisition = requisitionRepository.save(requisition);

        return mapToResponse(requisition);
    }

    @Transactional
    public RequisitionResponse approve(Long id) {
        RequisitionEntity requisition = requisitionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Requisition not found"));

        if (requisition.getStatus() != RequisitionStatus.WAITING_FOR_PROCESSING) {
            throw new RuntimeException("Only waiting requisitions can be approved");
        }

        requisition.setStatus(RequisitionStatus.APPROVED);
        requisition = requisitionRepository.save(requisition);

        return mapToResponse(requisition);
    }

    @Transactional
    public RequisitionResponse reject(Long id, String reason) {
        RequisitionEntity requisition = requisitionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Requisition not found"));

        requisition.setStatus(RequisitionStatus.REJECTED);
        requisition.setComment(requisition.getComment() + "\nREJECTED: " + reason);
        requisition = requisitionRepository.save(requisition);

        return mapToResponse(requisition);
    }

    public List<RequisitionResponse> listRequisitions(String status) {
        List<RequisitionEntity> requisitions;

        if (status != null && !status.isEmpty()) {
            RequisitionStatus reqStatus = RequisitionStatus.valueOf(status);
            requisitions = requisitionRepository.findByStatus(reqStatus);
        } else {
            requisitions = requisitionRepository.findAll();
        }

        return requisitions.stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    public RequisitionResponse getRequisitionById(Long id) {
        RequisitionEntity requisition = requisitionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Requisition not found"));
        return mapToResponse(requisition);
    }

    private synchronized String generateRequisitionNo() {
        long count = requisitionRepository.count();
        return "REQ-" + String.format("%06d", count + 1);
    }

    private RequisitionResponse mapToResponse(RequisitionEntity entity) {
        RequisitionResponse response = new RequisitionResponse();
        response.setId(entity.getId());
        response.setRequisitionNo(entity.getRequisitionNo());
        response.setRequiredDate(entity.getRequiredDate());
        response.setStatus(entity.getStatus().name());
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

        List<RequisitionLineResponse> lineResponses = entity.getLines().stream()
                .map(this::mapLineToResponse)
                .collect(Collectors.toList());
        response.setLines(lineResponses);

        return response;
    }

    private RequisitionLineResponse mapLineToResponse(RequisitionLineEntity entity) {
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
