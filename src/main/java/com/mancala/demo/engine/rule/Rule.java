package com.mancala.demo.engine.rule;

import com.mancala.demo.model.Pit;

public interface Rule {
    void apply(Pit lastPit);
}
