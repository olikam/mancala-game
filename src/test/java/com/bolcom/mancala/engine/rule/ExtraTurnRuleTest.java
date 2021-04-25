package com.bolcom.mancala.engine.rule;

import com.bolcom.mancala.engine.Board;
import com.bolcom.mancala.model.Pit;
import com.bolcom.mancala.model.Player;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.servlet.http.HttpSession;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ExtraTurnRuleTest {

    private Board board;

    private final Player turn = Player.PLAYER_1;

    @Mock
    private HttpSession httpSession;

    @InjectMocks
    private ExtraTurnRule sut;

    @BeforeEach
    void setUp() {
        board = Board.createInitialBoard();
        board.setTurn(turn);
        Pit head = board.getHead();
        Pit pit = head;
        List<Integer> stones = List.of(1, 6, 1, 12, 2, 10, 2, 7, 1, 8, 7, 7, 7, 1);
        int i = 0;
        do {
            pit.setStoneCount(stones.get(i++));
            pit = pit.getNext();
        } while (pit != head);
        when(httpSession.getAttribute("board")).thenReturn(board);
    }

    @Test
    void apply_happy_path() {
        // given:
        Player expectedTurn = Player.PLAYER_1;
        Pit lastPitLanded = board.getBigPit(Player.PLAYER_1);
        // when:
        sut.apply(lastPitLanded);
        Player actualTurn = board.getTurn();
        // then:
        assertThat(actualTurn).isEqualTo(expectedTurn);
    }

}