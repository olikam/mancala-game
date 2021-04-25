package com.bolcom.mancala.service;

import com.bolcom.mancala.engine.Board;
import com.bolcom.mancala.engine.play.PlayEngine;
import com.bolcom.mancala.engine.rule.RuleEngine;
import com.bolcom.mancala.exception.BoardNotSetupException;
import com.bolcom.mancala.model.BoardResponse;
import com.bolcom.mancala.model.Pit;
import com.bolcom.mancala.model.Player;
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