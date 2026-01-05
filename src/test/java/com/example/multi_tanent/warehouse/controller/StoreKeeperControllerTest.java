package com.example.multi_tanent.warehouse.controller;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.example.multi_tanent.warehouse.model.StoreKeeperRequest;
import com.example.multi_tanent.warehouse.model.StoreKeeperResponse;
import com.example.multi_tanent.warehouse.service.StoreKeeperService;
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

@WebMvcTest(StoreKeeperController.class)
@AutoConfigureMockMvc(addFilters = false)
class StoreKeeperControllerTest {

        @Autowired
        private MockMvc mockMvc;

        @Autowired
        private ObjectMapper objectMapper;

        @MockitoBean
        private StoreKeeperService storeKeeperService;

        private StoreKeeperRequest storeKeeperRequest;
        private StoreKeeperResponse storeKeeperResponse;

        @BeforeEach
        void setUp() {
                storeKeeperRequest = StoreKeeperRequest.builder()
                                .name("John Keeper")
                                .employeeId("EMP-001")
                                .warehouseId(1L)
                                .contactNumber("+1234567890")
                                .email("john.keeper@example.com")
                                .build();

                storeKeeperResponse = StoreKeeperResponse.builder()
                                .id(1L)
                                .name("John Keeper")
                                .employeeId("EMP-001")
                                .warehouseId(1L)
                                .contactNumber("+1234567890")
                                .email("john.keeper@example.com")
                                .build();
        }

        @Test
        void create_ShouldReturnStoreKeeperResponse() throws Exception {
                // Given
                when(storeKeeperService.create(any(StoreKeeperRequest.class))).thenReturn(storeKeeperResponse);

                // When/Then
                mockMvc.perform(post("/api/store-keepers")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(storeKeeperRequest)))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$.id", is(1)))
                                .andExpect(jsonPath("$.name", is("John Keeper")))
                                .andExpect(jsonPath("$.employeeId", is("EMP-001")))
                                .andExpect(jsonPath("$.warehouseId", is(1)))
                                .andExpect(jsonPath("$.email", is("john.keeper@example.com")));

                verify(storeKeeperService, times(1)).create(any(StoreKeeperRequest.class));
        }

        @Test
        void getAll_ShouldReturnStoreKeeperList() throws Exception {
                // Given
                StoreKeeperResponse storeKeeper2 = StoreKeeperResponse.builder()
                                .id(2L)
                                .name("Jane Keeper")
                                .employeeId("EMP-002")
                                .warehouseId(2L)
                                .contactNumber("+0987654321")
                                .email("jane.keeper@example.com")
                                .build();

                List<StoreKeeperResponse> storeKeepers = Arrays.asList(storeKeeperResponse, storeKeeper2);
                when(storeKeeperService.getAll()).thenReturn(storeKeepers);

                // When/Then
                mockMvc.perform(get("/api/store-keepers"))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$", hasSize(2)))
                                .andExpect(jsonPath("$[0].id", is(1)))
                                .andExpect(jsonPath("$[0].name", is("John Keeper")))
                                .andExpect(jsonPath("$[1].id", is(2)))
                                .andExpect(jsonPath("$[1].name", is("Jane Keeper")));

                verify(storeKeeperService, times(1)).getAll();
        }

        @Test
        void list_ByWarehouse_ShouldReturnWarehouseStoreKeepers() throws Exception {
                // Given
                Long warehouseId = 1L;
                List<StoreKeeperResponse> storeKeepers = Arrays.asList(storeKeeperResponse);
                when(storeKeeperService.list(eq(warehouseId))).thenReturn(storeKeepers);

                // When/Then
                mockMvc.perform(get("/api/store-keepers/{warehouseId}", warehouseId))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$", hasSize(1)))
                                .andExpect(jsonPath("$[0].warehouseId", is(1)))
                                .andExpect(jsonPath("$[0].name", is("John Keeper")));

                verify(storeKeeperService, times(1)).list(eq(warehouseId));
        }

        @Test
        void list_ByWarehouse_WhenEmpty_ShouldReturnEmptyList() throws Exception {
                // Given
                Long warehouseId = 999L;
                when(storeKeeperService.list(eq(warehouseId))).thenReturn(Arrays.asList());

                // When/Then
                mockMvc.perform(get("/api/store-keepers/{warehouseId}", warehouseId))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$", hasSize(0)));

                verify(storeKeeperService, times(1)).list(eq(warehouseId));
        }
}
