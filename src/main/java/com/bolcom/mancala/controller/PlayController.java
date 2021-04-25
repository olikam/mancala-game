package com.bolcom.mancala.controller;

import com.bolcom.mancala.model.BoardResponse;
import com.bolcom.mancala.model.PlayRequest;
import com.bolcom.mancala.service.PlayService;
import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@Api(tags = "Mancala Controller")
@RestController
@RequestMapping("mancala")
@RequiredArgsConstructor
public class PlayController {

    private final PlayService playService;

    @PostMapping("new-game")
    public ResponseEntity<BoardResponse> newGame() {
        return new ResponseEntity<>(playService.createNewGame(), HttpStatus.CREATED);
    }

    @PostMapping("play")
    public ResponseEntity<BoardResponse> play(@Valid @RequestBody PlayRequest request) {
        return new ResponseEntity<>(playService.playGame(request.getPlayer(), request.getPitId()), HttpStatus.OK);
    }
}
