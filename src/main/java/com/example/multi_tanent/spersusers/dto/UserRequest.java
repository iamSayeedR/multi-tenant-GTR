package com.example.multi_tanent.spersusers.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserRequest {
    private String tenantId;
    private String email;
    private String password;    
}
