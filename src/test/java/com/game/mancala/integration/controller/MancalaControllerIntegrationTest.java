package com.game.mancala.integration.controller;

import com.game.mancala.domain.dto.requests.StartRequestDTO;
import com.game.mancala.utils.MancalaMockData;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.nio.charset.StandardCharsets;

import static com.game.mancala.utils.MancalaMockData.asJsonString;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class MancalaControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    final MediaType MEDIA_TYPE_JSON_UTF8 = new MediaType("application", "json", StandardCharsets.UTF_8);
    final MancalaMockData utils = new MancalaMockData();

    @Test
    public void startGame() throws Exception {
        final StartRequestDTO startRequestDTO = utils.startRequestDTO;
        mockMvc.perform(
                        MockMvcRequestBuilders.post("/mancala/start")
                                .content(asJsonString(startRequestDTO))
                                .contentType(MEDIA_TYPE_JSON_UTF8)
                                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    void makeMove() throws Exception {
        mockMvc.perform(
                        MockMvcRequestBuilders.post("/mancala/move")
                                .content(asJsonString(utils.moveRequestDTO))
                                .contentType(MEDIA_TYPE_JSON_UTF8)
                                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(asJsonString(utils.mancalaResponseDTO)))
                .andDo(print());
    }

    @Test
    void endGame() throws Exception {
        mockMvc.perform(
                        MockMvcRequestBuilders.post("/mancala/end")
                                .content(asJsonString(utils.endGameRequestDTO))
                                .contentType(MEDIA_TYPE_JSON_UTF8)
                                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(asJsonString(utils.endGameResponseDTO)))
                .andDo(print());
    }
}