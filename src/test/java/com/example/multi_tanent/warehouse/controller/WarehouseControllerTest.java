package com.example.multi_tanent.warehouse.controller;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.example.multi_tanent.warehouse.model.WarehouseRequest;
import com.example.multi_tanent.warehouse.model.WarehouseResponse;
import com.example.multi_tanent.warehouse.service.WarehouseService;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(WarehouseController.class)
@AutoConfigureMockMvc(addFilters = false)
class WarehouseControllerTest {

        @Autowired
        private MockMvc mockMvc;

        @Autowired
        private ObjectMapper objectMapper;

        @MockitoBean
        private WarehouseService warehouseService;

        private WarehouseRequest warehouseRequest;
        private WarehouseResponse warehouseResponse;

        @BeforeEach
        void setUp() {
                warehouseRequest = WarehouseRequest.builder()
                                .name("Main Warehouse")
                                .code("WH-001")
                                .address("123 Main St, City")
                                .contactPerson("John Doe")
                                .contactNumber("+1234567890")
                                .build();

                warehouseResponse = WarehouseResponse.builder()
                                .id(1L)
                                .name("Main Warehouse")
                                .code("WH-001")
                                .address("123 Main St, City")
                                .contactPerson("John Doe")
                                .contactNumber("+1234567890")
                                .build();
        }

        @Test
        void create_ShouldReturnWarehouseResponse() throws Exception {
                // Given
                when(warehouseService.create(any(WarehouseRequest.class))).thenReturn(warehouseResponse);

                // When/Then
                mockMvc.perform(post("/api/warehouses")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(warehouseRequest)))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$.id", is(1)))
                                .andExpect(jsonPath("$.name", is("Main Warehouse")))
                                .andExpect(jsonPath("$.code", is("WH-001")))
                                .andExpect(jsonPath("$.address", is("123 Main St, City")))
                                .andExpect(jsonPath("$.contactPerson", is("John Doe")));

                verify(warehouseService, times(1)).create(any(WarehouseRequest.class));
        }

        @Test
        void getAll_ShouldReturnWarehouseList() throws Exception {
                // Given
                WarehouseResponse warehouse2 = WarehouseResponse.builder()
                                .id(2L)
                                .name("Secondary Warehouse")
                                .code("WH-002")
                                .address("456 Second St, City")
                                .contactPerson("Jane Smith")
                                .contactNumber("+0987654321")
                                .build();

                List<WarehouseResponse> warehouses = Arrays.asList(warehouseResponse, warehouse2);
                when(warehouseService.getAll()).thenReturn(warehouses);

                // When/Then
                mockMvc.perform(get("/api/warehouses"))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$", hasSize(2)))
                                .andExpect(jsonPath("$[0].id", is(1)))
                                .andExpect(jsonPath("$[0].name", is("Main Warehouse")))
                                .andExpect(jsonPath("$[0].code", is("WH-001")))
                                .andExpect(jsonPath("$[1].id", is(2)))
                                .andExpect(jsonPath("$[1].name", is("Secondary Warehouse")))
                                .andExpect(jsonPath("$[1].code", is("WH-002")));

                verify(warehouseService, times(1)).getAll();
        }

        @Test
        void getAll_WhenEmpty_ShouldReturnEmptyList() throws Exception {
                // Given
                when(warehouseService.getAll()).thenReturn(Arrays.asList());

                // When/Then
                mockMvc.perform(get("/api/warehouses"))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$", hasSize(0)));

                verify(warehouseService, times(1)).getAll();
        }
}
