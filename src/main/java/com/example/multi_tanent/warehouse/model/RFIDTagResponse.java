package com.example.multi_tanent.warehouse.model;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RFIDTagResponse {
    private Long id;
    private String tagCode;
    private Long itemId;
    private String itemCode;
    private String itemName;
    private Boolean active;
}
