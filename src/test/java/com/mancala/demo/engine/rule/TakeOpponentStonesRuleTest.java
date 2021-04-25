package com.mancala.demo.engine.rule;

import com.mancala.demo.engine.Board;
import com.mancala.demo.model.Pit;
import com.mancala.demo.model.Player;
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
class TakeOpponentStonesRuleTest {

    private Board board;

    private final Player turn = Player.PLAYER_1;

    @Mock
    private HttpSession httpSession;

    @InjectMocks
    private TakeOpponentStonesRule sut;

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
        //given:
        int expectedOpponentRemainingStoneCount = 0;
        int expectedBigPitStoneCount = 10;
        Pit lastPitLanded = board.getPit(turn, 3);
        //when:
        sut.apply(lastPitLanded);
        int actualOpponentRemainingStoneCount = board.getOppositePit(lastPitLanded).getStoneCount();
        int actualBigPitStoneCount = board.getBigPit(turn).getStoneCount();
        Player actualTurn = board.getTurn();
        //then:
        assertThat(actualTurn).isEqualTo(turn);
        assertThat(actualOpponentRemainingStoneCount).isEqualTo(expectedOpponentRemainingStoneCount);
        assertThat(actualBigPitStoneCount).isEqualTo(expectedBigPitStoneCount);
    }

    @Test
    void apply_should_not_apply_the_rule_when_lastPitLanded_stoneCount_is_not_one() {
        //given:
        int expectedOpponentRemainingStoneCount = 8;
        int expectedBigPitStoneCount = 2;
        Pit lastPitLanded = board.getPit(turn, 4);
        //when:
        sut.apply(lastPitLanded);
        int actualOpponentRemainingStoneCount = board.getOppositePit(lastPitLanded).getStoneCount();
        int actualBigPitStoneCount = board.getBigPit(turn).getStoneCount();
        Player actualTurn = board.getTurn();
        //then:
        assertThat(actualTurn).isEqualTo(turn);
        assertThat(actualOpponentRemainingStoneCount).isEqualTo(expectedOpponentRemainingStoneCount);
        assertThat(actualBigPitStoneCount).isEqualTo(expectedBigPitStoneCount);
    }

    @Test
    void apply_should_not_apply_the_rule_when_lastPitLanded_belongs_to_opponent() {
        //given:
        int expectedOpponentRemainingStoneCount = 10;
        int expectedBigPitStoneCount = 2;
        Pit lastPitLanded = board.getPit(turn.other(), 1);
        //when:
        sut.apply(lastPitLanded);
        int actualOpponentRemainingStoneCount = board.getOppositePit(lastPitLanded).getStoneCount();
        int actualBigPitStoneCount = board.getBigPit(turn).getStoneCount();
        Player actualTurn = board.getTurn();
        //then:
        assertThat(actualTurn).isEqualTo(turn);
        assertThat(actualOpponentRemainingStoneCount).isEqualTo(expectedOpponentRemainingStoneCount);
        assertThat(actualBigPitStoneCount).isEqualTo(expectedBigPitStoneCount);
    }

    @Test
    void apply_should_not_apply_the_rule_when_lastPitLanded_is_a_big_pit() {
        //given:
        int expectedBigPitStoneCount = 2;
        Pit lastPitLanded = board.getPit(turn, 7);
        //when:
        sut.apply(lastPitLanded);
        int actualBigPitStoneCount = board.getBigPit(turn).getStoneCount();
        Player actualTurn = board.getTurn();
        //then:
        assertThat(actualTurn).isEqualTo(turn);
        assertThat(actualBigPitStoneCount).isEqualTo(expectedBigPitStoneCount);
    }

}