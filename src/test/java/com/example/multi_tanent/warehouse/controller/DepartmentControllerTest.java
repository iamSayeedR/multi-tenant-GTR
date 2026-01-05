package com.example.multi_tanent.warehouse.controller;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.example.multi_tanent.warehouse.model.DepartmentRequest;
import com.example.multi_tanent.warehouse.model.DepartmentResponse;
import com.example.multi_tanent.warehouse.service.DepartmentService;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.Collections;
import java.util.List;

import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(DepartmentController.class)
@AutoConfigureMockMvc(addFilters = false)
class DepartmentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private DepartmentService departmentService;

    private DepartmentRequest departmentRequest;
    private DepartmentResponse departmentResponse;

    @BeforeEach
    void setUp() {
        departmentRequest = DepartmentRequest.builder()
                .name("IT Department")
                .code("IT-001")
                .type("DEPARTMENT")
                .warehouseId(1L)
                .build();

        departmentResponse = DepartmentResponse.builder()
                .id(1L)
                .name("IT Department")
                .code("IT-001")
                .type("DEPARTMENT")
                .warehouseId(1L)
                .warehouseName("Main Warehouse")
                .active(true)
                .build();
    }

    @Test
    @SneakyThrows
    void ShouldReturn200WhenDepartmentIsCreated() {

        // Given
        when(departmentService.create(departmentRequest)).thenReturn(departmentResponse);

        // When/Then
        mockMvc.perform(post("/api/departments")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(departmentRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.name", is("IT Department")))
                .andExpect(jsonPath("$.code", is("IT-001")))
                .andExpect(jsonPath("$.type", is("DEPARTMENT")))
                .andExpect(jsonPath("$.warehouseId", is(1)))
                .andExpect(jsonPath("$.warehouseName", is("Main Warehouse")))
                .andExpect(jsonPath("$.active", is(true)));
    }

    @Test
    @SneakyThrows
    void ShouldReturnAllDepartments() {
        // Given
        List<DepartmentResponse> departments = Collections.singletonList(departmentResponse);
        when(departmentService.listAll()).thenReturn(departments);

        // When/Then
        mockMvc.perform(get("/api/departments"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id", is(1)))
                .andExpect(jsonPath("$[0].name", is("IT Department")));
    }

    @Test
    @SneakyThrows
    void ShouldReturnDepartmentById() {
        // Given
        when(departmentService.getById(1L)).thenReturn(departmentResponse);

        // When/Then
        mockMvc.perform(get("/api/departments/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.name", is("IT Department")))
                .andExpect(jsonPath("$.code", is("IT-001")));
    }

    @Test
    @SneakyThrows
    void ShouldReturnNotFoundWhenIdIsInvalid() {
        // Given
        when(departmentService.getById(999L)).thenThrow(new RuntimeException("Department not found"));

        // When/Then
        mockMvc.perform(get("/api/departments/999"))
                .andExpect(status().is5xxServerError());
    }
}
