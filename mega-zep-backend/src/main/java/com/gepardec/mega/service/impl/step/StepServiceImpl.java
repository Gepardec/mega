package com.gepardec.mega.service.impl.step;

import com.gepardec.mega.db.repository.StepRepository;
import com.gepardec.mega.domain.model.Step;
import com.gepardec.mega.service.api.step.StepService;

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
                        .role(s.getRole() != null ? s.getRole().name() : null)
                        .build())
                .collect(Collectors.toList());
    }
}
