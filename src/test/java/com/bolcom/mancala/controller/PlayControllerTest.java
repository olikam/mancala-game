package com.bolcom.mancala.controller;

import com.bolcom.mancala.engine.Board;
import com.bolcom.mancala.exception.BoardNotSetupException;
import com.bolcom.mancala.exception.GlobalExceptionHandler;
import com.bolcom.mancala.model.BoardResponse;
import com.bolcom.mancala.model.PlayRequest;
import com.bolcom.mancala.model.Player;
import com.bolcom.mancala.service.PlayService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class PlayControllerTest {

    private MockMvc mockMvc;

    @Mock
    private PlayService playService;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @InjectMocks
    private PlayController sut;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(sut)
                .setControllerAdvice(new GlobalExceptionHandler(new MockHttpSession()))
                .build();
    }

    @Test
    @SneakyThrows
    void newGame_happy_path() {
        // given:
        BoardResponse expected = Board.createInitialBoard().getBoardResponse();
        when(playService.createNewGame()).thenReturn(expected);

        // when:
        ResultActions perform = mockMvc.perform(post("/mancala/new-game"));

        // then:
        MvcResult mvcResult = perform.andExpect(status().isCreated()).andReturn();
        BoardResponse actual = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), BoardResponse.class);
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    @SneakyThrows
    void newGame_should_return_500_when_playService_throws_exception() {
        // given:
        doThrow(RuntimeException.class).when(playService).createNewGame();

        // when:
        ResultActions perform = mockMvc.perform(post("/mancala/new-game"));

        // then:
        perform.andExpect(status().isInternalServerError());
    }

    @Test
    @SneakyThrows
    void playGame_happy_path() {
        // given:
        BoardResponse expected = BoardResponse.builder()
                .stones(List.of(0, 7, 7, 1, 7, 7, 7, 0))
                .turn(Player.PLAYER_2)
                .build();
        when(playService.playGame(any(Player.class), any(Integer.class))).thenReturn(expected);
        PlayRequest playRequest = new PlayRequest();
        playRequest.setPlayer(Player.PLAYER_1);
        playRequest.setPitId(1);

        // when:
        ResultActions perform = mockMvc.perform(post("/mancala/play")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(playRequest)));

        // then:
        MvcResult mvcResult = perform.andExpect(status().isOk()).andReturn();
        BoardResponse actual = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), BoardResponse.class);
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    @SneakyThrows
    void playGame_should_return_400_playService_throws_BoardNotSetupException() {
        // given:
        String exceptionMsg = "Please first setup a new board.";
        doThrow(new BoardNotSetupException(exceptionMsg)).when(playService).playGame(any(Player.class), any(Integer.class));
        PlayRequest playRequest = new PlayRequest();
        playRequest.setPlayer(Player.PLAYER_1);
        playRequest.setPitId(1);

        // when:
        ResultActions perform = mockMvc.perform(post("/mancala/play")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(playRequest)));

        // then:
        MvcResult mvcResult = perform.andExpect(status().isBadRequest()).andReturn();
        assertThat(mvcResult.getResponse().getContentAsString()).isEqualTo(exceptionMsg);
    }
}