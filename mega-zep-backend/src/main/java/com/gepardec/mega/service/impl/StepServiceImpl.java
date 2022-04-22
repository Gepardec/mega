package com.gepardec.mega.service.impl;

import com.gepardec.mega.db.repository.StepRepository;
import com.gepardec.mega.domain.model.Step;
import com.gepardec.mega.service.api.StepService;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@ApplicationScoped
public class StepServiceImpl implements StepService {

    @Inject
    StepRepository stepRepository;

    @Override
    @Transactional(Transactional.TxType.SUPPORTS)
    public List<Step> getSteps() {
        return stepRepository.findAll().list()
                .stream()
                .map(s -> Step.builder()
                        .dbId(s.getId())
                        .name(s.getName())
                        .ordinal(s.getOrdinal())
                        .role(s.getRole())
                        .build())
                .collect(Collectors.toList());
    }
}
