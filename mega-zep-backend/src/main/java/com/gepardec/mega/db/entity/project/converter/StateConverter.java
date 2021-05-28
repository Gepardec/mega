package com.gepardec.mega.db.entity.project.converter;

import com.gepardec.mega.db.entity.project.ProjectState;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import java.util.stream.Stream;

@Converter(autoApply = true)
public class StateConverter implements AttributeConverter<ProjectState, Integer> {

    @Override
    public Integer convertToDatabaseColumn(ProjectState state) {
        if (state == null) {
            return null;
        }
        return state.getStateId();
    }

    @Override
    public ProjectState convertToEntityAttribute(Integer stateId) {
        if (stateId == null) {
            return null;
        }

        return Stream.of(ProjectState.values())
                .filter(s -> s.getStateId() == stateId)
                .findFirst()
                .orElseThrow(IllegalArgumentException::new);
    }
}
