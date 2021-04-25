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
