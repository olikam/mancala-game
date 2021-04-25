package com.bolcom.mancala.engine.rule;

import com.bolcom.mancala.model.Pit;

public interface Rule {
    void apply(Pit lastPit);
}
