package com.example.multi_tanent.warehouse.controller;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.example.multi_tanent.warehouse.entity.GateOutStatus;
import com.example.multi_tanent.warehouse.model.AttachRFIDTagsRequest;
import com.example.multi_tanent.warehouse.model.GateOutRequest;
import com.example.multi_tanent.warehouse.model.GateOutResponse;
import com.example.multi_tanent.warehouse.service.GateOutService;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.LocalDateTime;
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

@WebMvcTest(GateOutController.class)
@AutoConfigureMockMvc(addFilters = false)
class GateOutControllerTest {

        @Autowired
        private MockMvc mockMvc;

        @Autowired
        private ObjectMapper objectMapper;

        @MockitoBean
        private GateOutService gateOutService;

        private GateOutRequest gateOutRequest;
        private GateOutResponse gateOutResponse;

        @BeforeEach
        void setUp() {
                gateOutRequest = GateOutRequest.builder()
                                .gateKeeperId(1L)
                                .vehicleNo("ABC-123")
                                .remarks("Test Remarks")
                                .build();

                gateOutResponse = GateOutResponse.builder()
                                .id(1L)
                                .gateOutNo("GO-2024-001")
                                .gateKeeperId(1L)
                                .gateKeeperName("Gate Keeper 1")
                                .vehicleNo("ABC-123")
                                .status(GateOutStatus.PENDING)
                                .timestamp(LocalDateTime.now())
                                .build();
        }

        @Test
        void createGateOut_ShouldReturnGateOutResponse() throws Exception {
                // Given
                when(gateOutService.createGateOut(any(GateOutRequest.class))).thenReturn(gateOutResponse);

                // When/Then
                mockMvc.perform(post("/api/gate-out")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(gateOutRequest)))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$.id", is(1)))
                                .andExpect(jsonPath("$.gateOutNo", is("GO-2024-001")))
                                .andExpect(jsonPath("$.vehicleNo", is("ABC-123")))
                                .andExpect(jsonPath("$.status", is("PENDING")));

                verify(gateOutService, times(1)).createGateOut(any(GateOutRequest.class));
        }

        @Test
        void listGateOuts_WithoutFilter_ShouldReturnAllGateOuts() throws Exception {
                // Given
                GateOutResponse gateOut2 = GateOutResponse.builder()
                                .id(2L)
                                .gateOutNo("GO-2024-002")
                                .vehicleNo("XYZ-789")
                                .status(GateOutStatus.DISPATCHED)
                                .build();

                List<GateOutResponse> gateOuts = Arrays.asList(gateOutResponse, gateOut2);
                when(gateOutService.listGateOuts(isNull())).thenReturn(gateOuts);

                // When/Then
                mockMvc.perform(get("/api/gate-out"))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$", hasSize(2)))
                                .andExpect(jsonPath("$[0].id", is(1)))
                                .andExpect(jsonPath("$[0].gateOutNo", is("GO-2024-001")))
                                .andExpect(jsonPath("$[1].id", is(2)))
                                .andExpect(jsonPath("$[1].gateOutNo", is("GO-2024-002")));

                verify(gateOutService, times(1)).listGateOuts(isNull());
        }

        @Test
        void listGateOuts_WithStatusFilter_ShouldReturnFilteredGateOuts() throws Exception {
                // Given
                List<GateOutResponse> gateOuts = Arrays.asList(gateOutResponse);
                when(gateOutService.listGateOuts(anyList())).thenReturn(gateOuts);

                // When/Then
                mockMvc.perform(get("/api/gate-out")
                                .param("status", "PENDING"))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$", hasSize(1)))
                                .andExpect(jsonPath("$[0].status", is("PENDING")));

                verify(gateOutService, times(1)).listGateOuts(anyList());
        }

        @Test
        void getGateOut_ShouldReturnGateOutById() throws Exception {
                // Given
                when(gateOutService.getGateOutById(eq(1L))).thenReturn(gateOutResponse);

                // When/Then
                mockMvc.perform(get("/api/gate-out/{id}", 1L))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$.id", is(1)))
                                .andExpect(jsonPath("$.gateOutNo", is("GO-2024-001")))
                                .andExpect(jsonPath("$.vehicleNo", is("ABC-123")));

                verify(gateOutService, times(1)).getGateOutById(eq(1L));
        }

        @Test
        void confirmGateOut_ShouldReturnConfirmedGateOut() throws Exception {
                // Given
                GateOutResponse confirmedResponse = GateOutResponse.builder()
                                .id(1L)
                                .gateOutNo("GO-2024-001")
                                .status(GateOutStatus.DISPATCHED)
                                .build();

                when(gateOutService.confirmGateOut(eq(1L))).thenReturn(confirmedResponse);

                // When/Then
                mockMvc.perform(post("/api/gate-out/{id}/confirm", 1L))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$.id", is(1)))
                                .andExpect(jsonPath("$.status", is("DISPATCHED")));

                verify(gateOutService, times(1)).confirmGateOut(eq(1L));
        }

        @Test
        void attachRFIDTags_ShouldReturnUpdatedGateOut() throws Exception {
                // Given
                AttachRFIDTagsRequest rfidRequest = AttachRFIDTagsRequest.builder()
                                .rfidTags(Arrays.asList("RFID-001", "RFID-002"))
                                .build();

                when(gateOutService.attachRFIDTags(eq(1L), anyList())).thenReturn(gateOutResponse);

                // When/Then
                mockMvc.perform(post("/api/gate-out/{id}/attach-rfid", 1L)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(rfidRequest)))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$.id", is(1)))
                                .andExpect(jsonPath("$.gateOutNo", is("GO-2024-001")));

                verify(gateOutService, times(1)).attachRFIDTags(eq(1L), anyList());
        }
}
