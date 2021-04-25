package com.mancala.demo.service;

import com.mancala.demo.engine.Board;
import com.mancala.demo.engine.play.PlayEngine;
import com.mancala.demo.engine.rule.RuleEngine;
import com.mancala.demo.exception.BoardNotSetupException;
import com.mancala.demo.model.BoardResponse;
import com.mancala.demo.model.Pit;
import com.mancala.demo.model.Player;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpSession;

import static com.mancala.demo.constants.Constants.BIG_PIT_ID;

@Service
@RequiredArgsConstructor
@Slf4j
public class PlayServiceImpl implements PlayService {

    private final HttpSession httpSession;
    private final RuleEngine ruleEngine;
    private final PlayEngine playEngine;

    /**
     * Creates a new board. Without creating a board using this method, the game cannot be played.
     *
     * @return Representation of the board for frontend.
     */
    @Override
    public BoardResponse createNewGame() {
        log.info("Setting up a new board. SessionId: {}", httpSession.getId());
        Board board = Board.createInitialBoard();
        log.info("Setting up a new board completed. SessionId: {}", httpSession.getId());
        httpSession.setAttribute("board", board);
        return board.getBoardResponse();
    }

    /**
     * Plays the game with respect to the parameters.
     *
     * @param player The player which is currently playing.
     * @param pitId  The id belonging to the pit which is being played by the player.
     * @return Representation of the board for frontend.
     * @throws BoardNotSetupException Throws this if the method is called without creating a board before.
     */
    @Override
    public BoardResponse playGame(Player player, int pitId) throws BoardNotSetupException {
        log.info("{} is playing with the pit {}. SessionId:{}", player, pitId, httpSession.getId());
        Board board = (Board) httpSession.getAttribute("board");
        if (httpSession.getAttribute("board") == null) {
            throw new BoardNotSetupException("Please first setup a new board.");
        }
        Pit currentPit = board.getPit(player, pitId);
        // If player plays a non-playable pit, do nothing.
        if (pitId == BIG_PIT_ID || currentPit.getStoneCount() == 0) {
            log.info("{} is playing with a non-playable pit {}. SessionId:{}", player, pitId, httpSession.getId());
            return board.getBoardResponse();
        }
        Pit lastPit = playEngine.sow(currentPit);
        httpSession.setAttribute("board", board);
        ruleEngine.apply(lastPit);
        BoardResponse boardResponse = board.getBoardResponse();
        log.info("Board is: {}", boardResponse);
        return boardResponse;
    }
}
