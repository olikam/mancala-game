package com.mancala.demo.service;

import com.mancala.demo.exception.BoardNotSetupException;
import com.mancala.demo.model.BoardResponse;
import com.mancala.demo.model.Player;

public interface PlayService {
    BoardResponse createNewGame();

    BoardResponse playGame(Player player, int pitId) throws BoardNotSetupException;
}
