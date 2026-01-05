package com.example.multi_tanent.warehouse.controller;

import com.example.multi_tanent.warehouse.model.Location;
import com.example.multi_tanent.warehouse.model.LocationNode;
import com.example.multi_tanent.warehouse.service.LocationService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(LocationController.class)
@AutoConfigureMockMvc(addFilters = false)
class LocationControllerTest {

        @Autowired
        private MockMvc mockMvc;

        @Autowired
        private ObjectMapper objectMapper;

        @MockitoBean
        private LocationService locationService;

        private Location location;

        @BeforeEach
        void setUp() {
                location = Location.builder()
                                .id(1L)
                                .code("LOC-001")
                                .name("Warehouse A - Zone 1")
                                .type("ZONE")
                                .warehouseId(1L)
                                .parentId(null)
                                .capacity(1000)
                                .build();
        }

        @Test
        void uploadLocation_ShouldReturnSuccess() throws Exception {
                // Given
                doNothing().when(locationService).uploadLocation(any(Location.class));

                // When/Then
                mockMvc.perform(post("/api/warehouse-locations")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(location)))
                                .andExpect(status().isOk())
                                .andExpect(content().string("Location saved"));

                verify(locationService, times(1)).uploadLocation(any(Location.class));
        }

        @Test
        void getAllLocations_ShouldReturnLocationList() throws Exception {
                // Given
                Location location2 = Location.builder()
                                .id(2L)
                                .code("LOC-002")
                                .name("Warehouse A - Zone 2")
                                .type("ZONE")
                                .warehouseId(1L)
                                .parentId(null)
                                .capacity(800)
                                .build();

                List<Location> locations = Arrays.asList(location, location2);
                when(locationService.findAll()).thenReturn(locations);

                // When/Then
                mockMvc.perform(get("/api/warehouse-locations"))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$", hasSize(2)))
                                .andExpect(jsonPath("$[0].id", is(1)))
                                .andExpect(jsonPath("$[0].code", is("LOC-001")))
                                .andExpect(jsonPath("$[0].name", is("Warehouse A - Zone 1")))
                                .andExpect(jsonPath("$[1].id", is(2)))
                                .andExpect(jsonPath("$[1].code", is("LOC-002")));

                verify(locationService, times(1)).findAll();
        }

        @Test
        void getChildren_ShouldReturnChildLocations() throws Exception {
                // Given
                Long parentId = 1L;
                Location child1 = Location.builder()
                                .id(2L)
                                .code("LOC-002")
                                .name("Aisle A1")
                                .type("AISLE")
                                .warehouseId(1L)
                                .parentId(parentId)
                                .build();

                Location child2 = Location.builder()
                                .id(3L)
                                .code("LOC-003")
                                .name("Aisle A2")
                                .type("AISLE")
                                .warehouseId(1L)
                                .parentId(parentId)
                                .build();

                List<Location> children = Arrays.asList(child1, child2);
                when(locationService.getChildren(eq(parentId))).thenReturn(children);

                // When/Then
                mockMvc.perform(get("/api/warehouse-locations/children/{parentId}", parentId))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$", hasSize(2)))
                                .andExpect(jsonPath("$[0].parentId", is(1)))
                                .andExpect(jsonPath("$[1].parentId", is(1)));

                verify(locationService, times(1)).getChildren(eq(parentId));
        }

        @Test
        void getByWarehouse_ShouldReturnWarehouseLocations() throws Exception {
                // Given
                Long warehouseId = 1L;
                List<Location> locations = Arrays.asList(location);
                when(locationService.getByWarehouse(eq(warehouseId))).thenReturn(locations);

                // When/Then
                mockMvc.perform(get("/api/warehouse-locations/warehouse/{warehouseId}", warehouseId))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$", hasSize(1)))
                                .andExpect(jsonPath("$[0].warehouseId", is(1)));

                verify(locationService, times(1)).getByWarehouse(eq(warehouseId));
        }

        @Test
        void getTree_ShouldReturnLocationTree() throws Exception {
                // Given
                Long warehouseId = 1L;
                LocationNode rootNode = LocationNode.builder()
                                .id(1L)
                                .code("LOC-001")
                                .name("Warehouse A")
                                .type("WAREHOUSE")
                                .children(Arrays.asList())
                                .build();

                List<LocationNode> tree = Arrays.asList(rootNode);
                when(locationService.getLocationTree(eq(warehouseId))).thenReturn(tree);

                // When/Then
                mockMvc.perform(get("/api/warehouse-locations/tree/{warehouseId}", warehouseId))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$", hasSize(1)))
                                .andExpect(jsonPath("$[0].id", is(1)))
                                .andExpect(jsonPath("$[0].code", is("LOC-001")));

                verify(locationService, times(1)).getLocationTree(eq(warehouseId));
        }
}
