package com.mancala.demo.model;

import com.mancala.demo.constants.Constants;
import lombok.Data;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.NotNull;

@Data
public class PlayRequest {
    @NotNull(message = "Player cannot be null")
    private Player player;
    @Range(min = 1, max = Constants.SMALL_PIT_COUNT, message = "pitId must be at least 1 and at most " + Constants.SMALL_PIT_COUNT)
    private int pitId;
}
