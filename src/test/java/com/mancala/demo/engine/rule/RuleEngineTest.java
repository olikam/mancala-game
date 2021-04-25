package com.mancala.demo.engine.rule;

import com.mancala.demo.engine.Board;
import com.mancala.demo.model.Pit;
import com.mancala.demo.model.Player;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.List;

import static org.mockito.Mockito.verify;

@SpringBootTest
class RuleEngineTest {

    private static Board board;

    @MockBean
    private TakeOpponentStonesRule takeOpponentStonesRule;

    @MockBean
    private ExtraTurnRule extraTurnRule;

    @Autowired
    private RuleEngine sut;

    @BeforeEach
    void setUp() {
        board = Board.createInitialBoard();
        board.setTurn(Player.PLAYER_1);
        Pit head = board.getHead();
        Pit pit = head;
        List<Integer> stones = List.of(1, 6, 1, 12, 2, 10, 2, 7, 1, 8, 7, 7, 7, 1);
        int i = 0;
        do {
            pit.setStoneCount(stones.get(i++));
            pit = pit.getNext();
        } while (pit != head);
    }

    @Test
    void apply() {
        // given:
        Pit lastPitLanded = board.getBigPit(Player.PLAYER_1);
        // when:
        sut.apply(lastPitLanded);
        // then:
        verify(takeOpponentStonesRule).apply(lastPitLanded);
        verify(extraTurnRule).apply(lastPitLanded);
    }

}