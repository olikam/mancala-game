package com.bolcom.mancala.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Player {
    PLAYER_1, PLAYER_2;

    public Player other() {
        return this == PLAYER_1 ? PLAYER_2 : PLAYER_1;
    }
}
