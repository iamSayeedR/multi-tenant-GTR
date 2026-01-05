package com.example.multi_tanent.warehouse.controller;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.example.multi_tanent.warehouse.model.RFIDTagRequest;
import com.example.multi_tanent.warehouse.model.RFIDTagResponse;
import com.example.multi_tanent.warehouse.service.RFIDTagService;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Arrays;
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

@WebMvcTest(RFIDTagController.class)
@AutoConfigureMockMvc(addFilters = false)
class RFIDTagControllerTest {

        @Autowired
        private MockMvc mockMvc;

        @Autowired
        private ObjectMapper objectMapper;

        @MockitoBean
        private RFIDTagService rfidTagService;

        private RFIDTagRequest rfidTagRequest;
        private RFIDTagResponse rfidTagResponse;

        @BeforeEach
        void setUp() {
                rfidTagRequest = RFIDTagRequest.builder()
                                .tagCode("RFID-001")
                                .itemId(1L)
                                .build();

                rfidTagResponse = RFIDTagResponse.builder()
                                .id(1L)
                                .tagCode("RFID-001")
                                .itemId(1L)
                                .itemName("Test Item")
                                .active(true)
                                .build();
        }

        @Test
        @SneakyThrows
        void shouldRegisterAndReturnRFIDTagResponse() {
                // Given
                when(rfidTagService.register(any(RFIDTagRequest.class))).thenReturn(rfidTagResponse);

                // When/Then
                mockMvc.perform(post("/api/rfid-tags")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(rfidTagRequest)))
                                .andExpect(status().isOk())
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$.id", is(1)))
                                .andExpect(jsonPath("$.tagCode", is("RFID-001")))
                                .andExpect(jsonPath("$.itemId", is(1)))
                                .andExpect(jsonPath("$.active", is(true)));

                verify(rfidTagService, times(1)).register(any(RFIDTagRequest.class));
        }

        @Test
        @SneakyThrows
        void shouldReturnRFIDTagList() throws Exception {
                // Given
                RFIDTagResponse tag2 = RFIDTagResponse.builder()
                                .id(2L)
                                .tagCode("RFID-002")
                                .itemId(2L)
                                .itemName("Test Item 2")
                                .active(true)
                                .build();

                List<RFIDTagResponse> tags = Arrays.asList(rfidTagResponse, tag2);
                when(rfidTagService.getAll()).thenReturn(tags);

                // When/Then
                mockMvc.perform(get("/api/rfid-tags"))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$", hasSize(2)))
                                .andExpect(jsonPath("$[0].id", is(1)))
                                .andExpect(jsonPath("$[0].tagCode", is("RFID-001")))
                                .andExpect(jsonPath("$[0].active", is(true)))
                                .andExpect(jsonPath("$[1].id", is(2)))
                                .andExpect(jsonPath("$[1].tagCode", is("RFID-002")));

                verify(rfidTagService, times(1)).getAll();
        }

        @Test
        void shouldReturnEmptyList() throws Exception {
                // Given
                when(rfidTagService.getAll()).thenReturn(List.of());

                // When/Then
                mockMvc.perform(get("/api/rfid-tags"))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$", hasSize(0)));

                verify(rfidTagService, times(1)).getAll();
        }
}
