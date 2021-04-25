package com.bolcom.mancala.engine;

import com.bolcom.mancala.model.BoardResponse;
import com.bolcom.mancala.model.Pit;
import com.bolcom.mancala.model.PitType;
import com.bolcom.mancala.model.Player;
import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

import static com.bolcom.mancala.constants.Constants.*;

@Getter
@EqualsAndHashCode
public class Board implements Serializable {
    private Pit head;
    private Pit tail;
    private Player turn;

    private Board() {
        this.turn = Player.PLAYER_1;
    }

    /**
     * Initializes a board to start the game.
     *
     * @return {@link Board}
     */
    public static Board createInitialBoard() {
        Board board = new Board();
        for (Player player : Player.values()) {
            IntStream.range(1, SMALL_PIT_COUNT + 1).forEach(i -> board.addPit(createSmallPit(i, player)));
            board.addPit(createBigPit(player));
        }
        return board;
    }

    private static Pit createBigPit(Player player) {
        return Pit.builder()
                .id(BIG_PIT_ID)
                .type(PitType.BIG)
                .owner(player)
                .stoneCount(INITIAL_BIG_PIT_STONE_COUNT)
                .build();
    }

    private static Pit createSmallPit(int id, Player player) {
        return Pit.builder()
                .id(id)
                .type(PitType.SMALL)
                .owner(player)
                .stoneCount(INITIAL_SMALL_PIT_STONE_COUNT)
                .build();
    }

    public Pit getPit(Player player, int pitId) {
        Pit pit = head;
        do {
            if (pit.getOwner() == player && pit.getId() == pitId) {
                return pit;
            }
            pit = pit.getNext();
        } while (!pit.equals(head));

        //cannot happen
        throw new AssertionError(String.format("Unexpected error: Pit cannot be found for player:%s and pitId:%s", player, pitId));
    }

    public Pit getBigPit(Player player) {
        return getPit(player, BIG_PIT_ID);
    }

    public Pit getOppositePit(Pit pit) {
        int oppositePitId = getOppositePitId(pit.getId());
        Player owner = pit.getOwner();
        Pit currentPit = pit;
        do {
            if (owner == pit.getOwner().other() && pit.getId() == oppositePitId) {
                return pit;
            }
            pit = pit.getNext();
        } while (!currentPit.equals(pit));

        //cannot happen
        throw new AssertionError();
    }

    /**
     * @return Converts Board to it's simpler representation to be used on frontend.
     */
    public BoardResponse getBoardResponse() {
        return BoardResponse.builder()
                .gameOver(isGameOver())
                .winner(calculateWinner())
                .stones(getStonesInOrder())
                .turn(turn)
                .build();
    }

    private void addPit(Pit pit) {
        if (head == null) {
            head = pit;
        } else {
            tail.setNext(pit);
        }
        tail = pit;
        tail.setNext(head);
    }

    /**
     * @return An ArrayList representation of the stones to be used on frontend.
     */
    private List<Integer> getStonesInOrder() {
        List<Integer> stones = new ArrayList<>();
        Pit pit = head;
        do {
            stones.add(pit.getStoneCount());
            pit = pit.getNext();
        } while (!pit.equals(head));
        return stones;
    }

    /**
     * @return The player who wins or null if there is no winner
     */
    private Player calculateWinner() {
        Player winner = null;
        if (isGameOver()) {
            emptySmallPits();
            int player1TotalScore = getBigPit(Player.PLAYER_1).getStoneCount();
            int player2TotalScore = getBigPit(Player.PLAYER_2).getStoneCount();
            if (player1TotalScore > player2TotalScore) {
                winner = Player.PLAYER_1;
            } else if (player2TotalScore > player1TotalScore) {
                winner = Player.PLAYER_2;
            }
        }
        return winner;
    }

    private boolean isGameOver() {
        return getSmallPitsTotalCount(Player.PLAYER_1) == 0 || getSmallPitsTotalCount(Player.PLAYER_2) == 0;
    }

    private void emptySmallPits() {
        Pit pit = head;
        do {
            if (pit.getType() == PitType.SMALL) {
                int removedStones = pit.removeAllStones();
                Player owner = pit.getOwner();
                getBigPit(owner).addStone(removedStones);
            }
            pit = pit.getNext();
        } while (pit != head);
    }

    private int getSmallPitsTotalCount(Player player) {
        int totalCount = 0;
        Pit pit = head;
        do {
            if (pit.getOwner() == player && pit.getType() == PitType.SMALL) {
                totalCount += pit.getStoneCount();
            }
            pit = pit.getNext();
        } while (!pit.equals(head));
        return totalCount;
    }

    private int getOppositePitId(int pitId) {
        return BIG_PIT_ID - pitId;
    }

    public void setTurn(Player turn) {
        this.turn = turn;
    }

    public void switchTurn() {
        this.turn = turn.other();
    }
}
