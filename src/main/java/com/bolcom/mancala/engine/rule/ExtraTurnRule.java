package com.bolcom.mancala.engine.rule;

import com.bolcom.mancala.engine.Board;
import com.bolcom.mancala.model.Pit;
import com.bolcom.mancala.model.PitType;
import lombok.RequiredArgsConstructor;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpSession;

@Component
@RequiredArgsConstructor
@Order(2)
public class ExtraTurnRule implements Rule {

    private final HttpSession httpSession;

    @Override
    public void apply(Pit lastPit) {
        Board board = (Board) httpSession.getAttribute("board");
        if (lastPit.getType() != PitType.BIG) {
            board.switchTurn();
        }
    }
}
