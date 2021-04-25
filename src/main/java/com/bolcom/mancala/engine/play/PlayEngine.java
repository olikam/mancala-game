package com.bolcom.mancala.engine.play;

import com.bolcom.mancala.model.Pit;
import com.bolcom.mancala.model.PitType;
import com.bolcom.mancala.model.Player;
import org.springframework.stereotype.Component;

@Component
public class PlayEngine {

    /**
     * Sows the stones on to the right, one in each of the following pits.
     *
     * @param starterPit The node whose stones will be removed from.
     * @return The node which the last stone landed in.
     */
    public Pit sow(Pit starterPit) {
        int stoneCount = starterPit.removeAllStones();
        Player owner = starterPit.getOwner();
        Pit pit = starterPit;
        while (stoneCount > 0) {
            pit = pit.getNext();
            if (isOpponentBigPit(owner, pit)) {
                continue;
            }
            pit.addStone(1);
            stoneCount--;
        }
        return pit;
    }

    private boolean isOpponentBigPit(Player owner, Pit pit) {
        return owner.other() == pit.getOwner() && pit.getType() == PitType.BIG;
    }
}
