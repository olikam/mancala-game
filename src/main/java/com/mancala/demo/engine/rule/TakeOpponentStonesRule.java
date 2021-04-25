package com.mancala.demo.engine.rule;

import com.mancala.demo.engine.Board;
import com.mancala.demo.model.Pit;
import com.mancala.demo.model.PitType;
import lombok.RequiredArgsConstructor;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpSession;

@Component
@RequiredArgsConstructor
@Order(1)
public class TakeOpponentStonesRule implements Rule {

    private final HttpSession httpSession;

    @Override
    public void apply(Pit lastPit) {
        Board board = (Board) httpSession.getAttribute("board");
        if (lastPit.getOwner() == board.getTurn()
                && lastPit.getStoneCount() == 1
                && lastPit.getType() == PitType.SMALL) {
            int removedStoneCount = board.getOppositePit(lastPit).removeAllStones() + lastPit.removeAllStones();
            board.getBigPit(board.getTurn()).addStone(removedStoneCount);
        }
    }
}
