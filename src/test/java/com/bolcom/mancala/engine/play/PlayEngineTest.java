package com.bolcom.mancala.engine.play;

import com.bolcom.mancala.engine.Board;
import com.bolcom.mancala.model.BoardResponse;
import com.bolcom.mancala.model.Pit;
import com.bolcom.mancala.model.Player;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class PlayEngineTest {

    private static Board board;

    @InjectMocks
    private PlayEngine sut;

    @BeforeAll
    static void setUp() {
        board = Board.createInitialBoard();
    }

    @Test
    @Order(1)
    void sow_happy_path() {
        // given:
        Player player = Player.PLAYER_1;
        int pitId = 1;
        Pit starterPit = board.getPit(player, pitId);
        List<Integer> lastStateStones = List.of(0, 7, 7, 7, 7, 7, 1, 6, 6, 6, 6, 6, 6, 0);
        int expectedLastPitId = 7;
        // when:
        Pit actualLastPit = sut.sow(starterPit);
        // then:
        BoardResponse actualBoardResponse = board.getBoardResponse();
        assertThat(actualBoardResponse.getStones()).isEqualTo(lastStateStones);
        assertThat(actualLastPit.getId()).isEqualTo(expectedLastPitId);
        assertThat(actualLastPit.getOwner()).isEqualTo(player);
    }

    @Test
    @Order(2)
    void sow_happy_path_two_consecutive_play() {
        // given:
        Player player = Player.PLAYER_2;
        int pitId = 2;
        Pit starterPit = board.getPit(player, pitId);
        List<Integer> lastStateStones = List.of(1, 7, 7, 7, 7, 7, 1, 6, 0, 7, 7, 7, 7, 1);
        int expectedLastPitId = 1;
        // when:
        Pit actualLastPit = sut.sow(starterPit);
        // then:
        BoardResponse actualBoardResponse = board.getBoardResponse();
        assertThat(actualBoardResponse.getStones()).isEqualTo(lastStateStones);
        assertThat(actualLastPit.getId()).isEqualTo(expectedLastPitId);
        assertThat(actualLastPit.getOwner()).isEqualTo(player.other());
    }

    @Test
    @Order(3)
    void sow_happy_path_three_consecutive_play() {
        // given:
        Player player = Player.PLAYER_1;
        int pitId = 3;
        Pit starterPit = board.getPit(player, pitId);
        List<Integer> lastStateStones = List.of(1, 7, 0, 8, 8, 8, 2, 7, 1, 8, 7, 7, 7, 1);
        int expectedLastPitId = 3;
        // when:
        Pit actualLastPit = sut.sow(starterPit);
        // then:
        BoardResponse actualBoardResponse = board.getBoardResponse();
        assertThat(actualBoardResponse.getStones()).isEqualTo(lastStateStones);
        assertThat(actualLastPit.getId()).isEqualTo(expectedLastPitId);
        assertThat(actualLastPit.getOwner()).isEqualTo(player.other());
    }

    @Test
    void playGame_end_game() {
        // given:
        Board board = createWinningBoard();
        Pit starterPit = board.getPit(Player.PLAYER_1, 6);
        // when:
        sut.sow(starterPit);
        // then:
        assertThat(board.getBoardResponse().isGameOver()).isEqualTo(true);
    }

    @Test
    void playGame_end_game_draw() {
        // given:
        Board board = createDrawBoard();
        Pit starterPit = board.getPit(Player.PLAYER_1, 6);
        // when:
        sut.sow(starterPit);
        // then:
        assertThat(board.getBoardResponse().isGameOver()).isEqualTo(true);
        assertThat(board.getBoardResponse().getWinner()).isNull();
    }

    private Board createWinningBoard() {
        Board board = Board.createInitialBoard();
        Pit pit = board.getHead();
        List<Integer> stonesInOrder = List.of(0, 0, 0, 0, 0, 1, 32, 0, 8, 0, 0, 0, 7, 24);
        for (Integer stoneCount : stonesInOrder) {
            pit.setStoneCount(stoneCount);
            pit = pit.getNext();
        }
        return board;
    }

    private Board createDrawBoard() {
        Board board = Board.createInitialBoard();
        board.setTurn(Player.PLAYER_2);
        Pit pit = board.getHead();
        List<Integer> stonesInOrder = List.of(0, 0, 0, 0, 0, 1, 35, 1, 0, 0, 0, 0, 0, 35);
        for (Integer stoneCount : stonesInOrder) {
            pit.setStoneCount(stoneCount);
            pit = pit.getNext();
        }
        return board;
    }
}