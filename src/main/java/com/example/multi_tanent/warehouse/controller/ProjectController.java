package com.example.multi_tanent.warehouse.controller;

import com.example.multi_tanent.warehouse.entity.ProjectTaskEntity;
import com.example.multi_tanent.warehouse.model.ProjectRequest;
import com.example.multi_tanent.warehouse.model.ProjectResponse;
import com.example.multi_tanent.warehouse.service.ProjectService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController("whProjectController")
@RequestMapping("/api/projects")
@CrossOrigin(origins = "http://localhost:5173")
@Tag(name = "Project Management", description = "Manage projects and project tasks for cost tracking")
public class ProjectController {

    private final ProjectService projectService;

    public ProjectController(ProjectService projectService) {
        this.projectService = projectService;
    }

    @PostMapping
    @Operation(summary = "Create project", description = "Create a new project for cost tracking")
    public ResponseEntity<ProjectResponse> create(@RequestBody ProjectRequest request) {
        ProjectResponse response = projectService.create(request);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    @Operation(summary = "List all projects", description = "Get all active projects")
    public ResponseEntity<List<ProjectResponse>> listAll() {
        List<ProjectResponse> projects = projectService.listAll();
        return ResponseEntity.ok(projects);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get project by ID", description = "Retrieve a specific project")
    public ResponseEntity<ProjectResponse> getById(
            @Parameter(description = "Project ID") @PathVariable Long id) {
        ProjectResponse project = projectService.getById(id);
        return ResponseEntity.ok(project);
    }

    @GetMapping("/{id}/tasks")
    @Operation(summary = "Get project tasks", description = "Get all tasks for a specific project")
    public ResponseEntity<List<ProjectTaskEntity>> getProjectTasks(
            @Parameter(description = "Project ID") @PathVariable Long id) {
        List<ProjectTaskEntity> tasks = projectService.getProjectTasks(id);
        return ResponseEntity.ok(tasks);
    }

    @PostMapping("/{id}/tasks")
    @Operation(summary = "Create project task", description = "Create a new task under a project")
    public ResponseEntity<ProjectTaskEntity> createTask(
            @Parameter(description = "Project ID") @PathVariable Long id,
            @RequestBody ProjectTaskEntity task) {
        ProjectTaskEntity saved = projectService.createProjectTask(id, task);
        return ResponseEntity.ok(saved);
    }
}
