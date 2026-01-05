package com.example.multi_tanent.warehouse.controller;

import com.example.multi_tanent.warehouse.model.Item;
import com.example.multi_tanent.warehouse.service.ItemService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ItemController.class)
@AutoConfigureMockMvc(addFilters = false)
class ItemControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private ItemService itemService;

    private Item item;

    @BeforeEach
    void setUp() {
        item = Item.builder()
                .id(1L)
                .name("Test Item")
                .code("ITEM-001")
                .description("Test item description")
                .category("Electronics")
                .uom("PCS")
                .defaultRate(new BigDecimal("100.00"))
                .defaultVatPercent(new BigDecimal("15.00"))
                .build();
    }

    @Test
    void uploadItem_ShouldReturnSuccess() throws Exception {
        // Given
        doNothing().when(itemService).uploadItem(any(Item.class));

        // When/Then
        mockMvc.perform(post("/api/warehouse-items")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(item)))
                .andExpect(status().isOk())
                .andExpect(content().string("Item uploaded"));

        verify(itemService, times(1)).uploadItem(any(Item.class));
    }

    @Test
    void getAllItems_ShouldReturnItemList() throws Exception {
        // Given
        Item item2 = Item.builder()
                .id(2L)
                .name("Test Item 2")
                .code("ITEM-002")
                .category("Hardware")
                .uom("PCS")
                .defaultRate(new BigDecimal("200.00"))
                .defaultVatPercent(new BigDecimal("15.00"))
                .build();

        List<Item> items = Arrays.asList(item, item2);
        when(itemService.findAll()).thenReturn(items);

        // When/Then
        mockMvc.perform(get("/api/warehouse-items"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].id", is(1)))
                .andExpect(jsonPath("$[0].name", is("Test Item")))
                .andExpect(jsonPath("$[0].code", is("ITEM-001")))
                .andExpect(jsonPath("$[1].id", is(2)))
                .andExpect(jsonPath("$[1].name", is("Test Item 2")));

        verify(itemService, times(1)).findAll();
    }

    @Test
    void getAllItems_WhenEmpty_ShouldReturnEmptyList() throws Exception {
        // Given
        when(itemService.findAll()).thenReturn(Arrays.asList());

        // When/Then
        mockMvc.perform(get("/api/warehouse-items"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(0)));

        verify(itemService, times(1)).findAll();
    }
}
