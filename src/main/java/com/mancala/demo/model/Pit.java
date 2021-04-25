package com.mancala.demo.model;

import lombok.*;

import java.io.Serializable;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@EqualsAndHashCode(of = {"id", "owner"})
public class Pit implements Serializable {
    private int id;
    private int stoneCount;
    private Player owner;
    private PitType type;
    private Pit next;

    public void addStone(int count) {
        stoneCount += count;
    }

    public int removeAllStones() {
        int stoneCountToBeRemoved = stoneCount;
        stoneCount = 0;
        return stoneCountToBeRemoved;
    }
}