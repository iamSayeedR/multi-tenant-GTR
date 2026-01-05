package com.example.multi_tanent.warehouse.model;

import jakarta.validation.constraints.NotEmpty;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AttachRFIDTagsRequest {
    @NotEmpty(message = "RFID tags list cannot be empty")
    private List<String> rfidTags;
}
