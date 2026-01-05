package com.example.multi_tanent.spersusers.dto;

import com.example.multi_tanent.master.enums.Role;
import lombok.Data;

import java.util.Set;

@Data
public class UserUpdateRequest {
    private String name;
    private Set<Role> roles;
    private Boolean isActive;
    private Long storeId;
    private Long locationId;
    private String password;
}