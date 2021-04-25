package com.mancala.demo.controller;

import com.mancala.demo.service.PlayService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import springfox.documentation.annotations.ApiIgnore;

@ApiIgnore
@Controller
@RequiredArgsConstructor
public class HomeController {

    private final PlayService playService;

    @GetMapping("/")
    String home() {
        return "home";
    }

    @GetMapping("/board")
    String board() {
        playService.createNewGame();
        return "board";
    }
}
