package com.example.multi_tanent.warehouse.controller;

import com.example.multi_tanent.warehouse.entity.ProjectEntity;
import com.example.multi_tanent.warehouse.entity.ProjectTaskEntity;
import com.example.multi_tanent.warehouse.repository.ProjectRepository;
import com.example.multi_tanent.warehouse.repository.ProjectTaskRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ProjectController.class)
@AutoConfigureMockMvc(addFilters = false)
class ProjectControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private ProjectRepository projectRepository;

    @MockitoBean
    private ProjectTaskRepository projectTaskRepository;

    private ProjectEntity project;
    private ProjectTaskEntity task;

    @BeforeEach
    void setUp() {
        project = new ProjectEntity();
        project.setId(1L);
        project.setCode("PRJ-001");
        project.setName("Warehouse Expansion");
        project.setDescription("Expand warehouse capacity");
        project.setStartDate(LocalDate.of(2024, 1, 1));
        project.setEndDate(LocalDate.of(2024, 12, 31));
        project.setActive(true);

        task = new ProjectTaskEntity();
        task.setId(1L);
        task.setName("Foundation Work");
        task.setDescription("Prepare foundation for new section");
        task.setProject(project);
    }

    @Test
    void create_ShouldReturnSavedProject() throws Exception {
        // Given
        when(projectRepository.save(any(ProjectEntity.class))).thenReturn(project);

        // When/Then
        mockMvc.perform(post("/api/projects")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(project)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.code", is("PRJ-001")))
                .andExpect(jsonPath("$.name", is("Warehouse Expansion")))
                .andExpect(jsonPath("$.active", is(true)));

        verify(projectRepository, times(1)).save(any(ProjectEntity.class));
    }

    @Test
    void listAll_ShouldReturnActiveProjects() throws Exception {
        // Given
        ProjectEntity project2 = new ProjectEntity();
        project2.setId(2L);
        project2.setCode("PRJ-002");
        project2.setName("Inventory System Upgrade");
        project2.setActive(true);

        List<ProjectEntity> projects = Arrays.asList(project, project2);
        when(projectRepository.findByActiveTrue()).thenReturn(projects);

        // When/Then
        mockMvc.perform(get("/api/projects"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].id", is(1)))
                .andExpect(jsonPath("$[0].code", is("PRJ-001")))
                .andExpect(jsonPath("$[1].id", is(2)))
                .andExpect(jsonPath("$[1].code", is("PRJ-002")));

        verify(projectRepository, times(1)).findByActiveTrue();
    }

    @Test
    void getById_ShouldReturnProject() throws Exception {
        // Given
        when(projectRepository.findById(eq(1L))).thenReturn(Optional.of(project));

        // When/Then
        mockMvc.perform(get("/api/projects/{id}", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.code", is("PRJ-001")))
                .andExpect(jsonPath("$.name", is("Warehouse Expansion")));

        verify(projectRepository, times(1)).findById(eq(1L));
    }

    @Test
    void getById_WithInvalidId_ShouldReturnError() throws Exception {
        // Given
        when(projectRepository.findById(eq(999L))).thenReturn(Optional.empty());

        // When/Then
        mockMvc.perform(get("/api/projects/{id}", 999L))
                .andExpect(status().is5xxServerError());

        verify(projectRepository, times(1)).findById(eq(999L));
    }

    @Test
    void getProjectTasks_ShouldReturnTaskList() throws Exception {
        // Given
        ProjectTaskEntity task2 = new ProjectTaskEntity();
        task2.setId(2L);
        task2.setName("Structural Work");
        task2.setProject(project);

        List<ProjectTaskEntity> tasks = Arrays.asList(task, task2);
        when(projectTaskRepository.findByProjectId(eq(1L))).thenReturn(tasks);

        // When/Then
        mockMvc.perform(get("/api/projects/{id}/tasks", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].id", is(1)))
                .andExpect(jsonPath("$[0].name", is("Foundation Work")))
                .andExpect(jsonPath("$[1].id", is(2)))
                .andExpect(jsonPath("$[1].name", is("Structural Work")));

        verify(projectTaskRepository, times(1)).findByProjectId(eq(1L));
    }

    @Test
    void createTask_ShouldReturnSavedTask() throws Exception {
        // Given
        when(projectRepository.findById(eq(1L))).thenReturn(Optional.of(project));
        when(projectTaskRepository.save(any(ProjectTaskEntity.class))).thenReturn(task);

        // When/Then
        mockMvc.perform(post("/api/projects/{id}/tasks", 1L)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(task)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.name", is("Foundation Work")));

        verify(projectRepository, times(1)).findById(eq(1L));
        verify(projectTaskRepository, times(1)).save(any(ProjectTaskEntity.class));
    }

    @Test
    void createTask_WithInvalidProjectId_ShouldReturnError() throws Exception {
        // Given
        when(projectRepository.findById(eq(999L))).thenReturn(Optional.empty());

        // When/Then
        mockMvc.perform(post("/api/projects/{id}/tasks", 999L)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(task)))
                .andExpect(status().is5xxServerError());

        verify(projectRepository, times(1)).findById(eq(999L));
        verify(projectTaskRepository, never()).save(any(ProjectTaskEntity.class));
    }
}
