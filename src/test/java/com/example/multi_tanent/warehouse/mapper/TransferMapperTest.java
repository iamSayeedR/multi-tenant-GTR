package com.example.multi_tanent.warehouse.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import com.example.multi_tanent.warehouse.entity.*;
import com.example.multi_tanent.warehouse.model.TransferResponse;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Arrays;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class TransferMapperTest {

    @InjectMocks
    private TransferMapper transferMapper;

    private InterLocationTransfer transferEntity;
    private InterLocationTransferLine transferLineEntity;
    private LocationEntity fromLocation;
    private LocationEntity toLocation;
    private ItemEntity itemEntity;

    @BeforeEach
    void setUp() {
        fromLocation = new LocationEntity();
        fromLocation.setId(1L);
        fromLocation.setName("Bin A-01");

        toLocation = new LocationEntity();
        toLocation.setId(2L);
        toLocation.setName("Bin B-02");

        itemEntity = new ItemEntity();
        itemEntity.setId(1L);
        itemEntity.setName("Widget");
        itemEntity.setCode("WDG-001");

        transferLineEntity = new InterLocationTransferLine();
        transferLineEntity.setId(1L);
        transferLineEntity.setItem(itemEntity);
        transferLineEntity.setRequisitionQty(100L);
        transferLineEntity.setIssuedQty(100L);
        transferLineEntity.setRatePerUnit(new BigDecimal("50.00"));
        transferLineEntity.setVatPercent(new BigDecimal("18.00"));

        transferEntity = new InterLocationTransfer();
        transferEntity.setId(1L);
        transferEntity.setIssueNo("TRF-001");
        transferEntity.setIssueDate(LocalDate.of(2024, 1, 1));
        transferEntity.setFromLocation(fromLocation);
        transferEntity.setToLocation(toLocation);
        transferEntity.setFromRFIDTag("RFID-FROM-123");
        transferEntity.setToRFIDTag("RFID-TO-456");
        transferEntity.setStatus(TransferStatus.PENDING);
        transferEntity.setLines(Arrays.asList(transferLineEntity));
    }

    @Test
    void toResponse_ShouldMapEntityToResponse() {
        // When
        TransferResponse response = transferMapper.toResponse(transferEntity);

        // Then
        assertThat(response).isNotNull();
        assertThat(response.getId()).isEqualTo(1L);
        assertThat(response.getIssueNo()).isEqualTo("TRF-001");
        assertThat(response.getIssueDate()).isEqualTo(LocalDate.of(2024, 1, 1));
        assertThat(response.getFromLocation()).isEqualTo("Bin A-01");
        assertThat(response.getToLocation()).isEqualTo("Bin B-02");
        assertThat(response.getFromRFIDTag()).isEqualTo("RFID-FROM-123");
        assertThat(response.getToRFIDTag()).isEqualTo("RFID-TO-456");
        assertThat(response.getStatus()).isEqualTo(TransferStatus.PENDING);
        assertThat(response.getLines()).hasSize(1);
    }

    @Test
    void toResponse_WithNullEntity_ShouldReturnNull() {
        // When
        TransferResponse response = transferMapper.toResponse(null);

        // Then
        assertThat(response).isNull();
    }

    @Test
    void toResponse_WithNullFromLocation_ShouldHandleGracefully() {
        // Given
        transferEntity.setFromLocation(null);

        // When
        TransferResponse response = transferMapper.toResponse(transferEntity);

        // Then
        assertThat(response).isNotNull();
        assertThat(response.getFromLocation()).isNull();
    }

    @Test
    void toResponse_WithNullToLocation_ShouldHandleGracefully() {
        // Given
        transferEntity.setToLocation(null);

        // When
        TransferResponse response = transferMapper.toResponse(transferEntity);

        // Then
        assertThat(response).isNotNull();
        assertThat(response.getToLocation()).isNull();
    }

    @Test
    void toResponse_WithNullLines_ShouldHandleGracefully() {
        // Given
        transferEntity.setLines(null);

        // When
        TransferResponse response = transferMapper.toResponse(transferEntity);

        // Then
        assertThat(response).isNotNull();
        assertThat(response.getLines()).isNull();
    }

    @Test
    void toResponse_ShouldMapLineItems() {
        // When
        TransferResponse response = transferMapper.toResponse(transferEntity);

        // Then
        assertThat(response.getLines()).hasSize(1);
        TransferResponse.Line line = response.getLines().get(0);
        assertThat(line.getLineId()).isEqualTo(1L);
        assertThat(line.getItemCode()).isEqualTo("WDG-001");
        assertThat(line.getItemName()).isEqualTo("Widget");
        assertThat(line.getRequisitionQty()).isEqualTo(100L);
        assertThat(line.getIssuedQty()).isEqualTo(100L);
        assertThat(line.getRatePerUnit()).isEqualByComparingTo(new BigDecimal("50.00"));
        assertThat(line.getVatPercent()).isEqualByComparingTo(new BigDecimal("18.00"));
    }

    @Test
    void toResponse_WithNullItemInLine_ShouldHandleGracefully() {
        // Given
        transferLineEntity.setItem(null);

        // When
        TransferResponse response = transferMapper.toResponse(transferEntity);

        // Then
        assertThat(response).isNotNull();
        assertThat(response.getLines()).hasSize(1);
        TransferResponse.Line line = response.getLines().get(0);
        assertThat(line.getItemCode()).isNull();
        assertThat(line.getItemName()).isNull();
    }
}
