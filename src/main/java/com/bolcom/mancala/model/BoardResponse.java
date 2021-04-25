package com.bolcom.mancala.model;

import lombok.*;

import java.util.List;

@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@EqualsAndHashCode
@ToString
public class BoardResponse {
    private List<Integer> stones;
    private Player turn;
    private Player winner;
    private boolean gameOver;
}
