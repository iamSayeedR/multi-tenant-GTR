package com.example.multi_tanent.warehouse.model;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RFIDTagRequest {
    private String tagCode;
    private Long itemId;
}
