package com.example.multi_tanent.warehouse.controller;

import com.example.multi_tanent.warehouse.model.GateInRequest;
import com.example.multi_tanent.warehouse.model.GateInResponse;
import com.example.multi_tanent.warehouse.service.GateInService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

//@ExtendWith(MockitoExtension.class)
@WebMvcTest(GateInController.class)
@org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc(addFilters = false)
class GateInControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private GateInService gateInService;

    @Autowired
    private ObjectMapper objectMapper;

    // @BeforeEach
    // public void setup() {
    // mockMvc = MockMvcBuilders.standaloneSetup(GateInController.class).build();
    // }

    @Test
    @SneakyThrows
    void shouldCreateGateInSuccessfully() {

        GateInRequest gateInRequest = GateInRequest
                .builder()
                .gateKeeperId(1L)
                .vehicleNo("1wb452")
                .build();

        GateInResponse gateInResponse = GateInResponse.builder().build();

        String valueAsString = objectMapper.writeValueAsString(gateInRequest);

        when(gateInService.createGateIn(gateInRequest)).thenReturn(gateInResponse);

        mockMvc
                .perform(
                        post("/api/gate-in").content(valueAsString)
                                .contentType("application/json"))
                .andExpect(status().isOk());
    }

}
