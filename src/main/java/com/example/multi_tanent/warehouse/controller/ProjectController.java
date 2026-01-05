package com.example.multi_tanent.warehouse.controller;

import com.example.multi_tanent.warehouse.entity.ProjectEntity;
import com.example.multi_tanent.warehouse.entity.ProjectTaskEntity;
import com.example.multi_tanent.warehouse.repository.ProjectRepository;
import com.example.multi_tanent.warehouse.repository.ProjectTaskRepository;
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

    private final ProjectRepository projectRepository;
    private final ProjectTaskRepository projectTaskRepository;

    public ProjectController(ProjectRepository projectRepository, ProjectTaskRepository projectTaskRepository) {
        this.projectRepository = projectRepository;
        this.projectTaskRepository = projectTaskRepository;
    }

    @PostMapping
    @Operation(summary = "Create project", description = "Create a new project for cost tracking")
    public ResponseEntity<ProjectEntity> create(@RequestBody ProjectEntity project) {
        ProjectEntity saved = projectRepository.save(project);
        return ResponseEntity.ok(saved);
    }

    @GetMapping
    @Operation(summary = "List all projects", description = "Get all active projects")
    public ResponseEntity<List<ProjectEntity>> listAll() {
        List<ProjectEntity> projects = projectRepository.findByActiveTrue();
        return ResponseEntity.ok(projects);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get project by ID", description = "Retrieve a specific project")
    public ResponseEntity<ProjectEntity> getById(
            @Parameter(description = "Project ID") @PathVariable Long id) {
        ProjectEntity project = projectRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Project not found"));
        return ResponseEntity.ok(project);
    }

    @GetMapping("/{id}/tasks")
    @Operation(summary = "Get project tasks", description = "Get all tasks for a specific project")
    public ResponseEntity<List<ProjectTaskEntity>> getProjectTasks(
            @Parameter(description = "Project ID") @PathVariable Long id) {
        List<ProjectTaskEntity> tasks = projectTaskRepository.findByProjectId(id);
        return ResponseEntity.ok(tasks);
    }

    @PostMapping("/{id}/tasks")
    @Operation(summary = "Create project task", description = "Create a new task under a project")
    public ResponseEntity<ProjectTaskEntity> createTask(
            @Parameter(description = "Project ID") @PathVariable Long id,
            @RequestBody ProjectTaskEntity task) {
        ProjectEntity project = projectRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Project not found"));
        task.setProject(project);
        ProjectTaskEntity saved = projectTaskRepository.save(task);
        return ResponseEntity.ok(saved);
    }
}
