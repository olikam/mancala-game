package com.mancala.demo.engine.rule;

import com.mancala.demo.model.Pit;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class RuleEngine {

    private final List<Rule> rules;

    public void apply(Pit lastPit) {
        rules.forEach(rule -> rule.apply(lastPit));
    }
}
