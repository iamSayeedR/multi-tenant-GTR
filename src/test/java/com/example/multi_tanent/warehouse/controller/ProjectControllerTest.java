package com.example.multi_tanent.warehouse.controller;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.example.multi_tanent.warehouse.entity.ProjectTaskEntity;
import com.example.multi_tanent.warehouse.model.ProjectRequest;
import com.example.multi_tanent.warehouse.model.ProjectResponse;
import com.example.multi_tanent.warehouse.service.ProjectService;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.LocalDate;
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

@WebMvcTest(ProjectController.class)
@AutoConfigureMockMvc(addFilters = false)
class ProjectControllerTest {

        @Autowired
        private MockMvc mockMvc;

        @Autowired
        private ObjectMapper objectMapper;

        @MockitoBean
        private ProjectService projectService;

        private ProjectResponse projectResponse;
        private ProjectRequest projectRequest;
        private ProjectTaskEntity task;

        @BeforeEach
        void setUp() {
                projectRequest = new ProjectRequest(
                                "Warehouse Expansion",
                                "PRJ-001",
                                "Expand warehouse capacity",
                                LocalDate.of(2024, 1, 1),
                                LocalDate.of(2024, 12, 31),
                                "ACTIVE",
                                java.math.BigDecimal.valueOf(100000));

                projectResponse = ProjectResponse.builder()
                                .id(1L)
                                .code("PRJ-001")
                                .name("Warehouse Expansion")
                                .description("Expand warehouse capacity")
                                .startDate(LocalDate.of(2024, 1, 1))
                                .endDate(LocalDate.of(2024, 12, 31))
                                .active(true)
                                .build();

                task = new ProjectTaskEntity();
                task.setId(1L);
                task.setName("Foundation Work");
                task.setDescription("Prepare foundation for new section");
        }

        @Test
        void create_ShouldReturnSavedProject() throws Exception {
                // Given
                when(projectService.create(any(ProjectRequest.class))).thenReturn(projectResponse);

                // When/Then
                mockMvc.perform(post("/api/projects")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(projectRequest)))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$.id", is(1)))
                                .andExpect(jsonPath("$.code", is("PRJ-001")))
                                .andExpect(jsonPath("$.name", is("Warehouse Expansion")))
                                .andExpect(jsonPath("$.active", is(true)));

                verify(projectService, times(1)).create(any(ProjectRequest.class));
        }

        @Test
        void listAll_ShouldReturnActiveProjects() throws Exception {
                // Given
                ProjectResponse project2 = ProjectResponse.builder()
                                .id(2L)
                                .code("PRJ-002")
                                .name("Inventory System Upgrade")
                                .active(true)
                                .build();

                List<ProjectResponse> projects = Arrays.asList(projectResponse, project2);
                when(projectService.listAll()).thenReturn(projects);

                // When/Then
                mockMvc.perform(get("/api/projects"))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$", hasSize(2)))
                                .andExpect(jsonPath("$[0].id", is(1)))
                                .andExpect(jsonPath("$[0].code", is("PRJ-001")))
                                .andExpect(jsonPath("$[1].id", is(2)))
                                .andExpect(jsonPath("$[1].code", is("PRJ-002")));

                verify(projectService, times(1)).listAll();
        }

        @Test
        void getById_ShouldReturnProject() throws Exception {
                // Given
                when(projectService.getById(eq(1L))).thenReturn(projectResponse);

                // When/Then
                mockMvc.perform(get("/api/projects/{id}", 1L))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$.id", is(1)))
                                .andExpect(jsonPath("$.code", is("PRJ-001")))
                                .andExpect(jsonPath("$.name", is("Warehouse Expansion")));

                verify(projectService, times(1)).getById(eq(1L));
        }

        @Test
        void getById_WithInvalidId_ShouldReturnError() throws Exception {
                // Given
                when(projectService.getById(eq(999L))).thenThrow(new RuntimeException("Project not found"));

                // When/Then
                mockMvc.perform(get("/api/projects/{id}", 999L))
                                .andExpect(status().is5xxServerError());

                verify(projectService, times(1)).getById(eq(999L));
        }

        @Test
        void getProjectTasks_ShouldReturnTaskList() throws Exception {
                // Given
                ProjectTaskEntity task2 = new ProjectTaskEntity();
                task2.setId(2L);
                task2.setName("Structural Work");

                List<ProjectTaskEntity> tasks = Arrays.asList(task, task2);
                when(projectService.getProjectTasks(eq(1L))).thenReturn(tasks);

                // When/Then
                mockMvc.perform(get("/api/projects/{id}/tasks", 1L))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$", hasSize(2)))
                                .andExpect(jsonPath("$[0].id", is(1)))
                                .andExpect(jsonPath("$[0].name", is("Foundation Work")))
                                .andExpect(jsonPath("$[1].id", is(2)))
                                .andExpect(jsonPath("$[1].name", is("Structural Work")));

                verify(projectService, times(1)).getProjectTasks(eq(1L));
        }

        @Test
        void createTask_ShouldReturnSavedTask() throws Exception {
                // Given
                when(projectService.createProjectTask(eq(1L), any(ProjectTaskEntity.class))).thenReturn(task);

                // When/Then
                mockMvc.perform(post("/api/projects/{id}/tasks", 1L)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(task)))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$.id", is(1)))
                                .andExpect(jsonPath("$.name", is("Foundation Work")));

                verify(projectService, times(1)).createProjectTask(eq(1L), any(ProjectTaskEntity.class));
        }

        @Test
        void createTask_WithInvalidProjectId_ShouldReturnError() throws Exception {
                // Given
                when(projectService.createProjectTask(eq(999L), any(ProjectTaskEntity.class)))
                                .thenThrow(new RuntimeException("Project not found"));

                // When/Then
                mockMvc.perform(post("/api/projects/{id}/tasks", 999L)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(task)))
                                .andExpect(status().is5xxServerError());

                verify(projectService, times(1)).createProjectTask(eq(999L), any(ProjectTaskEntity.class));
        }
}
