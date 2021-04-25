package com.mancala.demo.service;

import com.mancala.demo.engine.Board;
import com.mancala.demo.engine.play.PlayEngine;
import com.mancala.demo.engine.rule.RuleEngine;
import com.mancala.demo.exception.BoardNotSetupException;
import com.mancala.demo.model.BoardResponse;
import com.mancala.demo.model.Pit;
import com.mancala.demo.model.Player;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.function.Executable;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.servlet.http.HttpSession;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PlayServiceImplTest {

    @Mock
    private HttpSession httpSession;

    @Mock
    private RuleEngine ruleEngine;

    @Mock
    private PlayEngine playEngine;

    @InjectMocks
    private PlayServiceImpl sut;

    @Test
    void createNewGame_happy_path() {
        // given:
        BoardResponse expected = BoardResponse.builder()
                .turn(Player.PLAYER_1)
                .stones(List.of(6, 6, 6, 6, 6, 6, 0, 6, 6, 6, 6, 6, 6, 0))
                .build();
        // when:
        BoardResponse actual = sut.createNewGame();

        // then:
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void playGame_happy_path() {
        // given:
        Board board = Board.createInitialBoard();
        Player playingPlayer = Player.PLAYER_1;
        when(httpSession.getAttribute("board")).thenReturn(board);
        Pit lastPitLanded = Pit.builder().build();
        doNothing().when(ruleEngine).apply(lastPitLanded);
        when(playEngine.sow(any(Pit.class))).thenReturn(lastPitLanded);
        // when:
        BoardResponse actual = sut.playGame(playingPlayer, 2);
        // then:
        assertThat(actual).isEqualTo(board.getBoardResponse());
    }

    @Test
    void playGame_should_throw_BoardNotSetupException_if_a_board_was_not_created() {
        // given:
        Player player = Player.PLAYER_1;
        // when:
        Executable serviceCall = () -> sut.playGame(player, 1);
        // then:
        assertThrows(BoardNotSetupException.class, serviceCall);
    }
}