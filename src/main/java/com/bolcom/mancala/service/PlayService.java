package com.bolcom.mancala.service;

import com.bolcom.mancala.exception.BoardNotSetupException;
import com.bolcom.mancala.model.BoardResponse;
import com.bolcom.mancala.model.Player;

public interface PlayService {
    BoardResponse createNewGame();

    BoardResponse playGame(Player player, int pitId) throws BoardNotSetupException;
}
