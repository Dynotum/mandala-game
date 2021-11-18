package com.game.malanca.service;

import com.game.malanca.domain.dto.requests.PlayerRequestDTO;
import com.game.malanca.domain.dto.requests.StartRequestDTO;
import com.game.malanca.domain.dto.responses.MancalaResponseDTO;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

import static com.game.malanca.domain.dto.responses.PlayerTypeResponse.PLAYER_ONE;


@ExtendWith(SpringExtension.class)
@SpringBootTest
class MancalaServiceIntegrationTest {

    private MancalaService mancalaService;

    @Autowired
    public MancalaServiceIntegrationTest(MancalaService mancalaService) {
        this.mancalaService = mancalaService;
    }

    @Test
    void startGame() {
        final StartRequestDTO startRequestDTO = StartRequestDTO.builder()
                .players(Arrays.asList(
                        PlayerRequestDTO.builder()
                                .name("Lalin Canallin")
                                .build(),
                        PlayerRequestDTO.builder()
                                .name("Carlos Chavelin")
                                .build()))
                .build();

        final MancalaResponseDTO mancalaResponseDTO = mancalaService.startGame(startRequestDTO);
        final int[] playerOnePits = mancalaResponseDTO.getPlayers().get(PLAYER_ONE.playerTypeValue).getBoard().getPits();
        final int[] playerTwoPits = mancalaResponseDTO.getPlayers().get(PLAYER_ONE.playerTypeValue).getBoard().getPits();

        assertArrayEquals(new int[]{6, 6, 6, 6, 6, 6}, playerOnePits);
        assertArrayEquals(new int[]{6, 6, 6, 6, 6, 6}, playerTwoPits);

    }

    @Test
    void makeMove() {
    }

    @Test
    void endGame() {
    }
}