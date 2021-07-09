package com.gepardec.mega.db.entity.project.converter;

import com.gepardec.mega.db.entity.project.ProjectStep;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import java.util.stream.Stream;

@Converter(autoApply = true)
public class ProjectStepConverter implements AttributeConverter<ProjectStep, Integer> {

    @Override
    public Integer convertToDatabaseColumn(ProjectStep projectStep) {
        if (projectStep == null) {
            return null;
        }
        return projectStep.getId();
    }

    @Override
    public ProjectStep convertToEntityAttribute(Integer projectStepId) {
        if (projectStepId == null) {
            return null;
        }

        return Stream.of(ProjectStep.values())
                .filter(p -> p.getId() == projectStepId)
                .findFirst()
                .orElseThrow(IllegalArgumentException::new);
    }
}
