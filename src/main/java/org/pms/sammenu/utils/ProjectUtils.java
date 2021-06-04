package org.pms.sammenu.utils;

import org.pms.sammenu.domain.Project;
import org.pms.sammenu.exceptions.ResourceNotFoundException;
import org.pms.sammenu.repositories.ProjectRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class ProjectUtils {

    private ProjectRepository projectRepository;

    @Autowired
    public ProjectUtils(ProjectRepository projectRepository) {
        this.projectRepository = projectRepository;
    }

    public Project fetchProject(Long projectId){

        Optional<Project> project = projectRepository.findById(projectId);

        if (!project.isPresent())
            throw new ResourceNotFoundException("Project not with id: " + projectId);

        return project.get();
    }
}
