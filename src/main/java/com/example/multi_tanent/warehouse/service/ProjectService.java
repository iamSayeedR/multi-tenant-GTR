package com.example.multi_tanent.warehouse.service;

import com.example.multi_tanent.warehouse.entity.ProjectEntity;
import com.example.multi_tanent.warehouse.entity.ProjectTaskEntity;
import com.example.multi_tanent.warehouse.mapper.ProjectMapper;
import com.example.multi_tanent.warehouse.model.ProjectRequest;
import com.example.multi_tanent.warehouse.model.ProjectResponse;
import com.example.multi_tanent.warehouse.repository.ProjectRepository;
import com.example.multi_tanent.warehouse.repository.ProjectTaskRepository;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ProjectService {

    private final ProjectRepository projectRepository;
    private final ProjectTaskRepository projectTaskRepository;
    private final ProjectMapper projectMapper;

    public ProjectService(ProjectRepository projectRepository,
            ProjectTaskRepository projectTaskRepository,
            ProjectMapper projectMapper) {
        this.projectRepository = projectRepository;
        this.projectTaskRepository = projectTaskRepository;
        this.projectMapper = projectMapper;
    }

    @Transactional
    public ProjectResponse create(ProjectRequest request) {
        ProjectEntity project = projectMapper.toEntity(request);
        ProjectEntity saved = projectRepository.save(project);
        return projectMapper.toResponse(saved);
    }

    @Transactional(readOnly = true)
    public List<ProjectResponse> listAll() {
        return projectRepository.findByActiveTrue()
                .stream()
                .map(projectMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public ProjectResponse getById(Long id) {
        ProjectEntity project = projectRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Project not found with id: " + id));
        return projectMapper.toResponse(project);
    }

    @Transactional(readOnly = true)
    public List<ProjectTaskEntity> getProjectTasks(Long projectId) {
        return projectTaskRepository.findByProjectId(projectId);
    }

    @Transactional
    public ProjectTaskEntity createProjectTask(Long projectId, ProjectTaskEntity task) {
        ProjectEntity project = projectRepository.findById(projectId)
                .orElseThrow(() -> new RuntimeException("Project not found with id: " + projectId));
        task.setProject(project);
        return projectTaskRepository.save(task);
    }
}
