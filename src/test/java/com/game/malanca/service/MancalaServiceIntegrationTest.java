package com.game.malanca.service;

import com.game.malanca.domain.dto.requests.MoveRequestDTO;
import com.game.malanca.domain.dto.requests.PlayerMoveRequestDTO;
import com.game.malanca.domain.dto.requests.PlayerRequestDTO;
import com.game.malanca.domain.dto.requests.StartRequestDTO;
import com.game.malanca.domain.dto.responses.BoardResponseDTO;
import com.game.malanca.domain.dto.responses.MancalaResponseDTO;
import com.game.malanca.domain.dto.responses.PlayerResponseDTO;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Arrays;
import java.util.List;

import static com.game.malanca.domain.dto.responses.PlayerTypeResponse.PLAYER_TWO;
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
                                .name("Lalin")
                                .build(),
                        PlayerRequestDTO.builder()
                                .name("Carlos")
                                .build()))
                .build();

        final MancalaResponseDTO mancalaResponseDTO = mancalaService.startGame(startRequestDTO);
        final PlayerResponseDTO playerOne = mancalaResponseDTO.getPlayers().get(PLAYER_ONE.playerTypeValue);
        final PlayerResponseDTO playerTwo = mancalaResponseDTO.getPlayers().get(PLAYER_TWO.playerTypeValue);

        final int[] playerOnePits = playerOne.getBoard().getPits();
        final int[] playerTwoPits = playerTwo.getBoard().getPits();

        assertFalse(mancalaResponseDTO.isGameEnded());

        assertArrayEquals(new int[]{6, 6, 6, 6, 6, 6}, playerOnePits);
        assertArrayEquals(new int[]{6, 6, 6, 6, 6, 6}, playerTwoPits);

        assertEquals(playerOne.getPlayerType(), PLAYER_ONE);
        assertEquals(playerTwo.getPlayerType(), PLAYER_TWO);

        assertEquals(playerOne.getName(), "Lalin");
        assertEquals(playerTwo.getName(), "Carlos");

        assertEquals(playerOne.getBoard().getBigPit(), 0);
        assertEquals(playerTwo.getBoard().getBigPit(), 0);
    }

    @Test
    void makeMove() {
        final List<PlayerMoveRequestDTO> players = Arrays.asList(
                PlayerMoveRequestDTO.builder()
                        .playerType(PLAYER_ONE)
                        .name("Lalo")
                        .isPlayerTurn(true)
                        .board(
                                BoardResponseDTO.builder()
                                        .bigPit(2).
                                        pits(new int[]{0, 0, 0, 0, 0, 1}).build()
                        ).build(),
                PlayerMoveRequestDTO.builder()
                        .playerType(PLAYER_TWO)
                        .name("Carlos")
                        .isPlayerTurn(false)
                        .board(
                                BoardResponseDTO.builder()
                                        .bigPit(5).
                                        pits(new int[]{6, 6, 6, 6, 1, 6}).build()
                        ).build()
        );

        final MoveRequestDTO moveRequestDTO = MoveRequestDTO.builder()
                .pit(5)
                .players(players)
                .build();

        final MancalaResponseDTO mancalaResponseDTO = mancalaService.makeMove(moveRequestDTO);

        final PlayerResponseDTO playerOne = mancalaResponseDTO.getPlayers().get(PLAYER_ONE.playerTypeValue);
        final PlayerResponseDTO playerTwo = mancalaResponseDTO.getPlayers().get(PLAYER_TWO.playerTypeValue);

        assertArrayEquals(new int[]{0, 0, 0, 0, 0, 0}, playerOne.getBoard().getPits());
        assertArrayEquals(new int[]{6, 6, 6, 6, 1, 6}, playerTwo.getBoard().getPits());


        assertEquals(3, playerOne.getBoard().getBigPit());
        assertEquals(36, playerTwo.getBoard().getBigPit());

        assertTrue(mancalaResponseDTO.isGameEnded());

        assertFalse(playerOne.isPlayerTurn());
        assertFalse(playerTwo.isPlayerTurn());

        assertEquals(PLAYER_ONE, playerOne.getPlayerType());
        assertEquals(PLAYER_TWO, playerTwo.getPlayerType());
    }

    @Test
    void endGame() {
    }
}